package io.github.viccch.footprints.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import io.github.viccch.footprints.APP;
import io.github.viccch.footprints.AppCommunicationManager;
import io.github.viccch.footprints.MainActivity;
import io.github.viccch.footprints.R;
import io.github.viccch.footprints.ui.me.UserInfo;


public class LoginActivity extends AppCompatActivity {

    Button btn_login;
    Button btn_signup;

    EditText et_id;
    EditText et_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_id = findViewById(R.id.et_id);
        et_pwd = findViewById(R.id.et_pwd);

        btn_login = findViewById(R.id.btn_login);
        btn_signup = findViewById(R.id.btn_submit);

        btn_login.setOnClickListener(v -> login());
        btn_signup.setOnClickListener(v -> signup());

        pre_verify();
    }

    void login() {

        if (et_id.getText().toString().length() == 0) {
            Toast.makeText(this, "请填写用户名！", Toast.LENGTH_SHORT).show();
        } else if (et_pwd.getText().toString().length() == 0) {
            Toast.makeText(this, "请填写密码！", Toast.LENGTH_SHORT).show();
        } else {
            UserInfo userInfo = new UserInfo();
            userInfo.setUser_id(et_id.getText().toString());
            userInfo.setUser_password(et_pwd.getText().toString());

            if (verify(userInfo)) {
                save(userInfo);//保存账号密码
                entry(); //进入主页面
            } else {
                Toast.makeText(this, "用户名或密码错误！请重试", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //进入系统
    void entry() {
        Toast.makeText(this, "登录验证成功", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    void signup() {
        Intent signupIntent = new Intent(this, SignupActivity.class);
        startActivity(signupIntent);
    }

    //保存账号信息到本地
    void save(UserInfo userInfo) {
        APP.getInstance().setUser(userInfo);

        SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("user_id", userInfo.getUser_id());
        editor.putString("user_password", userInfo.getUser_password());
        editor.apply();
//        editor.commit();
    }

    boolean verify(UserInfo info) {
        boolean verify_flag = false;
        //登录验证
        try {
            String result = AppCommunicationManager.QueryUserInfoById(info.getUser_id());
            Gson gson = new Gson();

            List<UserInfo> userInfos = gson.fromJson(result, new TypeToken<List<UserInfo>>() {
            }.getType());

            for (UserInfo userInfo : userInfos) {
                if (userInfo.getUser_id().equals(info.getUser_id())
                        && userInfo.getUser_password().equals((info.getUser_password()))) {
                    verify_flag = true;
                    break;
                } else {
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return verify_flag;
        }
    }

    void pre_verify() {
        //获取本地存储的账号密码尝试登录
        SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        String user_id = sp.getString("user_id", null);
        String user_password = sp.getString("user_password", null);

        UserInfo info = new UserInfo();
        info.setUser_id(user_id);
        info.setUser_password(user_password);

//        if (verify(info)) {//本地验证
        if (info.getUser_id() != null
                && info.getUser_password() != null
                && !info.getUser_id().isEmpty()
                && !(info.getUser_password().isEmpty())) {
            save(info);//保存账号密码
            entry();
        } else {
            et_id.setText(info.getUser_id());
            et_pwd.setText(info.getUser_password());
        }
    }
}