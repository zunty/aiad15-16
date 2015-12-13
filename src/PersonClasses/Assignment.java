package PersonClasses;
import java.util.Vector;

import org.joda.time.*;

public class Assignment {
	private String name;
	DateTime startHour;
	DateTime endHour;
	Duration duration;
	Vector<Participant> participants;
	
	public Assignment(String name, DateTime sh, DateTime eh, String proposer){
		this.name = name;
		this.startHour = sh;
		this.endHour = eh;
		this.duration = new Duration(sh, eh);
		Participant sender = new Participant(proposer);
	}
	public Assignment(){
		this.name = "Joao";
		participants = new Vector<Participant>();
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
	
	public String print(){
		String toReturn = new String();
		
		toReturn= "|Assignment name:"+this.name
				+ " |Initial Time:"+this.startHour
				+ " |Final Time:"+this.endHour
				+ " |Duration:"+this.duration;
				//+ " |Atds:"+myParticipants();
		
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