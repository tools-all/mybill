package com.huaqin.mybill.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.huaqin.mybill.Constant;
import com.huaqin.mybill.R;
import com.huaqin.mybill.activity.MainActivity;
import com.huaqin.mybill.login.presenter.ILoginPresenter;
import com.huaqin.mybill.login.presenter.LoginPresenterCompl;
import com.huaqin.mybill.login.view.ILoginView;
import com.huaqin.mybill.util.HttpUtil;
import com.huaqin.mybill.util.SharedPreferenceUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity implements ILoginView, View.OnClickListener {
    private EditText editUser;
    private EditText editPass;
    private Button btnLogin;
    private Button skip_login_login;
    ILoginPresenter loginPresenter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("dengsb222222onCreate","onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //find view
        editUser = this.findViewById(R.id.et_login_username);
        editPass = this.findViewById(R.id.et_login_password);
        btnLogin = this.findViewById(R.id.btn_login_login);
        progressBar = this.findViewById(R.id.progress_login);
        skip_login_login = this.findViewById(R.id.skip_login_login);

        //set listener
        btnLogin.setOnClickListener(this);
        skip_login_login.setOnClickListener(this);

        //init
        loginPresenter = new LoginPresenterCompl(this);
        loginPresenter.setProgressBarVisiblity(View.INVISIBLE);

        StringBuilder userAndPasswordUrl = getUserAndPasswordUrl("mr_day@163.com", "razrjay1234");
        Log.i("dengsb222222", "userAndPasswordUrl = " + userAndPasswordUrl.toString());
        asyncValidate(userAndPasswordUrl.toString());
    }

    private StringBuilder getUserAndPasswordUrl(String name, String passwd) {
        StringBuilder sb = new StringBuilder();
        sb.append(Constant.NETEASEBASE).append("login?email=").append(name).append("&password=").append(passwd);
        return sb;
    }

    private void asyncValidate(final String account) {
        //account:http://39.108.131.225:3000/login?email=mr_day@163.com&password=razrjay1234
        new Thread(() -> {
            HttpUtil.getHttpUtilInstance().sendOkHttpRequest(account, new Callback() {
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    final String responseText = response.body().string();
                    try {
                        boolean isLoginSuccess = true;
                        JSONObject jsonObject = new JSONObject(responseText);
                        String resultCode = jsonObject.optString("code");
                        Log.i("deng-impl", "resultCode = " + resultCode);
                        if (!resultCode.equals("200")) {
                            isLoginSuccess = false;
                            return;
                        }
                        JSONObject account1 = jsonObject.getJSONObject("account");
                        long id = account1.optLong("id");


                        final Boolean result = true;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                    }
                }

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.i("deng-impl", "登陆失败");
                }
            });
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login_login:
                loginPresenter.setProgressBarVisiblity(View.VISIBLE);
                btnLogin.setEnabled(false);
                loginPresenter.doLogin(editUser.getText().toString(), editPass.getText().toString());

//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                if (imm != null) {
//                    imm.toggleSoftInput(0, 0);
//                }
                break;
            case R.id.skip_login_login:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }

    @Override
    public void onClearText() {
        editUser.setText("");
        editPass.setText("");
    }

    @Override
    public void onLoginResult(Boolean result, int code, long userid) {
        loginPresenter.setProgressBarVisiblity(View.GONE);
        btnLogin.setEnabled(true);
        if (code == 200) {
            Toast.makeText(this, "登录成功", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, MainActivity.class));
            //保存用户ID到本地,启动splashactivity界面时会使用这个id来判断是否已经登录过
            //这次我采用的方案时, loginactiivty的oncreate方法中直接把我自己的账号先登录
            //不要把自己的id存到sp里面
            SharedPreferenceUtils.removeByKey(this, "login_user_id");
            SharedPreferenceUtils.savePref("login_user_id", userid);
            finish();
        } else
            Toast.makeText(this, "登录失败，code = " + code, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSetProgressBarVisibility(int visibility) {
        progressBar.setVisibility(visibility);
    }

}
