package PersonClasses;

import jade.core.AID;

public class Qualifier {

	private String name;
	private AID agentName;
	
	public Qualifier(String name, AID agentName){
		this.name = name;
		this.agentName = agentName;
	}
	
	public Qualifier(String name){
		this.name = name;
		this.agentName = null;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public AID getAgentName() {
		return agentName;
	}
	public void setAgentName(AID agentName) {
		this.agentName = agentName;
	}
	
	public String toString(){
		return name;
	}
	
}
