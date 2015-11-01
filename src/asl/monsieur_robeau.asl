observations(0).

!start.

+!start : true <-
	robeau.recollect;
	!wander.

+!wander
	<- scan;
	move.

+blocked(N, E, W, S) : observations(X)
	<- robeau.compute(N, E, W, S);
	robeau.draw;
	O = X + 1;
	-+observations(O);
//	.println("Observations so far: ", O);
	.wait(10);
	!wander.
	