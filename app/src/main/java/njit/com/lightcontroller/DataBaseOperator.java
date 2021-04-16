package njit.com.lightcontroller;

/**
 * Created by lenovo on 2019/5/1.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;


public class DataBaseOperator {

    MyDataBaseHelper helper;
    SQLiteDatabase dbWriter;
    public DataBaseOperator(Context context) {
        helper=MyDataBaseHelper.getInstance(context);
        dbWriter=helper.getWritableDatabase();
    }


    /*
    向数据库里插入数据
     */
    public void insert(String tbName,ContentValues values){
        dbWriter.insert(tbName,null,values);
    }

    /*
    get table
     */
    public Cursor query(String tb){

        return dbWriter.query(tb,null,null,null,null,null,null);
    }


    public int update(String table, ContentValues values, String whereClause, String[] whereArgs){
        return dbWriter.update(table,values,whereClause,whereArgs);
    }



    public  void delete(int id){
        String[] args = new String[]{String.valueOf(id)};  //valueOf()将整型转为字符串类型
        dbWriter.delete(MyDataBaseHelper.ALARM_TB_NAME,"_id=?",args);
    }
}