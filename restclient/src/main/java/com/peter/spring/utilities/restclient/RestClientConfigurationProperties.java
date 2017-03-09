package com.peter.spring.utilities.restclient;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * RestClientConfigurationProperties contains all such properties which can be
 * externally configured for {@link RestClient}.
 * 
 * @author Peter Lehto
 */
@ConfigurationProperties(prefix = "com.peter.rest")
public class RestClientConfigurationProperties {

	private String serverBaseUrl;

	/**
	 * @return the base URL of the server towards which all
	 *         {@link RestMethodConfiguration} URLs are relative for.
	 */
	public String getServerBaseUrl() {
		return serverBaseUrl;
	}
}
