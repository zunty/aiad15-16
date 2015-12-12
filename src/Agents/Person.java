package Agents;

import java.time.format.DateTimeFormatter;
import java.util.Vector;

import org.joda.time.DateTime;
import org.joda.time.Duration;


import org.joda.time.format.DateTimeFormat;

import PersonClasses.AllAgents;
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
	public int assignmentsQuant;
	Vector<AllAgents> allAgents;
	public Schedule schedule;

	class ABTBehaviour extends CyclicBehaviour{

		boolean end = false;
		int myValue=0;
		//TODO:DECLARAR OUTRAS VARIAVEIS

		public ABTBehaviour(Agent person) {
			super (person);
		}

		@Override
		public void action() {

			while(!end){

				//findAllAgents();
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
							int typeOfResponse = 0;

							
							System.out.println("Disponibilidade");	
							//Verificar disponibilidade primeiro
							//0 = ok 1 = nogood 2 = stop
							System.out.println("Dono do schedule:"+Person.this.schedule.getOwner());
							typeOfResponse = Person.this.schedule.checkAvailability(initialTime, endingTime); 
							//}

							if(typeOfResponse == 0){
								System.out.println("Dono do schedule:" + Person.this.schedule.getOwner());
								//Person.this.schedule.addAssignment(new Assignment(eventName,initialTime,endingTime,1,msg.getSender().toString()));
								sendOK(msg,eventName,convID,initialTime,endingTime);
								//adiciona assignement
							}
							else if(typeOfResponse == 1){
								System.out.println("Vou mandar nogood----");
								sendNoGood(msg, eventName,convID,initialTime,endingTime);
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
						System.out.println(msg.getContent().toString());
						String[] parts = msg.getContent().split("-");
						
						String convID = parts[1];
						String eventName = parts[2];
						String[] partsInitTime = parts[3].split(":");
						String[] partsEndTime = parts[4].split(":");
						int[] initTime = new int[5];
						int[] endTime = new int[5];

						for(int i=0;i<5;i++){initTime[i] = Integer.parseInt(partsInitTime[i]);}
						DateTime initialTime = new DateTime(initTime[0],initTime[1],initTime[2],initTime[3],
								initTime[4]);
						System.out.println("Data inicial para confirmar: " + initialTime); 

						for(int i=0;i<5;i++){endTime[i] = Integer.parseInt(partsEndTime[i]);}
						DateTime endingTime = new DateTime(endTime[0],endTime[1],endTime[2],endTime[3],
								endTime[4]);
						System.out.println("Data Final para confirmar: " + endingTime); 
						
						switch (msgType) {
						case "OK?":
							System.out.println("I received an OK message!");
							//TODO: Adicionar vector para participantes
							//Adicionar evento aos dois utilizadores
							Person.this.schedule.addAssignment(new Assignment(eventName,initialTime,endingTime,1,msg.getSender().toString()));
							//TODO:send confirm
							sendConfirm(msg,eventName,convID,initialTime,endingTime);

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
				
				else if (msg.getPerformative() == jade.lang.acl.ACLMessage.CONFIRM) {
					
					String msgType = msg.getContent().substring(0, msg.getContent().indexOf('-'));
					System.out.println(msg.getContent().toString());
					String[] parts = msg.getContent().split("-");
					
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
					
					//Modificar para vetor de Participantes
					Person.this.schedule.addAssignment(new Assignment(eventName,initialTime,endingTime,1,msg.getSender().toString()));
					System.out.println("\n-----------------------------------------------------------------------------\n");

				}

				//nao aceitar
				else if (msg.getPerformative() == jade.lang.acl.ACLMessage.REJECT_PROPOSAL) {

				}

				else System.err.println("Received an invalid message type.");
			}	
		}	

		private void sendOK(jade.lang.acl.ACLMessage message, String eventName, String convID, DateTime init, DateTime end){
			jade.lang.acl.ACLMessage sendMsg = new jade.lang.acl.ACLMessage(jade.lang.acl.ACLMessage.INFORM);
			
			sendMsg.addReceiver(message.getSender());
			
			org.joda.time.format.DateTimeFormatter fmt = DateTimeFormat.forPattern("y:M:d:H:m");
			String initStr = fmt.print(init);
			org.joda.time.format.DateTimeFormatter fmt2 = DateTimeFormat.forPattern("y:M:d:H:m");
			String endStr = fmt2.print(end);
			
			sendMsg.setContent("OK?-" + convID + "-" + eventName + "-" + initStr + "-" + endStr);
			sendMsg.setConversationId(convID);
			System.out.println("\n mensagem a enviar \n" + sendMsg + "\n");
			send(sendMsg);
		}
		
		private void sendConfirm(jade.lang.acl.ACLMessage message, String eventName, String convID, DateTime init, DateTime end){
			jade.lang.acl.ACLMessage sendMsg = new jade.lang.acl.ACLMessage(jade.lang.acl.ACLMessage.CONFIRM);
			
			sendMsg.addReceiver(message.getSender());
			
			org.joda.time.format.DateTimeFormatter fmt = DateTimeFormat.forPattern("y:M:d:H:m");
			String initStr = fmt.print(init);
			org.joda.time.format.DateTimeFormatter fmt2 = DateTimeFormat.forPattern("y:M:d:H:m");
			String endStr = fmt2.print(end);
			
			sendMsg.setContent("OK?-" + convID + "-" + eventName + "-" + initStr + "-" + endStr);
			sendMsg.setConversationId(convID);
			System.out.println("\n mensagem a enviar \n" + sendMsg + "\n");
			send(sendMsg);
		}

		private void sendNoGood(jade.lang.acl.ACLMessage message, String eventName, String convID, DateTime init, DateTime end){
			jade.lang.acl.ACLMessage sendMsg = new jade.lang.acl.ACLMessage(jade.lang.acl.ACLMessage.INFORM);
			
			sendMsg.addReceiver(message.getSender());
			
			org.joda.time.format.DateTimeFormatter fmt = DateTimeFormat.forPattern("y:M:d:H:m");
			String initStr = fmt.print(init);
			org.joda.time.format.DateTimeFormatter fmt2 = DateTimeFormat.forPattern("y:M:d:H:m");
			String endStr = fmt2.print(end);
			
			sendMsg.addReceiver(message.getSender());
			sendMsg.setContent("NOGOOD-" + convID + "-" + eventName + "-" + initStr + "-" + endStr);
			sendMsg.setConversationId(convID);
			System.out.println("\n mensagem a enviar \n" + sendMsg + "\n");
			send(sendMsg);

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

		Object[] args = getArguments();
		if(args != null && args.length > 0) {
			this.name = (String) args[0];
			System.out.println("{"+Person.this.name+"}Arguments: " + args.toString());
		} else {
			//System.out.println("{"+Person.this.name+"}I don't have arguments.");
			Person.this.name= getAID().getLocalName();
		}

		// Base para a comunica��o de Agentes
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


		if(Person.this.name.equals("Pedro")){
		}
		else if(Person.this.name.equals("Joao")){
		}

		//this.assignmentsQuant = Person.this.schedule.getAssignments().size();
		Person.this.schedule= new Schedule(Person.this.name); 
		ABTBehaviour myBehaviour = new ABTBehaviour(this);
		addBehaviour(myBehaviour);

		Person.this.allAgents = new Vector<AllAgents>();
		findAllAgents();

		//TODO: PEOPLE ONLINE
	}

	private void findAllAgents() {
		// TODO Auto-generated method stub
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd1 = new ServiceDescription();
		sd1.setType("Person");
		template.addServices(sd1);

		try {
			DFAgentDescription[] result = DFService.search(this, template);
			System.out.println("length= "+ result.length);
			for(int i=0; i<result.length; ++i){


				System.out.println("vou inserir ");	

				AllAgents po = new AllAgents(result[i].getName());
				allAgents.addElement(po);
				System.out.println("agents.size: "+ allAgents.size());	
			}
		} catch(FIPAException e) { e.printStackTrace(); }

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
