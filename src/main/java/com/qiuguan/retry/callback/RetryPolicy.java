package com.qiuguan.retry.callback;

/**
 * @author created by qiuguan on 2021/12/22 19:40
 */
public interface RetryPolicy {

    /**
     * 能否重试
     * @param context the current retry status
     * @return true if the operation can proceed
     */
    boolean canRetry(RetryContext context);

    /**
     * Acquire resources needed for the retry operation. The callback is passed
     * in so that marker interfaces can be used and a manager can collaborate
     * with the callback to set up some state in the status token.
     * @param parent the parent context if we are in a nested retry.
     *
     * @return a {@link RetryContext} object specific to this policy.
     *
     */
    RetryContext open(RetryContext parent);

    /**
     * @param context a retry status created by the
     * {@link #open(RetryContext)} method of this policy.
     */
    void close(RetryContext context);

    /**
     * Called once per retry attempt, after the callback fails.
     *
     * @param context the current status object.
     * @param throwable the exception to throw
     */
    void registerThrowable(RetryContext context, Throwable throwable);

    boolean useAsync();
}
