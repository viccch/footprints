package io.github.viccch.footprints.ui.login;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import io.github.viccch.footprints.AppCommunicationManager;
import io.github.viccch.footprints.R;
import io.github.viccch.footprints.ui.me.UserInfo;

public class SignupActivity extends AppCompatActivity {

    Button btn_submit;

    EditText edit_id;
    EditText edit_pwd;
    EditText edit_pwd_r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edit_id = findViewById(R.id.edit_id);
        edit_pwd = findViewById(R.id.edit_pwd);
        edit_pwd_r = findViewById(R.id.edit_pwd_r);

        btn_submit = findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(v -> submit());
    }

    void submit() {

        String str_id = edit_id.getText().toString();
        String str_pwd = edit_pwd.getText().toString();
        String str_pwd_r = edit_pwd_r.getText().toString();

        if (str_id.length() == 0 || str_pwd.length() ==0 || str_pwd_r.length() == 0) {
            Toast.makeText(this, "未填写完整", Toast.LENGTH_SHORT).show();
            return;
        } else if (!str_pwd.equals(str_pwd_r)) {
            Toast.makeText(this, "密码不一致", Toast.LENGTH_SHORT).show();
            return;
        } else {


            try {
                String result = AppCommunicationManager.QueryUserInfoById(str_id);
                Gson gson = new Gson();

                List<UserInfo> userInfos = gson.fromJson(result, new TypeToken<List<UserInfo>>() {
                }.getType());

                for (UserInfo userInfo : userInfos) {
                    if (userInfo.getUser_id().equals(str_id)) {
                        Toast.makeText(this, "该用户名已注册", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                    }
                }

                UserInfo info = new UserInfo();

                info.setUser_id(edit_id.getText().toString());
                info.setUser_password(edit_pwd.getText().toString());

                AppCommunicationManager.AddUser(info);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
    }

}