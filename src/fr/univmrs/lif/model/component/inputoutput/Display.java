package fr.univmrs.lif.model.component.inputoutput;

import java.util.ArrayList;

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
 * Represents a Display
 * @author
 *
 */
@XmlRootElement
public class Display extends Component{
	
	private ArrayList<BooleanProperty> segments = new ArrayList<>();
	
	public Display() {
		setType(ComponentType.DISPLAY);
		setName(NameGenerator.generate(getType()));
		
	
		addInput(new Plug(this, PlugType.IN));
		addInput(new Plug(this, PlugType.IN));
		addInput(new Plug(this, PlugType.IN));
		addInput(new Plug(this, PlugType.IN));
		addInput(new Plug(this, PlugType.IN));
		addInput(new Plug(this, PlugType.IN));
		addInput(new Plug(this, PlugType.IN));
		
		for(int i = 0; i < this.getInputNumber(); i++)
			this.segments.add(new SimpleBooleanProperty(false));
	}

	/**
	 * Computes outgoing value 
	 */
	@Override
	public void processing() {
		if(this.simulation_state == PROCESSING) {
			for(int i = 0; i < this.getInputs().size(); i++) {
				if(this.getInputs().get(i).getWire().getState() == 1)
					this.getSegments().get(i).set(true);
				else
					this.getSegments().get(i).set(false);
			}
		}
	}

	@XmlTransient
	public ArrayList<BooleanProperty> getSegments() {
		return segments;
	}

	public void setSegments(ArrayList<BooleanProperty> segments) {
		this.segments = segments;
	}

	public BooleanProperty getActiveProperty(int id_segment) {
		return this.segments.get(id_segment);
	}
}
