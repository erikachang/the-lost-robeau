// Internal action code for project lost_robot

package robeau;

import hmm.HiddenMarkovModel;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

import java.util.ArrayList;
import java.util.List;

import robeau_environment.RobeauWorldModel;

public class recollect extends DefaultInternalAction {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2762434597824034410L;

	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {        
		RobeauWorldModel worldModel = RobeauWorldModel.get();
		
		List<Integer> fS = worldModel.getFreePositions();
		int n = fS.size();
		double p = 1/(double)n;
		double[] p0 = new double[n];
		for (int i = 0; i < n; i++) {
			p0[i] = p;
		}
		
		int c = 0;
		double[][] t = new double[n][n];
		List<Integer> neighbours;
		for (int i = 0; i < n; i++) {
			c = 0;
			neighbours = worldModel.neighbours(fS.get(i));
			for (int j = 0; j < n; j++) {
				if (neighbours.contains(fS.get(j))) {
					t[i][j] = 1/(double)(neighbours.size());
					c++;
					if (c > neighbours.size())
						break;
				}
			}
		}
		
		HiddenMarkovModel.create(p0, t);
        
        return true;
    }
}
