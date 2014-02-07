/*
 * 
 */
package edu.harvard.integer.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>IntegerService</code>.
 */
public interface IntegerServiceAsync {

	/**
	 * Mib import.
	 *
	 * @param fileName the file name
	 * @param mib the mib
	 * @param standardMib the standard mib
	 * @param callback the callback
	 */
	void mibImport(String fileName, String mib, boolean standardMib, AsyncCallback<String> callback);

}