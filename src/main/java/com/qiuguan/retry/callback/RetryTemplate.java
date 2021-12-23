package com.qiuguan.retry.callback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author created by qiuguan on 2021/12/22 20:07
 */
public class RetryTemplate implements RetryOperations {

    private final Logger logger = LoggerFactory.getLogger(RetryTemplate.class);

    private RetryPolicy retryPolicy;


    @Override
    public <T, E extends Throwable> T execute(RetryCallback<T, E> retryCallback, RecoveryCallback<T> recoveryCallback) throws E {
        RetryPolicy retryPolicy = this.retryPolicy;
        RetryContext retryContext = null;

        Throwable lastException = null;
        try {

            retryContext = open(retryPolicy);

            while (canRetry(retryPolicy, retryContext)) {
                logger.info("retry count: {}", retryContext.getRetryCount());

                //触发匿名函数，开始调用目标方法
                try {
                    //如果没有异常，则直接到最外层的finally 中
                    return retryCallback.doWithRetry(retryContext);

                } catch (Throwable e) {
                    lastException = e;

                    try {
                        registerThrowable(retryPolicy, retryContext, e);
                    } catch (Exception ex) {
                        throw new RuntimeException("Could not register throwable", ex);
                    }

                    //可以做回推机制，比如延迟多少时间后再去重试
                }
            }

            //整个 while 执行完毕后，都没有正常返回
            return handleRetryExhausted(retryContext, recoveryCallback);

        } catch (Throwable e) {
            throw RetryTemplate.<E>wrapIfNecessary(e);
        } finally {
            close(retryPolicy, retryContext, lastException == null);
        }
    }

    private <T> T handleRetryExhausted(RetryContext retryContext, RecoveryCallback<T> recoveryCallback) throws Throwable {
        if(recoveryCallback != null) {
            T recovered = recoveryCallback.recover(retryContext);
            retryContext.setAttribute(RetryContext.RECOVERED, true);
            return recovered;
        }

        throw wrapIfNecessary(retryContext.getLastThrowable());
    }

    @Override
    public <T, E extends Throwable> T execute(RetryCallback<T, E> retryCallback) throws E {
        return execute(retryCallback, null);
    }

    protected void close(RetryPolicy retryPolicy, RetryContext retryContext, boolean succeed) {
        if(succeed) {
            retryPolicy.close(retryContext);
            retryContext.setAttribute(RetryContext.CLOSED, true);
        }
    }

    private static <E extends Throwable> E wrapIfNecessary(Throwable throwable) {
        if (throwable instanceof Error) {
            throw (Error) throwable;
        } else if (throwable instanceof Exception) {
            @SuppressWarnings("unchecked")
            E rethrow = (E) throwable;
            return rethrow;
        } else {
            throw new RuntimeException("Exception in retry", throwable);
        }
    }

    private void registerThrowable(RetryPolicy retryPolicy, RetryContext retryContext, Throwable e) {
        retryPolicy.registerThrowable(retryContext, e);
    }

    protected boolean canRetry(RetryPolicy retryPolicy, RetryContext context) {
        return retryPolicy.canRetry(context);
    }

    public RetryPolicy getRetryPolicy() {
        return retryPolicy;
    }

    public void setRetryPolicy(RetryPolicy retryPolicy) {
        this.retryPolicy = retryPolicy;
    }

    protected RetryContext open(RetryPolicy retryPolicy) {
        return retryPolicy.open(null);
    }
}
