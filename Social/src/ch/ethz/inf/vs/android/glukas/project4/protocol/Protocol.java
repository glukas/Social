package ch.ethz.inf.vs.android.glukas.project4.protocol;

import ch.ethz.inf.vs.android.glukas.project4.Post;
import ch.ethz.inf.vs.android.glukas.project4.exceptions.DatabaseException;
import ch.ethz.inf.vs.android.glukas.project4.exceptions.NetworkException;
import ch.ethz.inf.vs.android.glukas.project4.protocol.UserRequest.RequestType;
import ch.ethz.inf.vs.android.glukas.project4.security.SecureChannel;
import ch.ethz.inf.vs.android.glukas.project4.security.SecureChannelDelegate;

/**
 * This is the main part of the package, where most of the protocol is implemented.
 * 
 * It linked together request from the user, through implementing the UserDelegate. On an other hand,
 * it handles calls back from the network, through implementing SecureChannelDelegate.
 */
public class Protocol implements ProtocolDelegate, SecureChannelDelegate {
	
	////
	//Life cycle
	////
	
	private static Protocol instance;
	
	/**
	 * Get a instance of Protocol. If it's the first time, it can take some time. (Has to retrieve data
	 * from the database.)
	 */
	public static Protocol getInstance() {
		if (instance == null) {
			return new Protocol();
		} else {
			return instance;
		}
	}
	
	private Protocol(){
		//TODO : set delegates (user, security and database layers)
		//TODO : set local user id
	}
	
	////
	//Members
	////
	
	private SecureChannel channel;
	private UserId localUserId;
	
	////
	//UserDelegate
	////
	
	@Override
	public void connect() throws NetworkException {
		String msg = JSONObjectFactory.createJSONObject(new UserRequest(RequestType.CONNECT)).toString();
		PublicHeader header = new PublicHeader(localUserId, null, new Byte("0"), 0);
		channel.connect(msg, header);
	}

	@Override
	public void disconnect() throws NetworkException {
		String msg = JSONObjectFactory.createJSONObject(new UserRequest(RequestType.DISCONNECT)).toString();
		PublicHeader header = new PublicHeader(localUserId, null, new Byte("0"), 0);
		channel.disconnect(msg, header);
	}

	@Override
	public void postPost(Post post) throws DatabaseException {
	}

	@Override
	public void getUserWall(String DistUsername) throws NetworkException {
	}

	@Override
	public void askFriendship(String DistUsername) throws NetworkException {
	}

	@Override
	public void discoverFriends() throws NetworkException {
	}

	////
	//SecureChannelDelegate
	////

	@Override
	public void onMessageReceived(String message, PublicHeader header) {
	}
}
