package fr.univmrs.lif.tools;

import fr.univmrs.lif.controller.MainController;
import fr.univmrs.lif.model.component.Component;
import fr.univmrs.lif.model.component.Module;

public class NameManager {
	
	public static boolean exist(String name){
		Module mainModule = MainController.getCurrentProject().getMain();
		if(mainModule == null) return false;
		if(mainModule.getName().equalsIgnoreCase(name)) return true;
		
		
		return seekName(name,mainModule);

	}
	
	private static boolean seekName(String name, Component component){
		
		if(component instanceof Module){
			for(Component c : ((Module) component).getComponents()){
				return seekName(name, c);
			}
		}
		else{
			return component.getName().equalsIgnoreCase(name);
		}
		
		return false;
		
	}

}
