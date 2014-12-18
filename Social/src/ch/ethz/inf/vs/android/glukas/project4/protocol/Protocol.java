package ch.ethz.inf.vs.android.glukas.project4.protocol;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import ch.ethz.inf.vs.android.glukas.project4.Post;
import ch.ethz.inf.vs.android.glukas.project4.BasicUser;
import ch.ethz.inf.vs.android.glukas.project4.User;
import ch.ethz.inf.vs.android.glukas.project4.UserDelegate;
import ch.ethz.inf.vs.android.glukas.project4.UserId;
import ch.ethz.inf.vs.android.glukas.project4.Wall;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseAccess;
import ch.ethz.inf.vs.android.glukas.project4.database.Utility;
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
public class Protocol implements ProtocolInterface, SecureChannelDelegate {

	////
	// Life cycle
	////

	private static Protocol instance;

	/**
	 * Get a instance of Protocol. Don't forget to call 'setProtocol' if it's the first time
	 */
	public static Protocol getInstance(DatabaseAccess db) {
		if (instance == null) {
			return new Protocol(db);
		} else {
			return instance;
		}
	}
	
	public Protocol(DatabaseAccess db) {
		database = db;
		localUser = database.getUser();
		secureChannel = new SecureChannel("winti.mooo.com", 9000, new DBCredentialStorage(db));
		secureChannel.setDelegate(this);
		messageRelay = new MessageRelay(secureChannel);
		userMapping = new HashMap<UserId, User>();
		refreshUserMapping();
	}
	
	private void refreshUserMapping() {
		List<User> users = database.getUserFriendsList();
		for (User user : users) {
			userMapping.put(user.getId(), user);
		}
		if (localUser != null) {
			userMapping.put(localUser.getId(), localUser);
		}
	}

	////
	// Members
	////

	private Map<NetworkMessage, Post> outgoingPosts = new HashMap<NetworkMessage, Post>();
	private boolean isConnected = false;
	private int numFailed = 0;
	
	// Communications with other components
	private SecureChannel secureChannel;
	private MessageRelay messageRelay;
	private UserDelegate userHandler;
	private DatabaseAccess database;

	//caching
	private User localUser;
	private Map<UserId, User> userMapping;
	
	//exceptional behaviors
	private final String unexpectedMsg = "Unexpected message arrived : ";

	////
	// ProtocolInterface
	////
	
	public int getNewPostId(UserId userId) {
		return database.getFriendMaxPostsId(userId)+1;
	}
	
	public Wall getUserWall() {
		return database.getUserWall();
	}
	
	@Override
	public User getUser() {
		return database.getUser();
	}

	@Override
	public void putUser(User user) {
		database.putUser(user);
		localUser = user;
		refreshUserMapping();
		//userMapping.put(user.getId(), user);
	}
	
	@Override
	public void putFriend(User friend) {
		database.putFriend(friend);
		refreshUserMapping();
		//userMapping.put(friend.getId(), friend);
		//Log.d(this.getClass().toString(), userMapping.toString());
	}
	
	@Override
	public User getFriend(UserId id) {
		return database.getFriend(id);
	}
	
	@Override
	public Map<UserId, User> getUserMapping() {
		refreshUserMapping();
		return Collections.unmodifiableMap(userMapping);
	}

	@Override
	public void connect() {
		messageRelay.connect(localUser.getId());
	}

	@Override
	public void disconnect() {
		messageRelay.disconnect(localUser.getId());
	}
	
	@Override
	public void post(UserId wallOwner, String text, Bitmap image) {
		
		if (wallOwner.getId().equals(localUser.getId())) {
			post(text, image);
			return;
		}
		
		Post post = new Post(getNewPostId(wallOwner), localUser.getId(), wallOwner, text, image, new Date());
		
		NetworkMessage message = assembleNetworkMessage(post);
		
		this.outgoingPosts.put(message, post);
		
		sendMessage(message);
	}
	
	private NetworkMessage assembleNetworkMessage(Post post) {
		Message msg = MessageFactory.newPostMessage(post, false);
		
		byte[] protocolMessageBytes = JSONObjectFactory.createJSONObject(msg).toString().getBytes();
		
		PublicHeader header = new PublicHeader(0, (short)protocolMessageBytes.length, StatusByte.POST.getByte(), post.getId(), post.getPoster(), post.getWallOwner());
		ByteBuffer assembledMessage = ByteBuffer.allocate(protocolMessageBytes.length+msg.getPayload().length);
		assembledMessage.put(protocolMessageBytes);
		assembledMessage.put(msg.getPayload());
		
		return new NetworkMessage(assembledMessage.array(), header);
	}
	
