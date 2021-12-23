package com.qiuguan.retry.callback;

/**
 * @author created by qiuguan on 2021/12/22 17:38
 */
public interface RetryCallback<T, E extends Throwable> {

    /**
     * 重试
     * @param context
     * @return
     * @throws E
     */
    T doWithRetry(RetryContext context) throws E;

}
