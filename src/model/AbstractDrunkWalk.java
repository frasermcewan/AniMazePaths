package model;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

import data.PointPair;

public abstract class AbstractDrunkWalk extends AbstractMaze {

	private final double sobriety = 0.7;

	protected Point current;
	protected final LinkedList<Point> escapeRoutes;
	private int lastMove = 1;
	
	public AbstractDrunkWalk(int width, int height) {
		super(width, height);
		escapeRoutes = new LinkedList<Point>();
	}

	@Override
	public PointPair step() {
		
		if(open.isEmpty()) {
			return null;
		}
		
		Point next = getNextTile();
		
		if(next == null) {
			restart();
			return step();
		} else {
			graph.add(next);
			openRemove(next);
			connectPoints(current, next);
			PointPair pair = new PointPair(current, next);
			escapeRoutes.add(current);
			current = next;
			return pair;
		}
	}

	private Point getNextTile() {
		//TODO: Make this method more elegant.  Possibly by
		//		making a MOVE enum.
		List<Point> neighbours = getNeighbours(current);
		Point next = null;
		if(!neighbours.isEmpty()) {
			if(rng.nextDouble() <= sobriety ) {
				Point nextB;
				switch (lastMove) {
				case 1:
					nextB = new Point(current.x - 1, current.y);
					break;
				case 2:
					nextB = new Point(current.x, current.y + 1);
					break;
				case 4:
					nextB = new Point(current.x + 1, current.y);
					break;
				case 8:
					nextB = new Point(current.x, current.y - 1);
					break;
				default:
					nextB = new Point(-1,-1);
					break;
				}
				if(neighbours.contains(nextB)) {
					next = nextB;
				} else {
					next = neighbours.get(rng.nextInt(neighbours.size()));
					switch(current.x - next.x) {
					case -1:
						lastMove = 4;
						break;
					case 1:
						lastMove = 1;
						break;
					default:
						switch (current.y - next.y) {
						case -1:
							lastMove = 2;
							break;
						case 1:
							lastMove = 8;
							break;
						default:
							System.err.println("DrunkWalk step() calculate lastMove WTF!");
						}	
					}
				}
			} else {
				next = neighbours.get(rng.nextInt(neighbours.size()));
				switch(current.x - next.x) {
				case -1:
					lastMove = 4;
					break;
				case 1:
					lastMove = 1;
					break;
				default:
					switch (current.y - next.y) {
					case -1:
						lastMove = 2;
						break;
					case 1:
						lastMove = 8;
						break;
					default:
						System.err.println("DrunkWalk step() calculate lastMove WTF!");
					}	
				}
			}
		}
		return next;
	}

	protected abstract void restart();

	@Override
	public void reset(Point start, Point goal) {
		super.reset(start, goal);
		escapeRoutes.clear();
		openRemove(start);
		current = start;
		graph.add(current);
		escapeRoutes.add(current);
	}
}
