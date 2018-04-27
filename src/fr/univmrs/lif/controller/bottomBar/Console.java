package fr.univmrs.lif.controller.bottomBar;

public class Console {
	
	static ConsoleController consoleController;
	public Console(ConsoleController consoleController) {
	 Console.consoleController = consoleController;
	}
	
	public static void print(String message){
		consoleController.logln(message);
	}
	
	public static void print(String message, String style){
		consoleController.logln(message,style);
	}
	
	public static void error(String message){
		consoleController.logln(message,"-fx-fill: red;");
	}
	
	public static void println(String message){
		consoleController.logln(message);
	}
	
	public static void println(String message, String style){
		consoleController.logln(message,style);
	}
	
	public static void errorln(String message){
		consoleController.logln(message,"-fx-fill: red;");
	}
	
	public static void errorln(String message,String style){
		consoleController.logln(message,"-fx-fill: red;" + style);
	}
	

}
