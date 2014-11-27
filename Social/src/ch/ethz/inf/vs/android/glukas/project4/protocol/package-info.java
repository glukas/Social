/**  
 *  Abstract :
 *  
 *  This package is the central part of the project (but not the main). It links every pieces together and
 *  ensures relay between user interface, database and security-network. It deals with consistency issues and
 *  data reachability.
 *  
 *  
 *  
 *  Packets Header :
 *  
 *  ------------------------------------------------------------------
 *  | Length  |  Reserved  |  Status  |  id  |  Sender  |  Receiver  |
 *  ------------------------------------------------------------------
 *  | /    /    /    /    /    /    /    /    /    /    /    /    /  |
 *  | /    /    /    /    /     CONTENT  /    /    /    /    /    /  |
 *  | /    /    /    /    /    /    /    /    /    /    /    /    /  |
 *  ------------------------------------------------------------------  
 *  
 *  Where:
 *  
 *  Total length, 352 bits. (Fixed)
 *  
 *  Length, length of the message including the header (32 bits)
 *  Reserved, are three bytes reserved for future use (24 bits)
 *  Status, is a byte describing the status of the message (8 bits)
 *  id, not unique id of the message (32 bits)
 *  Sender, id of the sender (128 bits)
 *  Receiver, id of the receiver (128 bits)
 *  
 *  i.e. Server has identity 0
 *  
 *  Status Byte :
 *  
 *  The status byte is used by the server to identify the action it has to provide to the message. There are
 *  five different kind of messages :
 *  
 *  Connect :
 *  
 *  The user wants to connect to the server. Sender is the id of the user to connect, receiver should be the
 *  id of the server (0). id is also 0.
 *  
 *  Disconnect :
 *  
 *  The user wants to disconnect from the server. Sender is the id of the user to disconnect, receiver
 *  should be the id of the server (0). id is also 0.
 *  
 *  Post :
 *  
 *  User wants to post a new message on someone's else wall. Server has to check if the user targeted by receiver
 *  field is connected. If it's the case, server has to forward the post to the targeted user. (It has to be sure
 *  that message was correctly delivered.) If the targeted user is not connected, then server has to cache the post.
 *  
 *  Send :
 *  
 *  User wants to send a message to someone. Server forward message to targeted user.
 *  
 *  Data :
 *  
 *  The user wants to retrieve some data. If the Sender = Receiver, then the user wants to get all posts that
 *  server has previously cached. Server has to send to the user all cached messages. (And, when ack, it can drop
 *  them.)
 *  
 *  
 *  JSON Protocol :
 *  
 *  This protocol is used to communicate with the server. Above there's the definition of the JSONObject
 *  in the form : 
 *  HEADER
 *  NAME1 :: VALUE1
 *  NAME2 :: VALUE2
 *  and so on.
 *  
 *  Where:
 *  
 *  ID is an integer
 *  USERNAME is a string
 *  NUM_M is an integer
 *  LINK is a string
 *  MESSAGE is a string
 *  HEADER is [header : length, reserved, status, msgId, senderId, receiverId]
 *  	with fields defined as above and
 *  	LENGTH, is the length of the message
 *  	RESERVED, are the three bytes reserved for future use
 *  	CONNECTION, is the connection status byte
 *  	DISCONNECTION, is the disconnection status byte
 *  	LOCAL_USER_ID, is the UserId of the local user
 *  	SERVER_ID, is the UserId of the server
 *  
 *  
 *  Note that for empty messages, only headers are important
 *  
 *  connection message (to server):
 *   	[header : LENGTH, RESERVED, CONNECTION, 0, LOCAL_USER_ID, SERVER_ID]
 *  
 *  disconnection message (from server):
 *  	[header : LENGTH, RESERVED, DISCONNECTION, 0, LOCAL_USER_ID, SERVER_ID]
 *  
 *  accept friendship message:
 *  	[header : LENGTH, RESERVED, SEND, 0, LOCAL_USER_ID, FRIEND_USER_ID]
 *  	cmd :: replyFriendship
 *  	response :: accept
 *  
 *  refuse friendship message:
 *  	[header : LENGTH, RESERVED, SEND, 0, LOCAL_USER_ID, FRIEND_USER_ID]
 *  	cmd :: replyFriendship
 *  	response :: reject
 *  
 *  demand friendship message:
 *  	[header : LENGTH, RESERVED, SEND, 0, LOCAL_USER_ID, FRIEND_USER_ID]
 *  	cmd :: askFriendship
 *  	from :: USERNAME
 *  
 *  get someone's posts
 *  	[header : LENGTH, RESERVED, DATA, 0, LOCAL_USER_ID, FRIEND_USER_ID]
 *  	cmd :: getPosts
 *  	id :: ID
 *  
 *  get someone's current state
 *  	[header : LENGTH, RESERVED, DATA, 0, LOCAL_USER_ID, FRIEND_USER_ID]
 *  	cmd :: getState
 *  
 *  send current state
 *  	[header : LENGTH, RESERVED, SEND, 0, LOCAL_USER_ID, FRIEND_USER_ID]
 *  	cmd :: sendState
 *  	id :: ID
 *  	numMessages :: NUM_M
 *  
 *  post text message on someone's wall:
 *  	[header : LENGTH, RESERVED, POST, ID, LOCAL_USER_ID, FRIEND_USER_ID]
 *  	cmd :: postText
 *  	id :: ID
 *  	text :: MESSAGE
 *  
 *  post picture message on someone's wall:
 *  	[header : LENGTH, RESERVED, POST, ID, LOCAL_USER_ID, FRIEND_USER_ID]
 *  	cmd :: postPicture
 *  	id :: ID
 *  	text :: MESSAGE
 *  	picture :: LINK
 *  
 *  send a text message to someone:
 *  	[header : LENGTH, RESERVED, SEND, ID, LOCAL_USER_ID, FRIEND_USER_ID]
 *  	cmd :: sendText
 *  	id :: ID
 *  	text :: MESSAGE
 *  
 *  send a picture message to someone:
 *  	[header : LENGTH, RESERVED, SEND, ID, LOCAL_USER_ID, FRIEND_USER_ID]
 *  	cmd :: sendPicture
 *  	id :: ID
 *  	text :: MESSAGE
 *  	picture :: LINK
 *  
 *  acknowledge a post:
 *  	[header : LENGTH, RESERVED, SEND, ID, LOCAL_USER_ID, FRIEND_USER_ID]
 *  	cmd :: ack
 *  	id :: ID
 *  
 *  server acknowledge a message to server
 * 		[header : LENGTH, RESERVED, CONNECT OR DISCONNECT, 0, SERVER_ID, LOCAL_USER_ID]
 *  
 */

package ch.ethz.inf.vs.android.glukas.project4.protocol;
