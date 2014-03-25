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

/**
 * @author David Taylor
 *
 */
//import org.jasig.cas.client.authentication.AttributePrincipal;
//import org.jasig.cas.client.validation.Assertion;
//import org.jasig.cas.client.validation.Cas20ProxyTicketValidator;
//import org.jasig.cas.client.validation.TicketValidationException;
 
public class TicketValidator {
//    public final boolean validateTicket(String ticket) {
//        AttributePrincipal principal = null;
//        String casServerUrl = "https://localhost/cas-server-webapp-3.5.2";
//        Cas20ProxyTicketValidator sv = new Cas20ProxyTicketValidator(casServerUrl);
//        sv.setAcceptAnyProxy(true);
//        try {
//            // there is no need, that the legacy application is accessible
//            // through this URL. But for validation purpose, even a non-web-app
//            // needs a valid looking URL as identifier.
//            String legacyServerServiceUrl = "https://localhost:8443/client-1.0/client.html";
//            Assertion a = sv.validate(ticket, legacyServerServiceUrl);
//            principal = a.getPrincipal();
//            System.out.println("user name:" + principal.getName());
//        } catch (TicketValidationException e) {
//            e.printStackTrace(); // bad style, but only for demonstration purpose.
//        }
//        return principal != null;
//    }
}