package com.myproject.demo1.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2017/2/16.
 */

public class ThreadUtil {
    private static Executor executor = Executors.newSingleThreadExecutor();
private static Handler handler = new Handler(Looper.getMainLooper());
    public static void runOnSubThread(Runnable runnable) {
        executor.execute(runnable);
    }

    public static void runOnUiThread(Runnable runnable) {
        handler.post(runnable);
    }
}
