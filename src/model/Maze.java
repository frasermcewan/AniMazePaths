package model;

import java.awt.Point;

import data.PointPair;

public interface Maze {
	PointPair step();
	int getValue(int x, int y);
	void reset(Point start, Point goal);
	int[][] getGrid();
	boolean forceJoin(Point p1, Point p2);
}
