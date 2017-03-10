package com.peter.spring.utilities.restclient;

import java.util.Objects;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * DefaultRestClient is the simple default implementation of {@link RestClient}.
 * 
 * @author Peter Lehto
 */
class DefaultRestClient implements RestClient {

	private final RestTemplate restTemplate;

	public DefaultRestClient(RestTemplate restTemplate) {
		this.restTemplate = Objects.requireNonNull(restTemplate);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <RESULT_TYPE, METHOD extends RestMethod<RESULT_TYPE>> RESULT_TYPE invoke(METHOD method) {
		String methodUrl = RestClientUtils.parseAndParametrizeRestMethodUri(method);
		ParameterizedTypeReference<RESULT_TYPE> completeResponseType = RestClientUtils.getCompleteResponseType(method);

		ResponseEntity<?> response = restTemplate.exchange(methodUrl, method.getHttpMethod(), null,
				completeResponseType);
		return (RESULT_TYPE) response.getBody();
	}

	protected RestTemplate getRestTemplate() {
		return restTemplate;
	}
}
