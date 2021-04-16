package njit.com.lightcontroller;

/**
 * Created by lenovo on 2019/5/1.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MyDataBaseHelper extends SQLiteOpenHelper {

    public static MyDataBaseHelper helper;

    private static final int DB_VERSION = 2;            //数据库版本
    public static final String DB_NAME ="wrist_db";       //数据库名称
    public static final String ALARM_TB_NAME="alarm_tb";    //表名称
    public static final String COL_TIME="alarm_time";
    public static final String COL_ALARM_STATUS="alarm_status";
    public static final String COL_ALARM_REPEAT_TIMES="alarm_times";



    private static final String CREATE_ALARM_TABLE="CREATE TABLE "+ALARM_TB_NAME
            +"(_id INTEGER PRIMARY KEY AUTOINCREMENT,"   //把建表语句定义为一个字符串常量
            +COL_ALARM_STATUS +" TEXT NOT NULL,"       //id列设为自增长
            +COL_ALARM_REPEAT_TIMES+" TEXT NOT NULL,"
            +COL_TIME+" TEXT NOT NULL);";



    public MyDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);                 //自带的MyDataBaseHelper()构造方法
    }         //必须有context才能对数据库进行操作；第二个参数是数据库名；
    //第三个参数允许我们在查询数据的时候返回一个自定义的cursor；
    //第四个参数表示当前数据库的版本号


    public static synchronized MyDataBaseHelper getInstance(Context context) { //使用单例模式创建类的实例

        if (helper == null) {
            helper = new MyDataBaseHelper(context, DB_NAME, null, DB_VERSION);
        }
        return helper;
    }


    private void createTable(SQLiteDatabase db)
    {
        db.execSQL(CREATE_ALARM_TABLE);             //执行sql建表语句


    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }  //重写onCreate和onUpgrade方法

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }
}
