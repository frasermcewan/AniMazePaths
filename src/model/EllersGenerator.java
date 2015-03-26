package model;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import data.PointPair;

public class EllersGenerator extends AbstractMaze {

	private int[][] sets;
	
	private int setId = 5;
	
	private final Random rng = new Random();
	private int x = 0 , y = 0;
	
	public EllersGenerator(int width, int height) {
		super(width, height);
	}

	@Override
	public PointPair step() {
		System.out.println(x + " " + y + " " + width + " " + height);
		if(x == width) {
			x = 0;
			y++;
			if(y >= height) {
				return null;
			}
		}
		Point p1 = new Point(x,y);
		List<Point> neighbours = getNeighbours(p1);
		if(neighbours.size() == 0) {
			printSets();
			return null;
		}
		Point p2 = neighbours.get(rng.nextInt(neighbours.size()));
		connectPoints(p1, p2);
		int set1 = getSet(p1);
		int set2 = getSet(p2);
		mergeSets(set1, set2);
		graph.add(p2);
		x++;
		return new PointPair(p1	, p2);
	}

	private void printSets() {
		for(int x = 0 ; x < width ; x++) {
			for(int y = 0 ; y < height ; y++) {
				System.out.print(sets[x][y]);
			}
			System.out.println();
		}
	}

	private void mergeSets(int set1, int set2) {
		if(set1 > set2) {
			int temp = set2;
			set2 = set1;
			set1 = temp;
		}
		for(int x = 0 ; x < width ; x++) {
			for(int y = 0 ; y < height ; y++) {
				Point p = new Point(x,y);
				if(getSet(p) == set2) {
					sets[x][y] = set1;
				}
			}
		}
	}

	@Override
	public int getValue(int x, int y) {
		return grid[x][y];
	}

	@Override
	public void reset(Point start, Point goal) {
		super.reset(start,goal);
		x = 0;
		y = 0;
		sets = new int[width][height];
		Point p1 = new Point(0,0);
		Point p2 = new Point(1,0);
		connectPoints(p1, p2);
		graph.add(p1);
		graph.add(p2);
		sets[p1.x][p1.y] = 1;
		sets[p2.x][p2.y] = 1;
		sets[0][1] = 2;
		sets[1][1] = 3;
		sets[2][0] = 4;
		x++;
	}

	@Override
	protected List<Point> getNeighbours(Point point) {
		List<Point> reachable = new LinkedList<Point>();
		Point[] all = new Point[] {
				new Point(point.x, point.y-1), //UP
				new Point(point.x, point.y+1), //DOWN
				new Point(point.x-1, point.y), //LEFT
				new Point(point.x+1, point.y), //RIGHT
		};
		int set = getSet(point);
//		System.out.println(point + " = " + set);
		for(Point n : all) {
			if(openContains(n)) {
//				System.out.println(n + " = " + getSet(n));
				if(set != getSet(n)) {
					reachable.add(n);
					if(getSet(n)==0) {
						sets[n.x][n.y] = setId++; 
					}
				}
			}
		}
//		System.out.println("-----------------------");
		return reachable;
	}
	
	private int getSet(Point p) {
		return sets[p.x][p.y];
	}
}

