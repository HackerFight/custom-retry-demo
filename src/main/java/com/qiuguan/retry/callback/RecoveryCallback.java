package com.qiuguan.retry.callback;

/**
 * @author created by qiuguan on 2021/12/23 15:02
 */
public interface RecoveryCallback<T> {

    /**
     * 全部重试后，仍然失败了，是否需要进一步处理
     * @param retryContext
     * @return
     * @throws Exception
     */
    T recover(RetryContext retryContext) throws Exception;
}
