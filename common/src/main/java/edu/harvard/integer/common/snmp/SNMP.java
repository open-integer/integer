/*
 *  Copyright (c) 2013 Harvard University and the persons
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

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.topology.ServiceElementManagementObject;

/**
 * @author David Taylor
 * 
 *         This class holds the definition of an SNMP OID.
 * 
 */
@Entity
public class SNMP extends ServiceElementManagementObject implements Serializable {
	/**
	 * Serialization version
	 */
	private static final long serialVersionUID = 1L;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "identifier", column = @Column(name = "snmpModuleId")),
			@AttributeOverride(name = "idType.classType", column = @Column(name = "snmpModuleType")),
			@AttributeOverride(name = "name", column = @Column(name = "snmpModuleName")) })
	private ID snmpModuleId = null;

	/*
	 * Description of the object as found in the MIB Module. In some cases, this
	 * description can be quite long.
	 */
	@Size(min=1, max=5000)
	private String description = null;

	/*
	 * This defines the read, read/write, or some objects (bad objects) are
	 * write only. This is a function of the object definition, not the access
	 * policy.
	 */
	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private MaxAccess maxAccess = null;

	/*
	 * The fully specified OID of the object (minus the instance data). For
	 * tables this would include the table glue.
	 */
	private String oid = null;

	/*
	 * From the object definition.
	 */
	private String textualConvetion = null;

	@ManyToOne
	private SnmpSyntax syntax = null;
	
	/*
	 * In some cases this information is not appropriate (orther than say
	 * string). In others degrees, or other information may be useful. In some
	 * cases this may come from or be equal to the Textual convention
	 * information.
	 */
	private String units = null;

	/**
	 * This boolean is used to indicate the SNMP object is a scalar VB or not.
	 */
	private Boolean scalarVB = null;
	
	public SNMP() {
		super();
	}
	
	/**
	 * @return the snmpModuleId
	 */
	public ID getSnmpModuleId() {
		return snmpModuleId;
	}

	/**
	 * @param snmpModuleId
	 *            the snmpModuleId to set
	 */
	public void setSnmpModuleId(ID snmpModuleId) {
		this.snmpModuleId = snmpModuleId;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the maxAccess
	 */
	public MaxAccess getMaxAccess() {
		return maxAccess;
	}

	/**
	 * @param maxAccess
	 *            the maxAccess to set
	 */
	public void setMaxAccess(MaxAccess maxAccess) {
		this.maxAccess = maxAccess;
	}

	/**
	 * @return the oid
	 */
	public String getOid() {
		return oid;
	}

	/**
	 * @param oid
	 *            the oid to set
	 */
	public void setOid(String oid) {
		this.oid = oid;
	}

	/**
	 * @return the textualConvetion
	 */
	public String getTextualConvetion() {
		return textualConvetion;
	}

	/**
	 * @param textualConvetion
	 *            the textualConvetion to set
	 */
	public void setTextualConvetion(String textualConvetion) {
		this.textualConvetion = textualConvetion;
	}

	/**
	 * @return the units
	 */
	public String getUnits() {
		return units;
	}

	/**
	 * @param units
	 *            the units to set
	 */
	public void setUnits(String units) {
		this.units = units;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SNMP [oid=" + oid + ", displayName=" + getName() + "]";
	}
	
	
	public Boolean getScalarVB() {
		return scalarVB;
	}

	public void setScalarVB(Boolean scalarVB) {
		this.scalarVB = scalarVB;
	}

	/**
	 * @return the syntax
	 */
	public SnmpSyntax getSyntax() {
		return syntax;
	}

	/**
	 * @param syntax the syntax to set
	 */
	public void setSyntax(SnmpSyntax syntax) {
		this.syntax = syntax;
	}
	

	// TODO: Add constraints. Could be min/max or max or enum list or ..

}
