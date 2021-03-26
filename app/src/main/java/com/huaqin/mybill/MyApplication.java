package com.huaqin.mybill;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

import java.util.concurrent.Executor;

public class MyApplication extends LitePalApplication {

    private Handler mHandler;
    private Executor mExecutor;
    private static Context context;
    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
        if (instance == null) {
            instance = this;
        }
        mHandler = new Handler();
        mExecutor = AsyncTask.THREAD_POOL_EXECUTOR;


    }

    public void runOnUi(Runnable runnable) {
        if (mHandler != null) {
            mHandler.post(runnable);
        } else {
            mHandler = new Handler(Looper.getMainLooper());
            mHandler.post(runnable);
        }
    }

    public void runOnBackground(Runnable runnable) {
        if (mExecutor != null) {
            mExecutor.execute(runnable);
        } else {
            mExecutor = AsyncTask.THREAD_POOL_EXECUTOR;
            mExecutor.execute(runnable);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        context = base;
    }

    public static Context getContext() {
        return context;
    }

    public static MyApplication getInstance() {
        return instance;
    }
}
