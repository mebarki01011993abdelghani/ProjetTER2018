package fr.univmrs.lif.model.component.gate;

import javax.xml.bind.annotation.XmlRootElement;

import fr.univmrs.lif.enumeration.ComponentType;
import fr.univmrs.lif.enumeration.PlugType;
import fr.univmrs.lif.model.component.Component;
import fr.univmrs.lif.model.component.Plug;
import fr.univmrs.lif.model.simulation.Simulation;
import fr.univmrs.lif.tools.NameGenerator;

/**
 * Represents an XorGate
 * @author
 *
 */
@XmlRootElement
public class XorGate extends Component {
	
	private XorGateRunnable runnable;
	private boolean started = false;

	public XorGate() {
		setType(ComponentType.XOR);
		setName(NameGenerator.generate(getType()));

		addInput(new Plug(this, PlugType.IN));
		addInput(new Plug(this, PlugType.IN));

		addOutput(new Plug(this, PlugType.OUT));
	}

	/**
	 * Computes outgoing value 
	 */
	@Override
	public void processing() {
		if(this.getSimulationState() == PROCESSING) {
			if(!this.started) {
				this.runnable = new XorGateRunnable(this);
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

	private class XorGateRunnable extends Thread {
		private XorGate model;

		public XorGateRunnable(XorGate model) {
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

				if(cpt == 1) this.model.getOutputs().get(0).getWire().setState(1);
				else this.model.getOutputs().get(0).getWire().setState(0);

				this.model.getOutputs().get(0).getWire().propagate();
			}
		}
	}
}
