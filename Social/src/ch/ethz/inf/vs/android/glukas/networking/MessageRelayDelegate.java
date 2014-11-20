package ch.ethz.inf.vs.android.glukas.networking;

import ch.ethz.inf.vs.android.glukas.project4.UserId;
import ch.ethz.inf.vs.android.glukas.project4.exceptions.FailureReason;

public interface MessageRelayDelegate {

	/**
	 * Connection of the user to the server succeeded
	 */
	public void onRegistrationSucceeded(UserId self, UserId other);
	
	/**
	 * Connection of the user to the server failed
	 * @param reason
	 */
	public void onRegistrationFailed(UserId self, UserId other, FailureReason reason);
	
	/**
	 * Disconnection of the user from the server succeeded
	 */
	public void onDeregistrationSucceeded();
	
	/**
	 * Disconnection of the user from the server failed
	 * @param reason
	 */
	public void onDeregistrationFailed(FailureReason reason);
	
}
