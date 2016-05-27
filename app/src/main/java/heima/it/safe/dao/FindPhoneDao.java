package heima.it.safe.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import heima.it.safe.bean.FindPhoneChild;
import heima.it.safe.bean.FindPhoneGroup;

/**
 * 作者：张琦 on 2016/5/27 19:22
 */
public class FindPhoneDao {
    public static List<FindPhoneGroup> findAll(Context context){
        File file = new File(context.getFilesDir(), "commonnum.db");
        SQLiteDatabase sd = SQLiteDatabase.openDatabase(file.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
        List<FindPhoneGroup> list = new ArrayList<>();
        String sql = "select name,idx from classlist";
        Cursor cursor = sd.rawQuery(sql, null);
        if(cursor != null){
            while(cursor.moveToNext()){
                String name = cursor.getString(0);
                int idx = cursor.getInt(1);
                FindPhoneGroup fpg = new FindPhoneGroup(name, idx);
                String childSql = "select _id, number, name from table" + fpg.getIdx();
                Cursor cCursor = sd.rawQuery(childSql, null);
                if(cCursor != null){
                    while(cCursor.moveToNext()){
                        int _id = cCursor.getInt(0);
                        String number = cCursor.getString(1);
                        String childName = cCursor.getString(2);
                        FindPhoneChild fpc = new FindPhoneChild(childName,_id,number);
                        fpg.list.add(fpc);
                    }
                    cCursor.close();
                }
                list.add(fpg);
            }
            cursor.close();
        }
        return list;
    }
}
