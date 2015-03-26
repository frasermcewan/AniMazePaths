package model;

import java.awt.Point;
import java.util.List;

public class LastEscapeDrunkWalk extends AbstractDrunkWalk {

	public LastEscapeDrunkWalk(int width, int height) {
		super(width, height);
	}

	@Override
	protected void restart() {
		current = escapeRoutes.removeLast();
		List<Point> neighbours = getNeighbours(current);
		while(neighbours.isEmpty()) {
			if(escapeRoutes.isEmpty())
				return;
			current = escapeRoutes.removeLast();
			neighbours = getNeighbours(current);
		}
	}
}
