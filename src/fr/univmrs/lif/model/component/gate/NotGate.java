package fr.univmrs.lif.model.component.gate;

import javax.xml.bind.annotation.XmlRootElement;

import fr.univmrs.lif.enumeration.ComponentType;
import fr.univmrs.lif.enumeration.PlugType;
import fr.univmrs.lif.model.component.Component;
import fr.univmrs.lif.model.component.Plug;
import fr.univmrs.lif.tools.NameGenerator;

/**
 * Represents a NotGate
 * @author
 *
 */
@XmlRootElement
public class NotGate extends Component {


	public NotGate() {
		setType(ComponentType.NOT);
		setName(NameGenerator.generate(getType()));

		super.addInput(new Plug(this, PlugType.IN));
		super.addOutput(new Plug(this, PlugType.OUT));
	}

	/**
	 * Computes outgoing value 
	 */
	@Override
	public void processing() {
		// Attente active en cas de mise en pause de la simulation ?
		if(this.simulation_state == PROCESSING) {

			// On a obligatoirement une seule entree et une seule sortie sur porte NON ?
			if(this.getInputs().get(0).getWire().getState() == 0) this.getOutputs().get(0).getWire().setState(1);
			else this.getOutputs().get(0).getWire().setState(0);

			this.getOutputs().get(0).getWire().propagate();
		}
	}

	@Override
	public void addInput(Plug plug) {}

	@Override
	public void addOutput(Plug plug) {}

}
