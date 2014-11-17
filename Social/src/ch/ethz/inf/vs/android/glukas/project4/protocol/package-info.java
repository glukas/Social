/**
 * Protocol Goals :
 * 
 * Abstract :
 * 
 * The purpose of this package is to make the link between networking, user interface, database and security.
 * It's the main logic of the project; it's in charge of retrieve data from the distributed network created
 * by the other users. Furthermore, it relays data asked by the network to be stored in/retrieved from its own 
 * database. Everything should be encrypted, i.e. data can not be accessed without identification.
 * 
 * Local Requirements :
 * 
 * 	(I) The database should not increase more than a given size defined by the user.
 * 
 * 	(II) If data stored on local copy not belongs to a friend, user shouldn't be able to read it.
 * 
 * Global Requirements :
 * 
 * 	(I) User should be able to access his / her friends' data with a highly percentage. (we defined this as
 *  >95%, but not that there's a trivial solution where it's not possible : everyone is never connected. The whole
 *  principle is based on the fact that people are more and more connected at every time.)
 *  
 *  (II) User should be able to make new friends. This can be done by physical proximity or by trusting
 *  a common friend.
 *  
 *  Non-functional Requirements :
 *  
 *  (I) Each user updates a clock which gives an identity at all his / her messages and create a partial
 *  order to sort messages.
 *  
 *  
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
 *  cmd :: connect
 *  
 *  disconnection message (from server):
 *  cmd :: disconnect
 *  
 *  accept friendship message:
 *  cmd :: replyFriendship
 *  response :: accept
 *  target :: USERNAME
 *  
 *  refuse friendship message:
 *  
 *  cmd :: replyFriendship
 *  response :: reject
 *  target :: USERNAME
 *  
 *  demand friendship message:
 *  
 *  cmd :: askFriendship
 *  target :: USERNAME
 *  
 *  get someone's wall:
 *  
 *  cmd :: getWall
 *  target :: USERNAME
 *  
 *  post text message on someone's wall:
 *  
 *  cmd :: postText
 *  target :: USERNAME
 *  id :: INT
 *  text :: TEXT
 *  
 *  post picture message on someone's wall:
 *  
 *  cmd :: postPicture
 *  target :: USERNAME
 *  id :: INT
 *  text :: TEXT
 *  picture :: LINK
 *  
 *  send a text message to someone:
 *  
 *  cmd :: sendText
 *  target :: USERNAME
 *  owner :: USERNAME
 *  id :: INT
 *  text :: TEXT
 *  
 *  send a picture message to someone:
 *  
 *  cmd :: sendPicture
 *  target :: USERNAME
 *  owner :: USERNAME
 *  id :: INT
 *  text :: TEXT
 *  picture :: LINK
 *  
 */

package ch.ethz.inf.vs.android.glukas.project4.protocol;
