/*package PersonClasses;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import org.joda.time.DateTime;

import Agents.Person;

public class ABTBehaviour extends Agent{

	Person person;
	class ABT extends CyclicBehaviour{

		boolean end = false;
		int myValue=0;
		Schedule schedule;
		//TODO:DECLARAR OUTRAS VARIAVEIS

		public ABT(Agent person) {
			person = person;
			schedule = new Schedule(person.getName());
			//schedule.addAssignment(new Assignment());
		}

		@Override
		public void action() {

			while(!end){


				jade.lang.acl.ACLMessage msg = blockingReceive();
				//System.out.println("Mensagem recebida" + msg);

				if(msg.getPerformative() == jade.lang.acl.ACLMessage.FAILURE){
					System.out.println("{"+person.getName()+"}received a failure message, this person is terminating");
					person.doDelete();
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
							System.out.println("This is an invitation to " + person.getName() + "!");
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
							int typeOfResponse = person.getScedule().checkAvailability(initialTime, endingTime); 

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
							System.err.println(person.getName() + " - Received an invalid PROPOSE message type.");
							break;
						}
					}
					else {
						System.err.println(person.getName() + " - Received an invalid message");
					}
				} 

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
	protected void setup() {

		// Base para a comunica��o de Agentes
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();


		try {
			DFService.register(this, dfd);
		} catch(FIPAException e) {
			e.printStackTrace();
		}

		person.assignmentsQuant = Person.getScedule().getAssignments().size();

		ABT myBehaviour = new ABT(person);
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
}*/


