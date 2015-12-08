package PersonClasses;
import java.util.Iterator;
import java.util.Vector;

import org.joda.time.DateTime;

public class Schedule {

	private String owner;
	private Vector<Assignment> assignments = new Vector<Assignment>();
	Vector<String> peopleHaveAssignesWith;
	

	public Schedule(String owner){
		this.owner = owner;
		assignments = new Vector<Assignment>();
	}

	public String getOwner(){
		return owner;
	}

	public void setOwner(String newOwner){
		this.owner = newOwner;
	}

	public Vector<Assignment> getAssignments() {
		return assignments;
	}

	public Assignment getAssignment(String name){
		for(int i = 0; i < assignments.size(); i++){
			if(assignments.elementAt(i).getName().equals(name))
				return assignments.elementAt(i);
		}
		return null;
	}

	public Assignment getAssignment(String name, String participant){
		for(int i = 0; i < assignments.size(); i++){
			if(assignments.elementAt(i).getName().equals(name) && assignments.elementAt(i).hasParticipant(participant))
				return assignments.elementAt(i);
		}
		return null;
	}

	public Assignment getAssignment(int i){
		if(i >= 0 && i < assignments.size())
			return assignments.elementAt(i);
		else
			return null;
	}

	public void setAssignments(Vector<Assignment> assignments) {
		this.assignments = assignments;
	}

	public void addAssignment(Assignment a) {

		assignments.add(a);
		System.out.println("Inseri no " + owner);
	}

	public String myAssignments() {

		String myassignments = new String();

		for(int i = 0; i < assignments.size(); i++) {
			myassignments += assignments.elementAt(i) + "\n";
		}

		return myassignments;
	}

	public void CancelEvent(int assignmentPosHour){
		assignments.remove(assignmentPosHour);
	}

	public int checkProposal(Proposal p){

		//TODO
		return 0;
	}

	public void insertProposal(Proposal p){

		//Se ainda n�o existir coloca no vetor
		if( getAssignment(p.getAssignmentName()) == null){
			//Modificar para construtor novo
			//Assignment e = new Assignment(p.getAssignmentName(), p.getStartHour(), p.getEndHour(), p.getProposer(), p.getPriority());
			//assignments.add(e);
			System.out.println("{"+owner+"}inserted new assignment: "+p.getAssignmentName());
		}
		else{
			System.out.println("That assignment" +p.getAssignmentName() + "already exists for " + owner);
		}
	}

	public boolean checkOverlappingAssignments(Proposal p)
	{
		//TODO
		return false;
	}

	//0 = ok 1 = nogood 2 = stop
	public int checkAvailability(DateTime init, DateTime end){

		System.out.println("Tempo inicial:" + init.toString());
		System.out.println("Tempo final" + end.toString());
		System.out.println("Tamanho assignment inicial do" + owner + ":" + assignments.size());

		for(int i=0;i<assignments.size();i++){
			System.out.println("entra!");
			//verifica - verificar quando começam a mesma hora
			if(  (init.isAfter(assignments.elementAt(i).getStartHour())&&init.isBefore(assignments.elementAt(i).getEndHour()))) {
				System.out.println("N�o est� dispon�vel");
				return 1;
			}
		}
		System.out.println("Est� dispon�vel");
		return 0;
	}

	public boolean getPeopleAssignmentsWith(String Person) {

		//TODO
		return true;
	}

}


