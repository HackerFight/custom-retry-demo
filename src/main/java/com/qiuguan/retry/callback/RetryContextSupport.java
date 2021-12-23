package com.qiuguan.retry.callback;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author created by qiuguan on 2021/12/22 20:57
 */
public class RetryContextSupport implements RetryContext{

    private final Map<String, Object> attributes = new LinkedHashMap<>();

    private final RetryContext parent;

    private volatile boolean terminate = false;

    private volatile int count;

    private volatile Throwable lastException;

    public RetryContextSupport(RetryContext parent) {
        this.parent = parent;
    }

    @Override
    public RetryContext getParent() {
        return this.parent;
    }

    @Override
    public int getRetryCount() {
        return count;
    }

    @Override
    public Throwable getLastThrowable() {
        return lastException;
    }

    @Override
    public void setAttribute(String name, Object value) {
        this.attributes.put(name, value);
    }

    public void registerThrowable(Throwable throwable) {
        this.lastException = throwable;
        if (throwable != null) {
            count++;
        }
    }
}
