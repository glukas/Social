package ch.ethz.inf.vs.android.glukas.project4.protocol;

public enum ConsistencyByte {
	CONNECTION("0");
	
	private final byte state;
	
	private ConsistencyByte(String s){
		this.state = new Byte(s);
	}
	
	public byte getState(){
		return state;
	}

}
