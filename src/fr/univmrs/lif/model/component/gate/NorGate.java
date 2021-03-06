package fr.univmrs.lif.model.component.gate;

import javax.xml.bind.annotation.XmlRootElement;

import fr.univmrs.lif.enumeration.ComponentType;
import fr.univmrs.lif.enumeration.PlugType;
import fr.univmrs.lif.model.component.Component;
import fr.univmrs.lif.model.component.Plug;
import fr.univmrs.lif.model.simulation.Simulation;
import fr.univmrs.lif.tools.NameGenerator;

/**
 * Represents a NorGate
 * @author
 *
 */
@XmlRootElement
public class NorGate extends Component {

	private NorGateRunnable runnable;
	private boolean started = false;

	public NorGate() {
		setType(ComponentType.NOR);
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
				this.runnable = new NorGateRunnable(this);
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
	
	private class NorGateRunnable extends Thread {
		private NorGate model;

		public NorGateRunnable(NorGate model) {
			this.model = model;
		}

		public void run() {
			while(this.model.getSimulationState() == Simulation.PROCESSING) {
				try {
					Thread.sleep(this.model.getTimeProcessing());
				} catch (InterruptedException e) {e.printStackTrace();}

				int i = 0;
				boolean or = false;

				// On cherche parmis tout les fils en entree s'il y en a un a 1
				while(i < this.model.getInputs().size()) {
					if(this.model.getInputs().get(i).getWire().getState() == 1)
						or = true;

					i++;
				}

				// On a obligatoirement une seule sortie sur porte NONOU ?
				// On inverse la valeur de sortie
				if(or) this.model.getOutputs().get(0).getWire().setState(0);
				else this.model.getOutputs().get(0).getWire().setState(1);

				this.model.getOutputs().get(0).getWire().propagate();
			}
		}
	}
}
