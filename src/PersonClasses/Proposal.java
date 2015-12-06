package PersonClasses;
import java.util.Vector;

import org.joda.time.*;

public class Proposal{

	String ProposalId;
	String assignmentName;
	DateTime startHour;
	DateTime endHour;
	String proposer;
	int priority; // 0 - optional , 1 - mandatory
	Vector<String> answered;

	public Proposal(String ProposalId, String assignmentName, DateTime startHour, DateTime endHour, String proposer, int priority){
		this.ProposalId = ProposalId;
		this.assignmentName = assignmentName;
		this.proposer = proposer;
		this.priority = priority;
		this.startHour = startHour;
		this.endHour = endHour;
		this.answered = new Vector<String>();//Pessoas que já responderam
	}
	
	public String getProposalId() {
		return ProposalId;
	}

	public void setProposalId(String ProposalId) {
		this.ProposalId = ProposalId;
	}
	
	public String getAssignmentName() {
		return assignmentName;
	}
	
	public void setAssignmentName(String assignmentName) {
		this.assignmentName = assignmentName;
	}
	
	public DateTime getStartHour() {
		return startHour;
	}
	
	public void setStartHour(DateTime startHour) {
		this.startHour = startHour;
	}
	
	public DateTime getEndHour() {
		return endHour;
	}
	
	public void setEndHour(DateTime endHour) {
		this.endHour = endHour;
	}
	
	public String getProposer() {
		return proposer;
	}
	
	public void setProposer(String proposer) {
		this.proposer = proposer;
	}
	
	public int getPriority() {
		return priority;
	}
	
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public Vector<String> getAnswered() {
		return answered;
	}

	public void setAnswered(Vector<String> answered) {
		this.answered = answered;
	}
	
	public void addToAnswered(String name){
		answered.add(name);
	}
	
	public String toString()
    {
        return "[PROPOSAL"+this.ProposalId+
        	" |assignmentName="+this.assignmentName+ 
            " |startHour="+this.startHour.toString()+
            " |endHour="+this.endHour.toString()+
            " |proposer="+this.proposer.toString()+
            " |priority="+this.priority+
            " |answered="+this.answered.toString()+
            "]";
    }    
	
}
