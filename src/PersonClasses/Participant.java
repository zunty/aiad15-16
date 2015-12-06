package PersonClasses;
public class Participant {
	String name;
	int priority;// 0 - opcional / 1 - obrigatorio
	
	public Participant(String name, int priority){
		this.name = name;
		this.priority = priority;
	}

	public Participant(String name){
		this.name = name;
		this.priority = 1;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public String print(){
		
		return "|Assignment name: "+this.name
				+ " |Priority: "+this.priority;
	}
}
