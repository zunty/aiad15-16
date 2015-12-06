package Agents;

import java.util.Vector;

import org.joda.time.DateTime;
import org.joda.time.Duration;


import PersonClasses.Assignment;
import PersonClasses.Participant;
import PersonClasses.Proposal;
import PersonClasses.Restriction;
import PersonClasses.Schedule;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.introspection.ACLMessage;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.lang.acl.MessageTemplate;




public class Person extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private static Schedule schedule;
	public int assignmentsQuant;
	
	public static Schedule getScedule(){
		return schedule;
	}
	
	class ScheduleBehaviour extends CyclicBehaviour{

		boolean foundEveryone = false;
		int currentAssignment=0;
		Vector<Proposal> proposals = null;
		Vector<Restriction> restrictions = null;
		public boolean allFinished = false;

		public ScheduleBehaviour(Agent person) {
			super (person);
			proposals = new Vector<Proposal>();
			restrictions = new Vector<Restriction>();

			Vector<Assignment> assignments = Person.this.schedule.getAssignments();

			//Number of Events
			for(int i=0; i< assignments.size(); i++){
				if(assignments.elementAt(i).hasNoParticipants())
					currentAssignment++;
			}
		}


		@Override
		public void action() {
			if (allFinished)
				return;



			jade.lang.acl.ACLMessage msg = blockingReceive();

			if(msg.getPerformative() == jade.lang.acl.ACLMessage.FAILURE){
				System.out.println("{"+Person.this.name+"}received a failure message, this person is terminating");
				Person.this.doDelete();
			}

			else if (msg.getPerformative() == jade.lang.acl.ACLMessage.PROPOSE) {
				int separatorIndex = msg.getContent().indexOf('-');
				if (separatorIndex != -1) {
					String msgType = msg.getContent().substring(0, msg.getContent().indexOf('-'));
					switch (msgType) {
					case "INVITATION":
						System.out.println("This is an invitation to " + name + "!");
						// TODO RegisterInvitation(msg);
						break;
					default:
						System.err.println("Received an invalid PROPOSE message type.");
						break;
					}
				}
				else {
					System.err.println("Received an invalid message");
				}
			} 

			else if (msg.getPerformative() == jade.lang.acl.ACLMessage.ACCEPT_PROPOSAL) {
				int separatorIndex = msg.getContent().indexOf('-');
				if (separatorIndex != -1) {
					String msgType = msg.getContent().substring(0, msg.getContent().indexOf('-'));
					switch (msgType) {
					case "INVITATION":
						System.out.println("This is an invitation!");
						// TODO RegisterInvitation(msg);
						break;
					default:
						System.err.println("Received an invalid PROPOSE message type.");
						break;
					}
				}
				else {
					System.err.println("Received an invalid message");
				}
			} 

			else if (msg.getPerformative() == jade.lang.acl.ACLMessage.REJECT_PROPOSAL) {
				int separatorIndex = msg.getContent().indexOf('-');
				if (separatorIndex != -1) {
					String msgType = msg.getContent().substring(0, msg.getContent().indexOf('-'));
					switch (msgType) {
					case "INVITATION":
						System.out.println("This is an invitation!");
						// TODO RegisterInvitation(msg);
						break;
					default:
						System.err.println("Received an invalid PROPOSE message type.");
						break;
					}
				}
				else {
					System.err.println("Received an invalid message");
				}
			} 

			else if (msg.getPerformative() == jade.lang.acl.ACLMessage.PROPOSE) {
				int separatorIndex = msg.getContent().indexOf('-');
				if (separatorIndex != -1) {
					String msgType = msg.getContent().substring(0, msg.getContent().indexOf('-'));
					switch (msgType) {
					case "INVITATION":
						System.out.println("This is an invitation!");
						// TODO RegisterInvitation(msg);
						break;
					default:
						System.err.println("Received an invalid PROPOSE message type.");
						break;
					}
				}
				else {
					System.err.println("Received an invalid message");
				}
			} 

			else if (msg.getPerformative() == jade.lang.acl.ACLMessage.INFORM_IF) {
				int separatorIndex = msg.getContent().indexOf('-');
				if (separatorIndex != -1) {
					String msgType = msg.getContent().substring(0, msg.getContent().indexOf('-'));
					switch (msgType) {
					case "INVITATION":
						System.out.println("This is an invitation!");
						// TODO RegisterInvitation(msg);
						break;
					default:
						System.err.println("Received an invalid PROPOSE message type.");
						break;
					}
				}
				else {
					System.err.println("Received an invalid message");
				}
			} 

			else if (msg.getPerformative() == jade.lang.acl.ACLMessage.CONFIRM) {
				int separatorIndex = msg.getContent().indexOf('-');
				if (separatorIndex != -1) {
					String msgType = msg.getContent().substring(0, msg.getContent().indexOf('-'));
					switch (msgType) {
					case "INVITATION":
						System.out.println("This is an invitation!");
						// TODO RegisterInvitation(msg);
						break;
					default:
						System.err.println("Received an invalid PROPOSE message type.");
						break;
					}
				}
				else {
					System.err.println("Received an invalid message");
				}
			} 

			else if (msg.getPerformative() == jade.lang.acl.ACLMessage.CANCEL) {
				int separatorIndex = msg.getContent().indexOf('-');
				if (separatorIndex != -1) {
					String msgType = msg.getContent().substring(0, msg.getContent().indexOf('-'));
					switch (msgType) {
					case "INVITATION":
						System.out.println("This is an invitation!");
						// TODO RegisterInvitation(msg);
						break;
					default:
						System.err.println("Received an invalid PROPOSE message type.");
						break;
					}
				}
				else {
					System.err.println("Received an invalid message");
				}
			} 




			else System.err.println("Received an invalid message type.");

		}
	}

	class ABTBehaviour extends CyclicBehaviour{

		boolean end = false;
		int myValue=0;
		//TODO:DECLARAR OUTRAS VARIAVEIS

		public ABTBehaviour(Agent person) {
			super (person);
			schedule = new Schedule(name);
			schedule.addAssignment(new Assignment());
		}

		@Override
		public void action() {

			while(!end){


				jade.lang.acl.ACLMessage msg = blockingReceive();
				//System.out.println("Mensagem recebida" + msg);

				if(msg.getPerformative() == jade.lang.acl.ACLMessage.FAILURE){
					System.out.println("{"+Person.this.name+"}received a failure message, this person is terminating");
					Person.this.doDelete();
				}

				else if (msg.getPerformative() == jade.lang.acl.ACLMessage.PROPOSE) {
					int separatorIndex = msg.getContent().indexOf('-');
					if (separatorIndex != -1) {

						String[] parts = msg.getContent().split("-");
						String msgType = parts[0];
						//Imprimir parametros do conteudo
						//for(int i=0; i< parts.length;i++){System.out.println("Parts[" + i + "] = " + parts[i]);}

						switch (msgType) {
						case "INVITATION":
							System.out.println("This is an invitation to " + name + "!");
							//Ler o conteudo e obter parametros da mensagem
							String convID = parts[1];
							String eventName = parts[2];
							String[] partsInitTime = parts[3].split(":");
							String[] partsEndTime = parts[4].split(":");
							int[] initTime = new int[5];
							int[] endTime = new int[5];

							for(int i=0;i<5;i++){initTime[i] = Integer.parseInt(partsInitTime[i]);}
							DateTime initialTime = new DateTime(initTime[0],initTime[1],initTime[2],initTime[3],
									initTime[4]);
							
							for(int i=0;i<5;i++){endTime[i] = Integer.parseInt(partsEndTime[i]);}
							DateTime endingTime = new DateTime(endTime[0],endTime[1],endTime[2],endTime[3],
									endTime[4]);

							System.out.println("Disponibilidade");	
							//Verificar disponibilidade primeiro
							//0 = ok 1 = nogood 2 = stop
							System.out.println("Dono do schedule:" + schedule.getOwner());
							int typeOfResponse = Person.schedule.checkAvailability(initialTime, endingTime); 

							if(typeOfResponse == 0){
								System.out.println("Dono do schedule:" + schedule.getOwner());
								schedule.addAssignment(new Assignment(eventName,initialTime,endingTime,1,msg.getSender().toString()));
								sendOK(msg);
								//adiciona assignement
								}
							else if(typeOfResponse == 1){
								System.out.println("Vou mandar nogood----");
								sendNoGood(msg);
							}
							else if(typeOfResponse == 2)
								sendStp(msg);
							else System.err.println("Erro no tipo de envio de mensagem");
							break;
						default:
							System.err.println(name + " - Received an invalid PROPOSE message type.");
							break;
						}
					}
					else {
						System.err.println(name + " - Received an invalid message");
					}
				} 
				//depos de mandar mensagem ok ou nogood 
				else if (msg.getPerformative() == jade.lang.acl.ACLMessage.INFORM) {

					int separatorIndex = msg.getContent().indexOf('-');
					if (separatorIndex != -1) {
						String msgType = msg.getContent().substring(0, msg.getContent().indexOf('-'));
						switch (msgType) {
						case "OK?":
							System.out.println("I received an OK message!");
							//TODO: Adicionar evento aos dois utilizadores
							break;
						case "NOGOOD":
							System.out.println("I received a NOGOOD message!"); 
							//TODO: Reenviar mensagem porpose com outras hora
							break;
						case "STP":
							System.out.println("I received a STP message!");
							//TODO: Rejeitar a proposta e n�o contra-propor
							//end = true;
							break;
						default:
							System.err.println("Received an invalid INFORM message type.");
							break;
						}
					}
					else {
						System.err.println("Received an invalid message");
					}
				}
				//nao aceitar
				else if (msg.getPerformative() == jade.lang.acl.ACLMessage.REJECT_PROPOSAL) {
					
				}

				else System.err.println("Received an invalid message type.");
			}	
		}	

		private void sendOK(jade.lang.acl.ACLMessage message){
			jade.lang.acl.ACLMessage sendMsg = new jade.lang.acl.ACLMessage(jade.lang.acl.ACLMessage.INFORM);
			String evento="quaquer merda"; //TODO:Ir buscar nome do envento

			sendMsg.addReceiver(message.getSender());
			sendMsg.setContent("OK?-" + evento);
			sendMsg.setConversationId("ID do evento");
			System.out.println(sendMsg + "mensagem a enviar");
			send(sendMsg);
			System.out.println(sendMsg + "mensagem enviada");
			
			

		}

		private void sendNoGood(jade.lang.acl.ACLMessage message){
			jade.lang.acl.ACLMessage sendMsg = new jade.lang.acl.ACLMessage(jade.lang.acl.ACLMessage.INFORM);
			String evento="quaquer merda"; //TODO:Ir buscar nome do envento

			sendMsg.addReceiver(message.getSender());
			sendMsg.setContent("NOGOOD-" + evento);
			sendMsg.setConversationId("ID do evento");
			System.out.println(sendMsg + "mensagem a enviar");
			send(sendMsg);
			System.out.println(sendMsg + "mensagem enviada");

		}

		private void sendStp(jade.lang.acl.ACLMessage message){
			jade.lang.acl.ACLMessage sendMsg = new jade.lang.acl.ACLMessage(jade.lang.acl.ACLMessage.INFORM);
			String evento="quaquer merda"; //TODO:Ir buscar nome do envento

			sendMsg.addReceiver(message.getSender());
			sendMsg.setContent("STP-" + evento);
			sendMsg.setConversationId("ID do evento");
			System.out.println(sendMsg + "mensagem a enviar");
			send(sendMsg);
			System.out.println(sendMsg + "mensagem enviada");

		}
	}

	// m�todo setup -------------------------------------------------------------------------------
	protected void setup() {
		
		// Base para a comunica��o de Agentes
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		this.name = getName();

		sd.setName(getName());
		System.out.println("I am " + this.name + " and this is my getName(): " + getName());
		sd.setType("Person");
		dfd.addServices(sd);

		String[] partNames = name.split("@");
		String nameSplit = partNames[0];
		schedule = new Schedule(nameSplit);
		Assignment a;

		if(nameSplit.equals("Joao")){
			System.out.println("EU sou o Jo�o");/*
			Vector<Participant> parti = new Vector<Participant>(); parti.add(new Participant("Miguel", 1));
			a =new Assignment("Study Session", new DateTime(2014, 11, 10, 8, 0), new DateTime(2014, 11, 10, 12, 0), parti, 1,"Pedro");
			Schedule.addAssignment(a);*/

		}

		try {
			DFService.register(this, dfd);
		} catch(FIPAException e) {
			e.printStackTrace();
		}

		this.assignmentsQuant = this.schedule.getAssignments().size();

		ABTBehaviour myBehaviour = new ABTBehaviour(this);
		addBehaviour(myBehaviour);

		//TODO: PEOPLE ONLINE
	}
	
	protected void exit() {
		System.out.println("Agent "+getLocalName()+": terminating");

		// termina comunica��o
		try {
			DFService.deregister(this);  
		} catch(FIPAException e) {
			e.printStackTrace();
		}

		this.doDelete();
	}
} 
