package ch.ethz.inf.vs.android.glukas.project4;

import java.util.List;

import ch.ethz.inf.vs.android.glukas.project4.exceptions.FailureReason;

/**
 * Interface to the network for calls back from the network
 */
public interface UserDelegate {
	
	/**
	 * The network received a post
	 * @param post
	 */
	public void onPostReceived(Post post);
	
	/**
	 * The connection to the server failed
	 * @param reason
	 */
	public void onConnectionFailed(FailureReason reason);
	
}
 