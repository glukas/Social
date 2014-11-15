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
 */

package ch.ethz.inf.vs.android.glukas.project4.protocol;
