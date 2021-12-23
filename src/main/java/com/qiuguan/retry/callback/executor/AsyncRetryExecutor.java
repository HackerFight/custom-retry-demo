package com.qiuguan.retry.callback.executor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author created by qiuguan on 2021/12/23 16:22
 */
public class AsyncRetryExecutor extends ThreadPoolTaskExecutor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        return null;
    }
}
