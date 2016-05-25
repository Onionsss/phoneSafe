package onionsss.it.blacknumber;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：张琦 on 2016/5/24 19:51
 */
public class DBHelpDao {

    private final DBHelp mHelp;

    public DBHelpDao(Context context){
        mHelp = new DBHelp(context);
    }

    public boolean insert(BlackNumber bn){
        SQLiteDatabase db = mHelp.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BlackConstant.FIELD_NAME,bn.getName());
        values.put(BlackConstant.FIELD_PHONE,bn.getPhone());
        values.put(BlackConstant.FIELD_MODE,bn.getMode());

        long insert = db.insert(BlackConstant.TABLE_NAME, null, values);

        db.close();
        return insert != -1;
    }

    public List<BlackNumber> findAll(){
        List<BlackNumber> list = new ArrayList<>();
        SQLiteDatabase db = mHelp.getWritableDatabase();
        Cursor cursor = db.query(BlackConstant.TABLE_NAME, null, null, null, null, null, null);
        if(cursor != null){
            while(cursor.moveToNext()){
                String name = cursor.getString(cursor.getColumnIndex(BlackConstant.FIELD_NAME));
                String phone = cursor.getString(cursor.getColumnIndex(BlackConstant.FIELD_PHONE));
                String mode = cursor.getString(cursor.getColumnIndex(BlackConstant.FIELD_MODE));
                list.add(new BlackNumber(name,mode,phone));
            }
            cursor.close();
        }
        db.close();
        return list;
    }
    public boolean update(BlackNumber bn) {
        SQLiteDatabase db = mHelp.getWritableDatabase();
        // update blacklist set blacklist_type = 1 where blacklist_number = '1001';
        String table = BlackConstant.TABLE_NAME;
        ContentValues values = new ContentValues();
        values.put(BlackConstant.FIELD_MODE, bn.getMode());
        String whereClause =  BlackConstant.FIELD_PHONE+"=?";
        String[] whereArgs = new String[]{bn.getPhone()};
        // 参2, 修改哪一列为什么值, 参3 where条件, 参4: where条件参数
        int update = db.update(table , values , whereClause , whereArgs);
        db.close();
        return update != 0;
    }

    public List<BlackNumber> queryPart(int limit, int offset){
        SQLiteDatabase db = mHelp.getWritableDatabase();
        List<BlackNumber> list = new ArrayList<>();
        String sql = "select "+BlackConstant.FIELD_NAME+","+BlackConstant.FIELD_PHONE+","+BlackConstant.FIELD_MODE+
                " from "+BlackConstant.TABLE_NAME+" limit ? offset ? ";
        Cursor cursor = db.rawQuery(sql, new String[]{limit + "", offset + ""});
        if(cursor != null){
            while(cursor.moveToNext()){
                String name = cursor.getString(0);
                String phone = cursor.getString(1);
                String mode = cursor.getString(2);
                list.add(new BlackNumber(name,mode,phone));
            }
            cursor.close();
        }
        return list;
    }
}
