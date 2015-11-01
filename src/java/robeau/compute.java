// Internal action code for project lost_robot

package robeau;

import hmm.HiddenMarkovModel;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;
import jason.environment.grid.Location;

import java.util.BitSet;
import java.util.List;

import robeau_environment.RobeauWorldModel;

public class compute extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
    	
    	BitSet readings = new BitSet(4);
    	readings.set(0, Boolean.parseBoolean(args[0].toString()));
    	readings.set(1, Boolean.parseBoolean(args[1].toString()));
    	readings.set(2, Boolean.parseBoolean(args[2].toString()));
    	readings.set(3, Boolean.parseBoolean(args[3].toString()));
    	
    	RobeauWorldModel worldModel = RobeauWorldModel.get();
    	
    	HiddenMarkovModel hmm = HiddenMarkovModel.get();
    	
    	double[][] o = hmm.generateObservationMatrix(worldModel, readings);
    	
    	hmm.doFiltering(o);
    	hmm.doSmoothing();
    	List<Integer> fP = worldModel.getFreePositions();
    	int mostLikelyState = fP.get(HiddenMarkovModel.argmax(hmm.getProbabilities()));
//    	Location l = worldModel.getAgPos(0);
//    	int mostLikelyState = RobeauWorldModel.positionAsInteger(l.x, l.y, 16);
    	ts.getAg().delBel(Literal.parseLiteral("current_position(X)"));
    	ts.getAg().addBel(Literal.parseLiteral("current_position(" + mostLikelyState + ")"));
    	
        return true;
    }
}
