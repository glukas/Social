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
 * -for future use [3 bytes]
 * -status byte [1 byte]
 * -message id / "virtual clock" [4 bytes]
 * -sender [16 bytes]
 * -recipient [16 bytes]
 * 
 * The sender is a unique id of the sender.
 * The recipient is the unique id of the recipient, or 0 the message is is a broadcast.
 * The pair (sender, message, id) is unique.
 * 
 * Security Protocols:
 * 
 * (Overview)
 *       On first application start, every peer generates a private broadcast key.
 *       A private broadcast key KA is of the form (KSim, (SKsign, SKverify), (SKdec, PKenc))
 *       A public broadcast key KA is of the form (KSim, PKverify, PKenc))
 *
 *  	 KSim is a symmetric key pair (K1, K2) where K1 is used for encryption and K2 for MACs.
 * 		 The keys (SKsign, SKverify) allow signatures, and (SKdec, PKenc) allows public key cryptography.
 * 		 To establish a friendship, peers exchange their public broadcast keys.
 * 		 Broadcast keys are used to encrypt the posts. They are also used to secure other protocol messages.
 * 
         Public-key encryption is done using RSA-OAEP (OEAP is a particular randomized padding)
 *       Public-key signatures are done using RSA-SHA256 ("Hash and Sign")
 *       The public keys are 2^11 bits (so a key pair has size 2^12 and both pairs have size 2^13).
 * 
 * 		 Private key encryption is done by combining a AES-128 in CBC mode (PKCS7Padding padding) and a HMAC based on SHA256 in an "Encrypt-then-Authenticate" fashion.
 * 		 So a private key is 128 or 256 bits, and a key pair is 128+256 bits.
 * 
 * (I)   Public Key exchange in physical proximity:
 *  
 * 	     **Simple key exchange protocol**
 *		
 *       A and B exchange the public keys used for encryption and verification over the channel.
 *       We assume no active attacker has access to the channel.

 *      **Symmetric Key exchange using public keys**
 *       A sends her symmetric encryption key KsimA to B encrypted under B's public encryption key, and authenticated under B's public authentication key
 *       The same with reversed roles.
 *
 * (II) 
 * 	   **Friends-of-a-friend Protocol**
 *     
 *     A can request a fresh list of friends of T:
 * 	   
 * 	   [A has T's broadcast key KT
 * 		T has A's broadcast key KA]
 * 
 * 	   Discovery 1) A -> T : {("list of friends", N_A)}_KsimT [fresh N_A]
 * 	   Discovery 2) T -> A : {"my friends", T, (B_0, ..., B_n), N_A}_KsimT //where (B_0, ...., B_n) are the friends of T
 *
 *	   [A holds a fresh list of friends of T]
 *
 *     **Friend Protocol**
 *
 * 	   Now A wants to establish a shared secret with B,
 * 	   where B has been reported as a friend-of-a-friend by means of the Discovery protocol.
 * 
 * 	   [A, B have the broadcast keys of their common friends (T_1, ..., T_M),
 *     The common friends of A and B have the broadcast keys of A and B,
 * 	   A has broadcast key KA,
 * 	   B has broadcast key KB]
 *  
 * 	   Friend 1)  A -> B : Sign(SKsignA, ("friend", B)) //certificate that A wants to friend B
 *	   
 *	   If B wants to friend A:
 *
 *	   Friend 2)  B -> A : Sign(SKsignB, ("friend", A))  //certificate that B wants to friend A
 *
 *     Friend 3a, 3b) Both A and B perform the Public Key Voting Protocol to get their respective public keys
 *     Friend 4) Then they perform a symmetric key exchange using public keys.
 *	
 *	   [A has B's broadcast key KB,
 * 	    B has A's broadcast key KA]
 *
 *	   **Public Key Voting Protocol**
 *
 *     Say B has a certificate from A that A wants to friend B.
 *     B wants to get trustworthy votes on the public key PKA of A.
 *     A special case is where just a single friend is asked for the public key.
 *     
 *     [B has the broadcast keys of his common friends (T_1, ..., T_M) with A,
 *     The common friends of A and B have the broadcast keys of A and B,
 * 	   A has public key PKA]
 *     
 *     [fresh N_B]
 *     
 *	   For all the common friends T in (T_1,...,T_M) of A and B: 
 *
 *		   Voting 1)  B -> T : Sign(SKsignA, ("friend", B)), N_B		  //certificate that A wants to friend B
 *	   
 *		   Voting 2)  T -> B : Sign(SKSignT, "vote", A, PKA, N_B),   //certificate that T believes A's key is PKA
 *		  
 *	   [B chooses the majority vote on PKA]
 *
 * (III) 
 *   
 *  (a) Transmission of Protocol messages:
 *       
 *       In the some of the protocols above, we have assumed the existence of a secure symmetric key message transmission mechanism.
 *       We use an encrypt-then-authenticate approach.
 *       
 *       Say A want to send protocol message p with header h to B
 *       
 *       [A and B share a secret key KAB=(K1, K2)]
 *       
 *       A -> B : Enc(K1, p), MAC(K2, Enc(K1, h|p))
 *       
 *       [Claim: p is transmitted securely, meaning CPA-secure and authenticated]
 *       
 *   (b) Transmission of Posts:
 *   
 *       As already mentioned, posts are encrypted using the broadcast keys. This allows a peer to encrypt its posts once for all friends.
 *       The encryption/authentication is the same as in (b)
 *       

 **/

package ch.ethz.inf.vs.android.glukas.project4.security;
