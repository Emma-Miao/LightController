package njit.com.lightcontroller;

/**
 * Created by lenovo on 2019/5/1.
 */

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.R.attr.button;
import static android.R.attr.data;
import static android.R.attr.id;


/**
 * Created by mc on 16-4-27.
 */
public class DeleteClockActivity extends AppCompatActivity {

    ListView listView;
    MyListViewAdapter adapter;
    final String address="http://192.168.137.89/sender.cgi";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);   //设置软件窗体没有标题
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clock_delete_view);      //给当前活动引入布局
        Intent intent=getIntent();
        final int data=intent.getIntExtra("id",0);
        ImageButton button = (ImageButton) findViewById(R.id.calcel_delete);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        listView = (ListView)findViewById(R.id.delete_alarm_list);
        initData();    //初始化数据

    }

    private void initData(){
        MyDataBaseHelper helper = MyDataBaseHelper.getInstance(this);  //获取MyDataBaseHelper类的实例
        SQLiteDatabase dWriter = helper.getWritableDatabase();    //打开数据库
        Cursor cursor = dWriter.query(MyDataBaseHelper.ALARM_TB_NAME,null,null,null,null,null,null);//查询表中的所有数据
        String[] alarmColums = new String[]{MyDataBaseHelper.COL_TIME,MyDataBaseHelper.COL_ALARM_REPEAT_TIMES};
        int[] layoutId = new int[]{R.id.alarm_delete_time,R.id.alarm_name_delete};//列名与控件id一一对应
        adapter = new MyListViewAdapter(this,R.layout.clock_delete_item,cursor,alarmColums,layoutId, CursorAdapter.FLAG_AUTO_REQUERY);//创建适配器对象
        listView.setAdapter(adapter);
    }


    public class MyListViewAdapter extends SimpleCursorAdapter{
        public MyListViewAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
            super(context, layout, c, from, to, flags);     //第四个参数from表示cursor对象中的字段
        }                                                   //flags用来决定该适配器的行为

        @Override
        public void bindView(View view, final Context context, final Cursor cursor) {
            super.bindView(view, context, cursor);
            final int id = cursor.getInt(0);
            final String time_string=cursor.getString(3);
            ImageButton imageButton = (ImageButton)view.findViewById(R.id.alarm_delete_button);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendOkHttpRequestGet(address,data,time_string);
                    DataBaseOperator operator = new DataBaseOperator(context);
                    operator.delete(id);      //删除数据库中对应的数据
                    cursor.requery();
                    adapter.notifyDataSetChanged();  //更新ListView


                }
            });

        }
        public void sendOkHttpRequestGet(final String address,final int id,final String time){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        OkHttpClient client=new OkHttpClient();
                        Request request=new Request.Builder()
                                .url(address+"?id="+id+"?delete time="+time)
                                .build();
                        Response response=client.newCall(request).execute();
                        String responseData=response.body().string();
                        Log.d("response",responseData);

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();

        }

    }
}
