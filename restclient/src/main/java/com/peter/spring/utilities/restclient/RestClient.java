package com.peter.spring.utilities.restclient;

/**
 * RestClient is the main entry point for all REST communication. RestClient is
 * intended to simply execute {@link RestMethod}s and returning their
 * corresponding return type of data.
 * 
 * @author Peter Lehto
 */
public interface RestClient {

	/**
	 * Invokes given {@link RestMethod} with the parameters that the particular
	 * instance might contain.
	 * 
	 * @param method
	 * @return an object of the type of RESULT_TYPE.
	 */
	<RESULT_TYPE, METHOD extends RestMethod<RESULT_TYPE>> RESULT_TYPE invoke(METHOD method);
}
