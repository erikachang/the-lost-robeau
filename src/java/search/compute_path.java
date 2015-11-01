// Internal action code for project robeau_hero

package search;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.Plan;
import jason.asSyntax.Term;
import jason.environment.grid.Location;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import robeau_environment.RobeauWorldModel;

public class compute_path extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
    	int s0 = Integer.parseInt(args[0].toString());
    	int g = Integer.parseInt(args[1].toString());
    	String endPlanName = "finished";
    	if (args.length > 2)
    		endPlanName = args[2].toString();
    	int w = RobeauWorldModel.get().getWidth();
    	
    	Location l0 = RobeauWorldModel.locationFromInteger(s0, w);
    	Location lg = RobeauWorldModel.locationFromInteger(g, w);
    	
    	Problem p = new SearchProblem(l0, lg);
    	
    	Plan p1 = ts.getAg().getPL().get("goto");
    	if (p1 != null) ts.getAg().getPL().remove("goto");
    	
    	List<Literal> plan = solve(p);
    	
    	if (plan != null && !plan.isEmpty()) {
	    	String planStr = "@goto +!moveTowardsGoal : true <- ";
	    	
	    	for (Literal l: plan) {
	    		planStr += l.toString() + ";scan;.wait(100);";
	    	}
	    	planStr += "!" + endPlanName + ".";
	    	Plan pl = Plan.parse(planStr);
	    	
	    	ts.getAg().getPL().add(pl);
    	} else {
    		String planStr = "@goto +!moveTowardsGoal : true <- !endgame.";
    		Plan pl = Plan.parse(planStr);
	    	
	    	ts.getAg().getPL().add(pl);
    	}
    	
        return true;
    }
    
    private List<Literal> solve(Problem p) {
    	return orSearch(p.getInitialState(), p, new LinkedList<Location>(), new HashSet<Location>());
    }

    private List<Literal> orSearch(Location s, Problem p, List<Location> path, HashSet<Location> explored) {
    	if (p.goalTest(s)) return new LinkedList<Literal>();
    	if (explored.contains(s)) return null;
    	
    	explored.add(s);
    	
    	for (Literal l: p.actions(s)) {
    		List<Location> newPath = new LinkedList<Location>(path);
    		newPath.add(s);
    		List<Literal> plan = andSearch(p.simulate(s, l), p, newPath, explored);
    		if (plan != null) {
    			plan.add(0, l);
    			return plan;
    		}
    	}
    	
    	return null;
    }
    
    private List<Literal> andSearch(List<Location> s, Problem p, List<Location> path, HashSet<Location> explored) {
    	List<List<Literal>> plans = new LinkedList<List<Literal>>();
    	
    	for (int i = 0; i < s.size(); i++) {
    		List<Literal> plan = orSearch(s.get(i), p, path, explored);
    		if (plan == null) return null;
    		plans.add(plan);
    	}
    	
    	List<Literal> plan = new LinkedList<Literal>();
    	for (List<Literal> ll: plans) {
    		if (ll != null) {
    			plan.addAll(ll);
    		}
    	}
    	
    	return plan;
    }
}
