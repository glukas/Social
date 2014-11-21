/**
 * Implementation of the server intended to run on a Raspberry Pi!
 * 
 * Since the R-Pi has only a small single core processor, this server is not designed as Thread-per-Connection, but as a NIO server implementation.
 * Currently the R-Pi is set up and reachable under the address 'winti.mooo.com:9000' (DynDNS)
 * 
 * Main:
 * 		Main class to start/kill the server
 * 
 * Message:
 * 		Representation of the data sent to the server, according to the public header.
 * 
 * MessageBuffer:
 * 		Stores (currently) all received messages per user. Every User has an UserMessageQueue with all messages addressed to him/her
 * 
 * UserMessageQueue:
 * 		PriorityQueue of all messages of one recipient
 * 
 * Server:
 * 		the non-blocking server implementation
 * 		ChangeRequest: 	class to initiate change of behaviour of the server loop, for example to change from receive to send
 * 		Status:			All possible status of messages get defined here
 * 
 * ServerEvent:
 * 		Representation of an server interaction with Message, reference to Server and corresponding socket
 * 
 * ConnectionWorker:
 * 		Handles the incoming messages and decides what to do with them
 * 
 * TODO
 * 		- Persistent mapping of User <--> Socket/Address
 * 		- Add more workers/threads to process data
 * 		- Add status
 * 		- ...
 * 
 */
/**
 * @author mathiasbirrer
 *
 */
package ch.ethz.inf.vs.server;