// Internal action code for project robeau_hero

package robeau;

import jason.asSemantics.Agent;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;
import rescue.RescueHelper;

public class choose_target extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
//    	RobeauWorldModel.positionAsInteger(l.x, l.y, worldModel.getWidth());
    	Agent self = ts.getAg();
    	
    	int robeau = Integer.parseInt(args[0].toString());
    	int topCandidate = RescueHelper.getInstance().topCandidate(robeau);
    	
    	
    	if (topCandidate >= 0) {	
    		self.delBel(Literal.parseLiteral("target(X)"));
    		self.addBel(Literal.parseLiteral("target(" + topCandidate + ")"));
    	}
    		
        return true;
    }
}
