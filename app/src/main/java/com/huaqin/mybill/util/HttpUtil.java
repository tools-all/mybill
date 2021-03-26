package com.huaqin.mybill.util;

import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.huaqin.mybill.net.PersistentCookieStore;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtil {
    private static final String TAG = "HttpUtil";
    static volatile HttpUtil defaultInstance;
    private static OkHttpClient mOkHttpClient;


    private HttpUtil() {

        final PersistentCookieStore cookieStore = new PersistentCookieStore();

        //创建cookieJar 用来保存登录的状态
        CookieJar cookieJar = new CookieJar() {
            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                if (cookies != null && cookies.size() > 0) {
                    for (Cookie item : cookies) {
                        cookieStore.add(url, item);
                    }
                }
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                Log.i("deng2", "hahahahaah222222 url.host() " + url.host());
                List<Cookie> cookies = cookieStore.get(url);
                return cookies;
            }
        };
        //通过newBuilder设置cookjar并构建OkHttpClient对象
        mOkHttpClient = new OkHttpClient()
                .newBuilder()
                .cookieJar(cookieJar)
                .connectTimeout(5, TimeUnit.SECONDS)
                .build();
    }

    public static HttpUtil getHttpUtilInstance() {
        if (defaultInstance == null) {
            synchronized (HttpUtil.class) {
                if (defaultInstance == null) {
                    defaultInstance = new HttpUtil();
                }
            }
        }
        return defaultInstance;
    }

    public void sendOkHttpRequest(String address, okhttp3.Callback callback) {
        Log.i("dengall-address", "address = " + address);
        Request request = new Request.Builder().url(address).build();
        mOkHttpClient.newCall(request).enqueue(callback);
    }


    /**
     * 后面考虑使用这个,而不是上面那个,因为这个可以直接把东西传出去
     *
     * @param action1
     * @return
     */
    public static JsonObject getResposeJsonObject(String action1) {
        Log.i("dengall-address", "address = " + action1);
        try {
            Request request = new Request.Builder()
                    .url(action1)
//                    .addHeader("Referer","http://music.163.com/")
//                    .addHeader("Cookie", "appver=1.5.0.75771")
                    .build();
            Response response = mOkHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                //response.body（）只调用一次。第二次就关闭了
                String c = response.body().string();
                JsonElement jsonElement = JsonParser.parseString(c);
                return jsonElement.getAsJsonObject();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
