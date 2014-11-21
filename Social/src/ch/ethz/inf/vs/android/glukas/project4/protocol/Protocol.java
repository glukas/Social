package ch.ethz.inf.vs.android.glukas.project4.protocol;

import android.content.Context;
import ch.ethz.inf.vs.android.glukas.project4.Post;
import ch.ethz.inf.vs.android.glukas.project4.UserDelegate;
import ch.ethz.inf.vs.android.glukas.project4.UserId;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseDelegate;
import ch.ethz.inf.vs.android.glukas.project4.exceptions.DatabaseException;
import ch.ethz.inf.vs.android.glukas.project4.exceptions.FailureReason;
import ch.ethz.inf.vs.android.glukas.project4.exceptions.NetworkException;
import ch.ethz.inf.vs.android.glukas.project4.networking.MessageRelay;
import ch.ethz.inf.vs.android.glukas.project4.networking.MessageRelayDelegate;
import ch.ethz.inf.vs.android.glukas.project4.protocol.Message.MessageType;
import ch.ethz.inf.vs.android.glukas.project4.protocol.Message;
import ch.ethz.inf.vs.android.glukas.project4.protocol.parsing.JSONObjectFactory;
import ch.ethz.inf.vs.android.glukas.project4.security.SecureChannel;
import ch.ethz.inf.vs.android.glukas.project4.security.SecureChannelDelegate;

/**
 * This is the main part of the package, where most of the protocol is implemented.
 * 
 * It linked together request from the user, through implementing the UserDelegate. On an other hand,
 * it handles calls back from the network, through implementing SecureChannelDelegate.
 */
public class Protocol implements ProtocolDelegate, SecureChannelDelegate, MessageRelayDelegate {
	
	////
	//Life cycle
	////
	
	private static Protocol instance;
	private static Context context;
	
	/**
	 * Get a instance of Protocol. If it's the first time, it can take some time. (Has to retrieve data
	 * from the database.)
	 */
	public static Protocol getInstance(Context context) {
		if (instance == null) {
			return new Protocol(context);
		} else {
			Protocol.context = context;
			return instance;
		}
	}
	
	private Protocol(Context context){
		//TODO : set delegates (user, security and database layers)
		//TODO : instantiate delegates (user, security and database layers)
		//TODO : set calls back
		//TODO : set local user id
	}
	
	////
	//Members
	////
	
	//Communications with other components
	private SecureChannel secureChannel;
	private MessageRelay messageRelay;
	private UserDelegate userHandler;
	private DatabaseDelegate database;
	
	
	private UserId localUserId;
	
	////
	//ProtocolDelegate
	////
	
	@Override
	public void connect() throws NetworkException {
		String msg = JSONObjectFactory.createJSONObject(new Message(MessageType.CONNECT)).toString();
		PublicHeader header = new PublicHeader(localUserId, null, ConsistencyByte.CONNECTION.getState(), 0);
		messageRelay.connect(msg, header);
	}

	@Override
	public void disconnect() throws NetworkException {
		String msg = JSONObjectFactory.createJSONObject(new Message(MessageType.DISCONNECT)).toString();
		PublicHeader header = new PublicHeader(localUserId, null, ConsistencyByte.CONNECTION.getState(), 0);
		messageRelay.disconnect(msg, header);
	}
	
	@Override
	public void postPost(Post post) throws DatabaseException {
		//TODO : save in database. Sent to the owner of the wall
		secureChannel.broadcastMessage(null, null);
	}

	@Override
	public void getUserWall(String distUsername) throws NetworkException {
		//TODO : check latest version
		//TODO : retrieve from network latest updates
		//TODO : make it asynchronous, this method should return asap
		database.getUserid(distUsername);
	}

	@Override
	public void askFriendship(String distUsername) throws NetworkException {
	}

	@Override
	public void discoverFriends() throws NetworkException {
	}
	
	@Override
	public void setDelegate(UserDelegate delegate) {
	}

	////
	//SecureChannelDelegate
	////

	@Override
	public void onMessageReceived(String message, PublicHeader header) {
	}

	////
	//MessageRelayDelegate
	////
	
	@Override
	public void onRegistrationSucceeded(UserId self, UserId other) {
	}

	@Override
	public void onRegistrationFailed(UserId self, UserId other, FailureReason reason) {
	}

	@Override
	public void onDeregistrationSucceeded() {
	}

	@Override
	public void onDeregistrationFailed(FailureReason reason) {
	}
}
