package app.rstone.com.contactsapp2;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.List;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Context ctx = Main.this;
        findViewById(R.id.moveLogin).setOnClickListener(
                (View v)->{
                    SQLiteHelper helper = new SQLiteHelper(ctx);    //helper라는 객체가 곧 DB가 된다
                    startActivity(new Intent(ctx, Login.class));
                }
        );
    }
    static class Member{int seq; String name, pw, email, phone, addr, photo;}
    static interface StatusService{public void perform();}
    static interface ListService{public List<?> perform();}
    static interface RetrieveService{public Object perform();}
    /*static interface DeleteService{public void perform();}
    static interface UpdateService{public void perform();}*/
    static String DBNAME = "rstone.db";
    static String MEMTAB = "MEMBER";
    static String MEMSEQ = "SEQ";
    static String MEMNAME = "name";
    static String MEMPW = "PW";
    static String MEMEMAIL = "EMAIL";
    static String MEMPHONE = "PHONE";
    static String MEMADDR = "ADDR";
    static String MEMPHOTO = "PHOTO";
    static abstract class QueryFactory{
        Context ctx;
        public QueryFactory(Context ctx) {
            this.ctx = ctx;
        }
        public abstract SQLiteDatabase getDatabase();
    }
    static class SQLiteHelper extends SQLiteOpenHelper{

        public SQLiteHelper(Context context) {
            super(context, DBNAME, null, 1);
            this.getWritableDatabase();
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql = String.format(" CREATE TABLE IF NOT EXISTS %s"+
                                       " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                                       "  %s TEXT, " +
                                       "  %s TEXT, " +
                                       "  %s TEXT, " +
                                       "  %s TEXT, " +
                                       "  %s TEXT, " +
                                       "  %s TEXT " +
                                       " )", MEMTAB, MEMSEQ, MEMNAME, MEMPW, MEMEMAIL, MEMPHONE, MEMADDR, MEMPHOTO
                                       );
            Log.d("실행할 쿼리 ::", sql);
            db.execSQL(sql);
            Log.d("=========================", "create 쿼리실행완료");
            sql = String.format(
                    "INSERT INTO %s"+
                            " (%s ," +
                            " %s , " +
                            " %s , " +
                            " %s , " +
                            " %s , " +
                            " %s  " +
                            " ) VALUES " +
                            " ('%s' ," +
                            "  '%s' , " +
                            "  '%s' , " +
                            "  '%s' , " +
                            "  '%s' , " +
                            "  '%s'  )", MEMTAB, MEMNAME, MEMPW, MEMEMAIL, MEMPHONE, MEMADDR, MEMPHOTO,
                                        MEMNAME, MEMPW, MEMEMAIL, MEMPHONE, MEMADDR, MEMPHOTO
            );
            String[] names = {"김분노", "김싫음", "김무서움", "김즐거움", "김슬픔"};
            String[] emails = {"anger", "disgust", "fear", "joy", "sadness"};
            for (int i=0; i<=4; i++){
                db.execSQL(
                        String.format(
                                "INSERT INTO %s"+
                                        " (%s ," +
                                        " %s , " +
                                        " %s , " +
                                        " %s , " +
                                        " %s , " +
                                        " %s  " +
                                        " ) VALUES " +
                                        " ('%s' ," +
                                        "  '%s' , " +
                                        "  '%s' , " +
                                        "  '%s' , " +
                                        "  '%s' , " +
                                        "  '%s'  )", MEMTAB, MEMNAME, MEMPW, MEMEMAIL, MEMPHONE, MEMADDR, MEMPHOTO,
                                names[i], "1", emails[i]+"@test.com", "010-1234-567"+i, "신촌"+i+"길", "profile_"+(i+1)
                        ));
            }
            Log.d("=========================", "insert 쿼리실행완료");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+MEMTAB);
            onCreate(db);
        }
    }

}
