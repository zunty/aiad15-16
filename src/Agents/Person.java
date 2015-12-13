package Agents;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
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
	int nOKS;
	int nOKSTotal;
	private Vector<String> v;


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


							//Verificar disponibilidade primeiro
							//0 = ok 1 = nogood 2 = stop
							typeOfResponse = Person.this.schedule.checkAvailability(initialTime, endingTime); 
							//}

							if(typeOfResponse == 0){
								//Person.this.schedule.addAssignment(new Assignment(eventName,initialTime,endingTime,1,msg.getSender().toString()));
								sendOK(msg,eventName,convID,initialTime,endingTime);
								//adiciona assignement
							}
							else if(typeOfResponse == 1){
								System.out.println("\nVou mandar nogood!");
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

						for(int i=0;i<5;i++){endTime[i] = Integer.parseInt(partsEndTime[i]);}
						DateTime endingTime = new DateTime(endTime[0],endTime[1],endTime[2],endTime[3],
								endTime[4]);

						switch (msgType) {
						case "OK?":
							System.out.println("Tamanho do vector v: " + v.size());
							//send confirm
							nOKS++;
							if(nOKS == nOKSTotal){

								//TODO:Enviar para todos
								System.out.println("Confirmaram todos"); 
								for(int i =0;i<v.size();i++){
									System.out.println(v.elementAt(i));
									//Esta mal, tem que enviar um sendConfirm para todos para marcar na agenda de cada um
									for(int j=0; j<allAgents.size();j++){
										System.out.println(v.elementAt(i) + " = " + allAgents.elementAt(j).getAid().getLocalName() );
										if(v.elementAt(i).equals(allAgents.elementAt(j).getAid().getLocalName())){
											sendConfirm(allAgents.elementAt(j).getAid(),eventName,convID,initialTime,endingTime);
											break;
										}
									}
								
								}
								//Inserir no próprio
								sendConfirm(this.getAgent().getAID(),eventName,convID,initialTime,endingTime);

							}
							break;
						case "NOGOOD":
							System.out.println("I received a NOGOOD message from " + name + " do evento " + eventName); 

							//TODO: Reenviar mensagem porpose com outras hora
							break;
						case "STP":
							System.out.println("I received a STP message!");
							//TODO: Rejeitar a proposta e nï¿½o contra-propor
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

				else if (msg.getPerformative() == jade.lang.acl.ACLMessage.PROPAGATE) {

					String msgType = msg.getContent().substring(0, msg.getContent().indexOf('-'));
					System.out.println("\n" + msg.getContent().toString() + "\n");
					String[] parts = msg.getContent().split("-");

					String convID = parts[1];
					String eventName = parts[2];
					String[] partsInitTime = parts[3].split(":");
					String[] partsEndTime = parts[4].split(":");
					int[] initTime = new int[5];
					int[] endTime = new int[5];
					String[] convidados = null;
					//Arrays.fill(convidados, null);
					convidados= parts[5].split(";");
					System.out.println("Tamanho dos convidados" + convidados.length);

					for(int i=0;i<5;i++){initTime[i] = Integer.parseInt(partsInitTime[i]);}
					DateTime initialTime = new DateTime(initTime[0],initTime[1],initTime[2],initTime[3],
							initTime[4]);

					for(int i=0;i<5;i++){endTime[i] = Integer.parseInt(partsEndTime[i]);}
					DateTime endingTime = new DateTime(endTime[0],endTime[1],endTime[2],endTime[3],
							endTime[4]);

					//TODO:CICLO FOR PARA TODOS OS USERS QUE VAO SER CONVIDADOS
					AID send = null;
					System.out.println("Vou limpar o v");
					v.removeAllElements();

					System.out.println("Limpei o v, agora contem " + v.size() + "elementos");

					int aux = 0;

					for(int i = 0;i<convidados.length;i++){
						System.out.println("Vou enviar um invitation para: " + convidados[i]);
						for(int j = 0;j<allAgents.size();j++){
							System.out.println("nome:" + allAgents.elementAt(j).getAid().getLocalName());
							if(convidados[i].equals(allAgents.elementAt(j).getAid().getLocalName())){
								send = allAgents.elementAt(j).getAid();
								if(aux == 0){
									v.addElement(allAgents.elementAt(j).getAid().getLocalName());
									aux--;}
							}
						}
						aux++;

						sendInvitation(msg,convID,eventName,initialTime,endingTime,send);
					}
					//Limpar array de convidados no fim
					nOKS=0;
					nOKSTotal=convidados.length;

				}

				else if (msg.getPerformative() == jade.lang.acl.ACLMessage.CONFIRM) {

					System.out.println("Entrei aqui");

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
			//System.out.println("\nMensagem a enviar \n" + sendMsg + "\n");
			send(sendMsg);
		}

		private void sendInvitation(jade.lang.acl.ACLMessage message, String eventName, String convID, DateTime init, DateTime end, AID guest){
			jade.lang.acl.ACLMessage sendMsg = new jade.lang.acl.ACLMessage(jade.lang.acl.ACLMessage.PROPOSE);

			sendMsg.addReceiver(guest);

			org.joda.time.format.DateTimeFormatter fmt = DateTimeFormat.forPattern("y:M:d:H:m");
			String initStr = fmt.print(init);
			org.joda.time.format.DateTimeFormatter fmt2 = DateTimeFormat.forPattern("y:M:d:H:m");
			String endStr = fmt2.print(end);

			sendMsg.setContent("INVITATION-" + convID + "-" + eventName + "-" + initStr + "-" + endStr);
			sendMsg.setConversationId(convID);
			//System.out.println("\nMensagem a enviar \n" + sendMsg + "\n");
			send(sendMsg);
		}

		private void sendConfirm(AID receiver, String eventName, String convID, DateTime init, DateTime end){
			jade.lang.acl.ACLMessage sendMsg = new jade.lang.acl.ACLMessage(jade.lang.acl.ACLMessage.CONFIRM);

			sendMsg.addReceiver(receiver);

			org.joda.time.format.DateTimeFormatter fmt = DateTimeFormat.forPattern("y:M:d:H:m");
			String initStr = fmt.print(init);
			org.joda.time.format.DateTimeFormatter fmt2 = DateTimeFormat.forPattern("y:M:d:H:m");
			String endStr = fmt2.print(end);

			sendMsg.setContent("OK?-" + convID + "-" + eventName + "-" + initStr + "-" + endStr);
			sendMsg.setConversationId(convID);
			System.out.println("\nMensagem a enviar \n" + sendMsg + "\n");
			send(sendMsg);
		}

		private void sendNoGood(jade.lang.acl.ACLMessage message, String eventName, String convID, DateTime init, DateTime end){
			jade.lang.acl.ACLMessage sendMsg = new jade.lang.acl.ACLMessage(jade.lang.acl.ACLMessage.INFORM);

			org.joda.time.format.DateTimeFormatter fmt = DateTimeFormat.forPattern("y:M:d:H:m");
			String initStr = fmt.print(init);
			org.joda.time.format.DateTimeFormatter fmt2 = DateTimeFormat.forPattern("y:M:d:H:m");
			String endStr = fmt2.print(end);

			sendMsg.addReceiver(message.getSender());
			sendMsg.setContent("NOGOOD-" + convID + "-" + eventName + "-" + initStr + "-" + endStr);
			sendMsg.setConversationId(convID);
			//System.out.println("\nMensagem a enviar \n" + sendMsg + "\n");
			send(sendMsg);

		}

		private void sendStp(jade.lang.acl.ACLMessage message){
			jade.lang.acl.ACLMessage sendMsg = new jade.lang.acl.ACLMessage(jade.lang.acl.ACLMessage.INFORM);
			String evento="EventoX"; //TODO:Ir buscar nome do envento

			sendMsg.addReceiver(message.getSender());
			sendMsg.setContent("STP-" + evento);
			sendMsg.setConversationId("ID do evento");
			System.out.println(sendMsg + "Mensagem a enviar");
			send(sendMsg);

		}
	}

	// mï¿½todo setup -------------------------------------------------------------------------------
	protected void setup() {

		Object[] args = getArguments();
		if(args != null && args.length > 0) {
			this.name = (String) args[0];
			System.out.println("{"+Person.this.name+"}Arguments: " + args.toString());
		} else {
			Person.this.name= getAID().getLocalName();
		}

		// Base para a comunicaï¿½ï¿½o de Agentes
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setName(getName());

		v = new Vector<String>();

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
			for(int i=0; i<result.length; ++i){

				AllAgents po = new AllAgents(result[i].getName());
				allAgents.addElement(po);
			}
		} catch(FIPAException e) { e.printStackTrace(); }

	}

	protected void exit() {
		System.out.println("Agent "+getLocalName()+": terminating");

		// termina comunicaï¿½ï¿½o
		try {
			DFService.deregister(this);  
		} catch(FIPAException e) {
			e.printStackTrace();
		}

		this.doDelete();
	}
} 
