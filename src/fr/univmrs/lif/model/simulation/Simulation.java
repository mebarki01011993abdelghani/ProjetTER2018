package fr.univmrs.lif.model.simulation;

public interface Simulation {
	public static int DEFAULT_TIME_PROCESSING = 10;
	
	public static int PROCESSING 	=  1;
	public static int PAUSED	 	=  0;
	public static int NOT_SIMULATED = -1;
	
	public void processing();
	
	public int getSimulationState();
	
	public void setSimulationState(int simulation_state);
}
