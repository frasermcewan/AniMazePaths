package solver;

import java.awt.Point;
import java.util.List;

public interface Solver {
	List<Point> solve(int[][] maze, Point start, Point goal);
}
