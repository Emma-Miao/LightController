package njit.com.lightcontroller;

/**
 * Created by lenovo on 2019/5/1.
 */

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.R.attr.data;
import static java.security.AccessController.getContext;


public class ClockShowActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener{



    private ListView listView;//alarm show list
    private ArrayList<String> sList=new ArrayList<>();
    private ImageButton iButton;//add clock button
    private SimpleCursorAdapter cursorAdapter;//简单的游标适配器
    private DataBaseOperator dbOpeater;
    private SQLiteDatabase wb;
    private Cursor mCursor;//数据库指针
    private final String TAG="AlarmShowActivity";
    private ImageView imageView;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.clock_show_view);
        imageView=(ImageView)findViewById(R.id.back);
        imageView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();
            }
        });
        listView=(ListView)findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        iButton=(ImageButton)findViewById(R.id.add_button);
        iButton.setOnClickListener(this);
        dbOpeater = new DataBaseOperator(this);//数据库对象

    }


    @Override
    protected void onResume() {
        super.onResume();
        mCursor=dbOpeater.query(MyDataBaseHelper.ALARM_TB_NAME);//获得alarm的table
        String [] colums = {MyDataBaseHelper.COL_TIME,MyDataBaseHelper.COL_ALARM_STATUS,MyDataBaseHelper.COL_ALARM_REPEAT_TIMES};
        int[] layoutsId = {R.id.alarm_time,R.id.alarm_status,R.id.alarm_repeat_times};
        cursorAdapter=new SimpleCursorAdapter(this,R.layout.clock_item,mCursor,colums,layoutsId, CursorAdapter.FLAG_AUTO_REQUERY);
        listView.setAdapter(cursorAdapter);
    }

    @Override
    protected void onStop() {
        mCursor.close();
        super.onStop();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.add_button:
                Intent intent =new Intent(this,ClockEditActivity.class);
                intent.putExtra("id",data);
                startActivity(intent);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        Intent intent=new Intent(this,ClockEditActivity.class);
        intent.putExtra("id",data);
        final TextView modify_time= (TextView) view.findViewById(R.id.alarm_time);
        intent.putExtra("time",modify_time.getText());  //向ClockEditActivity传送时间、位置信息
        intent.putExtra("position",position+1);
        Log.d(TAG,"要修改时间为"+modify_time.getText());
        startActivity(intent);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,DeleteClockActivity.class);
        intent.putExtra("id",data);
        startActivity(intent);
        return false;
    }


}


