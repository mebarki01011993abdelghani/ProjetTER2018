package fr.univmrs.lif.model.component.alu;

import javax.xml.bind.annotation.XmlRootElement;

import fr.univmrs.lif.enumeration.ComponentType;
import fr.univmrs.lif.enumeration.PlugType;
import fr.univmrs.lif.model.component.Component;
import fr.univmrs.lif.model.component.Plug;
import fr.univmrs.lif.tools.NameGenerator;

/**
 * Represents an Adder
 * @author
 *
 */
@XmlRootElement
public class Adder extends Component {
	
	// INFO : les entrees d'indice 0 des inputs et des outputs sont prevues pour les retenues (carry)
	public Adder() {
		setType(ComponentType.ADDER);
		setName(NameGenerator.generate(getType()));
	
		// carry
		addInput(new Plug(this, PlugType.IN));
		
		addInput(new Plug(this, PlugType.IN));
		addInput(new Plug(this, PlugType.IN));

		// carry
		addOutput(new Plug(this, PlugType.OUT));
		
		addOutput(new Plug(this, PlugType.OUT));
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
		
			int cpt = 0;
			for(int i = 0; i < this.getInputs().size(); i++) 
				if(this.getInputs().get(i).getWire().getState() == 1)
					cpt++;
			
			if(cpt == 1) {
				this.getOutputs().get(0).getWire().setState(0);
				this.getOutputs().get(1).getWire().setState(1);
			} else if(cpt == 2) {
				this.getOutputs().get(0).getWire().setState(1);
				this.getOutputs().get(1).getWire().setState(0);
			} else if(cpt == 3) {
				this.getOutputs().get(0).getWire().setState(1);
				this.getOutputs().get(1).getWire().setState(1);
			} else  {
				this.getOutputs().get(0).getWire().setState(0);
				this.getOutputs().get(1).getWire().setState(0);
			}
			
			this.getOutputs().get(0).getWire().propagate();
			this.getOutputs().get(1).getWire().propagate();
		}
		
	}

}
