package heima.it.safe.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;

/**
 * Created by 张琦 on 2016/4/27.
 */
public class SqliteDao {


    public static String getAddress(Context context, String phone) {
        File file = new File(context.getFilesDir(), "address.db");
        String str = "未知号码";
        SQLiteDatabase sd = SQLiteDatabase.openDatabase(file.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);

        if (phone.matches("^1[3-8]\\d{9}$")) {

            Cursor cursor = sd.rawQuery("select location from data2 where id=(select outkey from data1 where id=?)",
                    new String[]{phone.substring(0, 7)});

            if (cursor.moveToNext()) {
                str = cursor.getString(cursor.getColumnIndex("location"));
            }
        } else if (phone.matches("^\\d+$")) {
            switch (phone.length()) {
                case 3:
                    str = "报警电话";
                    break;
                case 4:
                    str = "模拟器";
                    break;
                case 5:
                    str = "客服电话";
                    break;
                case 7:
                case 8:
                    str = "本地电话";
                    break;
                default:
                    // 01088881234
                    // 048388888888
                    if (phone.startsWith("0") && phone.length() > 10) {// 有可能是长途电话
                        // 有些区号是4位,有些区号是3位(包括0)

                        // 先查询4位区号
                        Cursor cursor = sd.rawQuery(
                                "select location from data2 where area =?",
                                new String[]{phone.substring(1, 4)});

                        if (cursor.moveToNext()) {
                            str = cursor.getString(0);
                        } else {
                            cursor.close();

                            // 查询3位区号
                            cursor = sd.rawQuery(
                                    "select location from data2 where area =?",
                                    new String[]{phone.substring(1, 3)});

                            if (cursor.moveToNext()) {
                                str = cursor.getString(0);
                            }

                            cursor.close();
                        }
                    }
                    break;
            }
        }

        return str;
    }
}
