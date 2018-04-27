package fr.univmrs.lif.model.component.inputoutput;

import javax.xml.bind.annotation.XmlRootElement;

import fr.univmrs.lif.enumeration.ComponentType;
import fr.univmrs.lif.enumeration.PlugType;
import fr.univmrs.lif.model.component.Component;
import fr.univmrs.lif.model.component.Plug;
import fr.univmrs.lif.tools.NameGenerator;

/**
 * Represents a Ground
 * @author
 *
 */
@XmlRootElement
public class GND extends Component {
	
	public GND() {
		setType(ComponentType.GND);
		setName(NameGenerator.generate(type));		
		
		addOutput(new Plug(this, PlugType.OUT));
	}
	
	/**
	 * Computes outgoing value 
	 */
	@Override
	public void processing() {
		for(int i = 0; i < this.getOutputs().size(); i++) {
			this.getOutputs().get(i).getWire().setState(0);
			this.getOutputs().get(i).getWire().propagate();
		}
	}
}
