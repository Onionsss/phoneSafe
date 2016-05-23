package heima.it.safe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 作者：张琦 on 2016/5/23 18:43
 */
public class BlackNumberOpenHelp extends SQLiteOpenHelper{

    public BlackNumberOpenHelp(Context context) {
        super(context, "black.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table blacknumber(_id integer primary key autoincrement,name varchar(30),phone varchar(30) UNIQUE,mode varchar(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
