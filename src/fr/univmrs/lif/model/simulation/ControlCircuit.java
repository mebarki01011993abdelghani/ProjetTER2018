package fr.univmrs.lif.model.simulation;

import java.util.ArrayList;
import java.util.List;

import fr.univmrs.lif.controller.bottomBar.Console;
import fr.univmrs.lif.enumeration.ComponentFamily;
import fr.univmrs.lif.enumeration.ComponentType;
import fr.univmrs.lif.model.component.Component;
import fr.univmrs.lif.model.component.Module;
import fr.univmrs.lif.model.component.Plug;
import fr.univmrs.lif.model.component.msi.Multiplexer;

public class ControlCircuit {
	private Module main_module;

	private ArrayList<ErrorCircuit> errors;

	public ControlCircuit(Module main_module) {
		this.main_module = main_module;
		this.errors = new ArrayList<ErrorCircuit>();
	}

	public void start() {
		for(int i = 0; i < this.main_module.getComponents().size(); i++) {

			verify(this.main_module.getComponents().get(i));
		}
	}

	private void verify(Component current) {
		checkConnection(current);

		// Cas particulier

		if(current.getFamily() == ComponentFamily.INPUT)
			checkInput(current);

		if(current.getFamily() == ComponentFamily.OUTPUT)
			checkOutput(current);

		if(current.getFamily() == ComponentFamily.GATE)
			checkGate(current);

		if(current.getFamily() == ComponentFamily.MSI)
			checkMSI(current);

		if(current.getFamily() == ComponentFamily.ALU)
			checkALU(current);
	}

	/*** ERREURS RELATIVES AUX CONNEXIONS EN GENERAL ***/
	private void checkConnection(Component current) {
		// Traitement des inputs
		for(int i = 0; i < current.getInputs().size(); i++) {

			// Impossible d'avoir un input non relie
			if(current.getInputs().get(i).getWire() == null) {
				ErrorCircuit error = new ErrorCircuit(current);
				error.setModule(this.main_module.getName());
				error.setError("No component in input wire !");

				this.errors.add(error);
			}

			else {
				// Impossible d'avoir un input branche a un autre input
				List<Plug> inputs_before = current.getInputs().get(i).getWire().getHead().getInputs();
				for(int j = 0; j < inputs_before.size(); j++) {
					if(inputs_before.get(j).getWire().getTail() == current) {
						ErrorCircuit error = new ErrorCircuit(current);
						error.setModule(this.main_module.getName());
						error.setError("Component input connected with another component input !");

						this.errors.add(error);
					}
				}
			}
		}

		// Traitement des outputs
		for(int i = 0; i < current.getOutputs().size(); i++) {

			// Impossible d'avoir un output non relie
			if(current.getOutputs().get(i).getWire() == null) {
				ErrorCircuit error = new ErrorCircuit(current);
				error.setModule(this.main_module.getName());
				error.setError("No component in output wire !");

				this.errors.add(error);
			}
			
			else {
				// Impossible d'avoir un output branche a un autre output
				List<Plug> outputs_next = current.getOutputs().get(i).getWire().getTail().getOutputs();
				for(int j = 0; j < outputs_next.size(); j++) {
					if(outputs_next.get(j).getWire().getHead() == current) {
						ErrorCircuit error = new ErrorCircuit(current);
						error.setModule(this.main_module.getName());
						error.setError("Component output connected with another component output !");

						this.errors.add(error);
					}
				}
			}
		}
		
		
	}

	/*** ERREURS RELATIVE AUX INPUTS ***/
	private void checkInput(Component current) {
		// Impossible d'avoir d'inputs
		if(!current.getInputs().isEmpty()) {
			ErrorCircuit error = new ErrorCircuit(current);
			error.setModule(this.main_module.getName());
			error.setError("No inputs allowed in this type !");

			this.errors.add(error);
		}
	}

