package me.wondertwo.csu.async;


import me.wondertwo.csu.async.listener.NotifyListener;

import java.util.concurrent.Callable;

/**
 *
 * Created by wondertwo on 2016/4/6.
 */
public class AsyncTaskWarpper {

    private static AsyncTaskWarpper asyncTaskWarpper;

    private AsyncTaskWarpper() {

    }

    public static synchronized AsyncTaskWarpper getATWInstance() {
        if (null == asyncTaskWarpper) {
            asyncTaskWarpper = new AsyncTaskWarpper();
        }
        return asyncTaskWarpper;
    }

    public AsyncTaskWork doAsyncWork(final Runnable work,
                                     NotifyListener cancelListener,
                                     NotifyListener finishListener) {
        AsyncTaskWork asyncWork = new AsyncTaskWork(new Callable<Object>() {

            @Override
            public Object call() throws Exception {
                work.run();
                return null;
            }
        }, true, cancelListener, finishListener);
        asyncWork.executeTask();
        return asyncWork;
    }

    public AsyncTaskWork doAsyncWork(final Callable work,
                                     NotifyListener cancelListener,
                                     NotifyListener finishListener) {
        AsyncTaskWork asyncWork = new AsyncTaskWork(work, true,
                cancelListener, finishListener);
        asyncWork.executeTask();
        return asyncWork;
    }

    public AsyncTaskWork doAsyncWork(final Runnable work,
                                     NotifyListener cancelListener,
                                     NotifyListener progresslListener,
                                     NotifyListener finishListener) {
        AsyncTaskWork asyncWork = new AsyncTaskWork(new Callable<Object>() {

            @Override
            public Object call() throws Exception {
                work.run();
                return null;
            }
        }, true, cancelListener, progresslListener, finishListener);
        asyncWork.executeTask();
        return asyncWork;
    }

    public AsyncTaskWork doAsyncWork(final Callable<Object> work,
                                     NotifyListener cancelListener,
                                     NotifyListener progresslListener,
                                     NotifyListener finishListener) {
        AsyncTaskWork asyncWork = new AsyncTaskWork(work, true,
                cancelListener, progresslListener, finishListener);
        asyncWork.executeTask();
        return asyncWork;
    }

    public AsyncTaskWork doNotCancelableAsyncWork(final Callable<Object> work,
                                                  NotifyListener finishListener) {
        AsyncTaskWork asyncWork = new AsyncTaskWork(work, false, finishListener);
        asyncWork.executeTask();
        return asyncWork;
    }
}
