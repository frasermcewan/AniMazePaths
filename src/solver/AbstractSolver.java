package solver;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractSolver implements Solver {
	
	protected static final int UP = 8, RIGHT = 4, DOWN = 2, LEFT = 1;

	protected List<Point> getNeighbours(Point current, int[][] grid) {
		List<Point> neighbours = new LinkedList<Point>();
		int val = grid[current.x][current.y];
		if((val & UP) == UP)
			neighbours.add(new Point(current.x, current.y-1));
		if((val & DOWN) == DOWN)
			neighbours.add(new Point(current.x, current.y+1));
		if((val & LEFT) == LEFT)
			neighbours.add(new Point(current.x-1, current.y));
		if((val & RIGHT) == RIGHT)
			neighbours.add(new Point(current.x+1, current.y));
		return neighbours;
	}

}
