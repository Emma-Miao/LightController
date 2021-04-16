package njit.com.lightcontroller;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.attr.data;

public class LightPickerActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private TextView username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lightpicker);
        Button button1=(Button)findViewById(R.id.light1) ;    //获取控件实例
        Button button2=(Button)findViewById(R.id.light2) ;
        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){                  //为按钮添加监听器
                int data=1;
                Intent intent=new Intent(LightPickerActivity.this,MainActivity.class);
                intent.putExtra("id",data);
                startActivity(intent);
            }
        });
        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int data=2;
                Intent intent=new Intent(LightPickerActivity.this,MainActivity.class);
                intent.putExtra("id",data);
                startActivity(intent);
            }
        });
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);      //获取Toolbar实例
        setSupportActionBar(toolbar);                             //使Toolbar取代原本的Actionbar
        mDrawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView=(NavigationView)findViewById(R.id.nav_view);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);     //设置左上角导航按钮可以点击
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);//设置导航按钮图标
        }
        View nav_header=navView.getHeaderView(0);   //使NavigationView的头部中的TextView显示的用户名
        TextView tv=(TextView)nav_header.findViewById(R.id.username);//能随着登录用户的名字改变
        Intent intent=getIntent();
        final String data=intent.getStringExtra("username");    //获取上一个活动传来的数据
        tv.setText(data);
        navView.setCheckedItem(R.id.nav_setting);          //设置用户进入navigationview时选中setting
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            public boolean onNavigationItemSelected(MenuItem item){ //为navigationview中的控件添加响应事件
                switch (item.getItemId()){
                    case R.id.nav_setting:
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_about:
                        Intent intent=new Intent(LightPickerActivity.this,AboutActivity.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);    //打开滑动菜单
                break;
            default:
        }
        return true;
    }
}
