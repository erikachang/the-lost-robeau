// Internal action code for project lost_robot

package observer;

import hmm.HiddenMarkovModel;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.Event;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;

import java.util.List;

import robeau_environment.RobeauWorldModel;

public class evaluate extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        RobeauWorldModel worldModel = RobeauWorldModel.get();
        HiddenMarkovModel hmm = HiddenMarkovModel.get();
    	int time = Integer.parseInt(args[0].toString());
        List<Integer> fP = worldModel.getFreePositions();
        List<Integer> robeauPath = hmm.getSmoothedPath();
        List<Integer> truePath = worldModel.getStateSequence(time);

        int errors = 0;
        
        for (int i = 0; i < time; i++) {
        	robeauPath.set(i, fP.get(robeauPath.get(i)));
        	
        	if (!robeauPath.get(i).equals(truePath.get(i))) {
        		errors++;
        	}
		}
        
        double ratio = (double)errors/robeauPath.size();
        
        if (errors > 0) {
        	ts.getAg().addBel(Literal.parseLiteral("miss(" + ratio + "," + robeauPath.size() +")")); 
        } else {
        	ts.getAg().addBel(Literal.parseLiteral("threshold(" + robeauPath.size() + ")"));
        }

        return true;
    }
}
