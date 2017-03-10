package com.peter.spring.utilities.restclient;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.Collection;
import java.util.Objects;

import org.springframework.aop.support.AopUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

/**
 * RestClientUtils contains common utilities that {@link RestClient} might be
 * needing. It's primary feature is to be able to discover the generic type
 * definition of a {@link RestMethod} and return it as {@link Type}. This type
 * definition is then further used via {@link ParameterizedTypeReference} in
 * {@link RestClientUtils#getCompleteResponseType} that can directly be used
 * with Spring's {@link RestTemplate}.
 * 
 * @author Peter Lehto
 */
public class RestClientUtils {

	/**
	 * Finds the DTO type of the response from the given {@link RestMethod}. The
	 * DTO type is not necessarily the RESULT_TYPE as the RESULT_TYPE may be a
	 * Collection of DTOs. In either case the actual DTO type is returned and
	 * possible Collection type is omitted from the middle.
	 * 
	 * @param method
	 * @return DTO type associated with given method.
	 */
	@SuppressWarnings("unchecked")
	public static <RESULT_TYPE> Class<RESULT_TYPE> findResponseDtoType(RestMethod<RESULT_TYPE> method) {
		ResolvableType genericTypeDefinition = findGenericTypeDefinition(method);
		Class<?> genericType = genericTypeDefinition.resolve();
		if (Collection.class.isAssignableFrom(genericType)) {
			Class<?> genericCollectionType = genericTypeDefinition.resolveGeneric();
			return (Class<RESULT_TYPE>) genericCollectionType;
		} else {
			return (Class<RESULT_TYPE>) genericType;
		}
	}

	/**
	 * Finds the complete RESULT_TYPE definition wrapped into a
	 * {@link ParameterizedTypeReference}. The returned type reference can
	 * further be used with REST invocation return value handling.
	 * 
	 * @param method
	 * @return {@link ParameterizedTypeReference} describing the complete
	 *         generic RESULT_TYPE of the given method.
	 */
	public static <RESULT_TYPE> ParameterizedTypeReference<RESULT_TYPE> getCompleteResponseType(
			RestMethod<RESULT_TYPE> method) {
		return new ParameterizedTypeReference<RESULT_TYPE>() {
			@Override
			public Type getType() {
				return findGenericTypeDefinition(method).getType();
			}
		};
	}

	/**
	 * Internal helper method for finding the ResolvableType that describes the
	 * generic type definition of the given {@link RestMethod} method.
	 * 
	 * @param method
	 * @return {@link ResolvableType} describing the complete generic type part
	 *         of the method.
	 */
	private static <RESULT_TYPE> ResolvableType findGenericTypeDefinition(RestMethod<RESULT_TYPE> method) {
		ResolvableType restMethodResolvableType = ResolvableType.forClass(AopUtils.getTargetClass(method));

		// Spring is just amazing! \o/
		ResolvableType restMethodBaseResolvableType = restMethodResolvableType.getSuperType();
		ResolvableType genericTypeDefinition = restMethodBaseResolvableType.getGeneric();
		return genericTypeDefinition;
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
