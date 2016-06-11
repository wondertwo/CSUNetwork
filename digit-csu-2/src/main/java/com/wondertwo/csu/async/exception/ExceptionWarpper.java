package com.wondertwo.csu.async.exception;

/**
 * Created by wondertwo on 2016/4/6.
 */
public class ExceptionWarpper extends Exception {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    public static final int NO_ERROR = 0;
    public static final int UNKNOWN_ERROR = 1;

    protected int mExceptionType = 0;
    protected Object mExceptionData = null;

    public ExceptionWarpper(String message, Throwable throwable) {
        this(UNKNOWN_ERROR, message, throwable);
    }

    public ExceptionWarpper(int type, String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
        mExceptionType = type;
        mExceptionData = null;
    }

    public ExceptionWarpper(Throwable throwable) {
        super(throwable);
    }

    public ExceptionWarpper(int type) {
        this(type, "", null);
    }

    public ExceptionWarpper(int mExceptionType, String detailMessage, Object mExceptionData) {
        super(detailMessage);
        this.mExceptionType = mExceptionType;
        this.mExceptionData = mExceptionData;
    }

    public int getmExceptionType() {
        return mExceptionType;
    }

    public Object getmExceptionData() {
        return mExceptionData;
    }
}
