package onionsss.it.blacknumber;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 作者：张琦 on 2016/5/24 19:43
 */
public class DBHelp extends SQLiteOpenHelper{
    public DBHelp(Context context) {
        super(context, BlackConstant.DB_NAME, null, BlackConstant.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BlackConstant.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
