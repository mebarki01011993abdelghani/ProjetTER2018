package fr.univmrs.lif.model.component;


import javax.xml.bind.annotation.XmlRootElement;

import fr.univmrs.lif.enumeration.ComponentType;
import fr.univmrs.lif.enumeration.PlugType;
import fr.univmrs.lif.tools.NameGenerator;

@XmlRootElement
public class Joint extends Component {
		
	public Joint() {
		setType(ComponentType.JOINT);
		setName(NameGenerator.generate(getType()));
		
		addInput(new Plug(this, PlugType.IN));
		
		addOutput(new Plug(this, PlugType.OUT));
		addOutput(new Plug(this, PlugType.OUT));
		//
		removeOutput(new Plug(this, PlugType.OUT));
	}
	
	@Override
	public void processing() {
		try {
			Thread.sleep(this.getTimeProcessing());
		} catch (InterruptedException e) {e.printStackTrace();}
		
		int state = this.getInputs().get(0).getWire().getState();
		
		for(int i = 0; i < this.getOutputs().size(); i++) {
			this.getOutputs().get(i).getWire().setState(state);
			this.getOutputs().get(i).getWire().propagate();
		}
	}
}
