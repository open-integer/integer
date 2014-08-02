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

package edu.harvard.integer.common.yaml.vendorcontainment;

import java.util.List;

import edu.harvard.integer.common.yaml.YamlSnmpAssociation;

/**
 * @author David Taylor
 * 
 */
public class YamlSnmpLevelOID {

	private String name = null;

	private String contextOID = null;

	private String descriminatorOID = null;
	
	private String globalDescriminatorOID = null;

	private List<YamlSnmpLevelOID> children = null;

	/**
	 * List of SnmpServiceElementTypeDiscriptors for this SnmpLevel
	 */
	private List<YamlSnmpServiceElementTypeDiscriminator> disriminators = null;

	/**
	 * SnmpContainmentRelation definition to map the ServiceElement from this
	 * level to a different ServiceElement. Ex. entAliasMappingTable. Maps the
	 * logical components and physical components to and extrernal to the entity
	 * MIB object. entAliasMappingIdentifier.33.0 = ifIndex.6
	 */
	private YamlSnmpContainmentRelation containmentRelationship = null;
	
	private YamlSnmpParentChildRelationship parentChildRelationship = null;
	
	private List<YamlSnmpAssociation>  associations;

	private String category = null;
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the contextOID
	 */
	public String getContextOID() {
		return contextOID;
	}

	/**
	 * @param contextOID
	 *            the contextOID to set
	 */
	public void setContextOID(String contextOID) {
		this.contextOID = contextOID;
	}

	/**
	 * @return the descriminatorOID
	 */
	public String getDescriminatorOID() {
		return descriminatorOID;
	}

	/**
	 * @param descriminatorOID
	 *            the descriminatorOID to set
	 */
	public void setDescriminatorOID(String descriminatorOID) {
		this.descriminatorOID = descriminatorOID;
	}

	/**
	 * @return the children
	 */
	public List<YamlSnmpLevelOID> getChildren() {
		return children;
	}

	/**
	 * @param children
	 *            the children to set
	 */
	public void setChildren(List<YamlSnmpLevelOID> children) {
		this.children = children;
	}

	/**
	 * @return the disriminators
	 */
	public List<YamlSnmpServiceElementTypeDiscriminator> getDisriminators() {
		return disriminators;
	}

	/**
	 * @param disriminators
	 *            the disriminators to set
	 */
	public void setDisriminators(
			List<YamlSnmpServiceElementTypeDiscriminator> disriminators) {
		this.disriminators = disriminators;
	}

	/**
	 * @return the containmentRelationship
	 */
	public YamlSnmpContainmentRelation getContainmentRelationship() {
		return containmentRelationship;
	}

	/**
	 * @param containmentRelationship the containmentRelationship to set
	 */
	public void setContainmentRelationship(YamlSnmpContainmentRelation containmentRelationship) {
		this.containmentRelationship = containmentRelationship;
	}

	/**
	 * @return the parentChildRelationship
	 */
	public YamlSnmpParentChildRelationship getParentChildRelationship() {
		return parentChildRelationship;
	}

	/**
	 * @param parentChildRelationship the parentChildRelationship to set
	 */
	public void setParentChildRelationship(YamlSnmpParentChildRelationship parentChildRelationship) {
		this.parentChildRelationship = parentChildRelationship;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}


	/**
	 * 
	 * @return
	 */
	public String getGlobalDescriminatorOID() {
		return globalDescriminatorOID;
	}

	/**
	 * 
	 * @param globalDescriminatorOID
	 */
	public void setGlobalDescriminatorOID(String globalDescriminatorOID) {
		this.globalDescriminatorOID = globalDescriminatorOID;
	}

	
	public List<YamlSnmpAssociation> getAssociations() {
		return associations;
	}

	public void setAssociations(List<YamlSnmpAssociation> associations) {
		this.associations = associations;
	}
}
