package njit.com.lightcontroller; /**
 * Created by lenovo on 2019/5/1.
 */

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ClockEditActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener {
    private String TAG = "AlarmEditActivity";
    private ListView listView;
    private List<Map<String, String>> datalist;     //创建一个用嵌套Map集合的List集合
    private Map<String, String> map;       //Map集合
    private SimpleAdapter simpleAdapter;
    private Clock clock;
    final String address="http://192.168.137.89/sender.cgi";
    DataBaseOperator dbOperator;
    Calendar time_calender;
    String modify_time_string;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clock_edit);
        dbOperator = new DataBaseOperator(this);
        modify_time_string=getIntent().getStringExtra("time");    //获取上个活动传来的时间数据
        clock=new Clock();      //创建Clock类的对象
        if(modify_time_string!=null){
            Log.d(TAG,"modify");
            clock.setClockChangeWay("modify");
            clock.setTime(modify_time_string);
            clock.setWhichClock(getIntent().getIntExtra("position",0));
            Log.d(TAG,"修改位置为"+clock.getWhichClock());
        }else{
            clock.setClockChangeWay("new");
            Log.d(TAG,"new");
        }
        datalist = new ArrayList<>();    //初始化List
        ImageButton saveAlarm = (ImageButton) findViewById(R.id.save_alarm);//获取控件
        saveAlarm.setOnClickListener(this);   //添加监听器
        ImageButton cancelEditAlarm = (ImageButton) findViewById(R.id.cancel_edit_alarm);
        cancelEditAlarm.setOnClickListener(this);
        Date date = TimeTool.turnStringToDate(clock.getClockTime()+":00");//将字符串类型的时间转为时间格式
        time_calender=Calendar.getInstance();     //获取Canlendar实例
        Log.d("现在时间为",""+time_calender.getTime());
        time_calender.set(Calendar.HOUR_OF_DAY,date.getHours());//闹钟时间，新建的话就是当前时间
        time_calender.set(Calendar.MINUTE,date.getMinutes());
        Log.d("time_calendar为",""+time_calender.getTime());

    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onstop");
    }
    @Override
    protected void onResume() {
        super.onResume();
        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);//使用TimePicker选择时间
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(time_calender.getTime().getHours());
        timePicker.setCurrentMinute(time_calender.getTime().getMinutes());
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minuite) {
                time_calender.set(Calendar.HOUR_OF_DAY, hourOfDay);
                time_calender.set(Calendar.MINUTE, minuite);
                time_calender.set(Calendar.SECOND, 0);
                if (time_calender.before(Calendar.getInstance())) {
                    time_calender.add(Calendar.DAY_OF_MONTH, 1);
                }
            }
        });
        listView = (ListView) findViewById(R.id.alarm_edit_list_view);
        setDataList();//准备数据源
        simpleAdapter = new SimpleAdapter(this, datalist, R.layout.clock_edit_item,
                new String[]{"name", "value"}, new int[]{R.id.name, R.id.value});//创建适配器对象
        listView.setAdapter(simpleAdapter);//绑定适配器
        listView.setOnItemClickListener(this);//添加监听器
    }
    public void setDataList() {
        datalist.clear();
        map = new HashMap<>();            //HashMap的每一节对应listView的每一行
        map.put("name", "重复次数");   //向HashMap中添加数据
        map.put("value", clock.getRepeatTimes());
        datalist.add(map);
        map = new HashMap<>();
        map.put("name", "目标");
        map.put("value", clock.getClockStatus());
        datalist.add(map);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_alarm:
                saveAlarm();
                finish();
                break;
            case R.id.cancel_edit_alarm:
                finish();
                break;
        }
    }
    public void saveAlarm() {
        Intent intent=getIntent();
        int data=intent.getIntExtra("id",0);
        String time_string = TimeTool.turnDateToStringonlyTime(time_calender.getTime());
        sendRequestWithOkHttp(address,data,time_string);
        Log.d(TAG,time_string);
        Log.d(TAG,clock.getClockStatus());
        Log.d(TAG,clock.getRepeatTimes());
        if (time_calender.before(Calendar.getInstance())){//如果时间早于现在就是天数+1
            time_calender.set(Calendar.DAY_OF_MONTH,time_calender.get(Calendar.DAY_OF_MONTH)+1);
            Log.d(TAG,"闹钟天数为"+time_calender.get(Calendar.DAY_OF_MONTH));

        }
        ContentValues values = new ContentValues();    //创建ContentValues对象
        values.put(MyDataBaseHelper.COL_TIME, time_string);//存储数据
        values.put(MyDataBaseHelper.COL_ALARM_STATUS, clock.getClockStatus());
        values.put(MyDataBaseHelper.COL_ALARM_REPEAT_TIMES,clock.getRepeatTimes());
        if(clock.getClockChangeWay().equals("new")){

            dbOperator.insert(MyDataBaseHelper.ALARM_TB_NAME, values);  //向数据库中插入数据
            Log.d(TAG,"insert to dataBase");
            Log.d(TAG,"设置闹钟时间为"+time_calender.getTime());
            intent.putExtra("time",time_string);


        }else {
            dbOperator.update(MyDataBaseHelper.ALARM_TB_NAME,values,"alarm_time = ?",new String[]{modify_time_string});
            Log.d(TAG,"update to dataBase");            //更新数据库中的数据
            Log.d(TAG,"操作时间为"+time_calender.getTime());
            intent.putExtra("time",time_string);
        }

    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                chooseRepeatTimes();
                break;
            case 1:
                chooseAlarmStatus();
        }
    }
    private void chooseAlarmStatus() {
        String items[] = {"ON","OFF"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this) //弹出一个消息框
                .setTitle("目标")          //消息框的标题
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:clock.setClockStatus("ON");break;
                            case 1:clock.setClockStatus("OFF");break;
                        }
                        setDataList();
                        simpleAdapter.notifyDataSetChanged();   //更新ListView
                    }
                }).setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.create().show();
    }
    private void chooseRepeatTimes() {
        String items[] = {"仅一次","每天"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("重复次数")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                clock.setRepeatTimes("仅一次");
                                clock.setRepeatTimes_int(0);
                                break;
                            case 1:
                                clock.setRepeatTimes("每天");
                                clock.setRepeatTimes_int(1);
                                break;
                        }
                        setDataList();
                        simpleAdapter.notifyDataSetChanged();
                    }
                }).setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.create().show();
    }
    private void sendRequestWithOkHttp(final String address, final int id,final String time){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client=new OkHttpClient();
                    Request request=new Request.Builder()
                            .url(address+"?id="+id+"?time="+time)
                            .build();
                    Response response=client.newCall(request).execute();
                    String responseData=response.body().string();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
