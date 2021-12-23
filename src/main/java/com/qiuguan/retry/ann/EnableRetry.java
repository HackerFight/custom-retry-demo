package com.qiuguan.retry.ann;

import com.qiuguan.retry.config.AutoProxySelector;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author created by qiuguan on 2021/12/22 16:53
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(AutoProxySelector.class)
public @interface EnableRetry {

    boolean proxyTargetClass() default false;

    AdviceMode mode() default AdviceMode.PROXY;
}
