package Agents;
import org.joda.time.*;

import PersonClasses.Schedule;
import PersonClasses.Assignment;
import PersonClasses.Proposal;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.DFService;
import jade.domain.FIPAException;

import java.io.*;
import java.util.Vector;


public class Person extends Agent {

	private String name;
	Schedule schedule;

	
	// método setup -------------------------------------------------------------------------------
	protected void setup() {
		
		Object[] args = getArguments();
		if(args != null && args.length > 0) {
			this.name = (String) args[0];
			System.out.println("{"+Person.this.name+"}Arguments: " + args.toString());
		} else {
			System.out.println("{"+Person.this.name+"}I don't have arguments.");
		}

		// Base para a comunicação de Agentes
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setName(getName());
		System.out.println("I am " + this.name + " and this is my getName(): " + getName());
		sd.setType("Person");
		dfd.addServices(sd);

		try {
			DFService.register(this, dfd);
		} catch(FIPAException e) {
			e.printStackTrace();
		}

		Person.this.schedule = new Schedule(Person.this.name);

		/**
		 * 
		 * Inserir tarefas e participantes para essas tarefas 
		 * 
		 */
		
		System.out.println("{"+Person.this.name+"} has assignments with "+Person.this.schedule.getPeopleIHaveAssignmentsWith().toString());

		waitingForProposals();
	}

	public void waitingForProposals() {
		//TODO
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
