///*
// *  Copyright (c) 2014 Harvard University and the persons
// *  identified as authors of the code.  All rights reserved. 
// *
// *  Redistribution and use in source and binary forms, with or without
// *  modification, are permitted provided that the following conditions are
// *  met:
// * 
// * 	.    Redistributions of source code must retain the above copyright
// * 		 notice, this list of conditions and the following disclaimer.
// * 
// * 	.    Redistributions in binary form must reproduce the above copyright
// * 		 notice, this list of conditions and the following disclaimer in the
// * 		 documentation and/or other materials provided with the distribution.
// * 
// * 	.    Neither the name of Harvard University, nor the names of specific
// * 		 contributors, may be used to endorse or promote products derived from
// * 		 this software without specific prior written permission.
// * 
// * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
// * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
// * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
// * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
// * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
// * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
// * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
// * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
// * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
// * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
// * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
// *      
// */
//
package edu.harvard.integer.cas.filter;


import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.util.CommonUtils;

/**
 * @author David Taylor
 * 
 */
public class WildflyWebAuthFilter extends AbstractCasFilter {
	
	public void doFilter(final ServletRequest servletRequest,
			final ServletResponse servletResponse, final FilterChain chain)
			throws IOException, ServletException {
		
		final HttpServletRequest request = (HttpServletRequest) servletRequest;
		final HttpServletResponse response = (HttpServletResponse) servletResponse;
		final HttpSession session = request.getSession();
		final String ticket = CommonUtils.safeGetParameter(request,
				getArtifactParameterName());

		log.info("=================== Wildfly Web Auth Filter ===================== Ticket " + ticket);
				
		
		TicketValidator tv = new TicketValidator();
		String name = tv.validateTicket(ticket);
		log.info("User " + name + " Ticket " + ticket);
		//session.setAttribute("Name", name);
		
//		if (session != null
//				&& session.getAttribute(CONST_CAS_ASSERTION) == null
//				&& ticket != null) {
	//		try {
				final String service = constructServiceUrl(request, response);
				log.info("Attempting CAS ticket validation with service="
						+ service + " and ticket=" + ticket);

				log.info("user " + request.getParameter("username"));
				
//
//				Enumeration attributeNames = session.getAttributeNames();
//				while(attributeNames.hasMoreElements()) {
//					Object nextElement = attributeNames.nextElement();
//					log.info("1 Attribute " + nextElement + session.getAttribute((String) nextElement));
//				}
//				
//				 try {
//				
//				 request.login(service, ticket);
//				 } catch (ServletException e) {
//				 log.debug("JBoss Web authentication failed.");
//				 throw new
//				 GeneralSecurityException("JBoss Web authentication failed.");
//				 }
//				
//				
//				if (request.getUserPrincipal() instanceof AssertionPrincipal) {
//					final AssertionPrincipal principal = (AssertionPrincipal) request
//							.getUserPrincipal();
//					log.debug("Installing CAS assertion into session.");
//					session.setAttribute(CONST_CAS_ASSERTION,
//							principal.getAssertion());
//				} else {
//					log.debug("Aborting -- principal is not of type AssertionPrincipal");
////					throw new GeneralSecurityException(
////							"JBoss Web authentication did not produce CAS AssertionPrincipal.");
//				}
////			} catch (final GeneralSecurityException e) {
////				response.sendError(HttpServletResponse.SC_FORBIDDEN,
////						e.getMessage());
////			}
//		} else if (session != null && request.getUserPrincipal() == null) {
//			// There is evidence that in some cases the principal can disappear
//			// in JBoss despite a valid session.
//			// This block forces consistency between principal and assertion.
//			log.info("User principal not found.  Removing CAS assertion from session to force reauthentication.");
//			session.removeAttribute(CONST_CAS_ASSERTION);
//		}

//		chain.doFilter(request, response);
				
//		log.info("Request User " + request.getUserPrincipal());
//		Enumeration attributeNames = session.getAttributeNames();
//		while(attributeNames.hasMoreElements()) {
//			Object nextElement = attributeNames.nextElement();
//			log.info("2 Attribute " + nextElement + session.getAttribute((String) nextElement));
//		}

	}

}
