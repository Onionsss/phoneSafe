package heima.it.safe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 作者：张琦 on 2016/5/23 18:43
 */
public class BlackNumberOpenHelp extends SQLiteOpenHelper{

    public BlackNumberOpenHelp(Context context) {
        super(context, BlackListConstants.DB_NAME, null, BlackListConstants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BlackListConstants.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
