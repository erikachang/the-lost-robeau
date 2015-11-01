+!observe[source(S)] 
	: rescuer(R) & S == R
	<- scan.

+blocked(N, E, W, S)[source(percept)] 
	: rescuer(R) & not free(_)
	<- .println("Hey, I am over here!");
	.send(R, tell, prisoner(N, E, W, S)).
	
+robeauIsIn[source(monsieur_robeau)]
	<- +rescuer(monsieur_robeau);
	-robeauIsIn[source(monsieur_robeau)].
	
+free[source(X)] : rescuer(R) & R == X
	<- .println("Free at last!");
	.my_name(Me);
	.kill_agent(Me).