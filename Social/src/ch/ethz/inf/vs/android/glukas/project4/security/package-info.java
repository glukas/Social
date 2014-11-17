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
 * If time is tight, we can drop (II) - it is a very useful feature but not completely essential.
 *
 * (III) Peers can exchange messages in a way that provides chosen cyphertext level security and authenticated communication. It is possible to broadcast a message to all friends by encrypting it only once.
 * This is especially urgent to allow for efficient caching of messages.
 * It is highly desirable that even if a device is compromised at a later point, previous communication remains secured. This is an ambitious goal and may be out of scope.
 * 
 * 
 * Public Headers:
 * 
 * Messages have the following public header: (TODO this is a draft)
 * 
 * -length of the message in bytes, including this header [4 bytes]
 * -message id / "virtual clock" [4 bytes]
 * -number of milliseconds that have elapsed between midnight, January 1, 1970
 *  and the time the message was authored. [8 bytes] (TODO do we really need this in the public header, or
 *  												  can we base the application protocol on the virtual clocks?)
 * -sender [16 bytes]
 * -recipient [16 bytes]
 * 
 * The sender is a unique id of the sender.
 * The recipient is the unique id of the recipient, or 0 the message is is a broadcast.
 * The pair (sender, message id) is unique.
 * 
 * Security Protocols:
 * 
 * (Overview)
 *       On first application start, every peer generates a secret broadcast key.
 * 		 This key consists of a public/private key pair to allow signing, and two symmetric keys for encryption and MACs respectively.
 * 		 The public broadcast key is the private broadcast key without the private key used for signing.
 * 		 To establish a friendship, peers exchange their public broadcast keys, and establish a new shared secret between them. The shared
 * 		 secret is a symmetric key pair (K1, K2) where K1 is used for encryption, and K2 for message authentication.
 * 		 Broadcast keys are used to encrypt the posts.
 * 		 The shared secret is used to secure other protocol messages.
 * 
 * (I)   Key exchange in physical proximity:
 *  
 *   (a) DH-Key agreement protocol.
 * 	     To provide a way to compare keys, a one way function of them is exposed to the user.
 * 	     After a shared secret key has been established by DH, the participants use the broadcast key request protocol
 * 	     
 * 	 (b) Broadcast key request protocol:
 
 *	  	 Say A wants to get B's current broadcast key:
 *	  	 [A and B share secret key KAB,
 *	 	 B has current broadcast key KB]
 *	
 *	  	 A -> B : {"key request", A, N_A}_KAB [fresh N_A]
 *	     B -> A : {"key response", N_A, KB}_KAB
 *
 *	     [A has B's current broadcast key KB]
 *
 *	     Note that the broadcast key protocol does not provide forward secrecy, as someone that has KAB can get KB.
 *    	 Advanced (nice to have extension of broadcast key mechanism):
 *      			When a peer A removes a friend B, A generates a fresh broadcast key, and distributes it.
 *      			This ensures that B can only see the old posts that where made before removal.		

 * 
 * (II) A peer maintains a set of trusted friends.
 * 	   //- The first few friends a person makes are the trusted friends (->simple and makes some sense)
 *      //- All friends established using (I) are trusted friends (->as we have met in person, in practice similar to first strategy)
 *      - All friends are trusted (easiest strategy giving highest availability, we can start out using this strategy)
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

 *	   (TODO : draft, designed to be more practical for our setting, but still have to consider it rigorously)
 *	   Incorporates the exchange of the broadcast keys.
 *
 * 	   [A shares a secret key KAT with T,
 * 		B shares a secret key KBT with T,
 * 		A has broadcast key KA,
 * 		B has broadcast key KB]
 *  
 * 	   Friend 1)  A -> B : T, {"1", B, T, N_A, K_A}_KAT [fresh N_A]
 *	   Friend 2)  B -> T : {"1", B, T, N_A, K_A}_KAT, {"2", A, T, K_AB}_KBT  (if B wants to friend A) [fresh K_AB]
 *	   Friend 3)  T -> B : {"3a", A, T, K_AB, K_A}_KBT {"3b", B, T, K_AB, N_A, K_B}_KAT (B can start looking at A's posts even if A is unavailable)
 * 	   Friend 4)  B -> A : {"3b", B, T, K_AB, N_A, K_B}_KAT (A can start looking at B's post)
 *	   Friend 5)  A -> B : {"4", B}_KAB (if B doesn't receive this message during some interval,
 *										B decides that the protocol failed and stops looking at A's posts)
 *
 *	   [A and B share a fresh secret key KAB,
 *	   A has B's broadcast key KB,
 * 	   B has A's broadcast key KA]
 *
 *	   Note that if an adversary blocks message 4, then A cannot distinguish whether B has ignored the friend request or the protocol failed.
 *	   This is probably not a dangerous attack though.
 *	   Also note that if it is not security relevant that A and B have a consistent view on the state of their friendship, the last message can be dropped entirely.
 *
 *
 *	   Alternative (designed for higher availability and tries to avoid that B can be fooled into thinking A wants to be 
 *					his friend by a malicious friend T of B):
 *
 *     Note again that if all friends were to be trusted, any malicious friend could force B to use it in step 2,
 *     enabling it to fool B.
 *
 *    [A shares a secret key KAT with T,
 * 	   B shares a secret key KBT with T,
 * 	   A has broadcast key KA=(KEncA, KAuthA, (SKA, PKA)),
 * 	   B has broadcast key KB]
 *  
 * 	   Friend 1)  A -> B : Sign(SKA, {"Friend 1", B, N_A}_KA) [fresh N_A]
 *	   Friend 2)  B -> (T_1, ..., T_M) : Sign(SKA, {"1", B, N_A}_KA), Sign(SKB, {"1", A, N_B}_KB) (if B wants to friend A) [fresh N_B, for the common friends (T_1,...T_M)]
 *	   Friend 3)  T -> B : {"3a", A, N_B, KA}_KBT {"3b", B, N_A, KB}_KAT
 *																				(B chooses the majority vote on KA)
 * 	   Friend 4)  B -> A : {"3b", B, N_A, K_B}_KAT (for all T in (T_1,...,T_M))
 * 																			    (A chooses the majority vote on KB)
 *	   Friend 5)  A -> B : {"4", B}_KAB (if B doesn't receive this message during some interval,
 *										B decides that the protocol failed and stops looking at A's posts)
 *
 *	   [A has B's broadcast key KB,
 * 	    B has A's broadcast key KA]
 * 
 *     Version 3 ("Relaxed-Consensus on the public keys")
 * 
 * 	   [A, B have all the broadcast keys of their common friends (T_1, ..., T_M),
 *     The common friends of A and B have the broadcast keys of A and B,
 * 	   A has broadcast key KA,
 * 	   B has broadcast key KB]
 *  
 *     A public broadcast key KA is of the form (KSim, PKverify, PKenc))
 *     A private broadcast key KA is of the form (KSim, (SKsign, SKverify), (SKdec, PKenc))
 *  
 * 	   Friend 1)  A -> B : Sign(SKsignA, {"Friend 1", B, N_A}_KSimA) [fresh N_A]  //certificate stating that A wants to friend B
 *	   
 *	   If B wants to friend A:
 *
 *	   Friend 2)  B -> A : Sign(SKsignB, {"1", A, N_B}_KsimB) [fresh N_B] //certificate that B wants to friend A
 *
 *     Both A and B perform the public key voting protocol to get their respective public keys
 *	
 *	   [A has B's broadcast key KB,
 * 	    B has A's broadcast key KA]
 *
 *
 *	   Public Key Voting Protocol:
 *
 *     Say B has a certificate from A that A wants to friend B.
 *     B wants to get trustworthy votes on the public key of A.
 *     
 *     [A, B have all the broadcast keys of their common friends (T_1, ..., T_M),
 *     The common friends of A and B have the broadcast keys of A and B,
 * 	   A has broadcast key KA,
 * 	   B has broadcast key KB]
 *     
 *	   For all the common friends T in (T_1,...,T_M) of A and B:
 *
 *		   Voting 1)  B -> T : Sign(SKsignA, {"1", B, N_A}_KsimA),			  //certificate that A wants to friend B
 *	   
 *		   Voting 2)  T -> B : Sign(SKSignT, PEnc(PKencB, "vote", A, N_B, KA)), //certificate that T believes A's key is KA, encrypted under B's public key
 *		  
 *	   [B chooses the majority vote on KA]
 *
 *
 * (III) 
 * 
 * 	 (TODO: specify if the public protocol header is also authenticated - probably not, as the security implications are not clear)
 *	 (a) Transmission of Protocol messages:
 *       
 *       In the protocols above, we have assumed the existence of a secure symmetric key message transmission mechanism.
 *       We use an encrypt-then-authenticate approach.
 *       Using this approach with a CPA-secure private key encryption scheme and a secure MAC with unique tags gives CCA secure and authenticated communication.
 *       [Introduction to Modern Cryptography, Katz/Lindell, Theorem 4.25] 
 *       
 *       Say A want to send protocol message p to B
 *       
 *       [A and B share a secret key KAB=(K1, K2)]
 *       
 *       A -> B : Enc(K1, p), MAC(K2, Enc(K1, p))
 *       
 *       [p is transmitted securely, meaning CCA-secure and authenticated]
 *       
 *   (b) Transmission of Posts:
 *       
 *       As already mentioned, posts are encrypted using the broadcast keys. This allows a peer to encrypt its posts once for all friends.
 *       The method is similar to (b), but we additionally sign the MACs:
 *       The signatures are necessary because the MAC can be generated by anyone who has the public broadcast key for A: this
 *       includes all friends of A. We do not want to sign the whole message though, because public key cryptography is slow. Thus
 *       we sign the MACs, once for the author of the post, and once for the owner of the wall.
 *       
 *       So we do "Encrypt-Authenticate-Sign":
 *       
 *       Say A wants to post m to B's wall, and B has friends (B_0, ..., B_N)
 *       Posts must have unique identifiers, and include the owner of the wall and author of the post.
 *       Note that A=B is possible, in which case A->B is trivial.
 *       
 *       [A has private broadcast key ((SKA,PKA), KAEnc, KAAuth),
 *        B has private broadcast key ((SKB,PKB), KBEnc, KBAuth),
 *        A has B's public broadcast key (PKB, KBEnc, KBAuth),
 *        B has A's public broadcast key (PKA, KAEnc, KAAuth),
 *       
 *       A -> B : Enc(KBEnc, m), Sign(SKA, MAC(KBAuth, Enc(KBEnc, m)))
 *       B -> (B_0, ...., B_N) :  Enc(KBEnc, m), Sign(SKA, MAC(KBAuth, Enc(KBEnc, m))), Sign(SKB, MAC(KBAuth, Enc(KBEnc, m)))
 *       
 *     
 *       

 **/

package ch.ethz.inf.vs.android.glukas.project4.security;
