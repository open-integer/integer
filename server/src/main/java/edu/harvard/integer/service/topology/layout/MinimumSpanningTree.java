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

package edu.harvard.integer.service.topology.layout;

import java.awt.Dimension;
import java.awt.geom.Point2D;

import org.apache.commons.collections15.functors.ConstantTransformer;

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
public class MinimumSpanningTree implements TopologyTreeLayoutInterface {
	private Graph<LayoutNode, LayoutLink> graph = null;
	private TreeLayout<LayoutNode, LayoutLink> treeLayout = null;

	private Forest<LayoutNode, LayoutLink> forest = new DelegateForest<LayoutNode, LayoutLink>();
	
	public MinimumSpanningTree() {
		graph = new SparseMultigraph<LayoutNode, LayoutLink>();
	
	}
	
	/* (non-Javadoc)
	 * @see edu.harvard.integer.service.topology.layout.TopologyTreeLayoutInterface#setSize(java.awt.Dimension)
	 */
	@Override
	public void setSize(Dimension size) {
		MinimumSpanningForest2<LayoutNode, LayoutLink> prim = new MinimumSpanningForest2<LayoutNode, LayoutLink>(
				graph, new DelegateForest<LayoutNode, LayoutLink>(),
				DelegateTree.<LayoutNode, LayoutLink> getFactory(),
				new ConstantTransformer(1.0));

		forest = prim.getForest();
		treeLayout = new TreeLayout<LayoutNode, LayoutLink>(forest);
		

	}

	/* (non-Javadoc)
	 * @see edu.harvard.integer.service.topology.layout.TopologyTreeLayoutInterface#addLayoutNode(edu.harvard.integer.service.topology.layout.LayoutNode)
	 */
	@Override
	public boolean addLayoutNode(LayoutNode node) {
		return graph.addVertex(node);

	}

	/* (non-Javadoc)
	 * @see edu.harvard.integer.service.topology.layout.TopologyTreeLayoutInterface#addLayoutLInk(edu.harvard.integer.service.topology.layout.LayoutLink, edu.harvard.integer.service.topology.layout.LayoutNode, edu.harvard.integer.service.topology.layout.LayoutNode)
	 */
	@Override
	public boolean addLayoutLInk(LayoutLink link, LayoutNode sourcetNode,
			LayoutNode destinationNode) {
		return graph.addEdge(link, sourcetNode, destinationNode);

	}

	/* (non-Javadoc)
	 * @see edu.harvard.integer.service.topology.layout.TopologyTreeLayoutInterface#initialize()
	 */
	@Override
	public void initialize() {
		treeLayout.initialize();

	}

	/* (non-Javadoc)
	 * @see edu.harvard.integer.service.topology.layout.TopologyTreeLayoutInterface#getMapItemPosition(edu.harvard.integer.service.topology.layout.LayoutNode)
	 */
	@Override
	public Point2D getPoint(LayoutNode node) {
		
		return treeLayout.transform(node);
	}

}
