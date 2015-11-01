package search;

import jason.asSyntax.Literal;
import jason.environment.grid.Location;

import java.util.List;

public interface Problem {
	public Location getInitialState();
	public Location getGoalState();
	public List<Literal> actions(Location state);
	public boolean goalTest(Location state);
	public List<Location> simulate(Location state, Literal action);
}
