package ch.ethz.inf.vs.android.glukas.networking;

import java.util.List;

public interface FriendDiscoveryDelegate {
	
	public void onFriendsDiscoveredChanged(List<String> discoveredFriends);//TODO use something better than Strings?
	
}
