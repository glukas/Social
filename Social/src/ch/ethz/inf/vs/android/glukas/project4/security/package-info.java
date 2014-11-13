/**
* Security Goals:
*
* (I) Peers can establish a shared secret when in physical proximity.
* The key agreement happens over a short-ranged channel, and it is reasonable to assume a passive adversary model.
* It may be desirable to provide the possibility for ambitious users to explicitly compare their keys.
* It may be desirable if this comparison does not need to work over the full key length,
* although this would increase the demands on the protocol to avoid collision attacks.
*
* (II) Peers can use a commonly trusted peer to establish a shared secret over the Internet.
* This form of key agreement must be robust against an active adversary.
* In particular the newly established key must be fresh.
* It is highly desirable that a single compromised friend is not enough for the exchange to become insecure.
*
* (III) Peers can exchange messages in a way that provides chosen cyphertext level security and authenticated communication. It is possible to broadcast a message to all friends by encrypting it only once.
* This is especially urgent to allow for efficient caching of messages.
* It is highly desirable that even if a device is compromised at a later point, previous communication remains secured.

* Security Protocols:
* 
* (I) Key exchange in physical proximity:
* 	  DH-Key agreement protocol.
* 	  To provide a way to compare keys, a one way function of them is exposed to the user.
* 
* (II) A peer maintains a set of trusted friends.(TODO: decide strategy on how to choose them:)
* 	   - The first few friends a person makes are the trusted friends (->simple and makes some sense)
*      - All friends established using (I) are trusted friends (->as we have met in person, in practice similar to first strategy)
*      
* 	   Any peer A can get a list of friends-of-a-friend from any of the trusted peers.
* 	   Say A trusts T, then A can request a fresh list of friends of T:
* 	   
* 	   [A shares a secret key KAT with T]
* 
* 	   Discovery 1) A -> T : {"list of friends", N_A}_KAT [fresh N_A]
* 	   Discovery 2) T -> A : {T, (B_0, ..., B_n), N_A}_KAT
*
*	   [A holds a fresh list of friends of T]
*
* 	   Now A wants to establish a shared secret with B,
* 	   where B has been reported by T as a friend by means of the Discovery protocol.
* 	   The Exchange protocol is based on the Needham-Shroeder protocol with a fix to avoid replay attacks (attack by Lowe).
* 	   Note that the situation is more tricky, as all peers can act as T.
* 	   The identity of the trusted common friend T is included in the messages to avoid possible exploits of this new symmetry.
* 
* 	   (TODO: we may want to include the broadcast keys in the last two messages for efficiency/simplicity) 
* 
*      Version 1:
* 
* 	   [A shares a secret key KAT with T,
* 		B shares a secret key KBT with T]
*  
* 	   Exchange 1) A -> B : A, T
* 	   Exchange 2) B -> A : {A, T, N_B'}_KBT (if B wants to friend A) [fresh N_B']
*	   Exchange 3) A -> T : A, B, N_A, {A, T, N_B'}_KBT [fresh N_A]
*	   Exchange 4) T -> A : {N_A, KAB, B, T {KAB, A, T, N_B'}_KBT}_KAT [fresh KAB]
*	   Exchange 5) A -> B : {KAB, A, T, N_B'}_KBT
*	   Exchange 6) B -> A : {N_B}_KAB [fresh N_B]
*	   Exchange 7) A -> B : {N_B - 1}_KAB
*
*
*	   [A and B share a fresh secret key KAB]
*
*	   
*
*		Version 2: (TODO draft, designed to be more practical for our setting, but still have to consider it rigorously, also not sure if that much more practical)
*
* 	   [A shares a secret key KAT with T,
* 		B shares a secret key KBT with T]
*  
* 	   Friend request)  A -> B : T, {"1", B, T, N_A}_KAT [fresh N_A]
*	   Key request )  	B -> T : {"1", B, T, N_A}_KAT, {"2", A, T, KAB}_KBT  (if B wants to friend A) [fresh KAB]
*	   Key response )   T -> B : {"3a", A, T, KAB {"3b", B, T, N_A, KAB}_KAT}_KBT
* 	   Friend accept )  B -> A : {"3b", B, T, N_A, KAB}_KAT
*	   Friend ack )     A -> B : {"4", KAB}_KAB
*
*	   [A and B share a fresh secret key KAB]
*
*
* (III) (a) Broadcast keys: 
*       Peers distribute broadcast keys that are used for broadcasts. Posts that are broadcast are secured under these keys.
*		
*		Broadcast key request protocol:
*
*		Say A wants to get B's current ephemeral key:
*		[A and B share secret key KAB,
*		B has current ephemeral key KB]
*
*		A -> B : {"key request", N_A}_KAB [fresh N_A]
*		B -> A : {"key response", N_A, KB}_KAB
*
*		[A has B's current ephemeral key KB]
*
*		Note that the broadcast key protocol does not provide forward secrecy, as someone that has KAB can get KB.
*      	Advanced (nice to have extension of broadcast key mechanism):
*      			When a peer A removes a friend B, A generates a fresh broadcast key.
*      			This ensures that B can only see the old posts that where made before removal.		
*
*      	(b) Posts:
*       
*       As already mentioned, posts are encrypted using the broadcast keys. This allows a peer to encrypt its posts only once for all friends.
*       Posts should have unique identifiers to avoid replay (this is probably already the case in the application protocol).
*       
*       (c) General Message Transmission:
*       
*       In the protocols above, we have assumed the existence of a secure private key message transmission mechanism.
*       We can use an encrypt-then-authenticate approach using the first and second half of the keys for encryption and authentication respectively.
*       Using this approach with a CPA-secure private key encryption scheme and a secure MAC with unique tags gives CCA secure and authenticated communication. [Introduction to Modern Cryptography, Katz/Lindell, Theorem 4.25] 
*       Note that this means that all keys have to be twice as long as if only encryption would be used.
**/

package ch.ethz.inf.vs.android.glukas.project4.security;
