package onionsss.it.blacknumber;

/**
 * 作者：张琦 on 2016/5/24 19:44
 */
public class BlackConstant {
    public static final String DB_NAME = "black.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "blacknumber";
    public static final String FIELD_ID = "_id";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_PHONE = "phone";
    public static final String FIELD_MODE = "mode";

    public static final String SQL_CREATE_TABLE = "create table " +
            TABLE_NAME + "(" + FIELD_ID + " integer primary key autoincrement," + FIELD_NAME + " " +
            "varchar(30)," + FIELD_PHONE + " varchar(30) UNIQUE," + FIELD_MODE + " varchar(20))";
}
