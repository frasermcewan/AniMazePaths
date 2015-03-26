package solver;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class StupidSearch extends AbstractSolver {
	
	private final Random randomiser = new Random();

	@Override
	public List<Point> solve(int[][] maze, Point start, Point goal) {
		LinkedList<Point> path = new LinkedList<Point>();
		List<Point> nextPoints = new LinkedList<Point>();
		
		boolean found = false;
		
		path.add(start);
		
		while(!found) {
			Point current = path.getLast();
			if(current.equals(goal)) {
				return path;
			}
			nextPoints.clear();
			for(Point neighbour : getNeighbours(current, maze)) {
				if(!path.contains(neighbour)) {
					nextPoints.add(neighbour);
				}
			}
			if(nextPoints.isEmpty()) {
				path.clear();
				path.add(start);
			}
			else {
				Point next = nextPoints.get(randomiser.nextInt(nextPoints.size()));
				path.add(next);
			}
		}
		return null;
	}

}
