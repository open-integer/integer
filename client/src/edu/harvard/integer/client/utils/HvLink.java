package edu.harvard.integer.client.utils;

import com.emitrom.lienzo.client.core.shape.Line;
import com.google.gwt.touch.client.Point;

/**
 * The Class HvLink.
 */
public class HvLink {
	
	/** The line. */
	private Line line;
	
	/** The start point. */
	private Point startPoint;
	
	/** The end point. */
	private Point endPoint;

	/**
	 * Instantiates a new line points.
	 *
	 * @param line the line
	 * @param startPoint the start point
	 * @param endPoint the end point
	 */
	public HvLink(Line line, Point startPoint, Point endPoint) {
		this.line = line;
		this.startPoint = startPoint;
		this.endPoint = endPoint;
	}

	/**
	 * Gets the line.
	 *
	 * @return the line
	 */
	public Line getLine() {
		return line;
	}

	/**
	 * Sets the line.
	 *
	 * @param line the new line
	 */
	public void setLine(Line line) {
		this.line = line;
	}

	/**
	 * Gets the start point.
	 *
	 * @return the start point
	 */
	public Point getStartPoint() {
		return startPoint;
	}

	/**
	 * Sets the start point.
	 *
	 * @param startPoint the new start point
	 */
	public void setStartPoint(Point startPoint) {
		this.startPoint = startPoint;
	}

	/**
	 * Gets the end point.
	 *
	 * @return the end point
	 */
	public Point getEndPoint() {
		return endPoint;
	}

	/**
	 * Sets the end point.
	 *
	 * @param endPoint the new end point
	 */
	public void setEndPoint(Point endPoint) {
		this.endPoint = endPoint;
	}

}