	/*** ERREURS RELATIVE AUX OUTPUTS ***/
	private void checkOutput(Component current) {
		// Impossible d'avoir d'outputs
		if(!current.getOutputs().isEmpty()) {
			ErrorCircuit error = new ErrorCircuit(current);
			error.setModule(this.main_module.getName());
			error.setError("No outputs allowed in this type !");

			this.errors.add(error);
		}
	}

	/*** ERREURS RELATIVE AUX GATES ***/
	private void checkGate(Component current) {
		// Impossible d'avoir plus d'un output, sauf pout la jointure
		if(current.getOutputs().size() > 1 && current.getType() != ComponentType.JOINT ) {
			ErrorCircuit error = new ErrorCircuit(current);
			error.setModule(this.main_module.getName());
			error.setError("To many outputs !");

			this.errors.add(error);
		}

		// Cas particulier du NOT et de la jointure
		if(current.getType() == ComponentType.NOT || current.getType() == ComponentType.JOINT) {
			// Impossible d'avoir plus d'un input
			if(current.getInputs().size() > 1) {
				ErrorCircuit error = new ErrorCircuit(current);
				error.setModule(this.main_module.getName());
				error.setError("To many inputs !");

				this.errors.add(error);
			}
		}
	}

	/*** ERREURS RELATIVE AUX MSI ***/
	private void checkMSI(Component current) {
		// Cas particulier du decodeur
		if(current.getType() == ComponentType.DECODER) {
			int inputs = current.getInputs().size();
			int outputs = (int) Math.pow(2.0, (double) inputs);
			// pour k entrees il doit y avoir 2^k sorties
			if(outputs != current.getOutputs().size()) {
				ErrorCircuit error = new ErrorCircuit(current);
				error.setModule(this.main_module.getName());
				error.setError("Not enough outputs or to many inputs ! This component require 2^k outputs for k inputs.");

				this.errors.add(error);
			}
		}

		// Cas particulier du multiplexeur
		if(current.getType() == ComponentType.MUX) {
			int bit = ((Multiplexer) current).getBit();
			int inputs = (int) Math.pow(2.0, (double) bit) + bit;
			// pour k entrees il doit y avoir 2^k sorties
			if(inputs != current.getInputs().size()) {
				ErrorCircuit error = new ErrorCircuit(current);
				error.setModule(this.main_module.getName());
				error.setError("Not enough or to many inputs ! This component require " + inputs + " inputs.");

				this.errors.add(error);
			}

			// Impossible d'avoir plus d'un output
			if(current.getOutputs().size() > 1) {
				ErrorCircuit error = new ErrorCircuit(current);
				error.setModule(this.main_module.getName());
				error.setError("To many outputs !");

				this.errors.add(error);
			}
		}
	}

	/*** ERREURS RELATIVE AUX ALU ***/
	private void checkALU(Component current) {
		// Cas particulier de l'additionneur
		if(current.getType() == ComponentType.ADDER) {
			// Impossible d'avoir plus de deux inputs
			if(current.getInputs().size() > 3) {
				ErrorCircuit error = new ErrorCircuit(current);
				error.setModule(this.main_module.getName());
				error.setError("To many inputs ! This component require 2 inputs.");

				this.errors.add(error);
			}

			// Impossible d'avoir plus de deux outputs
			if(current.getOutputs().size() > 2) {
				ErrorCircuit error = new ErrorCircuit(current);
				error.setModule(this.main_module.getName());
				error.setError("To many outputs ! This component require 2 outputs.");

				this.errors.add(error);
			}
		}
	}

	public boolean isValid() {
		if(this.errors.isEmpty()) return true;
		else return false;
	}

	public ArrayList<ErrorCircuit> getErrors() {
		return this.errors;
	}

	public void printErrors() {
		if(this.errors.isEmpty())
			Console.println("No errors detected. Simulation can start.", "-fx-font-weight: bold");
		else {
			Console.println(this.errors.size() + " errors found:", "-fx-font-weight: bold");
			for(int i = 0; i < this.errors.size(); i++) {			
				Console.errorln(this.getErrors().get(i).toString());
			}
		}
	}
}
