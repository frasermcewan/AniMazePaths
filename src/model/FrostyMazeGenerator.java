package model;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import data.PointPair;

public class FrostyMazeGenerator extends AbstractMaze {

	private List<Point> available;
	
	private final Random rng = new Random();
	
	public FrostyMazeGenerator(int width, int height) {
		super(width, height);
	}

	@Override
	public PointPair step() {
		if(available.isEmpty())
			return null;
		Point next = available.remove(rng.nextInt(available.size()));
//		Point next = available.remove(0);
		PointPair join = findJoin(next);
		if (join != null) {
			graph.add(next);
			connectPoints(join.a,  join.b);
			available.remove(next);
			for(Point n : getNeighbours(next)) {
				if(!available.contains(n)) {
					available.add(n);
				}
			}
			return join;
		}
		System.out.println("FrostyMazeGenerator.step() WTF!");
		return null;
	}

	private PointPair findJoin(Point next) {
		List<Point> graphNeighbours = getGraphConnections(next);
		if(graphNeighbours.isEmpty()) {
			return null;
		} else {
			return new PointPair(graphNeighbours.get(rng.nextInt(graphNeighbours.size())), next);
		}
	}

	@Override
	public int getValue(int x, int y) {
		return grid[x][y];
	}

	@Override
	public void reset(Point start, Point goal) {
		super.reset(start, goal);
		Point p1;
		available = new LinkedList<Point>();
		List<Point> neighbours;
//		p = open.remove(rng.nextInt(open.size()));
		p1 = new Point(width/2, height/2);
		neighbours = getNeighbours(p1);
		available.addAll(neighbours);
		open.remove(p1);
		graph.add(p1);
		Point p2 = available.remove(rng.nextInt(available.size()));
		open.remove(p2);
		graph.add(p2);
		
		connectPoints(p1, p2);
	}


	private List<Point> getGraphConnections(Point point) {
		List<Point> reachable = new LinkedList<Point>();
		Point[] all = new Point[] {
				new Point(point.x, point.y-1), //UP
				new Point(point.x, point.y+1), //DOWN
				new Point(point.x-1, point.y), //LEFT
				new Point(point.x+1, point.y), //RIGHT
		};
		for(Point n : all) {
			if(graphContains(n)) {
				reachable.add(n);
			}
		}
		return reachable;
	}

}
