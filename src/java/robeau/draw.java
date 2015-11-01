// Internal action code for project lost_robot

package robeau;

import hmm.HiddenMarkovModel;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

import java.util.ArrayList;

import lost_robot.RobeauWorldModel;

public class draw extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
    	HiddenMarkovModel hmm = HiddenMarkovModel.get();
    	RobeauWorldModel worldModel = RobeauWorldModel.get();
		
		double[] p = hmm.getProbabilities();
		ArrayList<Integer> fP = worldModel.getFreePositions();

		for (int i = 0; i < p.length; i++) {
			int[] coords = RobeauWorldModel.positionFromInteger(fP.get(i), worldModel.getWidth());

			worldModel.drawDot(coords[0], coords[1], p[i]);
		}

        return true;
    }
}
