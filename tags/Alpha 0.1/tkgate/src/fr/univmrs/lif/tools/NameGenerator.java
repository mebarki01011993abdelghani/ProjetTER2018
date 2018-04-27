package fr.univmrs.lif.tools;

public class NameGenerator {
	
	private static int numGate;
	
	public static String generate(ComponentType type){

		if(type == ComponentType.AND)
			return "Gate "+ (++numGate);
		
		return null;
		
	}

}
