package ch.ethz.inf.vs.android.glukas.project4.protocol;

import java.util.List;

import android.content.Context;
import ch.ethz.inf.vs.android.glukas.project4.Post;
import ch.ethz.inf.vs.android.glukas.project4.User;
import ch.ethz.inf.vs.android.glukas.project4.UserDelegate;
import ch.ethz.inf.vs.android.glukas.project4.UserId;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseAccess;
import ch.ethz.inf.vs.android.glukas.project4.exceptions.DatabaseException;
import ch.ethz.inf.vs.android.glukas.project4.exceptions.FailureReason;
import ch.ethz.inf.vs.android.glukas.project4.exceptions.NetworkException;
import ch.ethz.inf.vs.android.glukas.project4.exceptions.UnhandledFunctionnality;
import ch.ethz.inf.vs.android.glukas.project4.networking.MessageRelay;
import ch.ethz.inf.vs.android.glukas.project4.networking.MessageRelayDelegate;
import ch.ethz.inf.vs.android.glukas.project4.protocol.Message.MessageType;
import ch.ethz.inf.vs.android.glukas.project4.protocol.Message;
import ch.ethz.inf.vs.android.glukas.project4.protocol.parsing.JSONObjectFactory;
import ch.ethz.inf.vs.android.glukas.project4.protocol.parsing.MessageParser;
import ch.ethz.inf.vs.android.glukas.project4.security.SecureChannel;
import ch.ethz.inf.vs.android.glukas.project4.security.SecureChannelDelegate;
import ch.ethz.inf.vs.android.glukas.project4.security.NetworkMessage;

/**
 * This is the main part of the package, where most of the protocol is
 * implemented.
 * 
 * It linked together request from the user, through implementing the
 * UserDelegate. On an other hand, it handles calls back from the network,
 * through implementing SecureChannelDelegate.
 */
public class Protocol implements ProtocolDelegate, SecureChannelDelegate,
		MessageRelayDelegate {

	////
	// Life cycle
	////

	private static Protocol instance;

	/**
	 * Get a instance of Protocol.
	 */
	public static Protocol getInstance(Context context, DatabaseAccess db) {
		if (instance == null) {
			return new Protocol(context, db);
		} else {
			return instance;
		}
	}

	private Protocol(Context context, DatabaseAccess db) {
		// TODO : instantiate delegates (user, security layers)
		// TODO : instantiate messages relays and channels 
		database = db;
		secureChannel.setDelegate(this);
		messageRelay.setDelegate(this);
		localUser = database.getUser();
	}

	////
	// Members
	////

	// Communications with other components
	private SecureChannel secureChannel;
	private MessageRelay messageRelay;
	private UserDelegate userHandler;
	private DatabaseAccess database;

	private User localUser;

	////
	// ProtocolDelegate
	////

	@Override
	public void connect() throws NetworkException {
		String msg = JSONObjectFactory.createJSONObject(
				new Message(MessageType.CONNECT), 0).toString();

		PublicHeader header = new PublicHeader(0, null,
				StatusByte.CONNECT.getByte(), 0, localUser.getId(), null);

		messageRelay.connect(msg, header);
	}

	@Override
	public void disconnect() throws NetworkException {
		String msg = JSONObjectFactory.createJSONObject(
				new Message(MessageType.DISCONNECT), 0).toString();

		PublicHeader header = new PublicHeader(0, null,
				StatusByte.DISCONNECT.getByte(), 0, localUser.getId(), null);

		messageRelay.disconnect(msg, header);
	}

	@Override
	public void postPost(Post post) throws DatabaseException {
		// The order matters !
		//database.putUserPost(post);
		//int msgId = post.getId();
		// TODO : methods actually added in DBAccess, just has to use them
		// int maxNumPosts = DatabaseManager.getNumPosts();
		// database.setUserMaxId(msgId);
		// database.setUserNumPosts(maxNumPosts);
	}

	@Override
	public void getUserWall(String distUsername) throws NetworkException {
		List<UserId> listUserId = database.getFriendId(distUsername);
		// if the list is bigger than one, then the user has two friends with
		// common user name
		if (listUserId.size() > 1) {
			new DatabaseException().printStackTrace();
		}
		Message msg = new Message(MessageType.GET_STATE);

		PublicHeader header = new PublicHeader(0, null,
				StatusByte.DATA.getByte(), 0, localUser.getId(), listUserId.get(0));

		secureChannel.sendMessage(new NetworkMessage(JSONObjectFactory
				.createJSONObject(msg, 0).toString(), header));
	}

	@Override
	public void askFriendship(String distUsername) throws NetworkException {
		//for the moment, we only use NFC
		try {
			throw new UnhandledFunctionnality();
		} catch (UnhandledFunctionnality e) {
			e.printStackTrace();
		}
	}

	@Override
	public void discoverFriends() throws NetworkException {
		//for the moment, we only use NFC
		try {
			throw new UnhandledFunctionnality();
		} catch (UnhandledFunctionnality e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setDelegate(UserDelegate delegate) {
		userHandler = delegate;
	}

	// //
	// SecureChannelDelegate
	// //

	@Override
	public void onMessageReceived(NetworkMessage message) {
		Message msg = MessageParser.parseMessage(message.text, message.header, database);

		// TODO : main part, a lot of cases split depending on MessageType

		// Friendship
		if (msg.getMessageType().equals(MessageType.ACCEPT_FRIENDSHIP)) {
			userHandler.onFriendshipAccepted();
		} else if (msg.getMessageType().equals(MessageType.REFUSE_FRIENDSHIP)) {
			userHandler.onFriendshipDeclined();
		}

		// Retrieve data
		else if (msg.getMessageType().equals(MessageType.ACK_POST)) {

		}

		// Post data
		else if (msg.getMessageType().equals(MessageType.POST_PICTURE)) {

		}

		// Unknown
		else {

		}
	}

	// //
	// MessageRelayDelegate
	// //

	@Override
	public void onRegistrationSucceeded(UserId self, UserId other) {
		userHandler.onConnectionSucceeded();
	}

	@Override
	public void onRegistrationFailed(UserId self, UserId other,
			FailureReason reason) {
		userHandler.onConnectionFailed(reason);
	}

	@Override
	public void onDeregistrationSucceeded() {
		userHandler.onDisconnectionSucceeded();
	}

	@Override
	public void onDeregistrationFailed(FailureReason reason) {
		userHandler.onDisconnectionFailed(reason);
	}

}
