package com.peter.spring.utilities.restclient;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.http.HttpMethod;

/**
 * RestMethod is the super interface for all RestMethods that need to be invoked
 * through {@link RestClient}
 * 
 * @author Peter Lehto
 *
 * @param <RESULT_TYPE>
 *            type of the item (dto) that invoking this method should return.
 *            May also be Void if invoking this method does not return any
 *            objects.
 */
public interface RestMethod<RESULT_TYPE> {

	void addParameter(String name, String value);

	void addParameter(String name, int value);

	void addParameter(String name, long value);

	void addParameter(String name, double value);

	void addParameter(String name, LocalDate value);

	/**
	 * @return Map<String, ?> of given parameters where the key is the name of
	 *         the parameter.
	 */
	Map<String, ?> getParameterMap();

	/**
	 * @return the {@link HttpMethod} defined in {@link RestMethodConfiguration}
	 *         annotation placed on the class of this RestMethod instance.
	 */
	default HttpMethod getHttpMethod() {
		if (!getClass().isAnnotationPresent(RestMethodConfiguration.class)) {
			throw new RuntimeException(getClass().getCanonicalName() + " is not annotated with "
					+ RestMethodConfiguration.class.getCanonicalName());
		}

		return getClass().getAnnotation(RestMethodConfiguration.class).method();
	}
}
