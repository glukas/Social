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
 *  Length, length of the message including the header (32 bits)
 *  Reserved, are three bytes reserved for futurw use (24 bits)
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
 *  NAME1 :: VALUE1
 *  NAME2 :: VALUE2
 *  and so on.
 *  
 *  Where:
 *  
 *  TEXT is a text (String format)
 *  LINK is a http link usable to download pictures in a future time (String format)
 *  INT is an integer
 *  USERNAME is an user name (String format)
 *  
 *  and where names and values in lower case are strings
 *  
 *  connection message (to server):
 *  	cmd :: connect
 *  
 *  disconnection message (from server):
 *  	cmd :: disconnect
 *  
 *  accept friendship message:
 *  	cmd :: replyFriendship
 *  	response :: accept
 *  
 *  refuse friendship message:
 *  	cmd :: replyFriendship
 *  	response :: reject
 *  
 *  demand friendship message:
 *  	cmd :: askFriendship
 *  	from :: USERNAME
 *  
 *  get someone's posts
 *  	cmd :: getPosts
 *  	id :: INT // not needed here right?
 *  
 *  get someone's current state
 *  	cmd :: getState
 *  
 *  send current state
 *  	cmd :: sendState
 *  	id :: INT
 *  	numMessages :: INT
 *  
 *  post text message on someone's wall:
 *  	cmd :: postText
 *  	id :: INT
 *  	text :: TEXT
 *  
 *  post picture message on someone's wall:
 *  	cmd :: postPicture
 *  	id :: INT
 *  	text :: TEXT
 *  	picture :: LINK
 *  
 *  send a text message to someone:
 *  	cmd :: sendText
 *  	id :: INT
 *  	text :: TEXT
 *  
 *  send a picture message to someone:
 *  	cmd :: sendPicture
 *  	id :: INT
 *  	text :: TEXT
 *  	picture :: LINK
 *  
 *  broadcast user's name:
 *  	cmd :: broadcast
 *  	user :: USERNAME
 *  
 *  acknowledge a post:
 *  	cmd :: ack
 *  	id :: INT
 *  
 */

package ch.ethz.inf.vs.android.glukas.project4.protocol;
