package ch.ethz.inf.vs.android.glukas.project4.exceptions;

import ch.ethz.inf.vs.android.glukas.project4.protocol.Message.MessageType;

public class UnknowRequestType extends ProtocolException {

	private static final long serialVersionUID = 1L;
	
	MessageType type;
	
	public UnknowRequestType(MessageType type){
		this.type = type;
	}

	@Override
	public String toString(){
		return "The type : "+type+ "is not recognized.\n"+super.toString();
	}
}
