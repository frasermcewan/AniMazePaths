package model;

import java.awt.Point;
import java.util.List;

public class FirstEscapeDrunkWalk extends AbstractDrunkWalk {

	public FirstEscapeDrunkWalk(int width, int height) {
		super(width, height);
	}

	@Override
	protected void restart() {
		current = escapeRoutes.removeFirst();
		List<Point> neighbours = getNeighbours(current);
		while(neighbours.isEmpty()) {
			if(escapeRoutes.isEmpty())
				return;
			current = escapeRoutes.removeFirst();
			neighbours = getNeighbours(current);
		}
	}
}
