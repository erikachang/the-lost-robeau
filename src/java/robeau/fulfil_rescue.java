// Internal action code for project robeau_hero

package robeau;

import jason.asSemantics.Agent;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;
import rescue.RescueHelper;

public class fulfil_rescue extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
    	
    	int state = Integer.parseInt(args[0].toString());
    	
    	RescueHelper rescueHelper = RescueHelper.getInstance();
    	rescueHelper.removeTarget(state);
    	
    	Agent self = ts.getAg();
    	self.delBel(Literal.parseLiteral("target(X)"));

        return true;
    }
}
