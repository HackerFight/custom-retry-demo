package com.qiuguan.retry.callback.executor;

import com.qiuguan.retry.callback.RetryContext;

/**
 * @author created by qiuguan on 2021/12/23 16:34
 */
@FunctionalInterface
public interface AsyncFilter {

    /**
     * 过滤
     * @return
     */
    boolean filter(RetryContext retryContext);
}
