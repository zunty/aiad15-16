package Agents;

import PersonClasses.Schedule;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.DFService;
import jade.domain.FIPAException;



public class Person extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	Schedule schedule;

	
	// método setup -------------------------------------------------------------------------------
	protected void setup() {

		// Base para a comunicação de Agentes
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		this.name = getName();
		sd.setName(getName());
		System.out.println("I am " + this.name + " and this is my getName(): " + getName());
		sd.setType("Person");
		dfd.addServices(sd);

		/*try {
			DFService.register(this, dfd);
		} catch(FIPAException e) {
			e.printStackTrace();
		}*/

		Person.this.schedule = new Schedule(Person.this.name);

		/**
		 * 
		 * Inserir tarefas e participantes para essas tarefas para futuros tes
		 * 
		 * //System.out.println("{"+Person.this.name+"} has assignments with "+Person.this.schedule.getPeopleIHaveAssignmentsWith().toString());
		 *
		 */
		
		
		waitingForProposals();
	}

	public void waitingForProposals() {
		//TODO
	    try {
	        while (true) {
	            System.out.println("Waiting for new proposals...");
	            Thread.sleep(5 * 1000);
	        }
	    } catch (InterruptedException e) {
	        e.printStackTrace();
	    }
		
		//At the end
		exit();
	}

	protected void exit() {
		System.out.println("Agent "+getLocalName()+": terminating");
		
		// termina comunicação
		try {
			DFService.deregister(this);  
		} catch(FIPAException e) {
			e.printStackTrace();
		}
		
		this.doDelete();
	}
} 
