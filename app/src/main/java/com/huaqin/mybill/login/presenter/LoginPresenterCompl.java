package com.huaqin.mybill.login.presenter;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.huaqin.mybill.Constant;
import com.huaqin.mybill.login.model.IUser;
import com.huaqin.mybill.login.view.ILoginView;
import com.huaqin.mybill.util.HttpUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class LoginPresenterCompl implements ILoginPresenter {
    ILoginView iLoginView;
    IUser user;
    Handler handler;

    public LoginPresenterCompl(ILoginView iLoginView) {
        this.iLoginView = iLoginView;
//		initUser();
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };
    }

    @Override
    public void clear() {
        iLoginView.onClearText();
    }

    @Override
    public void doLogin(String name, String passwd) {
//		Boolean isLoginSuccess = true;
//		final int code = user.checkUserValidity(name,passwd);
//		if (code!=0) isLoginSuccess = false;
//		final Boolean result = isLoginSuccess;
//		handler.postDelayed(new Runnable() {
//			@Override
//			public void run() {
//					iLoginView.onLoginResult(result, code);
//			}
//		}, 5000);

        StringBuilder userAndPasswordUrl = getUserAndPasswordUrl(name, passwd);
        Log.i("dengsb1111", "userAndPasswordUrl = " + userAndPasswordUrl.toString());
        asyncValidate(userAndPasswordUrl.toString());
    }

    private StringBuilder getUserAndPasswordUrl(String name, String passwd) {
        StringBuilder sb = new StringBuilder();
        sb.append(Constant.NETEASEBASE).append("login?email=").append(name).append("&password=").append(passwd);
        return sb;
    }


    /*
    okhttp异步POST请求 要求API level 21+
    account 本来想的是可以是 telphone或者username
    但目前只实现了telphone
   */
//    private void asyncValidate(final String account) {
//        /*
//         发送请求属于耗时操作，所以开辟子线程执行
//         上面的参数都加上了final，否则无法传递到子线程中
//        */
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                CookieJar cookieJar = new CookieJar() {
//                    private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
//
//                    @Override
//                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
//                        Log.i("deng2", "hahahahaha11111 cookies url: " + cookies.toString());
//                        for (Cookie cookie : cookies) {
//                            System.out.println("deng2 cookie Name:" + cookie.name());
//                            System.out.println("deng2 cookie Path:" + cookie.path());
//                        }
//                        cookieStore.put(url.host(), cookies);
////                        android.webkit.CookieManager cookieManager = android.webkit.CookieManager.getInstance();
////                        cookieManager.setCookie(account, cookies.toString());
//
//                    }
//
//                    @Override
//                    public List<Cookie> loadForRequest(HttpUrl url) {
//                        Log.i("deng2", "hahahahaah222222 url.host() " + url.host());
//                        List<Cookie> cookies = cookieStore.get(url.host());
//                        if (cookies == null) {
//                            System.out.println("deng2 没加载到cookie");
//                        }
//                        return cookies != null ? cookies : new ArrayList<Cookie>();
//                    }
//                };
//
//                OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                        .cookieJar(cookieJar)
//                        .build();
//
//                RequestBody formBody = new FormBody.Builder()
//                        .add("username", "mr_day@163.com")
//                        .add("password", "razrjay1234")
//                        .build();
//                final Request request = new Request.Builder()
//                        .url(account)
//                        .post(formBody)
//                        .build();
//
//
////                Request request = new Request.Builder().url(account).build();
//                okHttpClient.newCall(request).enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        Log.i("deng-impl", "onFailure");
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//
//                        final String responseText = response.body().string();
//                        try {
//                            boolean isLoginSuccess = true;
//                            JSONObject jsonObject = new JSONObject(responseText);
//                            String resultCode = (String) jsonObject.optString("code");
//                            Log.i("deng-impl", "resultCode = " + resultCode);
//                            if (!resultCode.equals("200")) {
//                                isLoginSuccess = false;
//                                return;
//                            }
//                            final Boolean result = true;
//                            handler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    iLoginView.onLoginResult(result, Integer.parseInt(resultCode));
//                                }
//                            });
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        } finally {
//                        }
//                    }
//                });
//            }
//        }).start();
//    }


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
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                iLoginView.onLoginResult(result, Integer.parseInt(resultCode), id);
                            }
                        });

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
    public void setProgressBarVisiblity(int visiblity) {
        iLoginView.onSetProgressBarVisibility(visiblity);
    }
}
