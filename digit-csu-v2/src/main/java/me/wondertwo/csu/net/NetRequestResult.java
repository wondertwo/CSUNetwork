package me.wondertwo.csu.net;

import me.wondertwo.csu.async.AsyncTaskWork;

/**
 * NetRequestResult，网络请求返回的结果集
 *
 * Created by wondertwo on 2016/4/6.
 */
public class NetRequestResult {

    private int what;
    private Object[] args;
    private AsyncTaskWork srcTask;

    public NetRequestResult(int what, Object... args) {
        this.setWhat(what);
        this.setArgs(args);
    }

    public int getWhat() {
        return what;
    }

    public void setWhat(int what) {
        this.what = what;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public AsyncTaskWork getSrcTask() {
        return srcTask;
    }

    public void setSrcTask(AsyncTaskWork srcTask) {
        this.srcTask = srcTask;
    }
}
