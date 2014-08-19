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

package edu.harvard.integer.service.selection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;

import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.IDType;
import edu.harvard.integer.common.selection.FilterNode;
import edu.harvard.integer.common.user.Location;

/**
 * @author David Taylor
 * 
 */
public class LocationTreeBuilder {

	private Location[] locations = null;

	private int numberOfLevels = 0;
	
	private String[] levelNames = null;

	private String[] levelValues = null;
	
	private Method[] getMethods = null;

	private FilterNode[] levelNodes = null;
	
	private Logger logger = null;

	public LocationTreeBuilder(Location[] locations, String[] levelNames,
			Logger logger) {
		this.locations = locations;
		this.levelNames = levelNames;
		this.logger = logger;
		this.numberOfLevels = levelNames.length;
		
		levelNodes = new FilterNode[numberOfLevels];
		
		levelValues = new String[numberOfLevels];
		
		getMethods = new Method[numberOfLevels];
		for (int i = 0; i < numberOfLevels; i++ ) {
			
			try {
				getMethods[i] = Location.class.getMethod("get" + levelNames[i]);
			} catch (SecurityException e) {

				e.printStackTrace();
			} catch (NoSuchMethodException e) {

				e.printStackTrace();
			}
		}
	}

	public List<FilterNode> createLocationTree() {

		if (locations == null || locations.length == 0)
			return null;

		List<FilterNode> children = new ArrayList<FilterNode>();
		
		for (int i = 0; i < numberOfLevels; i++) {

			levelValues[i] = getValueFromLocation(locations[0], getMethods[i]);
			
			FilterNode node = new FilterNode();
			node.setName(levelValues[i]);
			node.setItemId(new ID(Long.valueOf(0), node.getName(), new IDType(Location.class.getName())));
			node.setIdentifier(node.getItemId().getIdentifier());
			node.setChildren(new ArrayList<FilterNode>());
			
			levelNodes[i] = node;
			
			if (logger.isDebugEnabled())
				logger.debug("Create new level levelValue " + levelValues[i]);
		}
		
		
		for (int index = 0; index < locations.length; index++) {
			Location location = locations[index];
	
			String[] currentLevels = new String[levelNames.length];
		
			for (int i = 0; i < numberOfLevels; i++) {
				currentLevels[i] = getValueFromLocation(locations[index], getMethods[i]);
			}
			
			if (logger.isDebugEnabled())
				logger.debug("Create leaf node " + location.getName() + " Levels " + Arrays.toString(levelValues) + " Current " + Arrays.toString(currentLevels));
			FilterNode levelNode = new FilterNode();
			levelNode.setIdentifier(location.getIdentifier());
			levelNode.setName(location.getName());
			levelNode.setItemId(location.getID());
			
			if (currentLevels[numberOfLevels - 1].equals(levelValues[numberOfLevels - 1]))
				levelNodes[numberOfLevels - 1].getChildren().add(levelNode);
			else {
				for (int idx = numberOfLevels - 1; idx >= 0; idx--) {
					if (!currentLevels[idx].equals(levelValues[idx])) {
						if (idx > 0)
							levelNodes[idx - 1].getChildren().add(levelNodes[idx]);
						else
							children.add(levelNodes[0]);

						if (logger.isDebugEnabled())
							logger.debug("Create new level " + (idx) + " currentValues " + currentLevels[idx]);
						FilterNode node2 = new FilterNode();
						node2.setName(currentLevels[idx]);
						node2.setItemId(new ID(Long.valueOf(0), currentLevels[idx], new IDType(Location.class.getName())));
						node2.setIdentifier(node2.getItemId().getIdentifier());
						node2.setChildren(new ArrayList<FilterNode>());
						if (numberOfLevels - 1 == idx)
							node2.getChildren().add(levelNode);

						levelNodes[idx] = node2;			
						levelValues[idx] = currentLevels[idx];	
					} 

				}
			}
							
		}
		
		children.add(levelNodes[0]);
		
		return children;
	}

	private String getValueFromLocation(Location location, Method method) {
		try {
			String value = (String) method.invoke(location);
			if (value == null)
				return location.getID().getIdentifier() + ":" + method.getName();
			else
				return value;

		} catch (IllegalAccessException e) {

			e.printStackTrace();
		} catch (IllegalArgumentException e) {

			e.printStackTrace();
		} catch (InvocationTargetException e) {

			e.printStackTrace();
		}

		return "";
	}

}
