// Internal action code for project lost_robot

package robeau;

import hmm.HiddenMarkovModel;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

import java.util.ArrayList;
import java.util.BitSet;

import lost_robot.RobeauWorldModel;

public class compute extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
    	
    	BitSet readings = new BitSet(4);
    	readings.set(0, Boolean.parseBoolean(args[0].toString()));
    	readings.set(1, Boolean.parseBoolean(args[1].toString()));
    	readings.set(2, Boolean.parseBoolean(args[2].toString()));
    	readings.set(3, Boolean.parseBoolean(args[3].toString()));
    	
    	RobeauWorldModel worldModel = RobeauWorldModel.get();
    	
    	ArrayList<Integer> fP = worldModel.getFreePositions();
    	int n = fP.size();
    	double[][] o = new double[n][n];
    	double e = worldModel.getSensorError();
    	
    	BitSet actual;
    	int d;
    	
    	for (int i = 0; i < fP.size(); i++) {
    		actual = worldModel.getSurroundingBlockades(fP.get(i));
    		actual.xor(readings);
    		
    		d = 0;
        	
        	for (int j = 0; j < 4; j++) {
        		if (actual.get(j))
        			d++;
        	}
        	
        	o[i][i] = Math.pow((1 - e), (4 - d)) * Math.pow(e, d);
    	}
    	
    	HiddenMarkovModel hmm = HiddenMarkovModel.get();
    	
    	hmm.doFiltering(o);
    	hmm.doSmoothing();
    	
        return true;
    }
}
