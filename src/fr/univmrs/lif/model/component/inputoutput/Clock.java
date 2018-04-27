package fr.univmrs.lif.model.component.inputoutput;

import javax.xml.bind.annotation.XmlRootElement;

import fr.univmrs.lif.enumeration.ComponentType;
import fr.univmrs.lif.enumeration.PlugType;
import fr.univmrs.lif.model.component.Component;
import fr.univmrs.lif.model.component.Plug;
import fr.univmrs.lif.model.simulation.Simulation;
import fr.univmrs.lif.tools.NameGenerator;

/**
 * Represents a Clock
 * @author
 *
 */
@XmlRootElement
public class Clock extends Component {
	
	private int frequency = 2000;
	private ClockRunnable model;

	public Clock() {
		setType(ComponentType.CLOCK);
		setName(NameGenerator.generate(type));

		addOutput(new Plug(this, PlugType.OUT));
	}

	/**
	 * Computes outgoing value 
	 */
	@Override
	public void processing() {
		if(this.getSimulationState() == PROCESSING) {
			this.model = new ClockRunnable(this);
			this.model.start();
		} else {
			try {
				if(model != null)
				this.model.join();
			} catch (InterruptedException e) {e.printStackTrace();}
		}
		
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	
	private class ClockRunnable extends Thread {
		private Clock model;
		
		public ClockRunnable(Clock model) {
			this.model = model;
		}
		
		public void run() {
			int state = 0;
			while(this.model.getSimulationState() == Simulation.PROCESSING) {
				try {
					Thread.sleep(this.model.getFrequency());
				} catch (InterruptedException e) {e.printStackTrace();}

				for(int i = 0; i < this.model.getOutputs().size(); i++) {
					this.model.getOutputs().get(i).getWire().setState(state);
					this.model.getOutputs().get(i).getWire().propagate();
				}
				
				state = (state + 1) % 2;
			}
		}
	}
}
