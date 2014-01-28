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

package edu.harvard.integer.common.snmp;

import javax.persistence.Entity;

import edu.harvard.integer.common.BaseEntity;

/**
 * @author David Taylor
 * 
 *         Holder for MIB's that have been or are in the process of being
 *         imported into the Integer system. Once the MIB has been import a
 *         MIBImportResult will be created that contins the result of the
 *         import.
 */
@Entity
public class MIBImportInfo extends BaseEntity {

	/**
	 * Name of the MIB as given by the user.
	 */
	private String fileName = null;

	/**
	 * contents of the MIB.
	 */
	private String mib = null;

	/**
	 * Flag that if set == true to indicate that this is a standard MIB.
	 */
	private boolean standardMib = false;

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the mib
	 */
	public String getMib() {
		return mib;
	}

	/**
	 * @param mib
	 *            the mib to set
	 */
	public void setMib(String mib) {
		this.mib = mib;
	}

	/**
	 * @return the standardMib
	 */
	public boolean isStandardMib() {
		return standardMib;
	}

	/**
	 * @param standardMib
	 *            the standardMib to set
	 */
	public void setStandardMib(boolean standardMib) {
		this.standardMib = standardMib;
	}

}
