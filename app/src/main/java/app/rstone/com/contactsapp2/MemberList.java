package app.rstone.com.contactsapp2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.*;

import static app.rstone.com.contactsapp2.Main.*;


public class MemberList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_list);
        Context ctx = MemberList.this;
        ItemList query = new ItemList(ctx);
        //ItemDelete deletequery = new ItemDelete(ctx);

        ListView memberList = findViewById(R.id.memberList);
        memberList.setAdapter(new MemberAdapter(ctx, (ArrayList<Main.Member>) new ListService(){
            @Override
            public List<?> perform() {
                return query.execute();
            }
        }.perform()));
        memberList.setOnItemClickListener(
                (AdapterView<?> p, View v, int i, long l)->{
                    Intent intent = new Intent(ctx, MemberDetail.class);
                    Main.Member m = (Main.Member)memberList.getItemAtPosition(i);
                    Log.d("선택한 SEQ:::", m.seq+"");
                    intent.putExtra("seq", m.seq+"");
                    startActivity(intent);
                }
        );
        memberList.setOnItemLongClickListener(
                (AdapterView<?> p, View v, int i, long l)->{
                    //Toast.makeText(ctx, "길게 눌렀다 !!",Toast.LENGTH_LONG).show();
                    Main.Member m = (Main.Member) memberList.getItemAtPosition(i);
                    Log.d("선택한 SEQ 정보", m.seq+"");
                    new AlertDialog.Builder(ctx)
                            .setTitle("DELETE")
                            .setMessage("정말로 삭제할까요?")
                            .setPositiveButton(
                                    android.R.string.yes,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // 삭제 쿼리
                                            ItemDelete query = new ItemDelete(ctx);
                                            query.m.seq = m.seq;

                                            new StatusService() {
                                                @Override
                                                public void perform() {
                                                    query.execute();
                                                }
                                            }.perform();
                                            Toast.makeText(ctx, "삭제 완료 !!",Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(ctx, MemberList.class));
                                        }
                                    }
                            )
                            .setNegativeButton(
                                    android.R.string.no,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(ctx, "삭제 취소 !!",Toast.LENGTH_LONG).show();
                                        }
                                    }
                            ).show();

                    return true;
                }
        );

    }
    private class MemberDeleteQuery extends Main.QueryFactory{
        SQLiteOpenHelper helper;
        public MemberDeleteQuery(Context ctx) {
            super(ctx);
            helper = new Main.SQLiteHelper(ctx);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getWritableDatabase();
        }
    }
    private class ItemDelete extends MemberDeleteQuery{
        Main.Member m;
        public ItemDelete(Context ctx) {
            super(ctx);
            m = new Main.Member();
        }
        public void execute(){
            getDatabase().execSQL(String.format(
                    " DELETE FROM %s " +
                    " WHERE %s LIKE '%s' ", MEMTAB, MEMSEQ, m.seq)
            );
        }
    }
    private class MemberListQuery extends Main.QueryFactory{
        SQLiteOpenHelper helper;
        public MemberListQuery(Context ctx){
            super(ctx);
            helper = new Main.SQLiteHelper(ctx);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }
    }
    private class ItemList extends MemberListQuery{
        Main.Member m;
        public ItemList(Context ctx){
            super(ctx);
            m = new Main.Member();
        }
        public ArrayList<Main.Member> execute(){
            ArrayList<Main.Member> list = new ArrayList<>();
            Cursor c = this.getDatabase().rawQuery(    //resultset
                    " SELECT * FROM MEMBER", null
            );
            Main.Member m = null;
            if(c != null){
                while (c.moveToNext()){
                    m = new Main.Member();
                    m.seq = Integer.parseInt(c.getString(c.getColumnIndex(MEMSEQ)));
                    m.name = c.getString(c.getColumnIndex(MEMNAME));
                    m.pw = c.getString(c.getColumnIndex(MEMPW));
                    m.email = c.getString(c.getColumnIndex(MEMEMAIL));
                    m.phone = c.getString(c.getColumnIndex(MEMPHONE));
                    m.addr = c.getString(c.getColumnIndex(MEMADDR));
                    m.photo = c.getString(c.getColumnIndex(MEMPHOTO));
                    list.add(m);
                }
                Log.d("등록된 회원수가", list.size()+"");
            }
            return list;
        }

    }
    private class MemberAdapter extends BaseAdapter{
        ArrayList<Main.Member> list;
        LayoutInflater inflater;

        public MemberAdapter(Context ctx, ArrayList<Main.Member> list) {
            this.list = list;
            this.inflater = LayoutInflater.from(ctx);
        }
        private int[] photos = {
                R.drawable.profile_1,
                R.drawable.profile_2,
                R.drawable.profile_3,
                R.drawable.profile_4,
                R.drawable.profile_5
        };

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View v, ViewGroup g) {
            ViewHolder holder;
            if(v == null){
                v = inflater.inflate(R.layout.member_item, null);
                holder = new ViewHolder();
                holder.profile = v.findViewById(R.id.profile);
                holder.name = v.findViewById(R.id.name);
                holder.phone = v.findViewById(R.id.phone);
                v.setTag(holder);
            }else {
                holder = (ViewHolder)v.getTag();
            }
            holder.profile.setImageResource(photos[i]);
            holder.name.setText(list.get(i).name);
            holder.phone.setText(list.get(i).phone);
            return v;
        }
    }
    static class ViewHolder{
        ImageView profile;
        TextView name, phone;
    }
}
