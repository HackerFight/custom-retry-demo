package com.qiuguan.retry.callback;

import com.qiuguan.retry.callback.ex.BinaryExceptionClassifier;

import java.util.Collections;
import java.util.Map;

/**
 * @author created by qiuguan on 2021/12/22 19:42
 */
public class SimpleRetryPolicy implements RetryPolicy {

    public final static int DEFAULT_MAX_ATTEMPTS = 3;

    private volatile int maxRetryAttempts;

    private BinaryExceptionClassifier retryableClassifier;

    private volatile boolean useAsync;

    public SimpleRetryPolicy() {
        this(DEFAULT_MAX_ATTEMPTS, false);
    }

    public SimpleRetryPolicy(int maxAttempts, boolean useAsync) {
        this(maxAttempts, Collections
                .<Class<? extends Throwable>, Boolean> singletonMap(Exception.class, true), useAsync);
    }

    public SimpleRetryPolicy(int maxAttempts, Map<Class<? extends Throwable>, Boolean> retryableExceptions, boolean useAsync) {
        this(maxAttempts, retryableExceptions, useAsync, false);
    }


    public SimpleRetryPolicy(int maxAttempts, Map<Class<? extends Throwable>, Boolean> retryableExceptions, boolean useAsync,
                             boolean defaultValue) {
        super();
        this.maxRetryAttempts = maxAttempts;
        this.useAsync = useAsync;
        this.retryableClassifier = new BinaryExceptionClassifier(retryableExceptions, defaultValue);
    }


    @Override
    public boolean canRetry(RetryContext context) {
        Throwable t = context.getLastThrowable();
        return (t == null || retryForException(t)) && context.getRetryCount() < maxRetryAttempts;
    }

    private boolean retryForException(Throwable t) {
        return this.retryableClassifier.classify(t);
    }

    @Override
    public RetryContext open(RetryContext parent) {
        return new SimpleRetryContext(parent);
    }

    @Override
    public void close(RetryContext context) {

    }

    public int getMaxRetryAttempts() {
        return maxRetryAttempts;
    }

    public void setMaxRetryAttempts(int maxRetryAttempts) {
        this.maxRetryAttempts = maxRetryAttempts;
    }

    @Override
    public void registerThrowable(RetryContext context, Throwable throwable) {
        SimpleRetryContext simpleRetryContext = (SimpleRetryPolicy.SimpleRetryContext) context;
        simpleRetryContext.registerThrowable(throwable);
    }

    private static class SimpleRetryContext extends RetryContextSupport {

        public SimpleRetryContext(RetryContext parent) {
            super(parent);
        }
    }

    public void setUseAsync(boolean useAsync) {
        this.useAsync = useAsync;
    }

    @Override
    public boolean useAsync() {
        return this.useAsync;
    }
}
