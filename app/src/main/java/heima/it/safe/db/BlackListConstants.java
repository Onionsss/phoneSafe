package heima.it.safe.db;

/**
 * 作者：张琦 on 2016/5/24 09:54
 * 黑名单字段常量
 */
public interface BlackListConstants {
    String DB_NAME = "black.db";
    int DB_VERSION = 1;
    String TABLE_NAME = "blacknumber";
    String FIELD_ID = "_id";
    String FIELD_NAME = "name";
    String FIELD_PHONE = "phone";
    String FIELD_MODE = "mode";

    String MODE_1 = "1";
    String MODE_2 = "2";
    String MODE_3 = "3";
    String MODE_NONE = "none";

    String SQL_CREATE_TABLE = "create table " + TABLE_NAME + "(" + FIELD_ID + " integer primary key autoincrement," + FIELD_NAME + " varchar(30)," + FIELD_PHONE + " varchar(30) UNIQUE," + FIELD_MODE + " varchar(20))";
}
