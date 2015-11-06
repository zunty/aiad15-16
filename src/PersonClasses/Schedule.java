package PersonClasses;
import java.util.Vector;

public class Schedule {
	
	private String owner;
	Vector<Assignment> assignments = null;

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

	public void addAssignment(Assignment e) {
		assignments.add(e);

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

		//Se ainda não existir coloca no vetor
		if( getAssignment(p.getAssignmentName()) == null){

			Assignment e = new Assignment(p.getAssignmentName(), p.getStartHour(), p.getEndHour(), p.getProposer(), p.getPriority());
			assignments.add(e);
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

	
	public Object getPeopleIHaveAssignmentsWith() {
		// TODO 
		return null;
	}

}

	
