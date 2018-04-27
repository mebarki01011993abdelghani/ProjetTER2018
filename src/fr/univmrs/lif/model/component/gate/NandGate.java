package fr.univmrs.lif.model.component.gate;

import javax.xml.bind.annotation.XmlRootElement;

import fr.univmrs.lif.enumeration.ComponentType;
import fr.univmrs.lif.enumeration.PlugType;
import fr.univmrs.lif.model.component.Component;
import fr.univmrs.lif.model.component.Plug;
import fr.univmrs.lif.model.simulation.Simulation;
import fr.univmrs.lif.tools.NameGenerator;

/**
 * Represents a NandGate
 * @author
 *
 */
@XmlRootElement
public class NandGate extends Component {

	private NandGateRunnable runnable;
	private boolean started = false;

	public NandGate() {		
		setType(ComponentType.NAND);
		setName(NameGenerator.generate(getType()));

		addInput(new Plug(this, PlugType.IN));
		addInput(new Plug(this, PlugType.IN));

		addOutput(new Plug(this, PlugType.OUT));
	}

	/**
	 * Computes outgoing value 
	 */
	public void processing() {		
		if(this.getSimulationState() == PROCESSING) {
			if(!this.started) {
				this.runnable = new NandGateRunnable(this);
				this.runnable.start();
				this.started = true;
			}
		} else {
			this.started = false;

			try {
				if(runnable != null)
					this.runnable.join();
			} catch (InterruptedException e) {e.printStackTrace();}
		}
	}

	private class NandGateRunnable extends Thread {
		private NandGate model;

		public NandGateRunnable(NandGate model) {
			this.model = model;
		}

		public void run() {
			while(this.model.getSimulationState() == Simulation.PROCESSING) {
				try {
					Thread.sleep(this.model.getTimeProcessing());
				} catch (InterruptedException e) {e.printStackTrace();}

				int cpt = 0;
				for(int i = 0; i < this.model.getInputs().size(); i++) {
					if(this.model.getInputs().get(i).getWire().getState() == 1)
						cpt++;
				}

				// Si toutes les entrees sont a 1 alors on renvoie 0
				if(cpt == this.model.getInputs().size()) this.model.getOutputs().get(0).getWire().setState(0);
				else this.model.getOutputs().get(0).getWire().setState(1);

				this.model.getOutputs().get(0).getWire().propagate();
			}
		}
	}
}
