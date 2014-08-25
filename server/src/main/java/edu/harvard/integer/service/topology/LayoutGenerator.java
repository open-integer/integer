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
import java.util.Map;

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
import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.DAGLayout;
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

	private static int lastCall = 0;

	private Logger logger = LoggerFactory.getLogger(LayoutGenerator.class);

	public LayoutGenerator() {

	}

	public NetworkInformation generatePositions(
			NetworkInformation networkInformation) throws IntegerException {

		Graph<LayoutNode, LayoutLink> graph = null;
		Forest<LayoutNode, LayoutLink> forest = new DelegateForest<LayoutNode, LayoutLink>();

		graph = new SparseMultigraph<LayoutNode, LayoutLink>();

		HashMap<ID, LayoutNode> nodeMap = new HashMap<ID, LayoutNode>();

		LayoutNode rootNode = null;

		for (final Network network : networkInformation.getNetworks()) {

			LayoutNode node = new LayoutNode();
			node.setItemId(network.getID());
			
			if (network.getName() != null && network.getName().equals("0.0.0.0")) 
				rootNode = node;
			
			graph.addVertex(node);
			forest.addVertex(node);

			nodeMap.put(node.getItemId(), node);
		}

		for (final InterNetworkLink link : networkInformation.getLinks()) {
			LayoutLink myLink = new LayoutLink();
			myLink.setLinkId(link.getID());
			myLink.setSourceId(link.getSourceNetworkId());
			myLink.setDestId(link.getDestinationNetworkId());

			graph.addEdge(myLink, nodeMap.get(link.getSourceNetworkId()),
					nodeMap.get(link.getDestinationNetworkId()));
			forest.addEdge(myLink, nodeMap.get(link.getSourceNetworkId()),
					nodeMap.get(link.getDestinationNetworkId()));
		}

		AbstractLayout<LayoutNode, LayoutLink> layout = null;
		RadialTreeLayout<LayoutNode, LayoutLink> forestLayout = null;
		TreeLayout<LayoutNode, LayoutLink> treeLayout = null;

		logger.info("Layout method " + lastCall);

		switch (lastCall) {
		case 0:
			layout = new KKLayout<LayoutNode, LayoutLink>(graph);
			lastCall++;
			break;

		case 1:
			layout = new KKLayout<LayoutNode, LayoutLink>(graph);
			((KKLayout<LayoutNode, LayoutLink>) layout)
					.setAdjustForGravity(false);
			lastCall++;
			break;

		case 2:
			layout = new CircleLayout<LayoutNode, LayoutLink>(graph);
			lastCall++;
			break;

		case 3:
			layout = new ISOMLayout<LayoutNode, LayoutLink>(graph);
			lastCall++;
			break;

		case 4:
//			layout = new DAGLayout<LayoutNode, LayoutLink>(graph);
//			if (rootNode != null)
//				((DAGLayout<LayoutNode, LayoutLink>) layout).setRoot(rootNode);
//			
//			lastCall++;
//			break;

		case 5:
			forestLayout = new RadialTreeLayout<LayoutNode, LayoutLink>(forest);
			lastCall++;
			break;

		case 6:
			MinimumSpanningForest2<LayoutNode, LayoutLink> prim = new MinimumSpanningForest2<LayoutNode, LayoutLink>(
					graph, new DelegateForest<LayoutNode, LayoutLink>(),
					DelegateTree.<LayoutNode, LayoutLink> getFactory(),
					new ConstantTransformer(1.0));

			forest = prim.getForest();
			treeLayout = new TreeLayout<LayoutNode, LayoutLink>(forest);

			lastCall++;
			break;

		default:
			layout = new FRLayout<LayoutNode, LayoutLink>(graph);
			lastCall = 0;
			break;
		}

		if (layout != null) {
			layout.setSize(new Dimension(900, 800));
			layout.initialize();
		} else if (treeLayout != null) {
			treeLayout.initialize();
		} else if (forestLayout != null) {
			forestLayout.setSize(new Dimension(900, 800));
			forestLayout.initialize();
		}

		HashMap<ID, MapItemPosition> positions = new HashMap<ID, MapItemPosition>();

		for (ID id : nodeMap.keySet()) {
			LayoutNode node = nodeMap.get(id);

			MapItemPosition mapItemPosition = null;
			if (layout != null) {
				node.setXposition(layout.getX(node));
				node.setYposition(layout.getY(node));

				mapItemPosition = new MapItemPosition();
				mapItemPosition.setItemId(node.getItemId());
				mapItemPosition.setXposition(layout.getX(node));
				mapItemPosition.setYposition(layout.getY(node));
			} else if (treeLayout != null) {
				Point2D transform = treeLayout.transform(node);

				node.setXposition(transform.getX());
				node.setYposition(transform.getY());

				mapItemPosition = new MapItemPosition();
				mapItemPosition.setItemId(node.getItemId());
				mapItemPosition.setXposition(transform.getX());
				mapItemPosition.setYposition(transform.getY());

			} else {
				PolarPoint polarPoint = forestLayout.getPolarLocations().get(node);
				if (polarPoint != null) {
					
					Point2D transform = polarPoint.polarToCartesian(polarPoint);
					//Point2D transform = forestLayout.transform(node);
					node.setXposition(transform.getX());
					node.setYposition(transform.getY());

					mapItemPosition = new MapItemPosition();
					mapItemPosition.setItemId(node.getItemId());
					mapItemPosition.setXposition(transform.getX());
					mapItemPosition.setYposition(transform.getY());
				}
			}

			positions.put(node.getItemId(), mapItemPosition);
		}

		networkInformation.setPositions(positions);

		return networkInformation;
	}

	public HashMap<ID, MapItemPosition> generatePositions(Network network)
			throws IntegerException {

		Graph<LayoutNode, LayoutLink> graph = null;
		Forest<LayoutNode, LayoutLink> forest = new DelegateForest<LayoutNode, LayoutLink>();

		graph = new SparseMultigraph<LayoutNode, LayoutLink>();

		HashMap<ID, LayoutNode> nodeMap = new HashMap<ID, LayoutNode>();

		for (final ServiceElement serviceElement : network.getServiceElements()) {

			LayoutNode node = new LayoutNode();
			node.setItemId(serviceElement.getID());
			node.setIconName(serviceElement.getIconName());
			
			graph.addVertex(node);
			forest.addVertex(node);

			nodeMap.put(node.getItemId(), node);
		}

		if (network.getInterDeviceLinks() != null) {
			for (final InterDeviceLink link : network.getInterDeviceLinks()) {
				LayoutLink myLink = new LayoutLink();
				myLink.setLinkId(link.getID());
				myLink.setSourceId(link.getSourceServiceElementId());
				myLink.setDestId(link.getDestinationServiceElementId());

				LayoutNode srcNode = nodeMap.get(link.getSourceServiceElementId());
				LayoutNode destNode = nodeMap.get(link
						.getDestinationServiceElementId());
				
				if (srcNode != null && destNode != null) {
					graph.addEdge(myLink, srcNode, destNode);

					forest.addEdge(myLink, srcNode, destNode);
				}
			}
		}

		TreeLayout<LayoutNode, LayoutLink> treeLayout = null;
		AbstractLayout<LayoutNode, LayoutLink> layout = null;
		RadialTreeLayout<LayoutNode, LayoutLink> forestLayout = null;

		StringBuffer b = new StringBuffer("Create Layout:  ( ").append(lastCall).append(" )" );
		switch (lastCall) {
		case 0:
			layout = new KKLayout<LayoutNode, LayoutLink>(graph);
			b.append("KKLayout");
			lastCall++;
			break;

		case 1:
			layout = new KKLayout<LayoutNode, LayoutLink>(graph);
			((KKLayout<LayoutNode, LayoutLink>) layout)
					.setAdjustForGravity(false);
			
			b.append("KKLayout adjustForGravity off");
			lastCall++;
			break;

		case 2:
			layout = new CircleLayout<LayoutNode, LayoutLink>(graph);
			b.append("CircleLayout");
			lastCall++;
			break;

		case 3:
			layout = new ISOMLayout<LayoutNode, LayoutLink>(graph);
			b.append("ISOMLayout");
			lastCall++;
			break;

		case 4:
//			layout = new DAGLayout<LayoutNode, LayoutLink>(graph);
			lastCall++;
//			break;

		case 5:
			forestLayout = new RadialTreeLayout<LayoutNode, LayoutLink>(forest);
			forestLayout.setSize(new Dimension(900, 800));
			forestLayout.initialize();

			b.append("RadialTreeLayout");
			lastCall++;
			break;
			
		case 6:
			MinimumSpanningForest2<LayoutNode, LayoutLink> prim = new MinimumSpanningForest2<LayoutNode, LayoutLink>(
					graph, new DelegateForest<LayoutNode, LayoutLink>(),
					DelegateTree.<LayoutNode, LayoutLink> getFactory(),
					new ConstantTransformer(1.0));

			forest = prim.getForest();
			treeLayout = new TreeLayout<LayoutNode, LayoutLink>(forest);
			b.append("MinimumSpanningForest2");
			lastCall++;
			break;

		default:
			layout = new FRLayout<LayoutNode, LayoutLink>(graph);
			b.append("FRLayout");
			lastCall = 0;
			break;
		}

		logger.info(b.toString());
		
		if (layout != null) {
			layout.setSize(new Dimension(900, 800));
			layout.initialize();
			
		}

		HashMap<ID, MapItemPosition> positions = new HashMap<ID, MapItemPosition>();

		for (ID id : nodeMap.keySet()) {
			LayoutNode node = nodeMap.get(id);

			MapItemPosition mapItemPosition = null;
			if (layout != null) {
				node.setXposition(layout.getX(node));
				node.setYposition(layout.getY(node));

				mapItemPosition = new MapItemPosition();
				mapItemPosition.setItemId(node.getItemId());
				mapItemPosition.setIconName(node.getIconName());
				mapItemPosition.setXposition(layout.getX(node));
				mapItemPosition.setYposition(layout.getY(node));
			} else if (treeLayout != null) {
				Point2D transform = treeLayout.transform(node);

				node.setXposition(transform.getX());
				node.setYposition(transform.getY());

				mapItemPosition = new MapItemPosition();
				mapItemPosition.setIconName(node.getIconName());
				mapItemPosition.setItemId(node.getItemId());
				mapItemPosition.setXposition(transform.getX());
				mapItemPosition.setYposition(transform.getY());

			} else {
				if (forestLayout.getPolarLocations().get(node) != null) {
					Point2D transform = forestLayout.transform(node);
					node.setXposition(transform.getX());
					node.setYposition(transform.getY());

					mapItemPosition = new MapItemPosition();
					mapItemPosition.setIconName(node.getIconName());
					mapItemPosition.setItemId(node.getItemId());
					mapItemPosition.setXposition(transform.getX());
					mapItemPosition.setYposition(transform.getY());
				}
			}

			if (mapItemPosition != null)
				positions.put(node.getItemId(), mapItemPosition);
		}

		return positions;
	}
}
