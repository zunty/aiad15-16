package Agents;

import java.util.Vector;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import PersonClasses.AllAgents;
import PersonClasses.Assignment;
import PersonClasses.Schedule;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.DFService;
import jade.domain.FIPAException;

public class Person extends Agent {

	//Instancia variáveis de cada um dos agentes
	private static final long serialVersionUID = 1L;
	private String name;
	public int assignmentsQuant;
	Vector<AllAgents> allAgents;
	public Schedule schedule;
	int nOKS;
	int nOKSTotal;
	private Vector<String> v;

	//Comportamento ABT do Agente
	class ABTBehaviour extends CyclicBehaviour{


		private static final long serialVersionUID = 1L;
		//Instancia variáveis de cada um dos comportamentos de agentes
		boolean end = false;
		int myValue=0;

		//Construtor do ABTBehaviour
		public ABTBehaviour(Agent person) {
			super (person);
		}

		//Função onde ocorre todo o comportamento da classe ABTBehaviour
		@Override
		public void action() {

			while(!end){
				//Recebe uma qualquer mensagem destinada a ele
				jade.lang.acl.ACLMessage msg = blockingReceive();

				//Este tipo acontece quando alguma coisa enviada na mensagem não é correta
				if(msg.getPerformative() == jade.lang.acl.ACLMessage.FAILURE){
					System.out.println("{"+Person.this.name+"}received a failure message, this person is terminating");
					Person.this.doDelete();
				}

				//Este tipo acontece quando recebe uma mensagem do tipo REQUEST
				else if (msg.getPerformative() == jade.lang.acl.ACLMessage.REQUEST) {
					System.out.println("\n----------------------New Request-----------------------------\n");

					String[] parts = msg.getContent().split("-");
					String msgType = parts[0];
					switch (msgType) {
					//Imprime  schedule do proprio 
					case "SCHEDULE":
						System.out.println("Printing my schedule\n");
						Person.this.schedule.print();
						break;

					//Termina o proprio agente	
					case "END":
						end=true;
						exit();
						break;	
					
					//Adiciona assignment ao proprio agente
					case "ASSIGNMENT":
						String eventName = parts[2];
						String eventID = parts[1];
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
						//Verificar disponibilidade
						typeOfResponse = Person.this.schedule.checkAvailability(initialTime, endingTime); 

						//Se estiver disponivel o typeOfResponse e igual a 0
						if(typeOfResponse == 0){
							Person.this.schedule.addAssignment(new Assignment(eventName,initialTime,endingTime,msg.getSender().toString()));
						}
						//Se nao estiver disponivel o typeOfResponse e igual a 1
						else if(typeOfResponse == 1){
							System.out.println("\nVou mandar nogood!");
							sendNoGood(msg, eventName,eventID,initialTime,endingTime);
						}
						
					break;

					default:
						System.err.println(name + " - Received an invalid message type.");
						break;
					}
				}

				//Este tipo acontece quando recebe uma mensagem do tipo PROPOSE
				else if (msg.getPerformative() == jade.lang.acl.ACLMessage.PROPOSE) {

					//Separar várias componentes da mensagem, que se encontram separadas por "-"
					int separatorIndex = msg.getContent().indexOf('-');
					if (separatorIndex != -1) {

						String[] parts = msg.getContent().split("-");
						String msgType = parts[0];

						switch (msgType) {
						case "INVITATION":
							System.out.println("This is an invitation to " + name + "!" + "The new assignment is: " + parts[1]+ "\n");

							//Ler o conteudo e obter parametros da mensagem
							String eventID = parts[1];
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


							//Verificar disponibilidade do agente convidado
							typeOfResponse = Person.this.schedule.checkAvailability(initialTime, endingTime); 
							System.out.println();

							//Se estiver disponível o typeOfResponse é igual a 0
							if(typeOfResponse == 0){
								sendOK(msg,eventName,eventID,initialTime,endingTime);
							}
							//Se não estiver disponível o typeOfResponse é igual a 1
							else if(typeOfResponse == 1){
								System.out.println(name + ": " + "I'm gonna send a nogood message!" + "\n");
								sendNoGood(msg, eventName,eventID,initialTime,endingTime);
							}
							//Se enviar um stop o typeOfResponse é igual a 2
							else if(typeOfResponse == 2)
								sendStp(msg);
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

				//Este tipo acontece quando recebe uma mensagem do tipo INFORM
				else if (msg.getPerformative() == jade.lang.acl.ACLMessage.INFORM) {

					//Separar várias componentes da mensagem, que se encontram separadas por "-"
					int separatorIndex = msg.getContent().indexOf('-');
					if (separatorIndex != -1) {
						String msgType = msg.getContent().substring(0, msg.getContent().indexOf('-'));
						String[] parts = msg.getContent().split("-");

						String eventID = parts[1];
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
						//Quando um agente recebe uma mensagem de resposta positiva ao convite do próprio agente a outro
						case "OK?":
							//Cada pessoa que confirma 
							nOKS++;

							//Apenas se confirma a proposta se todos os convidados tiverem confirmado a disponiilidade para esse evento
							if(nOKS == nOKSTotal){
								System.out.println("\n" + name + ": " + "All agents confirmed their availability"); 
								for(int i =0;i<v.size();i++){
									for(int j=0; j<allAgents.size();j++){
										if(v.elementAt(i).equals(allAgents.elementAt(j).getAid().getLocalName())){
											sendConfirm(allAgents.elementAt(j).getAid(),eventName,eventID,initialTime,endingTime);
											break;
										}
									}

								}
								//Inserir no próprio
								sendConfirm(this.getAgent().getAID(),eventName,eventID,initialTime,endingTime);
								System.out.println(name + ": " + "Added all agents to the assignment " + eventID + "\n");


							}
							break;

							//Quando um agente recebe uma mensagem de resposta positiva ao convite do próprio agente a outro
						case "NOGOOD":
							System.out.println(name + ": " + "I received a NOGOOD message from " + msg.getSender().getLocalName() + " do evento " + eventID); 
							break;

							//Quando um agente recebe uma mensagem de resposta positiva ao convite do próprio agente a outro
						case "STP":
							System.out.println(name + ": " + "I received a STP message!");
							break;

							//Quando um agente recebe uma mensagem de resposta positiva ao convite do próprio agente a outro
						default:
							System.err.println(name + ": " + "Received an invalid INFORM message type.");
							break;
						}
					}
					else {
						System.err.println(name + ": " + "Received an invalid message");

					}
				}

				//Este tipo acontece quando recebe uma mensagem do tipo PROPAGATE
				else if (msg.getPerformative() == jade.lang.acl.ACLMessage.PROPAGATE) {

					System.out.println("\n------------------------SEND INVITATION-----------------------\n");
					System.out.println("\n" + msg.getContent().toString() + "\n");
					String[] parts = msg.getContent().split("-");

					String eventID = parts[1];
					String eventName = parts[2];
					String[] partsInitTime = parts[3].split(":");
					String[] partsEndTime = parts[4].split(":");
					int[] initTime = new int[5];
					int[] endTime = new int[5];
					String[] convidados = null;
					convidados= parts[5].split(";");

					for(int i=0;i<5;i++){initTime[i] = Integer.parseInt(partsInitTime[i]);}
					DateTime initialTime = new DateTime(initTime[0],initTime[1],initTime[2],initTime[3],
							initTime[4]);

					for(int i=0;i<5;i++){endTime[i] = Integer.parseInt(partsEndTime[i]);}
					DateTime endingTime = new DateTime(endTime[0],endTime[1],endTime[2],endTime[3],
							endTime[4]);

					//Reiniciar as variáveis após um novo pedido de evento
					AID send = null;
					v.removeAllElements();

					int aux = 0;

					//Obter os convidados para um determinado pedido de evento
					for(int i = 0;i<convidados.length;i++){
						System.out.println(name + ": " + "I'm gonna send an invitation to: " + convidados[i] + "\n");
						for(int j = 0;j<allAgents.size();j++){
							if(convidados[i].equals(allAgents.elementAt(j).getAid().getLocalName())){
								send = allAgents.elementAt(j).getAid();
								if(aux == 0){
									v.addElement(allAgents.elementAt(j).getAid().getLocalName());
									aux--;}
							}
						}
						aux++;
						sendInvitation(msg,eventID,eventName,initialTime,endingTime,send);
					}

					//Reiniciar variáveis para depois enviar apenas quando todos confirmarem pedido
					nOKS=0;
					nOKSTotal=convidados.length;

				}

				//Este tipo acontece quando receb uma mensagem do tipo CONFIRM
				else if (msg.getPerformative() == jade.lang.acl.ACLMessage.CONFIRM) {

					String[] parts = msg.getContent().split("-");

					String eventName = parts[1];
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

					//Adicionar tarefa no proprio
					Person.this.schedule.addAssignment(new Assignment(eventName,initialTime,endingTime,msg.getSender().toString()));

				}

				//Este tipo acontece quando receb uma mensagem com um tipo não existência
				else System.err.println("Received an invalid message type.");
			}	
		}	

		//Função auxiliar para enviar mensagens do tipo INFORM positivo
		private void sendOK(jade.lang.acl.ACLMessage message, String eventName, String eventID, DateTime init, DateTime end){
			jade.lang.acl.ACLMessage sendMsg = new jade.lang.acl.ACLMessage(jade.lang.acl.ACLMessage.INFORM);

			sendMsg.addReceiver(message.getSender());

			org.joda.time.format.DateTimeFormatter fmt = DateTimeFormat.forPattern("y:M:d:H:m");
			String initStr = fmt.print(init);
			org.joda.time.format.DateTimeFormatter fmt2 = DateTimeFormat.forPattern("y:M:d:H:m");
			String endStr = fmt2.print(end);

			sendMsg.setContent("OK?-" + eventID + "-" + eventName + "-" + initStr + "-" + endStr);
			sendMsg.setConversationId(eventID);
			send(sendMsg);
		}

		//Função auxiliar para enviar mensagens do tipo PROPOSE
		private void sendInvitation(jade.lang.acl.ACLMessage message, String eventName, String eventID, DateTime init, DateTime end, AID guest){
			jade.lang.acl.ACLMessage sendMsg = new jade.lang.acl.ACLMessage(jade.lang.acl.ACLMessage.PROPOSE);

			sendMsg.addReceiver(guest);

			org.joda.time.format.DateTimeFormatter fmt = DateTimeFormat.forPattern("y:M:d:H:m");
			String initStr = fmt.print(init);
			org.joda.time.format.DateTimeFormatter fmt2 = DateTimeFormat.forPattern("y:M:d:H:m");
			String endStr = fmt2.print(end);

			sendMsg.setContent("INVITATION-" + eventID + "-" + eventName + "-" + initStr + "-" + endStr);
			sendMsg.setConversationId(eventID);
			send(sendMsg);
		}

		//Função auxiliar para enviar mensagens do tipo CONFIRM
		private void sendConfirm(AID receiver, String eventName, String eventID, DateTime init, DateTime end){
			jade.lang.acl.ACLMessage sendMsg = new jade.lang.acl.ACLMessage(jade.lang.acl.ACLMessage.CONFIRM);

			sendMsg.addReceiver(receiver);

			org.joda.time.format.DateTimeFormatter fmt = DateTimeFormat.forPattern("y:M:d:H:m");
			String initStr = fmt.print(init);
			org.joda.time.format.DateTimeFormatter fmt2 = DateTimeFormat.forPattern("y:M:d:H:m");
			String endStr = fmt2.print(end);

			sendMsg.setContent("OK?-" + eventID + "-" + eventName + "-" + initStr + "-" + endStr);
			sendMsg.setConversationId(eventID);
			send(sendMsg);
		}

		//Função auxiliar para enviar mensagens do tipo INFORM negativo
		private void sendNoGood(jade.lang.acl.ACLMessage message, String eventName, String eventID, DateTime init, DateTime end){
			jade.lang.acl.ACLMessage sendMsg = new jade.lang.acl.ACLMessage(jade.lang.acl.ACLMessage.INFORM);

			org.joda.time.format.DateTimeFormatter fmt = DateTimeFormat.forPattern("y:M:d:H:m");
			String initStr = fmt.print(init);
			org.joda.time.format.DateTimeFormatter fmt2 = DateTimeFormat.forPattern("y:M:d:H:m");
			String endStr = fmt2.print(end);

			sendMsg.addReceiver(message.getSender());
			sendMsg.setContent("NOGOOD-" + eventID + "-" + eventName + "-" + initStr + "-" + endStr);
			sendMsg.setConversationId(eventID);
			send(sendMsg);

		}

		//Função auxiliar para enviar mensagens do tipo INFORM stop
		private void sendStp(jade.lang.acl.ACLMessage message){
			jade.lang.acl.ACLMessage sendMsg = new jade.lang.acl.ACLMessage(jade.lang.acl.ACLMessage.INFORM);
			String evento="EventoX"; //TODO:Ir buscar nome do envento

			sendMsg.addReceiver(message.getSender());
			sendMsg.setContent("STP-" + evento);
			sendMsg.setConversationId("ID do evento");
			send(sendMsg);

		}
	}

	//Inicia o agente
	// mï¿½todo setup -------------------------------------------------------------------------------
	protected void setup() {

		Object[] args = getArguments();
		if(args != null && args.length > 0) {
			this.name = (String) args[0];
			System.out.println("{"+Person.this.name+"}Arguments: " + args.toString());
		} else {
			Person.this.name= getAID().getLocalName();
		}

		// Base para a comunicacao do Agente e inicializacao
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


		//Inserir código para existirem dados para agentes que estejam a ser criados com o nome X
		if(Person.this.name.equals("Pedro")){
		}
		else if(Person.this.name.equals("Joao")){
		}

		Person.this.schedule= new Schedule(Person.this.name); 
		ABTBehaviour myBehaviour = new ABTBehaviour(this);
		addBehaviour(myBehaviour);


		Person.this.allAgents = new Vector<AllAgents>();
		findAllAgents();

	}

	//Encontra todos os agentes existentes no sistema
	private void findAllAgents() {

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

	//Termina o agente
	protected void exit() {
		System.out.println("Agent "+getLocalName()+": terminating");

		try {
			DFService.deregister(this);  
		} catch(FIPAException e) {
			e.printStackTrace();
		}

		this.doDelete();
	}
} 
