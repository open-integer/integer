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
package edu.harvard.integer.access.snmp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.AbstractTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TableEvent;
import org.snmp4j.util.TableUtils;

import edu.harvard.integer.access.element.ElementEndPoint;
import edu.harvard.integer.common.exception.CommonErrorCodes;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.exception.NetworkErrorCodes;
import edu.harvard.integer.common.type.displayable.NonLocaleErrorMessage;
import edu.harvard.integer.common.util.DisplayableInterface;


/**
 * The Class SnmpService is an singleton class to provide SNMP communication service.
 * It also contains methods for different type of SNMP requests.
 * 
 */
final public class SnmpService
{
	
	/** The Constant logger. */
	static final Logger logger = LoggerFactory.getLogger(SnmpService.class);
	
    /** The _snmp service used by Integer application */
    private static  SnmpService _snmpService;
    
    /** The _snmp is a SNMP4j object for SNMP communication. */
    private Snmp _snmp;
    
    
    /**
     * Instantiates a new snmp service.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private SnmpService() throws IOException
    {
        @SuppressWarnings("rawtypes")
		TransportMapping transport = new DefaultUdpTransportMapping();
        _snmp = new Snmp(transport);
        _snmp.listen();
    }
    
    /**
     * Instance.
     *
     * @return the snmp service
     */
    public static SnmpService instance()
    {
        if ( _snmpService == null )
        {
            try
            {
                _snmpService = new SnmpService();
            } 
            catch (IOException e)
            {
            	
            	e.printStackTrace();
                throw new RuntimeException( "IO error when create SnmpService " + e.toString());
            }
        }
        return _snmpService;
    }
    
    
    
    
    /**
     * Make a SNMP GET request.
     *
     * @param endPoint the end point
     * @param pdu the pdu
     * @return the pdu
     * @throws IntegerException the integer exception
     */
    public PDU getPdu( ElementEndPoint endPoint, PDU pdu) throws IntegerException
    {
        AbstractTarget target = SnmpCollectionUtil.createTarget(endPoint, true);
        pdu.setType(PDU.GET);        
        return sendPdu(pdu, target);
        
    }
    
    
    /**
     * Make a SNMP get request asynchronously.
     *
     * @param endPoint the end point
     * @param pdu the pdu
     * @return the pdu
     * @throws IntegerException the integer exception
     */
    public void getAsyncPdu( ElementEndPoint endPoint, PDU pdu,
    		                ResponseListener listener ) throws IntegerException
    {
        AbstractTarget target = SnmpCollectionUtil.createTarget(endPoint, true);
        pdu.setType(PDU.GET);        
        sendAsyncPdu(pdu, target, listener);
        
    }
    
    
  
    /**
     * Make a SNMP get request asynchronously. Application can pass in a user object for later reference.
     * 
     * @param endPoint
     * @param pdu
     * @param listener
     * @param userData
     * @throws IntegerException
     */
    public void getAsyncPdu( ElementEndPoint endPoint, PDU pdu,
    		                ResponseListener listener, Object userData ) throws IntegerException
    {
        AbstractTarget target = SnmpCollectionUtil.createTarget(endPoint, true);
        pdu.setType(PDU.GET);        
        sendAsyncPdu(pdu, target, listener, userData );
        
    }
    
    
    /**
     * Send SNMP request to device.
     *
     * @param pdu the pdu
     * @param target the target
     * @return the pdu
     * @throws IntegerException the integer exception
     */
    public PDU sendPdu( PDU pdu, AbstractTarget target  ) throws IntegerException {
    	
    	ResponseEvent response = null;
    	
    	try {
			response = _snmp.send(pdu, target);
		} catch (IOException e) {
			throw new IntegerException(null, NetworkErrorCodes.CannotReach, 
	           		   new DisplayableInterface[] { new NonLocaleErrorMessage(e.getLocalizedMessage()) });
		}
    	assertPDU(response);
        PDU rpdu = response.getResponse();
    	return rpdu;
    }
    
    
    
    /**
     * Make a SNMP4j table event request.
     * 
     * @param endPoint
     * @param columns
     * @return
     * @throws IntegerException 
     */
    public List<TableEvent> getTablePdu( ElementEndPoint endPoint, OID[] columns ) throws IntegerException {
    	
    	AbstractTarget target = SnmpCollectionUtil.createTarget(endPoint, true);  	
    	TableUtils tableUtils = new TableUtils(_snmp, new DefaultPDUFactory());
    	
    	List<TableEvent> tblEvents = tableUtils.getTable(target, columns, null, null);
    	for ( TableEvent te : tblEvents ) {
 
    		if ( te.isError() ) {
    			
    			throw new IntegerException(null, NetworkErrorCodes.SNMPError, 
 	           		   new DisplayableInterface[] { new NonLocaleErrorMessage(te.getErrorMessage()) });
    			
    		}
    	}
    	return tblEvents;      
    	
    }
    
    
    
    
    /**
     * This method is used to do SNMP response verification. Since SNMP request does not contains partial 
     * success, if there is any error occurs, it will throws an exception.
     * 
     * @param response
     * @throws IntegerException
     */
    public static void assertPDU( ResponseEvent response ) throws IntegerException {
    	
        PDU rpdu = response.getResponse();   	
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
           IntegerException ie = new IntegerException(null, NetworkErrorCodes.SNMPNoSuchError, 
        		   new DisplayableInterface[] { new NonLocaleErrorMessage(sb.toString()) });
           throw ie;
        }
        else if ( rpdu != null && rpdu.getErrorStatus() == 16 )
        {
        	IntegerException ie = new IntegerException(null, NetworkErrorCodes.AuthError, 
         		   new DisplayableInterface[] { new NonLocaleErrorMessage(rpdu.get(0).getVariable().toString()) });
        	throw ie;
        }
        else if (rpdu != null && rpdu.getErrorStatus() != 0)
        {   
        	IntegerException ie = new IntegerException(null, NetworkErrorCodes.SNMPError, 
          		   new DisplayableInterface[] { new NonLocaleErrorMessage(rpdu.getErrorStatusText()) });
        	throw ie;
        }
        else if ( rpdu == null )
        {
        	IntegerException ie = new IntegerException(null, NetworkErrorCodes.CannotReach);
        	throw ie;
        }   

