package com.peter.spring.utilities.restclient;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

import org.springframework.aop.support.AopUtils;
import org.springframework.core.ParameterizedTypeReference;
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
	 * Finds the DTO type of response from the given RestMethod. The DTO type is
	 * not necessarily the RESULT_TYPE as the RESULT_TYPE may be a
	 * Collection<DTO>. In either case the actual DTO type is returned.
	 * 
	 * @param method
	 * @return Class describing the DTO that this method would return.
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

	@SuppressWarnings("unchecked")
	public static <RESULT_TYPE> Optional<Class<? extends Collection<RESULT_TYPE>>> findResponseCollectionType(
			RestMethod<RESULT_TYPE> method) {
		ResolvableType genericTypeDefinition = findGenericTypeDefinition(method);
		Class<?> genericType = genericTypeDefinition.resolve();
		if (Collection.class.isAssignableFrom(genericType)) {
			return Optional.of((Class<? extends Collection<RESULT_TYPE>>) genericType);
		}

		return Optional.empty();
	}

	public static <RESULT_TYPE> ParameterizedTypeReference<RESULT_TYPE> getCompleteResponseType(
			RestMethod<RESULT_TYPE> method) {
		return new ParameterizedTypeReference<RESULT_TYPE>() {
			@Override
			public Type getType() {
				return findGenericTypeDefinition(method).getType();
			}
		};
	}

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
