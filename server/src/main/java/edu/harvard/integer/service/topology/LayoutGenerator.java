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

package edu.harvard.integer.service.topology;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.HashMap;

import org.apache.commons.collections15.functors.ConstantTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.integer.common.ID;
import edu.harvard.integer.common.exception.IntegerException;
import edu.harvard.integer.common.topology.InterDeviceLink;
import edu.harvard.integer.common.topology.InterNetworkLink;
import edu.harvard.integer.common.topology.MapItemPosition;
import edu.harvard.integer.common.topology.Network;
import edu.harvard.integer.common.topology.NetworkInformation;
import edu.harvard.integer.common.topology.ServiceElement;
import edu.harvard.integer.service.topology.layout.LayoutLink;
import edu.harvard.integer.service.topology.layout.LayoutNode;
import edu.harvard.integer.service.topology.layout.LayoutTypeEnum;
import edu.harvard.integer.service.topology.layout.MinimumSpanningTree;
import edu.harvard.integer.service.topology.layout.TopologyCircleLayout;
import edu.harvard.integer.service.topology.layout.TopologyFRLayout;
import edu.harvard.integer.service.topology.layout.TopologyISOMLayout;
import edu.harvard.integer.service.topology.layout.TopologyKKLayout;
import edu.harvard.integer.service.topology.layout.TopologyKKLayoutNoGravity;
import edu.harvard.integer.service.topology.layout.TopologyRadialTree;
import edu.harvard.integer.service.topology.layout.TopologyTreeLayoutInterface;
import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.PolarPoint;
import edu.uci.ics.jung.algorithms.layout.RadialTreeLayout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.algorithms.shortestpath.MinimumSpanningForest2;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;

/**
 * @author David Taylor
 * 
 */
public class LayoutGenerator {

	private static LayoutTypeEnum lastCall = LayoutTypeEnum.RandomLayout;

	private Logger logger = LoggerFactory.getLogger(LayoutGenerator.class);

	private LayoutTypeEnum layoutType = null;
	
	public LayoutGenerator( LayoutTypeEnum layoutType) {
		this.layoutType = layoutType;
		if (layoutType.equals(LayoutTypeEnum.RandomLayout))
			lastCall = LayoutTypeEnum.FRLayout;
		else
			lastCall = layoutType;
	}

	private TopologyTreeLayoutInterface getTreeLayout() {
		TopologyTreeLayoutInterface layout = null;
		
		switch (layoutType) {
		case MinimumSpanning:
			layout = new MinimumSpanningTree();
			break;

		case RadialTreeLayout:
			layout = new TopologyRadialTree();
			break;
			
		case CircleLayout:
			layout = new TopologyCircleLayout();
			break;
			
		case FRLayout:
			layout = new TopologyFRLayout();
			break;
			
		case ISOMLayout:
			layout = new TopologyISOMLayout();
			break;
			
		case KKLayout:
			layout = new TopologyKKLayout();
			break;
			
		case KKLayoutNoGravity:
			layout = new TopologyKKLayoutNoGravity();
			break;
			
		default:
			logger.error("Unsupported layout type " + layoutType);

		}
		
		return layout;
	}
	
	public HashMap<ID,MapItemPosition> generatePositions(
			NetworkInformation networkInformation) throws IntegerException {
		
		TopologyTreeLayoutInterface layout = getTreeLayout();
		if (layout == null)
			return new HashMap<ID, MapItemPosition>();
		
		HashMap<ID, LayoutNode> nodes = new HashMap<ID, LayoutNode>();
		
		
		for (final Network network : networkInformation.getNetworks()) {

			LayoutNode node = new LayoutNode();
			node.setItemId(network.getID());
			
			if (!layout.addLayoutNode(node) ) {
				logger.warn("Unable to add " + network.getID().toDebugString() + " To graph!!");
			}
			
			nodes.put(node.getItemId(), node);
			
		}
		
		for (final InterNetworkLink link : networkInformation.getLinks()) {
			LayoutLink myLink = new LayoutLink();
			myLink.setLinkId(link.getID());
			myLink.setSourceId(link.getSourceNetworkId());
		
			myLink.setDestId(link.getDestinationNetworkId());
			
			
			if (nodes.get(link.getSourceNetworkId()) == null) {
				logger.warn("Node " + link.getSourceNetworkId() + " not found for link " + link.getName());
				continue;
			}
			

			if (nodes.get(link.getDestinationNetworkId()) == null) {
				logger.warn("Node " + link.getDestinationNetworkId() + " not found for link " + link.getName());
				continue;
			}
			
			if (!layout.addLayoutLInk(myLink, nodes.get(link.getSourceNetworkId()), nodes.get(link.getDestinationNetworkId()))) {
				logger.warn("Unable to add link  from " + link.getSourceNetworkId() + " to " + link.getDestinationNetworkId() + " to graph");
			}
		}
		
		return getPositions(layout, nodes);
			
	}
	
	private HashMap<ID, MapItemPosition> getPositions(TopologyTreeLayoutInterface layout, HashMap<ID, LayoutNode> nodes) {

		layout.setSize(new Dimension(900, 800));
		layout.initialize();
		
		HashMap<ID, MapItemPosition> positions = new HashMap<ID, MapItemPosition>();

		for (ID id : nodes.keySet()) {
			LayoutNode node = nodes.get(id);
			Point2D position = layout.getPoint(node);
			
			if (position != null) {
				node.setXposition(position.getX());
				node.setYposition(position.getY());

				MapItemPosition mapItemPosition = new MapItemPosition();
				mapItemPosition.setItemId(node.getItemId());
				mapItemPosition.setXposition(position.getX());
				mapItemPosition.setYposition(position.getY());

				positions.put(id, mapItemPosition);
			} else
				logger.warn("No point found for " + id.toDebugString());
		}
		
			return positions;
	}
	
	public HashMap<ID, MapItemPosition> generatePositions(Network network)
			throws IntegerException {
		
		TopologyTreeLayoutInterface layout = getTreeLayout();
		if (layout == null)
			return new HashMap<ID, MapItemPosition>();
		
		HashMap<ID, LayoutNode> nodes = new HashMap<ID, LayoutNode>();
		
		
		for (final ServiceElement serviceElement : network.getServiceElements()) {

			LayoutNode node = new LayoutNode();
			node.setItemId(serviceElement.getID());
			node.setIconName(serviceElement.getIconName());
			
			if (!layout.addLayoutNode(node) ) {
				logger.warn("Unable to add " + serviceElement.getID().toDebugString() + " To graph!!");
			}
			
			nodes.put(node.getItemId(), node);
			
		}
		
		for (final InterDeviceLink link : network.getInterDeviceLinks()) {
			LayoutLink myLink = new LayoutLink();
			myLink.setLinkId(link.getID());
			myLink.setSourceId(link.getSourceNetworkId());
		
			myLink.setDestId(link.getDestinationNetworkId());
			
			if (!layout.addLayoutLInk(myLink, nodes.get(link.getSourceNetworkId()), nodes.get(link.getDestinationNetworkId()))) {
				logger.warn("Unable to add link  from " + link.getSourceNetworkId() + " to " + link.getDestinationNetworkId() + " to graph");
			}
		}
		
		return getPositions(layout, nodes);
			
	}
	
}
