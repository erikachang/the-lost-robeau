package search;

import jason.asSyntax.Literal;
import jason.environment.grid.Location;

import java.util.LinkedList;
import java.util.List;

import robeau_environment.RobeauWorldModel;

public class SearchProblem implements Problem {
	private Location initialState, goalState;
	
	public SearchProblem(Location s0, Location g) {
		initialState = s0;
		goalState = g;
	}
	
	public Location getInitialState() {
		return initialState;
	}
	
	public Location getGoalState() {
		return goalState;
	}

	public List<Literal> actions(Location state) {
		RobeauWorldModel worldModel = RobeauWorldModel.get();
		List<Literal> actionList = new LinkedList<Literal>();
		
		if (worldModel.isFreeOfObstacle(state.x, state.y-1)) {
			// NORTH
			actionList.add(Literal.parseLiteral("move(north)"));
		} 
		if (worldModel.isFreeOfObstacle(state.x+1, state.y)) {
			// EAST
			actionList.add(Literal.parseLiteral("move(east)"));
		}
		if (worldModel.isFreeOfObstacle(state.x-1, state.y)) {
			// WEST
			actionList.add(Literal.parseLiteral("move(west)"));
		} 
		if (worldModel.isFreeOfObstacle(state.x, state.y+1)){
			// SOUTH
			actionList.add(Literal.parseLiteral("move(south)"));
		}
		
		return actionList;
	}

	public boolean goalTest(Location state) {
		return state.equals(goalState);
	}

	public List<Location> simulate(Location state, Literal action) {
		List<Location> states = new LinkedList<Location>();
		Location newState = copyState(state);
		
		if (action.equals(Literal.parseLiteral("move(north)"))) {
			newState.y -= 1;
			states.add(newState);
		} else if (action.equals(Literal.parseLiteral("move(east)"))) {
			newState.x += 1;
			states.add(newState);
		} else if (action.equals(Literal.parseLiteral("move(west)"))) {
			newState.x -= 1;
			states.add(newState);
		} else if (action.equals(Literal.parseLiteral("move(south)"))) {
			newState.y += 1;
			states.add(newState);
		} else {
			throw new UnsupportedOperationException("Action is not available.");
		}
		
		return states;
	}
	
	private Location copyState(Location state) {
		return new Location(state.x, state.y);
	}
}
