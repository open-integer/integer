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
package edu.harvard.integer.agent.serviceelement.discovery;

import java.util.ArrayList;
import java.util.List;

import edu.harvard.integer.agent.serviceelement.Authentication;
import edu.harvard.integer.agent.serviceelement.ElementAccessTask;
import edu.harvard.integer.agent.serviceelement.ElementEndPoint;
import edu.harvard.integer.agent.serviceelement.access.AccessPort;
import edu.harvard.integer.common.topology.ServiceElement;


/**
 * The Class ElementDiscoverTask is used to discover IP network node.
 *
 * @author dchan
 */
public class ElementDiscoverTask extends ElementAccessTask<DiscoverNode> {

	
	/** The callback client during discovery. */
	private ElementDiscoverCB<ServiceElement>  cb; 
	
	/**
	 * Extra authentication used for discover.
	 */
	final private List<Authentication> otherAuths = new ArrayList<>();
	
	/**
	 * Extra ports used for discover.
	 */
	final private List<AccessPort> otherPorts = new ArrayList<>();
	
	
	/**
	 * Instantiates a new element discover task.
	 *
	 * @param cb the callback client
	 * @param elmEpt the network node service element.
	 */
	public ElementDiscoverTask( ElementDiscoverCB<ServiceElement> cb, ElementEndPoint elmEpt ) {
		
		super(elmEpt);
		this.cb = cb;
	}
	
	
	/**
	 * Add another port for discover.
	 *
	 * @param port the port
	 */
	public void addOtherPort( AccessPort port ) {
		otherPorts.add(port);
	}
	
	
	/**
	 * Add another auth for discover.
	 *
	 * @param auth the auth
	 */
	public void addOtherAuth( Authentication auth ) {
		otherAuths.add(auth);
	}
	
	
	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public DiscoverNode call() throws Exception {
		
	
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
