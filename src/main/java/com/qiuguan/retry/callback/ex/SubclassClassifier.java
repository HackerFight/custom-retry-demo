package com.qiuguan.retry.callback.ex;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author created by qiuguan on 2021/12/22 17:38
 */
public class SubclassClassifier<T, C> implements Classifier<T, C> {

	private final ConcurrentMap<Class<? extends T>, C> classified;

	private C defaultValue = null;

	@Override
	public C classify(T classifiable) {
		if (classifiable == null) {
			return defaultValue;
		}

		@SuppressWarnings("unchecked")
		Class<? extends T> exceptionClass = (Class<? extends T>) classifiable.getClass();
		if (this.classified.containsKey(exceptionClass)){
			return this.classified.get(exceptionClass);
		}

		//check for subclass
		C value = null;
		for (Class<?> cls = exceptionClass; !cls.equals(Object.class) && value == null; cls = cls.getSuperclass()){
			value = this.classified.get(cls);
		}

		if (value != null) {
			this.classified.put(exceptionClass, value);
		}

		if(value == null) {
			return defaultValue;
		}

		return value;
	}

	public SubclassClassifier() {
		this(null);
	}

	/**
	 * Create a {@link SubclassClassifier} with supplied default value.
	 * @param defaultValue the default value
	 */
	public SubclassClassifier(C defaultValue) {
		this(new HashMap<Class<? extends T>, C>(), defaultValue);
	}

	/**
	 * Create a {@link SubclassClassifier} with supplied default value.
	 * @param defaultValue the default value
	 * @param typeMap the map of types
	 */
	public SubclassClassifier(Map<Class<? extends T>, C> typeMap, C defaultValue) {
		super();
		this.classified = new ConcurrentHashMap<Class<? extends T>, C>(typeMap);
		this.defaultValue = defaultValue;
	}
}