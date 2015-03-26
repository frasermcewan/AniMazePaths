package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public abstract class AbstractMaze implements Maze {
	
	public static final int UP = 8, RIGHT = 4, DOWN = 2, LEFT = 1;
	private final int biasHoizontal = 1;
	private final int biasVertical = 1;
	public final int width, height;
	protected final int[][] grid;
	protected final boolean[][] openTest;
	protected final List<Point> graph;
	protected final List<Point> open;
	protected final Random rng = new Random();
	protected Point start, goal;
	
	public AbstractMaze (int width, int height) {
		grid = new int[width][height];
		openTest = new boolean[width][height];
		this.width = width;
		this.height = height;
		open = new ArrayList<Point>();
		graph = new LinkedList<Point>();
		for(int x = 0 ; x < width ; x++) {
			for(int y = 0 ; y < height ; y++) {
				grid[x][y] = 0;
			}
		}
		
		for(int x = 1 ; x < width-1 ; x++) {
			for(int y = 1 ; y < height-1 ; y++) {
				openAdd(new Point(x,y));
			}
		}
	}

	protected void openAdd(Point p) {
		open.add(p);
		openTest[p.x][p.y] = true; 
	}

	@Override
	public int getValue(int x, int y) {
		return grid[x][y];
	}
	
	@Override
	public void reset(Point start, Point goal) {
		this.start = start;
		this.goal = goal;
		open.clear();
		graph.clear();
		for(int x = 0 ; x < width ; x++) {
			for(int y = 0 ; y < height ; y++) {
				grid[x][y] = 0;
			}
		}
		
		for(int x = 1 ; x < width-1 ; x++) {
			for(int y = 1 ; y < height-1 ; y++) {
				openAdd(new Point(x,y));
			}
		}
		if(start != null) {
			openAdd(start);
		}
		if(goal != null) {
			openAdd(goal);
		}
	}

	@Override
	public int[][] getGrid() {
		int[][] copy = new int[width][height];
		for(int x = 0 ; x < width ; x++) {
			for(int y = 0 ; y < height ; y++) {
				copy[x][y] = grid[x][y];
			}
		}
		return copy;
	}

	protected void openRemove(Point p) {
		open.remove(p);
		openTest[p.x][p.y] = false; 
	}
	
	protected boolean graphContains(Point n) {
		if(n.x < 0 || n.y < 0 || n.x >= width || n.y >= height) {
			return false;
		}
		return grid[n.x][n.y] > 0;
	}

	public boolean openContains(Point n) {
		if(n.x < 0 || n.y < 0 || n.x >= width || n.y >= height) {
			return false;
		}
		return openTest[n.x][n.y];
	}
	
	protected boolean connectPoints(Point p1, Point p2) {
		if(p1.distance(p2) == 1) {
			switch(p1.x - p2.x) {
			case -1: //Right
				grid[p1.x][p1.y] |= RIGHT;
				grid[p2.x][p2.y] |= LEFT;
				return true;
			case 1: //Left
				grid[p1.x][p1.y] |= LEFT;
				grid[p2.x][p2.y] |= RIGHT;
				return true;
			case 0: 
				switch(p1.y - p2.y) {
				case -1: //Down
					grid[p1.x][p1.y] |= DOWN;
					grid[p2.x][p2.y] |= UP;
					return true;
				case 1: //Up
					grid[p1.x][p1.y] |= UP;
					grid[p2.x][p2.y] |= DOWN;
					return true;
				default :
					System.err.println("joinPoints() Horizontal WTF!");
					return false;
				}
			default:
				System.err.println("joinPoints() Vertical WTF!");
				return false;
			}
		} else {
			return false;
		}
	}	
	
	protected List<Point> getNeighbours(Point point) {
		List<Point> reachable = new ArrayList<Point>(4);
		List<Point> all = new LinkedList<Point>();
		for(int i = 0 ; i < biasVertical ; i++) {
			all.add(new Point(point.x, point.y-1)); //UP
			all.add(new Point(point.x, point.y+1)); //DOWN
		}
		for(int i = 0 ; i < biasHoizontal ; i++) {
			all.add(new Point(point.x-1, point.y)); //LEFT
			all.add(new Point(point.x+1, point.y)); //RIGHT
		}
		for(Point n : all) {
			if(openContains(n)) {
				reachable.add(n);
			}
		}
		return reachable;
	}
	
	public boolean forceJoin(Point p1, Point p2) {
		return connectPoints(p1, p2);
	}
}
