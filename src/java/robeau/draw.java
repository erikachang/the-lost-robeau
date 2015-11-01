// Internal action code for project lost_robot

package robeau;

import hmm.HiddenMarkovModel;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;
import jason.environment.grid.Location;

import java.util.ArrayList;
import java.util.List;

import robeau_environment.RobeauWorldModel;

public class draw extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
    	HiddenMarkovModel hmm = HiddenMarkovModel.get();
    	RobeauWorldModel worldModel = RobeauWorldModel.get();
		
		double[] p = hmm.getProbabilities();
		List<Integer> fP = worldModel.getFreePositions();
		ArrayList<double[]> matrix = new ArrayList<double[]>();
		Location l;
		for (int i = 0; i < p.length; i++) {
			l = RobeauWorldModel.locationFromInteger(fP.get(i), worldModel.getWidth());
			matrix.add(new double[] {l.x, l.y, p[i]} );
			worldModel.drawMatrix(matrix);
		}

        return true;
    }
}
