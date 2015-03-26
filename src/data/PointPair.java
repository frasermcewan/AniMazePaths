package data;

import java.awt.Point;

public class PointPair {
	public final Point a, b;
	public PointPair(Point a, Point b) {
		this.a = a;
		this.b = b;
	}
	@Override
	public String toString() {
		return "(" + a.x + "," + a.y + ") -> (" + b.x + "," + b.y + ")";
	}
}
