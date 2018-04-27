package fr.univmrs.lif.model.component.inputoutput;

import javax.xml.bind.annotation.XmlRootElement;

import fr.univmrs.lif.enumeration.ComponentType;
import fr.univmrs.lif.enumeration.PlugType;
import fr.univmrs.lif.model.component.Component;
import fr.univmrs.lif.model.component.Plug;
import fr.univmrs.lif.tools.NameGenerator;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * Represents a Led
 * @author
 *
 */
@XmlRootElement
public class Led extends Component{

	BooleanProperty active = new SimpleBooleanProperty(false);

	public Led(){
		setType(ComponentType.LED);
		setName(NameGenerator.generate(getType()));
	
		addInput(new Plug(this, PlugType.IN));
	}

	public BooleanProperty activeProperty() {
		return active;
	}

	public Boolean getActive() {
		return active.get();
	}

	public void setActive(Boolean active) {
		this.active.set(active);
	}

	/**
	 * Computes outgoing value 
	 */
	@Override
	public void processing() {
		if(this.simulation_state == PROCESSING) {
			if(getInputs().get(0).getWire().getState()== 1)
				active.set(true);
			else
				active.set(false);
		}
	}
}
