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

package edu.harvard.integer.service.distribution;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.exception.SystemErrorCodes;
import edu.harvard.integer.service.BaseManagerInterface;

/**
 * @author David Taylor
 * @param <ManagerTypeEnum>
 *
 */
public abstract class CallWithRetry<T, M extends BaseManagerInterface> {

	private int numberOfTrys = 0;
	private int count = 0;
	private int numberOfDeadlocks = 0;
	
	private Logger logger = LoggerFactory.getLogger(CallWithRetry.class);
	
	private List<IntegerException> errors = null;
	
	private ManagerTypeEnum managerType = null;
	
	public CallWithRetry() {
		
	}
	
	public CallWithRetry(int numberOfTrys, Class<M> mgrClass) {
		this.numberOfTrys = numberOfTrys;
		this.managerType = DistributionManager.getManagerType(mgrClass);	
	}
	
	public int getNumberOfTrys() {
		return count;
	}
	
	public  T invokeCall() throws IntegerException {
		T retValue = null;
		count = 0;
		
		 M manager = null;
		
		while (count++ < numberOfTrys) {
			 try {
				
				 manager = DistributionManager.getManager(managerType);
				 
				 retValue = call(manager);
			
				 if (count > 1 && logger.isDebugEnabled()) {
					 logger.debug("Found " + count + " Errors before action succeded!");
				 }
				 
				 return retValue;
				
			 } catch (IntegerException e) {
				 saveError(e); 
				
				 System.err.println("IntegerException cought! " + e.toString() + " try " + count);
				 
				 if (count < numberOfTrys)
					 continue;
				 
				 logger.error("Unexpeced error processing " + e.toString(), e);

			 } catch (Throwable e) {
				 System.err.println("Exception cought! " + e.toString() + " try " + count);
				 
				saveError(new IntegerException(e, SystemErrorCodes.UnknownException));
				
				if (count < numberOfTrys)
					 continue;
				
				e.printStackTrace();
			 } finally {
				 manager = null;
			 }
		}
		
	
		System.err.println("Unable to complete call!! Errors prevended completion ");
		for (IntegerException error : errors) {
			System.err.println(error.getCause().toString());
			error.getCause().printStackTrace();
		}
		
		// Throw the last exception.
		throw errors.get(0);
		
	}
	
	public int getNumberOfDeadlocks() {
		return numberOfDeadlocks;
	}
	
	private void saveError(IntegerException e) {
		
		if (errors == null)
			errors = new ArrayList<IntegerException>();
		
		errors.add(e);
		
	}

	public IntegerException[] getExceptions() {
		if (errors == null)
			return new IntegerException[0];
		
		return (IntegerException[]) errors.toArray(new IntegerException[errors.size()]);
	}
	
	
	protected abstract T call(M manager) throws IntegerException;
}
