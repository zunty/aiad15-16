package PersonClasses;
import java.util.Vector;

import org.joda.time.*;

public class Assignment {
	private String name;
	DateTime startHour;
	DateTime endHour;
	Duration duration;
	int priority; // 0 - opcional , 1 - obrigatorio
	Vector<Participant> participants;
	
	public Assignment(String name, DateTime sh, DateTime eh,/* Vector<Participant> parti,*/ int priority, String proposer){
		this.name = name;
		this.startHour = sh;
		this.endHour = eh;
		//this.duration = new Duration(sh, eh);
		this.priority = priority;
		//this.participants = parti;
		//Participant sender = new Participant(proposer);
		//participants.addElement(sender);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DateTime getStartHour() {
		return startHour;
	}

	public void setStartHour(DateTime startHour) {
		this.startHour = startHour;
		endHour = startHour.plus(duration);
	}

	public DateTime getEndHour() {
		return endHour;
	}

	public void setEndHour(DateTime endingHour) {
		this.endHour = endingHour;
	}
	
	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}
	
	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getPriorityOfParticipant(String name) {
		for(int i = 0; i < participants.size(); i++){
			if(participants.elementAt(i).getName().equals(name))
				return participants.elementAt(i).getPriority();
		}
		return -1;
	}

	public Vector<Participant> getParticipant() {
		return participants;
	}

	public void setParticipant(Vector<Participant> participant) {
		this.participants = participant;
	}

	public boolean hasParticipant(String name){
		for(int i = 0; i < participants.size(); i++){
			if(participants.elementAt(i).getName().equals(name))
				return true;;
		}
		return false;
	}
	
	public void reset(){
		//TODO
	}
	
	public String print(){
		String toReturn = new String();
		
		toReturn= "|Assignment name:"+this.name
				+ " |SH:"+this.startHour
				+ " |EH:"+this.endHour
				+ " |Dur:"+this.duration
				+ " |Prio:"+this.priority
				+ " |Atds:"+myParticipants();
		
		return toReturn;
	}
	
	public String myParticipants(){
		String ret = "[";
		
		for(int i = 0; i < participants.size(); i++){
			ret += participants.elementAt(i).toString();
		}
		ret += "]";
		return ret;
	}
	
	public boolean hasNoParticipants(){

		return participants.size() == 0;
	}
	
}
