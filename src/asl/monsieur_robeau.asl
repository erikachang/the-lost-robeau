observations(0).
prisoners(0).

!start.

+!start
	<- robeau.recollect;
	scan;
	.broadcast(tell, robeauIsIn);
	!routine.

+!routine : prisoners(X) & X > 0
	<- !rescue.
+!routine 
	<- -+prisoners(0);
	.println("Is there anybody out there?");
	!!wait_for_prisoners;
	.broadcast(achieve, observe).


+!wait_for_prisoners
	<- .wait("+prisoner", 2500);
	!wait_for_prisoners.
-!wait_for_prisoners : prisoners(X) & X > 0
	<- .println("Initiating search for prisoners!");
	!rescue.
-!wait_for_prisoners
	<- ?observations(X);
	.println("My work here is done! Observations: ", X); 
	?current_position(A);
	search.compute_path(A, 47, endSimulation);
	!moveTowardsGoal.

+!endSimulation
	<- .my_name(Me);
	.kill_agent(Me). 

@blocked[atomic]
+blocked(N, E, W, S)[source(percept)] 
	: observations(X)
	<- robeau.compute(N, E, W, S);
	O = X + 1;
	-+observations(O).
//	.println("Observations so far: ", O).

@anotherPrisoner[atomic]
+!anotherPrisoner
	<- ?prisoners(P);
	Ps = P + 1;
	-+prisoners(Ps).

+prisoner(N, E, W, S)[source(Ag)] : true
	<- !!anotherPrisoner;
	robeau.store(N, E, W, S);
	-prisoner(N, E, W, S)[source(Ag)];
	.println("Agent ", Ag, " acknowledged.").


+!rescue : prisoners(X) & X > 0
	<- ?current_position(A);
	robeau.choose_target(A);
	?target(B); 
	search.compute_path(A, B);
	.println("I'm at ", A, " and going for ", B, ".");
	!moveTowardsGoal.
+!rescue
	<- !routine.
-!rescue : not target(_)
	<- .println("No targets left.");
	+target(47);
	!routine.
-!rescue : not current_position(_)
	<- .println("current position unknown, cannot proceed...").

+!retry
	<- ?current_position(A);
	?target(B);
	search.compute_path(A, B);
	!moveTowardsGoal.
-!retry
	<- !retry.

+!recover
	<- .println("It seems I'm lost...");
	random_move;
	scan;
	.wait(100);
	!retry.

-!moveTowardsGoal
	<- !recover.

@updateCounter[atomic]
+!updateCounter
	<- ?prisoners(X);
	N = X - 1;
	-+prisoners(N).

+!finished : current_position(A) & target(B) & A == B
	<- .print("Arrived at the target. Looking for prisoners...");
	robeau.fulfil_rescue(B);
	free_agent;
	!updateCounter;
	!rescue.
+!finished : current_position(A) & target(B) & A \== B
	<- .print("I don't think this is it... Let me try this again.");
	!rescue.
-!finished 
	<- .print("No prisoners found. Maybe I got lost midway.");
	!rescue.
	
+!endgame
	<- -+prisoners(0); 
	!routine.
	
+free(Ag)
	<- .send(Ag, tell, free).