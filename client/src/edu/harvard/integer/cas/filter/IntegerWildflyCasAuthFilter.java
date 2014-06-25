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

package edu.harvard.integer.cas.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.integer.util.LoggerUtil;

/**
 * @author David Taylor
 *
 */
public class IntegerWildflyCasAuthFilter extends Cas20ProxyReceivingTicketValidationFilter {

	private Logger logger = LoggerFactory.getLogger(IntegerWildflyCasAuthFilter.class);
	
	/* (non-Javadoc)
	 * @see org.jasig.cas.client.validation.AbstractTicketValidationFilter#onSuccessfulValidation(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.jasig.cas.client.validation.Assertion)
	 */
	@Override
	protected void onSuccessfulValidation(HttpServletRequest request,
			HttpServletResponse response, Assertion assertion) {
		
		AttributePrincipal principal = assertion.getPrincipal();
		logger.info("========== User " + principal.getName() + "        ==================");
		logger.info("== Request User " + request.getUserPrincipal() + " ==================");
		logger.info("=== Remote User " + LoggerUtil.filterLog(request.getRemoteUser()) + "    ==================");
		logger.info("=== Remote Host " + LoggerUtil.filterLog(request.getRemoteHost()) + "    ==================");

		
		super.onSuccessfulValidation(request, response, assertion);
	}

	
	
}
