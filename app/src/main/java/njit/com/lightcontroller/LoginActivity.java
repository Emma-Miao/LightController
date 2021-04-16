package njit.com.lightcontroller;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import static njit.com.lightcontroller.R.id.username;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private EditText accountEdit;
    private EditText passwordEdit;
    private Button login;
    private CheckBox rememberPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        pref= PreferenceManager.getDefaultSharedPreferences(this);
        accountEdit = (EditText) findViewById(R.id.account);
        passwordEdit = (EditText) findViewById(R.id.password);
        rememberPass=(CheckBox)findViewById(R.id.remember_pass);
        login = (Button) findViewById(R.id.login);
        boolean isRemember=pref.getBoolean("remember_password",false);
        if(isRemember){
            String account=pref.getString("account","");//获取存储的账户、密码和是否记住
            String password=pref.getString("password","");//如果没有找到则使用方法中传入的默认值代替
            accountEdit.setText(account);
            passwordEdit.setText(password);
            rememberPass.setChecked(true);
        }
        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = accountEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    editor=pref.edit();
                    if(rememberPass.isChecked()){
                        editor.putBoolean("remember_password",true); //向对象中添加数据
                        editor.putString("account",account);
                        editor.putString("password",password);
                    }else {
                        editor.clear();    //清空
                    }
                    editor.apply();     //提交
                    Intent intent = new Intent(LoginActivity.this, LightPickerActivity.class);
                    intent.putExtra("username",account);
                    startActivity(intent);
                    finish();

                }
            }

        });
    }
}
