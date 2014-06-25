package edu.harvard.integer.client.utils;

import com.emitrom.lienzo.client.core.shape.Line;

/**
 * The Class LinePoints.
 */
public class LinePoints {
	
	/** The line. */
	private Line line;
	
	/** The start point. */
	private Coordinate startPoint;
	
	/** The end point. */
	private Coordinate endPoint;

	/**
	 * Instantiates a new line points.
	 *
	 * @param line the line
	 * @param startPoint the start point
	 * @param endPoint the end point
	 */
	public LinePoints(Line line, Coordinate startPoint, Coordinate endPoint) {
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
	public Coordinate getStartPoint() {
		return startPoint;
	}

	/**
	 * Sets the start point.
	 *
	 * @param startPoint the new start point
	 */
	public void setStartPoint(Coordinate startPoint) {
		this.startPoint = startPoint;
	}

	/**
	 * Gets the end point.
	 *
	 * @return the end point
	 */
	public Coordinate getEndPoint() {
		return endPoint;
	}

	/**
	 * Sets the end point.
	 *
	 * @param endPoint the new end point
	 */
	public void setEndPoint(Coordinate endPoint) {
		this.endPoint = endPoint;
	}

}
