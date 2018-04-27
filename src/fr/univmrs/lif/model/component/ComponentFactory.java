package fr.univmrs.lif.model.component;

import fr.univmrs.lif.enumeration.ComponentType;
import fr.univmrs.lif.model.component.alu.Adder;
import fr.univmrs.lif.model.component.gate.AndGate;
import fr.univmrs.lif.model.component.gate.XnorGate;
import fr.univmrs.lif.model.component.gate.NandGate;
import fr.univmrs.lif.model.component.gate.NorGate;
import fr.univmrs.lif.model.component.gate.NotGate;
import fr.univmrs.lif.model.component.gate.OrGate;
import fr.univmrs.lif.model.component.gate.XorGate;
import fr.univmrs.lif.model.component.inputoutput.Clock;
import fr.univmrs.lif.model.component.inputoutput.Display;
import fr.univmrs.lif.model.component.inputoutput.GND;
import fr.univmrs.lif.model.component.inputoutput.InputModule;
import fr.univmrs.lif.model.component.inputoutput.Led;
import fr.univmrs.lif.model.component.inputoutput.OutputModule;
import fr.univmrs.lif.model.component.inputoutput.Switch;
import fr.univmrs.lif.model.component.inputoutput.VDD;
import fr.univmrs.lif.model.component.msi.Decoder;
import fr.univmrs.lif.model.component.msi.Multiplexer;
import javafx.geometry.Point2D;

public class ComponentFactory {

	public static Component buildComponent(ComponentType type) {
		if (type == ComponentType.NOT)
			return new NotGate();
		if (type == ComponentType.AND)
			return new AndGate();
		if (type == ComponentType.NAND)
			return new NandGate();
		if (type == ComponentType.OR)
			return new OrGate();
		if (type == ComponentType.NOR)
			return new NorGate();
		if (type == ComponentType.XOR)
			return new XorGate();
		if (type == ComponentType.XNOR)
			return new XnorGate();
		if (type == ComponentType.SWITCH)
			return new Switch();
		if (type == ComponentType.VDD)
			return new VDD();
		if (type == ComponentType.GND)
			return new GND();
		if (type == ComponentType.LED)
			return new Led();
		if (type == ComponentType.CLOCK)
			return new Clock();
		if (type == ComponentType.DISPLAY)
			return new Display();
		if (type == ComponentType.MUX)
			return new Multiplexer();
		if (type == ComponentType.DECODER)
			return new Decoder();
		if (type == ComponentType.ADDER)
			return new Adder();
		if (type == ComponentType.INPUTMODULE)
			return new InputModule();
		if (type == ComponentType.OUTPUTMODULE)
			return new OutputModule();
		else
			System.err.println("Factory : bad component identifyer");
			return null;
	}



}
