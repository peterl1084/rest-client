package com.peter.spring.utilities.restclient;

import java.time.LocalDate;

/**
 * RestMethod is the super interface for all RestMethods that need to be invoked
 * through {@link RestClient}
 * 
 * @author Peter Lehto
 *
 * @param <RESULT_ITEM_TYPE>
 *            type of the item (dto) that invoking this method should return.
 *            May also be Void if invoking this method does not return any
 *            objects.
 */
public interface RestMethod<RESULT_ITEM_TYPE> {

	void addParameter(String name, String value);

	void addParameter(String name, Number value);

	void addParameter(String name, LocalDate value);
}
