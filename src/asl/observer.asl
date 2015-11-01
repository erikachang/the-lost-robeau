interval(1).

!evaluate.

+!evaluate
	: interval(X)
	<- observer.check_time(X);
	.wait(10);
	!evaluate.
-!evaluate
	<- !evaluate.
	
+time(X)
	<- observer.evaluate(X).
	
+miss(X, T) 
	<- .println("Differences at t = ", T, ": ", X).
	
+threshold(X)
	<- .println("At t = ", X, ": 0.0").