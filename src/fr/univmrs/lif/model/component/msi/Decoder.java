package fr.univmrs.lif.model.component.msi;

import javax.xml.bind.annotation.XmlRootElement;

import fr.univmrs.lif.enumeration.ComponentType;
import fr.univmrs.lif.enumeration.PlugType;
import fr.univmrs.lif.model.component.Component;
import fr.univmrs.lif.model.component.Plug;
import fr.univmrs.lif.tools.NameGenerator;

/**
 * Represents a Decoder
 * @author
 *
 */
@XmlRootElement
public class Decoder extends Component {
	
	private int bit;
	
	public Decoder() {
		setType(ComponentType.DECODER);
		setName(NameGenerator.generate(getType()));
	
		addInput(new Plug(this, PlugType.IN));
		
		addOutput(new Plug(this, PlugType.OUT));
		addOutput(new Plug(this, PlugType.OUT));
		
		this.bit = 1;
	}
	
	public Decoder(int bit) {
		setType(ComponentType.DECODER);
		setName(NameGenerator.generate(getType()));
		

		for(int i = 0; i < bit; i++)
			addInput(new Plug(this, PlugType.IN));
		
		int outputs = (int) Math.pow(2.0, (double) bit);
	
		for(int i = 0; i < outputs; i++)
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
			for(int i = 0; i < this.getInputs().size(); i++) 
				aut = aut + this.getInputs().get(i).getWire().getState();
			
			int output = Integer.parseInt(aut, 2);
			
			for(int i = 0; i < this.getOutputs().size(); i++)
				this.getOutputs().get(i).getWire().setState(0);
			
			this.getOutputs().get(output).getWire().setState(1);
			
			for(int i = 0; i < this.getOutputs().size(); i++)
				this.getOutputs().get(i).getWire().propagate();
		}
	}
	
	public int getBit() {
		return bit;
	}

	public void setBit(int bit) {
		this.bit = bit;
	}
}
