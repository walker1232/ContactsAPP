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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

        Log.d("이름", m.name);
        Log.d("이메일", m.email);
        Log.d("전화번호", m.phone);
        int prof = getResources()
                .getIdentifier(this.getPackageName()+":drawable/"+m.photo, null,null);
        ImageView profile = findViewById(R.id.profile);
        profile.setImageDrawable(
                getResources().getDrawable(prof, ctx.getTheme()));
        /*profile.setImageDrawable(
                getResources().getDrawable(getResources()
                        .getIdentifier(this.getPackageName()+":drawable/"+m.photo, null,null);, ctx.getTheme())); 위의 code를 이처럼 축약 가능*/
        TextView name = findViewById(R.id.name);
        name.setText(m.name);
        TextView phone = findViewById(R.id.phone);
        phone.setText(m.phone);
        TextView email = findViewById(R.id.email);
        email.setText(m.email);
        TextView addr = findViewById(R.id.addr);
        addr.setText(m.addr);

        /*findViewById(R.id.list).setOnClickListener(
                (View v)->{
                    startActivity(new Intent(ctx, MemberList.class));
                }
        );
        findViewById(R.id.update).setOnClickListener(
                (View v)->{
                    startActivity(new Intent(ctx, MemberUpdate.class));
                }
        );*/
        findViewById(R.id.updateBtn).setOnClickListener(
                (View v)->{
                    Intent moveUpdate = (new Intent(ctx, MemberUpdate.class));
                    Log.d("업데이트로 넘기기위한 seq", seq);
                    moveUpdate.putExtra("spec", m.seq+","+
                                                            m.name+","+
                                                            m.pw+","+
                                                            m.email+","+
                                                            m.phone+","+
                                                            m.addr+","+
                                                            m.photo+",");
                    startActivity(moveUpdate);
                }
        );
        findViewById(R.id.callBtn).setOnClickListener(
                (View v)->{

                }
        );
        findViewById(R.id.dialBtn).setOnClickListener(
                (View v)->{

                }
        );
        findViewById(R.id.smsBtn).setOnClickListener(
                (View v)->{

                }
        );
        findViewById(R.id.emailBtn).setOnClickListener(
                (View v)->{

                }
        );
        findViewById(R.id.albumBtn).setOnClickListener(
                (View v)->{

                }
        );
        findViewById(R.id.movieBtn).setOnClickListener(
                (View v)->{

                }
        );
        findViewById(R.id.mapBtn).setOnClickListener(
                (View v)->{

                }
        );
        findViewById(R.id.musicBtn).setOnClickListener(
                (View v)->{

                }
        );
        findViewById(R.id.listBtn).setOnClickListener(
                (View v)->{

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
                    " SELECT * FROM %s " +
                            " WHERE %s like '%s' ",MEMTAB, MEMSEQ, seq ), null
            );
            Main.Member m = null;
            if(c != null){
                //Log.d("c의 정보", String.valueOf(c.getCount()));
                while (c.moveToNext()){
                    m = new Main.Member();
                    m.seq = Integer.parseInt(c.getString(c.getColumnIndex(MEMSEQ)));
                    m.name = c.getString(c.getColumnIndex(MEMNAME));
                    m.pw = c.getString(c.getColumnIndex(MEMPW));
                    m.email = c.getString(c.getColumnIndex(MEMEMAIL));
                    m.phone = c.getString(c.getColumnIndex(MEMPHONE));
                    m.addr = c.getString(c.getColumnIndex(MEMADDR));
                    m.photo = c.getString(c.getColumnIndex(MEMPHOTO));
                    //Log.d("seq 정보", seq);
                }
            }
            return m;
        }


    }
}
