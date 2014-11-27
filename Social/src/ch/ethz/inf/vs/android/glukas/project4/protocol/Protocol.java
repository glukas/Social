package ch.ethz.inf.vs.android.glukas.project4.protocol;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import android.content.Context;
import android.util.Log;
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
		wallAsked = new TreeSet<UserId>();
	}

	////
	// Members
	////

	// Communications with other components
	private SecureChannel secureChannel;
	private MessageRelay messageRelay;
	private UserDelegate userHandler;
	private DatabaseAccess database;

	//caching
	private User localUser;
	private Set<UserId> wallAsked;
	
	//exceptional behaviors
	private final String unexpectedMsg = "Unexpected message arrived : ";

	////
	// ProtocolDelegate
	////

	@Override
	public void connect() throws NetworkException {
		//TODO (only sketch)
		String msg = JSONObjectFactory.createJSONObject(
				MessageFactory.newTypeMessage(MessageType.CONNECT), 0).toString();

		PublicHeader header = new PublicHeader(0, null,
				StatusByte.CONNECT.getByte(), 0, localUser.getId(), null);

		messageRelay.connect(msg, header);
	}

	@Override
	public void disconnect() throws NetworkException {
		//TODO (only sketch)
		String msg = JSONObjectFactory.createJSONObject(
				MessageFactory.newTypeMessage(MessageType.DISCONNECT), 0).toString();

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
		// TODO proper exceptions handling
		if (listUserId == null || listUserId.size() == 0 || listUserId.size() > 1) {
			new DatabaseException().printStackTrace();
		}
		UserId userId = listUserId.get(0);
		//retrieve data already known from database
		
		//ask distant user if already all messages are in database
		Message msg = MessageFactory.newTypeMessage(MessageType.GET_STATE);
		PublicHeader header = new PublicHeader(0, null, StatusByte.DATA.getByte(), 0, localUser.getId(), userId);
		secureChannel.sendMessage(new NetworkMessage(JSONObjectFactory.createJSONObject(msg, 0).toString(), header));
		wallAsked.add(userId);
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

	////
	// SecureChannelDelegate
	////

	@Override
	public void onMessageReceived(NetworkMessage message) {
		//react to an incoming message
		Message msg = MessageParser.parseMessage(message.text, message.header, database);
		
		MessageType type = msg.getRequestType();
			// Server
		if (type.equals(MessageType.CONNECT)) {
			onConnectReceived(msg);
		} else if (type.equals(MessageType.DISCONNECT)) {
			onDisconnectReceived(msg);
	
			// Friends
		} else if (type.equals(MessageType.SEARCH_USER)) {
			onSearchUserReceived(msg);
		} else if (type.equals(MessageType.ACCEPT_FRIENDSHIP)) {
			onAcceptFriendshipReceived(msg);
		} else if (type.equals(MessageType.REFUSE_FRIENDSHIP)) {
			onRefuseFriendshipReceived(msg);
		} else if (type.equals(MessageType.ASK_FRIENDSHIP)) {
			onAskFriendshipReceived(msg);
		} else if (type.equals(MessageType.BROADCAST)) {
			onBroadcastReceived(msg);
	
			// Post new messages
		} else if (type.equals(MessageType.POST_PICTURE)) {
			onPostPictureReceived(msg);
		} else if (type.equals(MessageType.POST_TEXT)) {
			onPostTextReceived(msg);
		} else if (type.equals(MessageType.ACK_POST)) {
			onAckPostReceived(msg);
	
			// Retrieve data
		} else if (type.equals(MessageType.GET_POSTS)) {
			onGetPostsReceived(msg);
		} else if (type.equals(MessageType.SHOW_IMAGE)) {
			onShowImageReceived(msg);
		} else if (type.equals(MessageType.SEND_PICTURE)) {
			onSendPictureReceived(msg);
		} else if (type.equals(MessageType.SEND_TEXT)) {
			onSendTextReceived(msg);
		} else if (type.equals(MessageType.SEND_STATE)) {
			onSendStateReceived(msg);
		} else if (type.equals(MessageType.GET_STATE)) {
			onGetStateReceived(msg);
		}
	}
	
	private void onPostPictureReceived(Message msg) {
		//for the moment, we react exactly the same as on text received
		onPostReceived(msg);
	}
	
	private void onPostTextReceived(Message msg) {
		//for the moment, we react exactly the same as on picture received
		onPostReceived(msg);
	}
	
	private void onPostReceived(Message msg) {
		//A friend posted a post onto the wall's local user
		int msgId = database.getFriendMaxPostsId(localUser.getId());
		Post post = new Post(msg, msgId);
		database.putFriendPost(post, localUser.getId());
		//TODO : implement acknowledgment 
	}
	
	private void onAckPostReceived(Message msg) {
		//for the moment, the model ensures that the post will be posted, thus
		//it can be silently ignored
	}
	
	private void onGetPostsReceived(Message msg) {
		List<Post> listPosts = database.getAllFriendPostsFrom(localUser.getId(), msg.getId());
		for (Post post : listPosts){
			String msgTxt = JSONObjectFactory.createJSONObject(MessageFactory.newPostMessage(post, localUser, msg.getSender(), true)).toString();
			PublicHeader header = new PublicHeader(0, null, StatusByte.SEND.getByte(), post.getId(), localUser.getId(), msg.getSender().getId());
			NetworkMessage networkMsg = new NetworkMessage(msgTxt, header);
			secureChannel.sendMessage(networkMsg);
		}
	}
	
	private void onShowImageReceived(Message msg) {
		//TODO : future use
	}
	
	private void onSendPictureReceived(Message msg) {
		//for the moment, we react exactly the same as on text received
		onSendReceived(msg);
	}
	
	private void onSendTextReceived(Message msg) {
		//for the moment, we react exactly the same as on picture received
		onSendReceived(msg);
	}
	
	private void onSendReceived(Message msg) {
		//A friend send a post to the local user
		Post post = new Post(msg, msg.getPostId());
		database.putFriendPost(post, msg.getSender().getId());
	}
	
	private void onSendStateReceived(Message msg) {
		//Someone send his / her state, so retrieve data from the message
		UserId userId = msg.getSender().getId();
		int maxNumMsgs = msg.getNumM();
		int maxPostId = msg.getId();
		
		//check if local user was waiting on this wall
		boolean isWaitingOnWall = wallAsked.contains(userId);
		if (isWaitingOnWall) {
			//ask to have posts that local user doesn't have in database
			int oldMaxPostId = database.getFriendMaxPostsId(userId);
			Message msgToSend = MessageFactory.newGetPostsMessage(oldMaxPostId, localUser, msg.getSender());
			PublicHeader header = new PublicHeader(0, null, StatusByte.SEND.getByte(), 0, localUser.getId(), userId);
			NetworkMessage networkMessage = new NetworkMessage(JSONObjectFactory.createJSONObject(msgToSend).toString(), header);
			secureChannel.sendMessage(networkMessage);
		}
		
		//And stores informations into the database
		database.setFriendMaxPostsId(maxNumMsgs, userId);
		database.setFriendPostsCount(maxPostId, userId);
	}
	
	private void onGetStateReceived(Message msg) {
		//Someone wants user state, so retrieve data from database
		int maxPostId = database.getFriendMaxPostsId(localUser.getId());
		int maxNumMsgs = database.getFriendMaxPostsId(localUser.getId());
		//create message encapsulating all informations
		User userToSend = msg.getSender();
		String msgToSend = JSONObjectFactory.createJSONObject(MessageFactory.newSendStateMessage(localUser, userToSend, maxPostId, maxNumMsgs)).toString();
		PublicHeader header = new PublicHeader(0, null, StatusByte.SEND.getByte(), 0, localUser.getId(), userToSend.getId());
		//send reply
		secureChannel.sendMessage(new NetworkMessage(msgToSend, header));
	}
	
	//unexpected messages
	
	private void onAcceptFriendshipReceived(Message msg) {
		//We use NFC for the moment
		Log.d(getClass().toString(), unexpectedMsg+msg.toString());
	}
	
	private void onRefuseFriendshipReceived(Message msg) {
		//We use NFC for the moment
		Log.d(getClass().toString(), unexpectedMsg+msg.toString());
	}
	
	private void onAskFriendshipReceived(Message msg) {
		//We use NFC for the moment
		Log.d(getClass().toString(), unexpectedMsg+msg.toString());
	}
	
	private void onConnectReceived(Message msg) {
		//User should not be targeted by these kind of messages
		Log.d(getClass().toString(), unexpectedMsg+msg.toString());
	}
	
	private void onDisconnectReceived(Message msg) {
		//User should not be targeted by these kind of messages
		Log.d(getClass().toString(), unexpectedMsg+msg.toString());
	}
	
	private void onSearchUserReceived(Message msg) {
		//User should not be targeted by these kind of messages
		Log.d(getClass().toString(), unexpectedMsg+msg.toString());
	}
	
	private void onBroadcastReceived(Message msg) {
		//User should not be targeted by these kind of messages
		Log.d(getClass().toString(), unexpectedMsg+msg.toString());
	}

	////
	// MessageRelayDelegate
	////

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
