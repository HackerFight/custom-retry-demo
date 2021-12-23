package com.qiuguan.retry.callback.ex;

import java.io.Serializable;

public interface Classifier<C, T> extends Serializable {

	/**
	 * Classify the given object and return an object of a different type, possibly an
	 * enumerated type.
	 * 
	 * @param classifiable the input object. Can be null.
	 * @return an object. Can be null, but implementations should declare if this is the
	 * case.
	 */
	T classify(C classifiable);

}