package fr.univmrs.lif.model.component.inputoutput;

import javax.xml.bind.annotation.XmlRootElement;

import fr.univmrs.lif.enumeration.ComponentType;
import fr.univmrs.lif.enumeration.PlugType;
import fr.univmrs.lif.model.component.Component;
import fr.univmrs.lif.model.component.Plug;
import fr.univmrs.lif.tools.NameGenerator;

@XmlRootElement
public class InputModule extends Component{
	
	// This is plug ref of module view
	private Plug PlugIn;
	
	public InputModule() {
		setType(ComponentType.INPUTMODULE);
		setName(NameGenerator.generate(getType()));
	
		addOutput(new Plug(this, PlugType.OUT));
	}
	
	@Override
	public void processing() {
		int state = this.PlugIn.getWire().getState();
		this.getOutputs().get(0).getWire().setState(state);
		this.getOutputs().get(0).getWire().propagate();
	}
	
	
	public Plug getPlugIn() {
		return PlugIn;
	}

	public void setPlugIn(Plug plugIn) {
		PlugIn = plugIn;
	}

}