    	/**
    	 * Nothing to be determined, check if the peer address occurs or not.
    	 */
    	if ( response.getPeerAddress() == null ) {
    		IntegerException ie = new IntegerException(null, NetworkErrorCodes.CannotReach);
        	throw ie;    		
    	}    	
    }
    
    
    
    /**
     * Send asynchronous PDU. The response is on "listener" for the request.
     * 
     * @param pdu
     * @param target
     * @param listener
     * @throws IntegerException
     */
    public void sendAsyncPdu( PDU pdu, 
    		                  AbstractTarget target,
    		                  ResponseListener listener ) throws IntegerException {
    	
    	sendAsyncPdu(pdu, target, listener, null);
    }
  
    
    /**
     * Send asynchronous PDU. The response is on "listener"
     * 
     * @param pdu
     * @param target
     * @param listener
     * @param userHandler
     * @throws IntegerException
     */
    public void sendAsyncPdu( PDU pdu, 
            AbstractTarget target,
            ResponseListener listener,
            Object userHandler) throws IntegerException 
    {	
          try {
              _snmp.send(pdu, target, userHandler, listener);
          } 
          catch (IOException e) {
        	  throw new IntegerException(e, CommonErrorCodes.IOError);
          }
     }
    
    
    
    /**
     * Gets the next pdu.
     *
     * @param endPoint the end point
     * @param pdu the pdu
     * @return the next pdu
     * @throws IntegerException the integer exception
     */
    public PDU getNextPdu( ElementEndPoint endPoint, 
                            PDU pdu ) throws IntegerException
    {
        AbstractTarget target =  SnmpCollectionUtil.createTarget(endPoint, true);
        pdu.setType(PDU.GETNEXT);
        return sendPdu(pdu, target);
        
    }
    
    
    
    /**
     * Gets the all entry pdu by SNMP getNext.
     *
     * @param endPoint the end point
     * @param pdu the pdu
     * @return the all entry pdu by next
     * @throws IntegerException the integer exception
     */
    public ArrayList<PDU>   getAllEntryPduByNext( ElementEndPoint endPoint, 
                                                  PDU pdu ) throws IntegerException
    {
        VariableBinding vb = pdu.get(0);
        String checkOid = vb.getOid().toString();
        
        ArrayList<PDU> retList = new ArrayList<PDU>();
        PDU rpdu = pdu;
        
        AbstractTarget target = SnmpCollectionUtil.createTarget(endPoint, true);
        while ( rpdu != null && rpdu.get(0).getOid().toString().indexOf(checkOid) >= 0 )
        {        
            rpdu.setType(PDU.GETNEXT);
            rpdu = sendPdu(rpdu, target);
            retList.add( new PDU(rpdu) );
            
        }        
        return retList;    
    }
    
    
    
    
    /**
     * Send SNMPSet request.
     *
     * @param endPoint the end point
     * @param pdu the pdu
     * @return the pdu
     * @throws IntegerException the integer exception
     */
    public PDU setPdu( ElementEndPoint endPoint, PDU pdu ) throws IntegerException
    {       
        AbstractTarget target = SnmpCollectionUtil.createTarget(endPoint, false);
        pdu.setType(PDU.SET);
        PDU rpdu = sendPdu(pdu, target);
        
        return rpdu;
    }
    
    
    
    /**
     * Make a SNMP  getBulk request..
     *
     * @param endPoint the end point
     * @param pdu the pdu
     * @return the bulk pdu
     * @throws IntegerException the integer exception
     */
    public PDU getBulkPdu( ElementEndPoint endPoint, 
            PDU pdu ) throws IntegerException
   {
    	return getBulkPdu( endPoint, pdu, 0, 1);
   }
    
    
    
    /**
     * Make a SNMP getBulk request.
     *
     * @param endPoint the end point
     * @param pdu the pdu
     * @param nonRepeaters the non repeaters
     * @param maxReperters the max reperters
     * @return the bulk pdu
     * @throws IntegerException the integer exception
     */
    public PDU getBulkPdu( ElementEndPoint endPoint, 
                           PDU pdu,
                           int nonRepeaters,
                           int maxReperters ) throws IntegerException
    {        
    	AbstractTarget target = SnmpCollectionUtil.createTarget(endPoint, true);
    	pdu.setType(PDU.GETBULK);
    	pdu.setNonRepeaters(nonRepeaters);
        pdu.setMaxRepetitions(maxReperters);
         
         PDU rpdu = sendPdu(pdu, target);
         return rpdu;
    }
    

}

