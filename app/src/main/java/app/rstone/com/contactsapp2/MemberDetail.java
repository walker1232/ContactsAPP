package app.rstone.com.contactsapp2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import static app.rstone.com.contactsapp2.Main.*;

public class MemberDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_detail);
        Context ctx = MemberDetail.this;
        Intent intent = this.getIntent();
        String seq = intent.getExtras().getString("seq");
        Log.d("넘어온 seq 값:::", seq);
        String seq2 = intent.getStringExtra("seq");
        Log.d("넘어온 seq2 값:::", seq2);
        ItemDetail query = new ItemDetail(ctx);
        query.seq = seq;
        Main.Member m = (Main.Member) new RetrieveService() {
            @Override
            public Object perform() {
                return query.execute();
            }
        }.perform();
        Log.d("정보", m.name);
        findViewById(R.id.list).setOnClickListener(
                (View v)->{
                    startActivity(new Intent(ctx, MemberList.class));
                }
        );
        findViewById(R.id.update).setOnClickListener(
                (View v)->{
                    startActivity(new Intent(ctx, MemberUpdate.class));
                }
        );
    }
    private class MemberDetailQuery extends Main.QueryFactory{
        SQLiteOpenHelper helper;
        public MemberDetailQuery(Context ctx) {
            super(ctx);
            helper = new Main.SQLiteHelper(ctx);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }
    }
    private class ItemDetail extends MemberDetailQuery{
        String seq;

        public ItemDetail(Context ctx) {
            super(ctx);
        }
        public Main.Member execute(){
            Cursor c = this.getDatabase().rawQuery(String.format(
                    " SELECT * FROM MEMBER " +
                            " WHERE SEQ like '%s' ",seq ), null
            );
            Main.Member m = null;
            if(c != null){
                Log.d("c의 정보", String.valueOf(c.getCount()));
                while (c.moveToNext()){
                    m = new Main.Member();
                    m.seq = Integer.parseInt(c.getString(c.getColumnIndex(MEMSEQ)));
                    m.name = c.getString(c.getColumnIndex(MEMNAME));
                    m.pw = c.getString(c.getColumnIndex(MEMPW));
                    m.email = c.getString(c.getColumnIndex(MEMEMAIL));
                    m.phone = c.getString(c.getColumnIndex(MEMPHONE));
                    m.addr = c.getString(c.getColumnIndex(MEMADDR));
                    m.photo = c.getString(c.getColumnIndex(MEMPHOTO));
                    Log.d("seq 정보", seq);
                }
            }
            return m;
        }


    }
}
