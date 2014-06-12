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

package edu.harvard.integer.util;

/**
 * Utility methods to help with creating log messages.
 * 
 * @author David Taylor
 * 
 */
public class LoggerUtil {

	/**
	 * Remove all CRLF chars from the log message.
	 * 
	 * CRLF Injection(1 flaw) Description Fix Required by Policy Veracode
	 * Detailed Report prepared for Harvard University - Jun 11, 2014. The
	 * acronym CRLF stands for "Carriage Return, Line Feed" and refers to the
	 * sequence of characters used to denote the end of a line of text. CRLF
	 * injection vulnerabilities occur when data enters an application from an
	 * untrusted source and is not properly validated before being used. For
	 * example, if an attacker is able to inject a CRLF into a log file, he
	 * could append falsified log entries, thereby misleading administrators or
	 * cover traces of the attack. If an attacker is able to inject CRLFs into
	 * an HTTP response header, he can use this ability to carry out other
	 * attacks such as cache poisoning. CRLF vulnerabilities primarily affect
	 * data integrity.
	 * 
	 * 
	 * @param message
	 * @return
	 */
	public static String filterLog(String message) {
		return message.replace("\n", "").replace("\r", "");
	}
}