	@Override
	public void post(String text, Bitmap image) {
		//Local post: first post locally, then send over the network
		Post post = new Post(getNewPostId(localUser.getId()), localUser.getId(), localUser.getId(), text, image, new Date());
		boolean success = postLocally(post);
		if (success) {
			sendMessage(assembleNetworkMessage(post));
		}
	}
	
	private boolean postLocally(Post post) {
		boolean alreadyContained = database.containsPost(post.getId(), post.getPoster(), post.getWallOwner());
		boolean success = !alreadyContained && database.putPost(post);
		if (success) {
			Log.d(this.getClass().toString(), "postLocally : " + post.getText() + " , " + post.getPoster().getId() + " , " + post.getWallOwner().getId());
			int localNumberOfPosts = database.getFriendPostsCount(post.getWallOwner());
			int localTimestamp = database.getFriendMaxPostsId(post.getWallOwner());
			database.setFriendMaxPostsId(Math.max(post.getId(), localTimestamp), post.getWallOwner());
			database.setFriendPostsCount(localNumberOfPosts+1, post.getWallOwner());
			userHandler.onPostReceived(post);
		}
		return success;
	}

	@Override
	public void getUserWall(UserId userId) {
		getUserPosts(userId, 0);
	}
	
	@Override
	public void getUserPosts(UserId userId, int postId) {
		List<Post> newPosts = database.getAllFriendPostsFrom(userId, postId);
		
		for (Post post : newPosts) {
			this.userHandler.onPostReceived(post);
		}
		
		getState(userId);
	}
	
	@Override
	public void getSomeUserPosts(UserId userId, int numberPosts) {
		getSomeUserPosts(userId, numberPosts, Integer.MAX_VALUE);
	}
	
	@Override
	public void getSomeUserPosts(UserId userId, int numberPosts, int postId) {
		//retrieve posts already in the database
		Log.d(this.getClass().toString(), "getSomeUserPosts " + userId.toString());
		
		new AsyncGetSomeLatestPostsDatabaseQuery(numberPosts, postId).execute(userId);
		
		//ask for update
		getState(userId);
	}
	
	private void getState(UserId userId) {
		Message msg = MessageFactory.newTypeMessage(MessageType.GET_STATE);
		PublicHeader request = new PublicHeader(PublicHeader.BYTES_LENGTH_HEADER, null, StatusByte.DATA.getByte(), 0, localUser.getId(), userId);
		sendMessage(new NetworkMessage(JSONObjectFactory.createJSONObject(msg).toString(), request));
	}

	@Override
	public void setDelegate(UserDelegate delegate) {
		userHandler = delegate;
	}
	
	private void sendMessage(NetworkMessage message) {
		if (!isConnected) {
			this.connect();
		}
		this.secureChannel.sendMessage(message);
	}

	////
	// SecureChannelDelegate
	////

	@Override
	public void onMessageReceived(NetworkMessage message) {
		//react to an incoming message
		if (message.text.length == 0) {
			onHeaderReceived(message.header);
		} else {
			//The header tells us how much of the data is is JSON protocol, and how much is image payload (the rest)

			Message msg = MessageParser.parseMessage(message.text , message.header);
			MessageType type = msg.getRequestType();
			
			if (type.equals(MessageType.POST_TEXT)) {
				onPostReceived(msg);
			} else if (type.equals(MessageType.ACK_POST)) {
				onAckPostReceived(msg);
				
			} else if (type.equals(MessageType.GET_POSTS)) {// Retrieve data
				onGetPostsReceived(msg);
				
			} else if (type.equals(MessageType.SEND_TEXT)) {
				onPostReceived(msg);
				
			} else if (type.equals(MessageType.SEND_STATE)) {
				onSendStateReceived(msg);
				
			} else if (type.equals(MessageType.GET_STATE)) {
				onGetStateReceived(msg);
				
			} else {
				Log.e(this.getClass().toString(), this.unexpectedMsg);
			}
		}

	}
	
	@Override
	public void onSendFailed(NetworkMessage message) {
		Post post = this.outgoingPosts.get(message);
		if (post != null) {
			numFailed += 1;
			this.outgoingPosts.remove(post);
			this.userHandler.onPostReceived(new Post(post.getId()+numFailed, post.getPoster(), post.getWallOwner(), post.getText()+" - Delivery Failed", post.getImage(), post.getDateTime()));
		}
	}

	@Override
	public void onSendSucceeded(NetworkMessage message) {
		Post post = this.outgoingPosts.get(message);
		if (post != null) {
			postLocally(post);
			this.outgoingPosts.remove(post);
		}
	}
	
