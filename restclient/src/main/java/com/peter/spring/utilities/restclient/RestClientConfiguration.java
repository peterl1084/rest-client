package com.peter.spring.utilities.restclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RestClientConfiguration is the means to enable using {@link RestClient} bean.
 * 
 * @author Peter Lehto
 */
@Configuration
@EnableConfigurationProperties(RestClientConfigurationProperties.class)
public class RestClientConfiguration {

	private final RestClientConfigurationProperties properties;

	@Autowired
	public RestClientConfiguration(RestClientConfigurationProperties properties) {
		this.properties = properties;
	}

	@Bean
	public RestClient provideRestClient(RestTemplateBuilder templateBuilder) {
		templateBuilder = templateBuilder.rootUri(properties.getServerBaseUrl());
		templateBuilder = templateBuilder.setConnectTimeout(properties.getConnectionTimeout());
		templateBuilder = templateBuilder.setReadTimeout(properties.getReadTimeout());
		return new DefaultRestClient(templateBuilder.build());
	}
}
