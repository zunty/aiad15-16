package Agents;
public class MyAgent extends jade.core.Agent{
	
	public void setup(){
		System.out.println("Hi, I'm " + this.getLocalName());
		
	}
	
	public void afterMove(){
		System.out.println(this.getLocalName() + " just moved...");
		
	}
	
	
	
}