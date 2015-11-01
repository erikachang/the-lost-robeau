// Internal action code for project lost_robot

package observer;

import hmm.HiddenMarkovModel;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;

public class check_time extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        HiddenMarkovModel hmm = HiddenMarkovModel.get();
        
        if (hmm == null) {
        	return false;
        }
        
        int time = hmm.getTime();
        int interval = Integer.parseInt(args[0].toString());
        
        if (time > 0 && time % interval == 0) {
        	ts.getAg().addBel(Literal.parseLiteral("time(" + time + ")"));
        }
        
        return true;
    }
}
