package njit.com.lightcontroller;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.R.attr.data;
import static android.R.attr.id;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;


public class MainActivity extends AppCompatActivity {
    private SmartLightView mSmartLightView;
    private TextView mTvLumin;
    private SeekBar mSbLumin;
    private TextView mTvWarmLight;
    private SeekBar mSbWarmLight;
    final String address="http://192.168.137.138/sender.cgi?";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSmartLightView = (SmartLightView) findViewById(R.id.SmartLightView);//获取自定义控件
        mSmartLightView.setSmartLightViewOnClickListener(new SmartLightView.SmartLightViewOnClickListener() {
            @Override
            public void lightStatusCallBack(boolean isOpen) {
                Log.e("miaojuanjuan", "点击事件回调，当前开灯状态：" + isOpen);
            }
        });
        Intent intent=getIntent();
        final int data=intent.getIntExtra("id",0);
        sendRequestWithOkHttp(address,data);        //向服务器发送请求灯光状态

        mTvLumin = (TextView) findViewById(R.id.tvLumin);  //获取控件实例
        mSbLumin = (SeekBar) findViewById(R.id.sbLumin);
        mTvWarmLight = (TextView) findViewById(R.id.tvWarmLight);
        mSbWarmLight = (SeekBar) findViewById(R.id.sbWarmLight);
        //亮度
        mSbLumin.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {  //为seekbar添加监听器
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mSmartLightView.setProgressLumin(seekBar.getProgress());
                String progress=Integer.toString(seekBar.getProgress());   //将亮度条进度值转为字符串类型
                sendOkHttpRequestGet(address,progress,data);      //向服务器发送灯的id和进度值
                Toast.makeText(MainActivity.this,progress,Toast.LENGTH_LONG).show(); //通知用户此时的数值
            }
        });
        //冷暖
        mSbWarmLight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mSmartLightView.setProgressTemper(seekBar.getProgress());
             /*   String progress=Integer.toString(seekBar.getProgress());
                sendOkHttpRequestGet(address,progress,data);
                Toast.makeText(MainActivity.this,progress,Toast.LENGTH_LONG).show();*/
            }
        });
    }
    private void sendRequestWithOkHttp(final String address, final int id){//使用Get方式发送请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client=new OkHttpClient();
                    Request request=new Request.Builder()
                            .url(address+id)
                            .build();
                    Response response=client.newCall(request).execute();
                    String responseData=response.body().string();
                    int Lu=Integer.parseInt(responseData);
                    mSmartLightView.setProgressLumin(Lu);
                    mSbLumin.setProgress(Lu);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void sendOkHttpRequestGet(final String address,final String progress,final int id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client=new OkHttpClient();
                    Request request=new Request.Builder()
                            .url(address+id+progress)
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
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){  //菜单选择
        switch(item.getItemId()){
            case R.id.clock:
                Intent intent=new Intent(MainActivity.this,ClockShowActivity.class);
                intent.putExtra("id",data);
                startActivity(intent);
            default:
        }
        return true;
    }
}
