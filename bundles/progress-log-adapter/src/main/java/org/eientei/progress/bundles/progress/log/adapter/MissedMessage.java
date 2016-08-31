package org.eientei.progress.bundles.progress.log.adapter;

/**
 * Created by Alexander Tumin on 2016-08-31
 */
public class MissedMessage {
    private int severirty;
    private String msg;
    private Throwable throwable;

    public MissedMessage(int severirty, String msg, Throwable throwable) {
        this.severirty = severirty;
        this.msg = msg;
        this.throwable = throwable;
    }

    public MissedMessage(int i, String s) {
        this.severirty = i;
        this.msg = s;
    }

    public int getSeverirty() {
        return severirty;
    }

    public String getMsg() {
        return msg;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
