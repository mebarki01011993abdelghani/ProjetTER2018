package fr.univmrs.lif.model.component.inputoutput;

import javax.xml.bind.annotation.XmlRootElement;

import fr.univmrs.lif.enumeration.ComponentType;
import fr.univmrs.lif.enumeration.PlugType;
import fr.univmrs.lif.model.component.Component;
import fr.univmrs.lif.model.component.Plug;
import fr.univmrs.lif.tools.NameGenerator;

@XmlRootElement
public class OutputModule extends Component {

	// This is plug ref of module view
	private Plug PlugOut;
	
	public OutputModule() {
		setType(ComponentType.OUTPUTMODULE);
		setName(NameGenerator.generate(getType()));
	
		addInput(new Plug(this, PlugType.IN));	
	}
	
	@Override
	public void processing() {
		int state = this.getInputs().get(0).getWire().getState();
		this.PlugOut.getWire().setState(state);
		this.PlugOut.getWire().propagate();
	}

	public Plug getPlugOut() {
		return PlugOut;
	}

	public void setPlugOut(Plug plugOut) {
		PlugOut = plugOut;
	}

}
