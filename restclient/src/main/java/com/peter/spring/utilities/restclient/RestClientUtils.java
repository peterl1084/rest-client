package com.peter.spring.utilities.restclient;

import java.net.URI;
import java.util.Collection;
import java.util.Objects;

import org.springframework.aop.support.AopUtils;
import org.springframework.core.ResolvableType;
import org.springframework.web.util.UriTemplate;

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

	/**
	 * Parses the serviceUrl from given restMethod.
	 * 
	 * @param restMethod
	 * @return serviceUrl that can be used in context with baseUrl.
	 * @throws IllegalArgumentException
	 *             if the type of given restMethod does not declare
	 *             {@link RestMethodConfiguration} annotation.
	 */
	public static <RESULT_TYPE> String parseAndParametrizeRestMethodUri(RestMethod<RESULT_TYPE> restMethod) {
		Class<?> targetClass = AopUtils.getTargetClass(Objects.requireNonNull(restMethod));
		if (!targetClass.isAnnotationPresent(RestMethodConfiguration.class)) {
			throw new IllegalArgumentException(targetClass.getCanonicalName() + " does not declare "
					+ RestMethodConfiguration.class.getSimpleName() + " annotation");
		}

		RestMethodConfiguration configuration = targetClass.getAnnotation(RestMethodConfiguration.class);
		String uri = configuration.serviceUri();

		UriTemplate uriTemplate = new UriTemplate(uri);
		URI expandedUri = uriTemplate.expand(restMethod.getParameterMap());
		return expandedUri.toString();
	}
}
