// Internal action code for project robeau_hero

package robeau;

import hmm.HiddenMarkovModel;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.Plan;
import jason.asSyntax.PlanBody;
import jason.asSyntax.PlanBodyImpl;
import jason.asSyntax.Term;
import jason.asSyntax.Trigger;
import jason.asSyntax.Trigger.TEOperator;
import jason.asSyntax.Trigger.TEType;

import java.util.BitSet;
import java.util.List;

import rescue.RescueHelper;
import robeau_environment.RobeauWorldModel;

public class store extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {

    	BitSet readings = new BitSet(4);
    	for (int i = 0; i < 4; i++) {
    		readings.set(i, Boolean.parseBoolean(args[i].toString()));
    	}
    	
    	RescueHelper list = RescueHelper.getInstance();
    	
    	HiddenMarkovModel hmm = HiddenMarkovModel.get();
    	double[][] observation = hmm.generateObservationMatrix(RobeauWorldModel.get(), readings);
    	double[] probabilities = hmm.doSingleFiltering(observation);
    	RobeauWorldModel worldModel = RobeauWorldModel.get();
    	List<Integer> fP = worldModel.getFreePositions();
    	
    	double threshold = HiddenMarkovModel.max(probabilities) / 2;
    	
    	for (int i = 0; i < probabilities.length; i++) {
    		if (probabilities[i] > threshold) {
    			int pos = fP.get(i);
				list.addTarget(pos);
    		}
    	}

        return true;
    }
}
