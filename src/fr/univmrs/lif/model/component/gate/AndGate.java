package fr.univmrs.lif.model.component.gate;

import javax.xml.bind.annotation.XmlRootElement;

import fr.univmrs.lif.enumeration.ComponentType;
import fr.univmrs.lif.enumeration.PlugType;
import fr.univmrs.lif.model.component.Component;
import fr.univmrs.lif.model.component.Plug;
import fr.univmrs.lif.model.simulation.Simulation;
import fr.univmrs.lif.tools.NameGenerator;

/**
 * Represents an AndGate
 * @author
 *
 */
@XmlRootElement
public class AndGate extends Component {

	private AndGateRunnable runnable;
	private boolean started = false;

	public AndGate() {
		setType(ComponentType.AND);
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
				this.runnable = new AndGateRunnable(this);
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

	private class AndGateRunnable extends Thread {
		private AndGate model;

		public AndGateRunnable(AndGate model) {
			this.model = model;
		}

		public void run() {
			while(this.model.getSimulationState() == Simulation.PROCESSING) {
				try {
					Thread.sleep(this.model.getTimeProcessing());
				} catch (InterruptedException e) {e.printStackTrace();}

				int i = 0;
				boolean and = true;

				// On cherche parmis tout les fils en entree s'il y en a un a 0
				while(and && i < this.model.getInputs().size()) {
					if(this.model.getInputs().get(i).getWire().getState() == 0)
						and = false;

					i++;
				}

				// On a obligatoirement une seule sortie sur porte ET ?
				if(and) this.model.getOutputs().get(0).getWire().setState(1);
				else this.model.getOutputs().get(0).getWire().setState(0);

				this.model.getOutputs().get(0).getWire().propagate();
			}
		}
	}
}


