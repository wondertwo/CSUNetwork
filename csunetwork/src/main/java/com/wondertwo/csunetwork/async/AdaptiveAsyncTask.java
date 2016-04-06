package com.wondertwo.csunetwork.async;

import android.os.AsyncTask;
import android.os.Build;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by wondertwo on 2016/4/6.
 */
public abstract class AdaptiveAsyncTask<Params, Progress, Result> extends
        AsyncTask<Params, Progress, Result> {

    private static final int CPU_COUT = Runtime.getRuntime().availableProcessors();

    private static final Executor FIX_EXECUTORS = Executors.newFixedThreadPool(Math.max(CPU_COUT, 5));

    /**
     * 3.0以上系统默认使用SerialExecutor，是单线程池，这里将并发同时请求数量增大。
     */
    public AsyncTask<Params, Progress, Result> executeTask(Params... params) {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return super.execute(params);
        } else {
            return super.executeOnExecutor(FIX_EXECUTORS, params);
        }
    }
}
