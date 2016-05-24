package heima.it.safe.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import heima.it.safe.bean.BlackNumber;
import heima.it.safe.db.BlackNumberOpenHelp;

/**
 * 作者：张琦 on 2016/5/23 18:53
 */
public class BlackNumberDaoImpl implements BlackNumberDao{
    private BlackNumberOpenHelp help;
    private static final String TABLE_NAME = "blacknumber";
    private static final String NAME = "name";
    private static final String PHONE = "phone";
    private static final String MODE = "mode";
    public BlackNumberDaoImpl(Context context){
        help = new BlackNumberOpenHelp(context);
    }

    @Override
    public boolean insert(BlackNumber bn) {
        SQLiteDatabase db = help.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME,bn.getName());
        values.put(PHONE,bn.getPhone());
        values.put(MODE,bn.getMode());
        long insert = db.insert(TABLE_NAME, null, values);
        db.close();
        return insert==-1?false:true;
    }

    @Override
    public boolean delete(BlackNumber bn) {
        return false;
    }

    @Override
    public boolean update(BlackNumber bn) {
        return false;
    }

    @Override
    public BlackNumber find(BlackNumber bn) {
        return null;
    }

    @Override
    public List<BlackNumber> findAll() {
        List<BlackNumber> list = new ArrayList<>();
        SQLiteDatabase db = help.getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null, null);
        while(cursor.moveToNext()){
            list.add(new BlackNumber(cursor.getString(cursor.getColumnIndex(NAME)),cursor.getString(cursor.getColumnIndex(PHONE)),cursor.getString(cursor.getColumnIndex(MODE))));
        }
        db.close();
        cursor.close();
        return list;
    }
}
