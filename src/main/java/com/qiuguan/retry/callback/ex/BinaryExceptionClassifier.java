package com.qiuguan.retry.callback.ex;

import java.util.Map;

/**
 * @author created by qiuguan on 2021/12/23 10:33
 */
public class BinaryExceptionClassifier extends SubclassClassifier<Throwable, Boolean> {


    public BinaryExceptionClassifier(boolean defaultValue) {
        super(defaultValue);
    }

    public BinaryExceptionClassifier(Map<Class<? extends Throwable>, Boolean> typeMap) {
        this(typeMap, false);
    }

    public BinaryExceptionClassifier(Map<Class<? extends Throwable>, Boolean> typeMap, boolean defaultValue) {
        super(typeMap, defaultValue);
    }
}
