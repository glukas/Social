package ch.ethz.inf.vs.android.glukas.project4.exceptions;

import ch.ethz.inf.vs.android.glukas.project4.protocol.UserRequest.RequestType;

public class UnknowRequestType extends Exception {

	private static final long serialVersionUID = 1L;
	
	RequestType type;
	
	public UnknowRequestType(RequestType type){
		this.type = type;
	}

	@Override
	public String toString(){
		return "The type : "+type+ "is not recognized.\n"+super.toString();
	}
}
