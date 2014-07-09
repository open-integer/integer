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

package edu.harvard.integer.service.yaml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.topology.Category;
import edu.harvard.integer.common.yaml.vendorcontainment.YamlCategory;
import edu.harvard.integer.service.persistance.dao.topology.CategoryDAO;

/**
 * This helper class is used to parse the YAML Category tree. The parseCategory
 * does the parsing. The printCategory is used to display the parsed YAML.
 * 
 * @author David Taylor
 * 
 */
public class YamlCategoryParser {

	private HashMap<String, YamlCategory> yamlCategories = null;

	private HashMap<String, Category> dbCategories = null;

	private CategoryDAO dao = null;

	/**
	 * 
	 * @param yamlCategories
	 * @param dbCategories
	 * @param dao
	 */
	public YamlCategoryParser(HashMap<String, YamlCategory> yamlCategories,
			HashMap<String, Category> dbCategories, CategoryDAO dao) {
		super();
		this.yamlCategories = yamlCategories;
		this.dbCategories = dbCategories;
		this.dao = dao;
	}

	/**
	 * Print the parsed YAML file. The database ID's will be displayed for all
	 * categories that are read in.
	 * 
	 * @param load
	 * @param string
	 *            . Indent to use for this category. This will get added to for
	 *            child categories so the children are indented.
	 * 
	 * @param categories
	 */
	public void printCategories(YamlCategory load, String indent) {

		StringBuffer b = new StringBuffer();

		Category dbCategory = dbCategories.get(load.getName().toLowerCase());
		if (dbCategory != null) {
			b.append(indent).append(dbCategory.getID().toDebugString())
					.append('\n');

			b.append(indent).append("Description: ")
					.append(dbCategory.getDescription()).append('\n');

			if (dbCategory.getChildIds() != null) {
				b.append(indent).append("Children: ");

				for (ID childId : dbCategory.getChildIds()) {
					b.append(" ").append(childId.getName()).append(" - ")
							.append(childId.getIdentifier());
				}

				b.append('\n');
			}
		}

		System.out.println(b.toString());

		if (load.getCategories() != null) {
			for (YamlCategory child : load.getCategories()) {
				printCategories(child, indent + "    ");
			}
		}

	}

	/**
	 * Parse the YAML file. Create or update the database with data found in
	 * YAML file.
	 * 
	 * 
	 * @param yamlCategory
	 * @return ID. database ID of the Category read in.
	 * 
	 * @throws IntegerException
	 */
	public ID parseCategory(YamlCategory yamlCategory) throws IntegerException {

		List<ID> childIds = new ArrayList<ID>();

		if (yamlCategory.getCategories() != null) {

			for (YamlCategory child : yamlCategory.getCategories()) {

				// pass layer and discovery down to child categories if not set
				// on the child
				if (yamlCategory.getLayer() != null && child.getLayer() == null)
					child.setLayer(yamlCategory.getLayer());

				if (yamlCategory.getDiscovery() != null
						&& child.getDiscovery() == null)
					child.setDiscovery(yamlCategory.getDiscovery());

				ID childId = parseCategory(child);
				if (childId != null)
					childIds.add(childId);
			}
		}

		Category category = null; // dao.findByName(yamlCategory.getName());
		if (category == null) {
			category = new Category();
			category.setName(yamlCategory.getName());
		}

		category.setDescription(yamlCategory.getDescription());
		if (category.getChildIds() == null)
			category.setChildIds(new ArrayList<ID>());

		category.getChildIds().addAll(childIds);

		category = dao.update(category);

		return category.getID();
	}

	/**
	 * Parse the category list to link the parent category list to the proper
	 * parents. This is the first step in parsing the YAML file.
	 * 
	 * @param yamlCategory
	 * @throws IntegerException
	 */
	public void parseParentCategory(YamlCategory yamlCategory)
			throws IntegerException {

		yamlCategories.put(yamlCategory.getName().toLowerCase(), yamlCategory);

		if (yamlCategory.getCategories() != null) {
			List<YamlCategory> childCategories = new ArrayList<YamlCategory>();
			childCategories.addAll(yamlCategory.getCategories());

			for (YamlCategory child : childCategories) {

				parseParentCategory(child);
			}
		}

		if (yamlCategory.getParents() != null) {
			for (String parentName : yamlCategory.getParents()) {
				YamlCategory parentCategory = yamlCategories.get(parentName);
				if (parentCategory.getCategories() == null) {
					parentCategory.setCategories(new ArrayList<YamlCategory>());
				}

				parentCategory.getCategories().add(yamlCategory);
			}
		}

	}
}
