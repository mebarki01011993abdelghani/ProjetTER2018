package fr.univmrs.lif.model;

public class Notification {

	public static final int NEW_PROJECT = 0;
	public static final int DELETE_PROJECT = 1;
	public static final int SET_CURRENT_PROJECT = 2;
	public static final int SAVED = 3;
	public static final int SET_CURRENT_MODULE = 2;
	public static final int PROJECT_NAME_MODIFYIED = 5;
	public static final int NEW_MODULE = 8;
	public static final int REFRESH_PANEL = 9;
	

	
	
	private int type;
	private int projectIndex;
	private String name; 
	
	public Notification(int notif){
		type = notif;
	}	
	
	
	
	public Notification(int type, String name) {
		super();
		this.type = type;
		this.name = name;
	}



	public Notification(int type, int projectIndex, String name) {
		super();
		this.type = type;
		this.projectIndex = projectIndex;
		this.name = name;
	}



	public Notification(int notif, int project){
		type = notif;
		projectIndex = project;
	}

	public int getType() {
		return type;
	}

	public int getProjectIndex() {
		return projectIndex;
	}	
	
	
}
