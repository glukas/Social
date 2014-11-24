package ch.ethz.inf.vs.android.glukas.project4.protocol;

/**
 * This class is used to communicate the status of a message send over the network. This byte is defined
 * in package-info
 */
public enum StatusByte {
	CONNECT("0"),
	DISCONNECT("1"),
	DATA("2"),
	POST("3"),
	SEND("4"),
	UNKNOWN("F");
	
	private final byte state;
	
	private StatusByte(String s){
		this.state = new Byte(s);
	}
	
	/**
	 * Return the byte representation of this status
	 */
	public byte getByte(){
		return state;
	}
	
	/**
	 * Return the StatusByte corresponding of byte provided
	 */
	public static StatusByte constructStatusByte(Byte b){
		switch(b.intValue()) {
		case 0:
			return StatusByte.CONNECT;
		case 1:
			return StatusByte.DISCONNECT;
		case 2: 
			return StatusByte.DATA;
		case 3: 
			return StatusByte.POST;
		case 4: 
			return StatusByte.SEND;
		default:
			return StatusByte.UNKNOWN;
		}
	}
}
