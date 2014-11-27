package ch.ethz.inf.vs.android.glukas.project4.protocol.parsing;

/**
 * Enumeration of possible commands (names for JSONObject) used in internal protocol
 */
enum Cmds{
	CMD("cmd"),
	FROM("from"),
	ID("id"),
	TEXT("text"),
	PIC("picture"),
	ACK("ack"),
	RESPONSE("response"),
	NUM_M("numMessages");
	
	private String name;
	
	Cmds(String s){
		this.name = s;
	}
	
	public String getStr(){
		return name;
	}
}