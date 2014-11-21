package ch.ethz.inf.vs.android.glukas.project4.protocol;

/**
 * This class is used to create the consistency byte on the header's packet. It's used
 * mainly to help communication.
 */
public enum ConsistencyByte {
	CONNECTION("0"),
	DATA_SEARCH("1"),
	POST("2"),
	SEND("3"),
	FRIENDS("4");
	
	private final byte state;
	
	private ConsistencyByte(String s){
		this.state = new Byte(s);
	}
	
	public byte getState(){
		return state;
	}

}
