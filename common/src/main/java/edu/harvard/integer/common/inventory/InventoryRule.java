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

package edu.harvard.integer.common.inventory;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import edu.harvard.integer.common.BaseEntity;
import edu.harvard.integer.common.ID;

/**
 * Inventory rules configure the system with regard to the analysis to apply to
 * successive runs of the Topology and ServiceElementDiscoveryManagers.
 * 
 * @author David Taylor
 * 
 */
@Entity
public class InventoryRule extends BaseEntity {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Date and Time the InventoryRule was created.
	 */
	private Date created = null;

	/**
	 * Date and time the InventoryRule was last modified.
	 */
	private Date modified = null;

	/**
	 * Number of discovery passes that must have run before an element is deemed
	 * to be a new element in the network. Usually this will be 1 meaning that
	 * after one pass something found that had not previously existed will be
	 * deemed new and added to the system.
	 */
	private Integer numPassesNew = null;

	/**
	 * Number of discovery passes that must have run before an element is deemed
	 * to be missing in the network. Usually this will be 1 meaning that after
	 * one pass something missing that had previously existed will be deemed
	 * missing.
	 */
	private Integer numPassesMissing = null;

	/**
	 * An enumerated list of actions to take: Add - not notification Add and
	 * notify
	 */
	@Enumerated(EnumType.STRING)
	private InventoryRuleNewActionEnum newAction = null;

	/**
	 * An enumerated list of actions to take when an object has been determined
	 * to be 'missing': delete delete and notify Warn Warn and notify
	 */
	@Enumerated(EnumType.STRING)
	private InventoryRuleMissingActionEnum missingAction = null;

	/**
	 * The ID for the selection instance associated with this inventoryRule.
	 * Since there can be many inventory rules, it is simpler to have only one
	 * selection with each rule. Since inventory rules may apply to all the same
	 * selection criteria as other parts of the system, a selection object is
	 * used here to indicate the scope of systems needed. The view list will be
	 * null in the selection object when associated with an Inventory rule.
	 */
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "identifier", column = @Column(name = "selectionId")),
			@AttributeOverride(name = "idType.classType", column = @Column(name = "selectionType")),
			@AttributeOverride(name = "name", column = @Column(name = "selectionName")) })
	private ID selectionId = null;

	/**
	 * @return the created
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * @param created
	 *            the created to set
	 */
	public void setCreated(Date created) {
		this.created = created;
	}

	/**
	 * @return the modified
	 */
	public Date getModified() {
		return modified;
	}

	/**
	 * @param modified
	 *            the modified to set
	 */
	public void setModified(Date modified) {
		this.modified = modified;
	}

	/**
	 * @return the numPassesNew
	 */
	public Integer getNumPassesNew() {
		return numPassesNew;
	}

	/**
	 * @param numPassesNew
	 *            the numPassesNew to set
	 */
	public void setNumPassesNew(Integer numPassesNew) {
		this.numPassesNew = numPassesNew;
	}

	/**
	 * @return the numPassesMissing
	 */
	public Integer getNumPassesMissing() {
		return numPassesMissing;
	}

	/**
	 * @param numPassesMissing
	 *            the numPassesMissing to set
	 */
	public void setNumPassesMissing(Integer numPassesMissing) {
		this.numPassesMissing = numPassesMissing;
	}

	/**
	 * @return the newAction
	 */
	public InventoryRuleNewActionEnum getNewAction() {
		return newAction;
	}

	/**
	 * @param newAction
	 *            the newAction to set
	 */
	public void setNewAction(InventoryRuleNewActionEnum newAction) {
		this.newAction = newAction;
	}

	/**
	 * @return the missingAction
	 */
	public InventoryRuleMissingActionEnum getMissingAction() {
		return missingAction;
	}

	/**
	 * @param missingAction
	 *            the missingAction to set
	 */
	public void setMissingAction(InventoryRuleMissingActionEnum missingAction) {
		this.missingAction = missingAction;
	}

	/**
	 * @return the selectionId
	 */
	public ID getSelectionId() {
		return selectionId;
	}

	/**
	 * @param selectionId
	 *            the selectionId to set
	 */
	public void setSelectionId(ID selectionId) {
		this.selectionId = selectionId;
	}

}
