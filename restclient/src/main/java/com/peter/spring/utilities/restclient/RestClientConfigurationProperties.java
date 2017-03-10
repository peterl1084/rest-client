package com.peter.spring.utilities.restclient;

import java.util.Objects;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * RestClientConfigurationProperties contains all such properties which can be
 * externally configured for {@link RestClient}.
 * 
 * @author Peter Lehto
 */
@ConfigurationProperties(prefix = "rest.client")
public class RestClientConfigurationProperties {

	private String serverBaseUrl;
	private Integer connectionTimeout;
	private Integer readTimeout;

	/**
	 * @return the base URL of the server towards which all
	 *         {@link RestMethodConfiguration} URLs are relative for.
	 */
	public String getServerBaseUrl() {
		return Objects.requireNonNull(serverBaseUrl, "Server URL is not specified in application properties with "
				+ getConfigurationKeyPrefix() + ".serverBaseUrl");
	}

	public void setServerBaseUrl(String serverBaseUrl) {
		this.serverBaseUrl = serverBaseUrl;
	}

	public Integer getConnectionTimeout() {
		return Objects.requireNonNull(connectionTimeout,
				"Connection timeout is not specified in application properties with " + getConfigurationKeyPrefix()
						+ ".connectionTimeout");
	}

	public void setConnectionTimeout(Integer connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public Integer getReadTimeout() {
		return Objects.requireNonNull(readTimeout, "Read timeout is not specified in application properties with "
				+ getConfigurationKeyPrefix() + ".readTimeout");
	}

	public void setReadTimeout(Integer readTimeout) {
		this.readTimeout = readTimeout;
	}

	private String getConfigurationKeyPrefix() {
		return getClass().getAnnotation(ConfigurationProperties.class).prefix();
	}
}
