package com.qiuguan.retry.config;

import com.qiuguan.retry.ann.EnableRetry;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;
import org.springframework.context.annotation.AutoProxyRegistrar;

/**
 * @author created by qiuguan on 2021/12/22 17:46
 */
public class AutoProxySelector extends AdviceModeImportSelector<EnableRetry> {

    @Override
    protected String[] selectImports(AdviceMode adviceMode) {
        switch (adviceMode) {
            case PROXY:
                return new String[]{AutoProxyRegistrar.class.getName(),
                        ProxyRetryConfiguration.class.getName()};
            case ASPECTJ:
                throw new UnsupportedOperationException("unsupport operation");
            default:
                return null;
        }
    }
}
