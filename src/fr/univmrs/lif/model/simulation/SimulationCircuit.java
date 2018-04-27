package fr.univmrs.lif.model.simulation;

import fr.univmrs.lif.controller.bottomBar.Console;
import fr.univmrs.lif.enumeration.ComponentFamily;
import fr.univmrs.lif.enumeration.ComponentType;
import fr.univmrs.lif.model.component.Component;
import fr.univmrs.lif.model.component.Module;
import fr.univmrs.lif.model.component.inputoutput.Display;
import fr.univmrs.lif.model.component.inputoutput.Led;

public class SimulationCircuit {
	
	private Module main;
	
	public SimulationCircuit(Module main) {
		this.main = main;
	}
	
	public void start() {
		for(int i = 0; i < main.getComponents().size(); i++) {
			main.getComponents().get(i).setSimulationState(Simulation.PROCESSING);
			if(main.getComponents().get(i).getType() == ComponentType.MODULE) {
				SimulationCircuit s = new SimulationCircuit((Module) main.getComponents().get(i));
				s.start();
			}
		}
		
		initialize_inputs();
	}

	public void pause() {
		for(int i = 0; i < main.getComponents().size(); i++) {
			main.getComponents().get(i).setSimulationState(Simulation.PAUSED);
			main.getComponents().get(i).processing();
			if(main.getComponents().get(i).getType() == ComponentType.MODULE) {
				SimulationCircuit s = new SimulationCircuit((Module) main.getComponents().get(i));
				s.pause();
			}
		}
	}
	
	public void stop() {
		for(int i = 0; i < main.getComponents().size(); i++) {
			main.getComponents().get(i).setSimulationState(Simulation.NOT_SIMULATED);
			main.getComponents().get(i).processing();
			
			for(int j = 0; j < main.getComponents().get(i).getAllWires().size(); j++) 
				main.getComponents().get(i).getAllWires().get(j).setState(0);
			
			if(main.getComponents().get(i).getFamily() == ComponentFamily.OUTPUT)
				switchOff(main.getComponents().get(i));
			
			if(main.getComponents().get(i).getType() == ComponentType.MODULE) {
				SimulationCircuit s = new SimulationCircuit((Module) main.getComponents().get(i));
				s.stop();
			}
		}
	}
	
	private void switchOff(Component component) {
		if(component.getType() == ComponentType.LED)
			((Led) component).setActive(false);
		
		if(component.getType() == ComponentType.DISPLAY) {
			for(int i = 0; i < component.getInputNumber(); i++)
				((Display) component).getSegments().get(i).setValue(false);
		}
		
	}

	private void initialize_inputs() {
		for(int i = 0; i < main.getComponents().size(); i++)
			if(main.getComponents().get(i).getFamily() == ComponentFamily.INPUT)
				main.getComponents().get(i).processing();
	}
}
