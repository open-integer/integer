package edu.harvard.integer.client.utils;

import com.emitrom.lienzo.client.core.shape.Line;

public class LinePoints {
	private Line line;
	private Coordinate startPoint;
	private Coordinate endPoint;

	public LinePoints(Line line, Coordinate startPoint, Coordinate endPoint) {
		this.line = line;
		this.startPoint = startPoint;
		this.endPoint = endPoint;
	}

	public Line getLine() {
		return line;
	}

	public void setLine(Line line) {
		this.line = line;
	}

	public Coordinate getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(Coordinate startPoint) {
		this.startPoint = startPoint;
	}

	public Coordinate getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(Coordinate endPoint) {
		this.endPoint = endPoint;
	}

}
