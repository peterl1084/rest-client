package com.peter.spring.utilities.restclient;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

/**
 * AbstractRestMethod contains the very basic functionality of any
 * {@link RestMethod}. This base class should most likely be used for
 * implementing any {@link RestMethod}.
 * 
 * @author Peter Lehto
 *
 * @param <RESULT_ITEM_TYPE>
 */
public class AbstractRestMethod<RESULT_ITEM_TYPE> implements RestMethod<RESULT_ITEM_TYPE> {

	private Map<String, String> parameterMap;

	public AbstractRestMethod() {
		parameterMap = new HashMap<>();
	}

	@Override
	public void addParameter(String name, String value) {
		assertParameters(name, value);
		parameterMap.put(name, value);
	}

	@Override
	public void addParameter(String name, Number value) {
		throw new UnsupportedOperationException("Implementation missing");
	}

	@Override
	public void addParameter(String name, LocalDate value) {
		throw new UnsupportedOperationException("Implementation missing");
	}

	/**
	 * Checks that there are given parameters, that they are not null and not
	 * empty.
	 * 
	 * @param params
	 * @throws IllegalArgumentException
	 *             if given params is null, empty or contains a single parameter
	 *             that is null or empty String.
	 */
	private void assertParameters(String... params) throws IllegalArgumentException {
		if (params == null) {
			throw new IllegalArgumentException("No parameters specified");
		}

		if (params.length == 0) {
			throw new IllegalArgumentException("Empty parameter array specified");
		}

		Arrays.asList(params).forEach(parameter -> {
			if (!StringUtils.hasText(parameter)) {
				throw new IllegalArgumentException("Empty parameter");
			}
		});
	}
}
