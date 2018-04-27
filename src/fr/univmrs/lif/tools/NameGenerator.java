package fr.univmrs.lif.tools;

import fr.univmrs.lif.enumeration.ComponentType;

public class NameGenerator {
	
	private static int numGate;
	private static int numWire;
	private static int numModule;
	private static int numPlug;
	private static int numInput;
	private static int numOutput;
	private static int numJoint;
	private static int numMSI;
	private static int numALU;
	
	public static String generate(ComponentType type){

		switch (type) {
		case NOT:
			return "Gate "+ (++numGate);
		case NAND:
			return "Gate "+ (++numGate);
		case AND:
			return "Gate "+ (++numGate);
		case OR:
			return "Gate "+ (++numGate);
		case NOR:
			return "Gate "+ (++numGate);
		case XOR:
			return "Gate "+ (++numGate);
		case XNOR:
			return "Gate "+ (++numGate);
		case SWITCH:
			return "Input "+ (++numInput);
		case CLOCK:
			return "Input "+ (++numInput);
		case VDD:
			return "Input "+ (++numInput);
		case GND:
			return "Input "+ (++numInput);
		case LED:
			return "Output "+ (++numOutput);
		case DISPLAY:
			return "Output "+ (++numOutput);
		case WIRE:
			return "Wire "+ (++numWire);
		case PLUG:
			return "Pin "+ (++numPlug);
		case JOINT:
			return "Join "+ (++numJoint);
		case MUX:
			return "MSI "+ (++numMSI);
		case DECODER:
			return "MSI "+ (++numMSI);
		case ADDER:
			return "ALU "+ (++numALU);
		case MODULE:
			return "Module "+ (++numModule);
		case INPUTMODULE:
			return "Input "+ (++numInput);
		case OUTPUTMODULE:
			return "Output "+ (++numOutput);
		default:
			return null;
		
		}
	}

}
