package com.huaqin.mybill.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;

import com.huaqin.mybill.R;
import com.huaqin.mybill.login.LoginActivity;
import com.huaqin.mybill.util.SharedPreferenceUtils;

public class StartActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }


    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //你需要跳转的地方的代码
                // 如果登录就直接启动MainActivity
                Long login_user_id = SharedPreferenceUtils.getPrefLong("login_user_id", 0l);
                Log.i("denglogin_user_id", "login_user_id = " + login_user_id);
                if (login_user_id == 0) {
                    startActivity(new Intent(StartActivity.this, LoginActivity.class));
                    overridePendingTransition(0, 0);

                } else {
                    startActivity(new Intent(StartActivity.this, MainActivity.class));
                    overridePendingTransition(0, 0);
                }
                finish();
            }
        }, 2000); //延迟5秒跳转

    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
