package fr.univmrs.lif.model.component.msi;

import javax.xml.bind.annotation.XmlRootElement;

import fr.univmrs.lif.enumeration.ComponentType;
import fr.univmrs.lif.enumeration.PlugType;
import fr.univmrs.lif.model.component.Component;
import fr.univmrs.lif.model.component.Plug;
import fr.univmrs.lif.tools.NameGenerator;

/**
 * Represents a Multiplexer
 * @author
 *
 */
@XmlRootElement
public class Multiplexer extends Component {
	
	private int bit;
	
	public Multiplexer() {
		setType(ComponentType.MUX);
		setName(NameGenerator.generate(getType()));
		
		addInput(new Plug(this, PlugType.IN));
		
		addInput(new Plug(this, PlugType.IN));
		addInput(new Plug(this, PlugType.IN));

		addOutput(new Plug(this, PlugType.OUT));
		
		this.bit = 1;
	}
	
	public Multiplexer(int bit) {
		setType(ComponentType.MUX);
		setName(NameGenerator.generate(getType()));
		
		int inputs = (int) Math.pow(2.0, (double) bit) + bit;
		inputNumber.set(inputs);
		for(int i = 0; i < inputs; i++)
			addInput(new Plug(this, PlugType.IN));

		outputNumber.set(1);
		addOutput(new Plug(this, PlugType.OUT));
		
		this.bit = bit;
	}

	/**
	 * Computes outgoing value 
	 */
	@Override
	public void processing() {
		if(this.simulation_state == PROCESSING) {
			try {
				Thread.sleep(this.getTimeProcessing());
			} catch (InterruptedException e) {e.printStackTrace();}
			
			String aut = "";
			for(int i = 0; i < this.bit; i++) 
				aut = aut + this.getInputs().get(i).getWire().getState();
			
			int output = Integer.parseInt(aut, 2);
			this.getOutputs().get(0).getWire().setState(this.getInputs().get(this.bit + output).getWire().getState());
			
			this.getOutputs().get(0).getWire().propagate();
		}
	}

	public int getBit() {
		return bit;
	}

	public void setBit(int bit) {
		this.bit = bit;
	}
}
