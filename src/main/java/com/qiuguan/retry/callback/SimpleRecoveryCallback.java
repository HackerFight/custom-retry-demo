package com.qiuguan.retry.callback;

/**
 * @author created by qiuguan on 2021/12/23 15:10
 */
public class SimpleRecoveryCallback implements RecoveryCallback<Object>{


    @Override
    public Object recover(RetryContext retryContext) throws Exception {
        return "retry count: " + (retryContext.getRetryCount() + 1) + " please handle";
    }
}
