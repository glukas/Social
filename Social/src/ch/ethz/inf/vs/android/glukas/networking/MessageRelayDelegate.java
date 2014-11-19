package ch.ethz.inf.vs.android.glukas.networking;

import ch.ethz.inf.vs.android.glukas.project4.exceptions.FailureReason;
import ch.ethz.inf.vs.android.glukas.project4.protocol.User;

public interface MessageRelayDelegate {

	/**
	 * Connection of the user to the server succeeded
	 */
	public void onRegistrationSucceeded(User self, User other);
	
	/**
	 * Connection of the user to the server failed
	 * @param reason
	 */
	public void onRegistrationFailed(User self, User other, FailureReason reason);
	
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
