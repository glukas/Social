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
 *  -----------------------------------------------
 *  | Sender  |  Receiver  |  Consistency  |  id  |
 *  -----------------------------------------------
 *  | /    /    /    /    /    /    /    /    /   |
 *  | /    /    /  CONTENT     /    /    /    /   |
 *  | /    /    /    /    /    /    /    /    /   |
 *  -----------------------------------------------  
 *  
 *  Where:
 *  
 *  Sender, id of the sender (128 bits)
 *  Receiver, id of the receiver (128 bits)
 *  Consistency, byte used for consistency (8 bits)
 *  id, not unique id of the message (32 bits)
 *  
 *  i.e. Server has identity 0
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
 *  	ack :: post
 *  	id :: INT
 *  
 */

package ch.ethz.inf.vs.android.glukas.project4.protocol;
