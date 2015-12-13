package PersonClasses;
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
		System.out.println(owner + ": I have inserted one new assignment");
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

	//0 = ok 1 = nogood 2 = stop
	public int checkAvailability(DateTime init, DateTime end){

		for(int i=0;i<assignments.size();i++){
			//verifica quando se iniciam a mesma hora
			if(  (init.isAfter(assignments.elementAt(i).getStartHour())&&init.isBefore(assignments.elementAt(i).getEndHour()))) {
				System.out.println("This assignment can't be at this time for " + owner + "\n");
				return 1;
			}
			else if(init.equals(assignments.elementAt(i).getStartHour())){
				System.out.println("This assignment can't be at this time for " + owner + "\n");	return 1;
			}
		}
		System.out.println(owner + " is available during this time\n");
		return 0;
	}
	
	public void print(){
		//System.out.println("tamanha do assign: "+ assignments.size());
		for(int i=0; i< assignments.size();i++){
			System.out.println(assignments.elementAt(i).print());
		}
	}

}


