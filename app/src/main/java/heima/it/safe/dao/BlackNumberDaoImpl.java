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
        SQLiteDatabase db = help.getWritableDatabase();
        int delete = db.delete(TABLE_NAME, PHONE + "=?", new String[]{bn.getPhone()});

        db.close();
        return delete != 0;
    }

    @Override
    public boolean update(BlackNumber bn) {
        SQLiteDatabase db = help.getWritableDatabase();
        // update blacklist set blacklist_type = 1 where blacklist_number = '1001';
        String table = TABLE_NAME;
        ContentValues values = new ContentValues();
        values.put(MODE, bn.getMode());
        String whereClause =  PHONE+"=?";
        String[] whereArgs = new String[]{bn.getPhone()};
        // 参2, 修改哪一列为什么值, 参3 where条件, 参4: where条件参数
        int update = db.update(table , values , whereClause , whereArgs);
        db.close();
        return update != 0;
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


    /**
     * 分页/分批查询
     * @param limit 每次查询多少条数据
     * @param offset 偏移量
     * @return
     */
    @Override
    public List<BlackNumber> findPart(int limit, int offset) {
        // select blacklist_number, blacklist_type from blacklist limit 10 offset 20;
        SQLiteDatabase db = help.getReadableDatabase();
        String sql = "select " + PHONE + ", "
                + MODE + ","+NAME+" from "
                + TABLE_NAME + " limit ? offset ? ";
        String[] selectionArgs = new String[] {limit+"", String.valueOf(offset)};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        List<BlackNumber> infos = new ArrayList<BlackNumber>();
        if(cursor != null) {
            while (cursor.moveToNext()) {
                String phone = cursor.getString(0);
                String mode = cursor.getString(1);
                String name = cursor.getString(2);
                BlackNumber info = new BlackNumber(name,phone,mode);
                infos.add(info);
            }
            cursor.close();
        }
        db.close();
        return infos;
    }
}
