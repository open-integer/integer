/*
 *  Copyright (c) 2014 Harvard University and the persons
 *  identified as authors of the code.  All rights reserved. 
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are
 *  met:
 * 
 * 	.    Redistributions of source code must retain the above copyright
 * 		 notice, this list of conditions and the following disclaimer.
 * 
 * 	.    Redistributions in binary form must reproduce the above copyright
 * 		 notice, this list of conditions and the following disclaimer in the
 * 		 documentation and/or other materials provided with the distribution.
 * 
 * 	.    Neither the name of Harvard University, nor the names of specific
 * 		 contributors, may be used to endorse or promote products derived from
 * 		 this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *      
 */

package edu.harvard.integer.common.exception;

import java.text.MessageFormat;
import java.util.Locale;

import edu.harvard.integer.common.util.DisplayableInterface;

/**
 * 
 * All exceptions created by the Integer system will be an IntegerException. The
 * exception will take an ErrorCode and optional arguments. The cause of the
 * exception may be includes when known.
 * 
 * @author David Taylor
 * 
 * 
 */
public class IntegerException extends Exception implements DisplayableInterface {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Throwable cause = null;
	private ErrorCodeInterface errorCode = null;
	private DisplayableInterface[] arguments = null;

	public IntegerException(Throwable cause, ErrorCodeInterface errorCode) {
		super(cause);

		this.errorCode = errorCode;
	}

	/**
	 * Create an exception with error code and arguments for the message of the
	 * exception.
	 * 
	 * @param cause
	 * @param errorCode
	 * @param arguments
	 */
	public IntegerException(Throwable cause, ErrorCodeInterface errorCode,
			DisplayableInterface[] arguments) {

		super(errorCode.getErrorCode(), cause);
		this.arguments = arguments;
		this.errorCode = errorCode;
	}

	/**
	 * Create a localized message for this exception.
	 */
	public String toDisplayString(Locale locale) {
		// TODO: the message must be converted to a localized message. Once the
		// bundles are added this must be implemented.

		if (errorCode != null && errorCode.getErrorCode() != null
				&& errorCode.getErrorCode().indexOf("{0}") > 0) {
			MessageFormat mf = new MessageFormat(errorCode.getErrorCode());
			return mf.format(arguments);
		} else
			return errorCode.toString();
	}

	/**
	 * Return the arguments for this exception.
	 * 
	 * @return
	 */
	public DisplayableInterface[] getMessageArguments() {
		return arguments;
	}

	/**
	 * @return the cause
	 */
	public Throwable getCause() {
		return cause;
	}

	/**
	 * @param cause
	 *            the cause to set
	 */
	public void setCause(Throwable cause) {
		this.cause = cause;
	}

	/**
	 * @return the errorCode
	 */
	public ErrorCodeInterface getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode
	 *            the errorCode to set
	 */
	public void setErrorCode(ErrorCodeInterface errorCode) {
		this.errorCode = errorCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#toString()
	 */
	@Override
	public String toString() {

		return toDisplayString(Locale.US);
	}

}
