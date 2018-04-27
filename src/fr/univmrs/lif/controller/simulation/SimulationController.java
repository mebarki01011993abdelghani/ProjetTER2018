package fr.univmrs.lif.controller.simulation;

import fr.univmrs.lif.controller.MainController;
import fr.univmrs.lif.controller.bottomBar.Console;
import fr.univmrs.lif.model.simulation.ControlCircuit;
import fr.univmrs.lif.model.simulation.SimulationCircuit;

public class SimulationController {
	MainController mainController;
	
	ControlCircuit controlCircuit;
	SimulationCircuit simulationCircuit;

	public SimulationController(MainController mainController) {
		this.mainController = mainController;
		
		}
	
	/**
	 * Verify if the circuit can be simulated
	 * @return
	 */
	public boolean verifyCircuit(){
		controlCircuit = new ControlCircuit(MainController.getCurrentProject().getMain());
		simulationCircuit = new SimulationCircuit(MainController.getCurrentProject().getMain());
	
		
		controlCircuit.start();
		
		if(controlCircuit.isValid()){
			controlCircuit.printErrors();
			return true;
		}else{
			// TODO a mettre dans la fenetre de log
			controlCircuit.printErrors();
			return false;
		}	
	}
	
	/**
	 * Start simulation
	 */
	public void startSimulation(){
		simulationCircuit.start();
		Console.println("-> Simulation is started.");
	}
	
	/**
	 * pause simulation
	 */
	public void pauseSimulation(){
		simulationCircuit.pause();
		Console.println("-> Simulation is paused.");
	}
	
	/**
	 * stop simulation
	 */
	public void stopSimulation(){
		simulationCircuit.stop();
		Console.println("-> Simulation is stopped.");
	}
	

}
