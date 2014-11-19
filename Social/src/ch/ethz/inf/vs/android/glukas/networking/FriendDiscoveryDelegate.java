package ch.ethz.inf.vs.android.glukas.networking;

import java.util.List;

public interface FriendDiscoveryDelegate {
	
	/**
	 * Called when the list of discovered peers changed.
	 * This method is called on the thread that the corresponding FriendDiscovery was instantiated.
	 * @param discoveredPeers the list of all peers that are available
	 */
	public void onPeersDiscoveredChanged(List<FriendDiscovery.Peer> discoveredPeers);
	
	/**
	 * Called when a friendship was established.
	 * @param peer
	 */
	public void onFriendshipRequestAccepted(FriendDiscovery.Peer peer);
}
