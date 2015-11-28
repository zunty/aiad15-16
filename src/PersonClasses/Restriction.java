package PersonClasses;

import PersonClasses.Qualifier;
import PersonClasses.Proposal;

public class Restriction {
	Proposal proposal;
	Qualifier id;
	
	public Restriction(Proposal proposal, Qualifier id){
		this.proposal = proposal;
		this.id = id;
	}
	
	public Proposal getProposal() {
		return proposal;
	}
	public void setP(Proposal proposal) {
		this.proposal = proposal;
	}
	public Qualifier getId() {
		return id;
	}
	public void setId(Qualifier id) {
		this.id = id;
	}
}
