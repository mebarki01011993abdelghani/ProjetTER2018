package fr.univmrs.lif.model.component.inputoutput;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import fr.univmrs.lif.enumeration.ComponentType;
import fr.univmrs.lif.enumeration.PlugType;
import fr.univmrs.lif.model.component.Component;
import fr.univmrs.lif.model.component.Plug;
import fr.univmrs.lif.tools.NameGenerator;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * Represents a Switch
 * @author
 *
 */
@XmlRootElement
public class Switch extends Component {

	private BooleanProperty on = new SimpleBooleanProperty(false);	

	
	public Switch() {
		setType(ComponentType.SWITCH);
		setName(NameGenerator.generate(type));		
		
		addOutput(new Plug(this, PlugType.OUT));
	}

	public void switchOn() {
		this.on.set(true);
		processing();
	}
	
	public void switchOff() {
		this.on.set(false);
		processing();
	}
	
	/**
	 * Computes outgoing value 
	 */
	public void processing() {
		if(this.simulation_state == PROCESSING) {
			
			for (int i = 0; i < this.getOutputs().size(); i++) {
				if(this.on.get())
					this.getOutputs().get(i).getWire().setState(1);
				
				else
					this.getOutputs().get(i).getWire().setState(0);
				
				this.getOutputs().get(i).getWire().propagate();
			}
		}
	}
	
	public BooleanProperty getOnProperty() {
		return on;
	}

	public Boolean getOn() {
		return on.get();
	}

	public void setOn(boolean on) {
		this.on.set(on);
	}

	@Override
	@XmlTransient
	public int getSimulationState() {
		return this.simulation_state;
	}

	@Override
	public void setSimulationState(int simulation_state) {
		this.simulation_state = simulation_state;
	}
}
