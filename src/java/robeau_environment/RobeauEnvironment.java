package robeau_environment;

import jason.asSyntax.Literal;
import jason.asSyntax.Structure;
import jason.asSyntax.Term;
import jason.environment.Environment;
import jason.environment.grid.Location;

import java.util.BitSet;
import java.util.List;
import java.util.Random;

import com.sun.istack.internal.logging.Logger;

public class RobeauEnvironment extends Environment {

	private Logger logger = Logger.getLogger(getClass());
	
	private final String MOVE = "move";
	private final Term SCAN = Literal.parseLiteral("scan");
	private final Term FREE_AGENT = Literal.parseLiteral("free_agent");
	private final Term RANDOM_MOVE = Literal.parseLiteral("random_move");

	private final Random randy = new Random(System.currentTimeMillis());

	private RobeauWorldModel worldModel;
	private RobeauWorldView worldView;

	public RobeauEnvironment() {
		worldModel = RobeauWorldModel.world1();
		worldView = new RobeauWorldView(worldModel);
		worldModel.setView(worldView);
		worldModel.setSensorError(0.3);
	}

	@Override
	public boolean executeAction(String agentName, Structure action) {
		clearAllPercepts();

		boolean result = false;

		int agId = getAgId(agentName);
		if (agId < 0)
			return result;
		
		if (action.getFunctor().equals(MOVE)) {
			Direction direction = Direction.valueOf(action.getTerm(0).toString().toUpperCase());
			result = worldModel.move(agId, direction);
		} else if (action.equals(RANDOM_MOVE)) {
			Location l = worldModel.getAgPos(agId);
			
			List<Integer> neighbours = worldModel.neighbours(RobeauWorldModel.integerPosition(l.x, l.y, worldModel.getWidth()));
			
			int di = randy.nextInt(neighbours.size());
			Location n = RobeauWorldModel.locationFromInteger(neighbours.get(di), worldModel.getWidth());
			
			if (n.x > l.x) {
				result = worldModel.move(0, Direction.EAST);
			} else if (n.x < l.x) {
				result = worldModel.move(0, Direction.WEST);
			} else if (n.y < l.y) {
				result = worldModel.move(0, Direction.NORTH);
			} else {
				result = worldModel.move(0, Direction.SOUTH);
			}
		} else if (action.equals(SCAN)) {
			Location l = worldModel.getAgPos(agId);
			
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

			addPercept(agentName, Literal.parseLiteral("blocked(" + readings.get(0) + ","
					+ readings.get(1) + "," + readings.get(2) + ","
					+ readings.get(3) + ")"));
			
			return true;
		} else if (action.equals(FREE_AGENT)) {
			Location l = worldModel.getAgPos(0);
			
			List<Integer> prisonersAtL = worldModel.prisonersAt(l);
			result = !prisonersAtL.isEmpty();
			
			if (result) {
				String prisonerName;
				for (Integer i: prisonersAtL) {
					worldModel.setPrisonerFree(i);
					prisonerName = String.format("prisoner_unit%d", i);
					addPercept("monsieur_robeau", Literal.parseLiteral("free(" + prisonerName + ")"));
				}
			}
		} else {
			logger.warning("Unrecognized action + " + action.getFunctor() + ".");
		}
		return result;
	}

	private int getAgId(String agentName) {
		if (agentName.equals("monsieur_robeau")) {
			return 0;
		} else if (agentName.startsWith("prisoner_unit")) {
			return Integer.parseInt(agentName.substring(13));
		}
		logger.info("Unrecognized agent name" + agentName + ".");
		return -1;	
	}
}