	////
	//IMPLEMENTATION
	////
	
	private void onHeaderReceived(PublicHeader header) {
		if (header.getConsistency() == StatusByte.CONNECT.getByte()) {
			this.isConnected = true;
		}
		if (header.getConsistency() == StatusByte.DISCONNECT.getByte()) {
			this.isConnected = false;
		}
	}

	private void onPostReceived(Message msg) {
		if (!msg.sender.equals(localUser.getId())) {
			Post post = new Post(msg);
			Log.d(this.getClass().toString(), "post : " + post.toString());
			postLocally(post);
		}
	}
	
	private void onAckPostReceived(Message msg) {
		//for the moment, the model ensures that the post will be posted, thus
		//it can be silently ignored
	}
	
	//This gets called if some user wants to get the wall of the local user
	//the msg specifies what the last seen id is.
	private void onGetPostsReceived(Message msg) {
		List<Post> listPosts = database.getAllFriendPostsFrom(localUser.getId(), msg.getId());
		for (Post post : listPosts){
			Message postMessage = MessageFactory.newPostMessage(post, true);
			byte[] msgTxt = JSONObjectFactory.createJSONObject(postMessage).toString().getBytes();
			PublicHeader header = new PublicHeader(0, (short)msgTxt.length, StatusByte.SEND.getByte(), post.getId(), localUser.getId(), msg.getSender());
			
			ByteBuffer assembledMessage = ByteBuffer.allocate(msgTxt.length + postMessage.getPayload().length);
			assembledMessage.put(msgTxt);
			assembledMessage.put(postMessage.getPayload());
			
			NetworkMessage networkMsg = new NetworkMessage(assembledMessage.array(), header);
			secureChannel.sendMessage(networkMsg);
		}
	}
	
	private void onSendStateReceived(Message msg) {
		//Someone send his / her state, so retrieve data from the message
		UserId friendId = msg.getSender();
		int numMsgs = msg.getNumM();
		int maxPostId = msg.getId();
		
		//ask to have posts that local user doesn't have in database
		int oldMaxPostId = database.getFriendMaxPostsId(friendId);
		int oldNumMsgs = database.getFriendPostsCount(friendId);
		
		Log.d(this.getClass().toString(), "maxId:"+ oldMaxPostId + ", count:" + oldNumMsgs);
		
		if (oldNumMsgs < numMsgs) {
			//TODO think about if this really guarantees that no messages are lost
			Message msgToSend = MessageFactory.newGetPostsMessage(oldMaxPostId, localUser.getId(), msg.getSender());
			PublicHeader header = new PublicHeader(0, null, StatusByte.SEND.getByte(), 0, localUser.getId(), friendId);
			NetworkMessage networkMessage = new NetworkMessage(JSONObjectFactory.createJSONObject(msgToSend).toString(), header);
			secureChannel.sendMessage(networkMessage);
		}
		
	}
	
	private void onGetStateReceived(Message msg) {
		
		Log.d(this.getClass().toString(), "onGetStateReceived");
		
		//Someone wants user state, so retrieve data from database
		int maxPostId = database.getUserMaxPostsId();
		int maxNumMsgs = database.getUserPostsCount();
		
		//create message encapsulating all informations
		UserId userToSend = msg.getSender();
		String msgToSend = JSONObjectFactory.createJSONObject(MessageFactory.newSendStateMessage(localUser.getId(), userToSend, maxPostId, maxNumMsgs), maxNumMsgs).toString();
		PublicHeader header = new PublicHeader(0, null, StatusByte.SEND.getByte(), 0, localUser.getId(), userToSend);
		
		//send reply
		secureChannel.sendMessage(new NetworkMessage(msgToSend, header));
	}
	
	
	///
	//ASYNCHRONOUS DATABASE QUERIES
	///
	
	private class AsyncGetSomeLatestPostsDatabaseQuery extends AsyncTask<UserId, Void, List<Post>> {

		public final int numberOfPosts;
		public final int postId;
		
		public AsyncGetSomeLatestPostsDatabaseQuery(int numberOfPosts, int postId) {
			this.numberOfPosts = numberOfPosts;
			this.postId = postId;
		}
		
		@Override
		protected List<Post> doInBackground(UserId...  userId) {
			
			List<Post> listPosts = database.getSomeLatestPosts(userId[0], numberOfPosts, postId);
			
			return listPosts;
		}
		
		@Override
		protected void onPostExecute(List<Post> listPosts) {
			for (Post p : listPosts) {
				userHandler.onPostReceived(p);
			}
		}
		
	}
	
}
