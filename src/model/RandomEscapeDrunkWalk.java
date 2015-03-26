package model;

import java.awt.Point;
import java.util.List;

public class RandomEscapeDrunkWalk extends AbstractDrunkWalk {

	public RandomEscapeDrunkWalk(int width, int height) {
		super(width, height);
	}

	@Override
	protected void restart() {
		current = escapeRoutes.remove(rng.nextInt(escapeRoutes.size()));
		List<Point> neighbours = getNeighbours(current);
		while(neighbours.isEmpty()) {
			if(escapeRoutes.isEmpty())
				return;
			current = escapeRoutes.remove(rng.nextInt(escapeRoutes.size()));
			neighbours = getNeighbours(current);
		}
	}

}
