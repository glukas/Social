package ch.ethz.inf.vs.android.glukas.project4.protocol;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import android.util.Log;
import ch.ethz.inf.vs.android.glukas.project4.Post;
import ch.ethz.inf.vs.android.glukas.project4.BasicUser;
import ch.ethz.inf.vs.android.glukas.project4.UserDelegate;
import ch.ethz.inf.vs.android.glukas.project4.UserId;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseAccess;
import ch.ethz.inf.vs.android.glukas.project4.exceptions.DatabaseException;
import ch.ethz.inf.vs.android.glukas.project4.exceptions.UnhandledFunctionnality;
import ch.ethz.inf.vs.android.glukas.project4.networking.MessageRelay;
import ch.ethz.inf.vs.android.glukas.project4.protocol.Message.MessageType;
import ch.ethz.inf.vs.android.glukas.project4.protocol.Message;
import ch.ethz.inf.vs.android.glukas.project4.protocol.parsing.JSONObjectFactory;
import ch.ethz.inf.vs.android.glukas.project4.protocol.parsing.MessageParser;
import ch.ethz.inf.vs.android.glukas.project4.security.DBCredentialStorage;
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
public class Protocol implements ProtocolDelegate, SecureChannelDelegate {

	////
	// Life cycle
	////

	private static Protocol instance;

	/**
	 * Get a instance of Protocol.
	 */
	public static Protocol getInstance(DatabaseAccess db) {
		if (instance == null) {
			return new Protocol(db);
		} else {
			return instance;
		}
	}

	private Protocol(DatabaseAccess db) {
		database = db;
		localUser = database.getUser();
		secureChannel = new SecureChannel("winti.mooo.com", 9000, new DBCredentialStorage(db));
		secureChannel.setDelegate(this);
		messageRelay = new MessageRelay(secureChannel);
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
	private BasicUser localUser;
	private volatile Set<UserId> wallAsked;
	
	//exceptional behaviors
	private final String unexpectedMsg = "Unexpected message arrived : ";

	////
	// ProtocolDelegate
	////

	@Override
	public void connect() {
		messageRelay.connect(localUser.getId());
	}

	@Override
	public void disconnect() {
		messageRelay.disconnect(localUser.getId());
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
		
		database.putUserPost(post);
		int msgId = post.getId();
		int maxNumPosts = database.getUserPostsCount()+1;
		database.setUserMaxPostsId(msgId);
		database.setUserPostsCount(maxNumPosts);
		
		
	}

	@Override
	public void getUserWall(UserId userId) {
		getUserPosts(userId, 0);
	}
	
	@Override
	public void getUserPosts(UserId userId, int postId) {
		//retrieve data already known from database
		database.getAllFriendPostsFrom(userId, postId);
		//ask distant user if already all messages are in database
		Message msg = MessageFactory.newTypeMessage(MessageType.GET_STATE);
		PublicHeader header = new PublicHeader(0, null, StatusByte.DATA.getByte(), 0, localUser.getId(), userId);
		secureChannel.sendMessage(new NetworkMessage(JSONObjectFactory.createJSONObject(msg, 0).toString(), header));
		wallAsked.add(userId);
	}
	
	@Override
	public void getSomeUserPosts(UserId userId, int numberPosts) {
		int oldestPost = database.getFriendMaxPostsId(userId);
		getSomeUserPosts(userId, numberPosts, oldestPost);
	}
	
	@Override
	public void getSomeUserPosts(UserId userId, int numberPosts, int postId) {
		//List<Post> listPosts = database.getSomeUserPosts(userId, numberPosts, postId);
		List<Post> listPosts = null;
		for (Post p : listPosts){
			userHandler.onPostReceived(p);
		}
		
		Message msg = MessageFactory.newTypeMessage(MessageType.GET_STATE);
		PublicHeader header = new PublicHeader(0, null, StatusByte.DATA.getByte(), 0, localUser.getId(), userId);
		secureChannel.sendMessage(new NetworkMessage(JSONObjectFactory.createJSONObject(msg, 0).toString(), header));
		wallAsked.add(userId);
	}

	@Override
	public void askFriendship(String distUsername) {
		//for the moment, we only use NFC
		try {
			throw new UnhandledFunctionnality();
		} catch (UnhandledFunctionnality e) {
			e.printStackTrace();
		}
	}

	@Override
	public void discoverFriends() {
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
		Message msg = MessageParser.parseMessage(message.getText(), message.header, database);
		
		MessageType type = msg.getRequestType();
	
			// Friends
		if (type.equals(MessageType.SEARCH_USER)) {
			onSearchUserReceived(msg);
		} else if (type.equals(MessageType.ACCEPT_FRIENDSHIP)) {
			onAcceptFriendshipReceived(msg);
		} else if (type.equals(MessageType.REFUSE_FRIENDSHIP)) {
			onRefuseFriendshipReceived(msg);
		} else if (type.equals(MessageType.ASK_FRIENDSHIP)) {
			onAskFriendshipReceived(msg);
		}
	
			// Post new messages
		else if (type.equals(MessageType.POST_PICTURE)) {
			onPostPictureReceived(msg);
		} else if (type.equals(MessageType.POST_TEXT)) {
			onPostTextReceived(msg);
		} else if (type.equals(MessageType.ACK_POST)) {
			onAckPostReceived(msg);
		}
	
			// Retrieve data
		else if (type.equals(MessageType.GET_POSTS)) {
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
		BasicUser userToSend = msg.getSender();
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
	
	private void onSearchUserReceived(Message msg) {
		//User should not be targeted by these kind of messages
		Log.d(getClass().toString(), unexpectedMsg+msg.toString());
	}
}
