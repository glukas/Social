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
* (III) Peers can exchange messages in a way that provides chosen cyphertext level security.
* In addition to encryption, message authenticity and freshness should be provided.
* It is highly desirable that even if a device is compromised at a later point, previous communication remains secured.
* This necessitates additional ephemeral keys.
* The precise life span of short lived keys depends on the pattern of communication.
*
*
* Security Protocols:
* 
* (I) Key exchange in physical proximity:
* 	  DH-Key agreement protocol.
* 	  To provide a way to compare keys, a one way function of them is exposed to the user.
* 
* (II) When the number of friends of a peer A changes a random subset of friend is chosen to be trusted.
* 	   Any peer A can get a list of friends-of-a-friend from any of the trusted peers.
* 
* 	   Say A trusts T, then A can request a fresh list of friends of T:
* 	   
* 	   [A shares a secret key KAT with T]
* 
* 	   Discovery 1) A -> T : {"list of friends", N_A}_KAT [fresh N_A]
* 	   Discovery 2) T -> A : { (B_0, ..., B_n}, N_A}_KAT
*
*	   [A holds a fresh list of friends of T]
*
* 	   Now A wants to establish a shared secret with B,
* 	   where B has been reported by T as a friend by means of the Discovery protocol.
* 	   The Exchange protocol is based on the Needham-Shroeder protocol with a fix to avoid replay attacks (attack by Lowe).
* 
* 	   [A shares a secret key KAT with T,
* 		B shares a secret key KBT with T]
*  
* 	   Exchange 1) A -> B : A, T
* 	   Exchange 2) B -> A : {A, T, N_B'}_KBT (if B wants to friend A) [fresh N_B']
*	   Exchange 3) A -> T : A, B, N_A, {A, T, N_B'}_KBT [fresh N_A]
*	   Exchange 4) T -> A : {N_A, KAB, B, {KAB, A, N_B'}_KBT}_KAT [fresh KAB]
*	   Exchange 5) A -> B : {KAB, A, N_B'}_KBT
*	   Exchange 6) B -> A : {N_B}_KAB [fresh N_B]
*	   Exchange 7) A -> B : {N_B - 1}_KAB
*
*	   [A and B share a fresh secret key KAB]
*
**/

package ch.ethz.inf.vs.android.glukas.project4.security;
