package com.qiuguan.retry.ann;

import java.lang.annotation.*;

/**
 * @author created by qiuguan on 2021/12/22 16:54
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface Retryable {

    Class<? extends Throwable>[] value() default {};

    int maxAttempts() default 3;

    boolean async() default false;

}
