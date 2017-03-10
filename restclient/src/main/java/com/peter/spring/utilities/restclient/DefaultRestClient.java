package com.peter.spring.utilities.restclient;

import java.util.Objects;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

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
	public <RESULT_TYPE, METHOD extends RestMethod<RESULT_TYPE>> RESULT_TYPE invoke(METHOD method) {
		String methodUrl = RestClientUtils.parseAndParametrizeRestMethodUri(method);
		Class<RESULT_TYPE> responseType = RestClientUtils.findResponseType(method);
		return null;
	}
	
}
