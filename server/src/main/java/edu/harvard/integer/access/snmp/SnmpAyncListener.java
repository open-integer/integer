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
package edu.harvard.integer.agent.serviceelement.access;

import org.snmp4j.PDU;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.smi.VariableBinding;

import edu.harvard.integer.common.exception.ErrorCodeInterface;
import edu.harvard.integer.common.exception.NetworkErrorCodes;

/**
 * The listener interface for receiving snmpAync events.
 * The class that is interested in processing a snmpAync
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addSnmpAyncListener<code> method. When
 * the snmpAync event occurs, that object's appropriate
 * method is invoked.
 *
 * @author dchan
 */
public abstract class SnmpAyncListener implements ResponseListener {

	/* (non-Javadoc)
	 * @see org.snmp4j.event.ResponseListener#onResponse(org.snmp4j.event.ResponseEvent)
	 */
	@Override
	public void onResponse(ResponseEvent event) {

		PDU rpdu = event.getResponse();
		ErrorCodeInterface eCode = null;
    	String errMsg = null;
    	
    	if ( rpdu != null && rpdu.getErrorStatus() == 2 )
        {                 	                     
           StringBuffer sb = new StringBuffer();
           for ( int j=0; j<rpdu.size(); j++ )
           {
            	VariableBinding v = rpdu.get(j);
            	sb.append(v.getOid().toString() + " " );
            	if ( j > 2 )
            	{
            		break;
            	}
            }
           eCode = NetworkErrorCodes.SNMPNoSuchError;
           errMsg = sb.toString();
        }
        else if ( rpdu != null && rpdu.getErrorStatus() == 16 )
        {
        	eCode = NetworkErrorCodes.AuthError;
        	errMsg = rpdu.get(0).getVariable().toString();
        }
        else if (rpdu != null && rpdu.getErrorStatus() != 0)
        {   
        	eCode = NetworkErrorCodes.SNMPError;
        	errMsg = rpdu.getErrorStatusText();
        }
        else if ( rpdu == null )
        {
        	eCode = NetworkErrorCodes.SNMPError;
        }   
    	applicationResponse(rpdu, eCode, errMsg);
	}
	
	/**
	 * Application response. If error is not null, the response pdu is not valid.
	 *
	 * @param pdu the pdu
	 * @param error the error
	 * @param errMsg the err msg
	 */
	public abstract void applicationResponse( PDU pdu,  ErrorCodeInterface error, String errMsg );

}
