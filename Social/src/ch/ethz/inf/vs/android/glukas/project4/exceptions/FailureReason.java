package ch.ethz.inf.vs.android.glukas.project4.exceptions;

/**
 * This enumeration contains reasons of exceptional behaviors
 */

public enum FailureReason {
	
	NETWORK("Network failure"),
	DATA_NOT_ACCESSIBLE("Data is not accessible");
	
	private final String reason;
	
	private FailureReason(String s){
		reason = s;
	}
	
	/**
	 * @return a String format of reason
	 */
	public String getReasonStr(){
		return reason;
	}

}
