package lost_robot;

import jason.asSyntax.Literal;
import jason.asSyntax.Structure;
import jason.asSyntax.Term;
import jason.environment.Environment;
import jason.environment.grid.Location;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Random;

public class RobeauEnvironment extends Environment {

	private final Term MOVE = Literal.parseLiteral("move");
	private final Term SCAN = Literal.parseLiteral("scan");

	private final Random randy = new Random(System.currentTimeMillis());

	private RobeauWorldModel worldModel;
	private RobeauWorldView worldView;

	public RobeauEnvironment() {
		this.worldModel = RobeauWorldModel.world1();
		this.worldView = new RobeauWorldView(worldModel);
		this.worldModel.setView(this.worldView);
		worldModel.setSensorError(0.0);
	}

	@Override
	public boolean executeAction(String agentName, Structure action) {
		clearAllPercepts();

		boolean result = false;

		if (action.equals(MOVE)) {
			Location l = worldModel.getAgPos(0);
			
			ArrayList<Integer> neighbours = worldModel.neighbours(RobeauWorldModel.positionAsInteger(l.x, l.y, worldModel.getWidth()));
			
			int di = randy.nextInt(neighbours.size());
			int coords[] = RobeauWorldModel.positionFromInteger(neighbours.get(di), worldModel.getWidth());
			
			if (coords[0] > l.x) {
				result = worldModel.move(0, "east");
			} else if (coords[0] < l.x) {
				result = worldModel.move(0, "west");
			} else if (coords[1] < l.y) {
				result = worldModel.move(0, "north");
			} else {
				result = worldModel.move(0, "south");
			}

		} else if (action.equals(SCAN)) {
			Location l = worldModel.getAgPos(0);
			BitSet readings = new BitSet(4);
			double sensorError = worldModel.getSensorError()*100;
			
			if (randy.nextInt(101) < sensorError) {
				readings.set(0, worldModel.isFreeOfObstacle(l.x, l.y - 1));
			} else {
				readings.set(0, !worldModel.isFreeOfObstacle(l.x, l.y - 1));
			}

			if (randy.nextInt(101) < sensorError) {
				readings.set(1, worldModel.isFreeOfObstacle(l.x + 1, l.y));
			} else {
				readings.set(1, !worldModel.isFreeOfObstacle(l.x + 1, l.y));
			}

			if (randy.nextInt(101) < sensorError) {
				readings.set(2, worldModel.isFreeOfObstacle(l.x - 1, l.y));
			} else {
				readings.set(2, !worldModel.isFreeOfObstacle(l.x - 1, l.y));
			}

			if (randy.nextInt(101) < sensorError) {
				readings.set(3, worldModel.isFreeOfObstacle(l.x, l.y + 1));
			} else {
				readings.set(3, !worldModel.isFreeOfObstacle(l.x, l.y + 1));
			}

			addPercept(Literal.parseLiteral("blocked(" + readings.get(0) + ","
					+ readings.get(1) + "," + readings.get(2) + ","
					+ readings.get(3) + ")"));
			
			return true;
		}

		return result;
	}
}
