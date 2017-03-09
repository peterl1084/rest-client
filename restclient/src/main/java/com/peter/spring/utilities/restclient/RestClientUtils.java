package com.peter.spring.utilities.restclient;

import java.util.Collection;

import org.springframework.aop.support.AopUtils;
import org.springframework.core.ResolvableType;

/**
 * RestClientUtils contains common utilities that {@link RestClient} might be
 * needing.
 * 
 * @author Peter Lehto
 */
public class RestClientUtils {

	/**
	 * Finds the RESULT_TYPE type of given {@link RestMethod}. The type is
	 * detected from the generic type provided for RestMethod. If Generic type
	 * is Collection the internal generic type is also inspected and returned.
	 * 
	 * @param method
	 * @return Class<RESULT_TYPE> describing what kind of item (dto) the method
	 *         should return.
	 */
	@SuppressWarnings("unchecked")
	public static <RESULT_TYPE> Class<RESULT_TYPE> findResponseType(RestMethod<RESULT_TYPE> method) {
		ResolvableType restMethodResolvableType = ResolvableType.forClass(AopUtils.getTargetClass(method));

		// Spring is just amazing! \o/
		ResolvableType restMethodBaseResolvableType = restMethodResolvableType.getSuperType();
		ResolvableType genericTypeDefinition = restMethodBaseResolvableType.getGeneric();
		Class<?> genericType = genericTypeDefinition.resolve();
		if (Collection.class.isAssignableFrom(genericType)) {
			Class<?> genericCollectionType = genericTypeDefinition.resolveGeneric();
			return (Class<RESULT_TYPE>) genericCollectionType;
		} else {
			return (Class<RESULT_TYPE>) genericType;
		}
	}
}
