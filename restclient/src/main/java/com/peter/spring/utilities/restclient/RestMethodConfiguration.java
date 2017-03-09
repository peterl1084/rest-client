package com.peter.spring.utilities.restclient;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.http.HttpMethod;

/**
 * RestMethodConfiguration is an annotation that should be applied on all
 * {@link RestMethod} implementations for configuring how they're executed.
 * 
 * @author Peter Lehto
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RestMethodConfiguration {

	/**
	 * @return the type of {@link HttpMethod} that should be used for invoking
	 *         the REST call.
	 */
	HttpMethod method() default HttpMethod.GET;

	/**
	 * @return relative service url to which this method invocation is directed
	 *         to.
	 */
	String serviceUrl();
}
