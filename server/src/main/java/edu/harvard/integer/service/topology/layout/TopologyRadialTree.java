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

import edu.uci.ics.jung.algorithms.layout.PolarPoint;
import edu.uci.ics.jung.algorithms.layout.RadialTreeLayout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.Forest;

/**
 * @author David Taylor
 *
 */
public class TopologyRadialTree implements TopologyTreeLayoutInterface {

	private RadialTreeLayout<LayoutNode, LayoutLink> layout = null;
	private Forest<LayoutNode, LayoutLink> forest = null;
	
	/**
	 * 
	 */
	public TopologyRadialTree() {
		forest = new DelegateForest<LayoutNode, LayoutLink>();
	}
	
	/* (non-Javadoc)
	 * @see edu.harvard.integer.service.topology.TopologyTreeLayoutInterface#setSize(java.awt.Dimension)
	 */
	@Override
	public void setSize(Dimension size) {
		layout = new RadialTreeLayout<LayoutNode, LayoutLink>(forest, 100);
	}

	/* (non-Javadoc)
	 * @see edu.harvard.integer.service.topology.TopologyTreeLayoutInterface#addLayoutNode(edu.harvard.integer.service.topology.LayoutNode)
	 */
	@Override
	public boolean addLayoutNode(LayoutNode node) {
		return forest.addVertex(node);

	}

	/* (non-Javadoc)
	 * @see edu.harvard.integer.service.topology.TopologyTreeLayoutInterface#addLayoutLInk(edu.harvard.integer.service.topology.LayoutLink)
	 */
	@Override
	public boolean addLayoutLInk(LayoutLink link, LayoutNode sourcetNode, LayoutNode destinationNode) {
		return forest.addEdge(link, sourcetNode, destinationNode);

	}

	/* (non-Javadoc)
	 * @see edu.harvard.integer.service.topology.TopologyTreeLayoutInterface#initialize()
	 */
	@Override
	public void initialize() {


	}

	/* (non-Javadoc)
	 * @see edu.harvard.integer.service.topology.TopologyTreeLayoutInterface#getMapItemPosition(edu.harvard.integer.common.ID)
	 */
	@Override
	public Point2D getPoint(LayoutNode node) {
		PolarPoint polarPoint = layout.getPolarLocations().get(node);

		if (polarPoint != null)
			return PolarPoint.polarToCartesian(polarPoint);
		else
			return null;
	}

	

}
