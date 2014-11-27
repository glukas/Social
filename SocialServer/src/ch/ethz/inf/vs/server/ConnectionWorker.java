package ch.ethz.inf.vs.server;

import java.math.BigInteger;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import ch.ethz.inf.vs.android.glukas.project4.UserId;
import ch.ethz.inf.vs.server.MessageBuffer;

public class ConnectionWorker implements Runnable {
	  private List<ServerEvent> queue = new LinkedList<ServerEvent>();
	  private HashMap<BigInteger, SocketChannel> connectedUsers = new HashMap<BigInteger, SocketChannel>();
	  private MessageBuffer cache = new MessageBuffer();
	  
	  public void processData(Server server, SocketChannel socket, byte[] data, int count) {
		  //Create Message out of the byte array
		  Message received = new Message(data);
		  System.out.println("Received data has size: " + data.length + " bytes");
		  System.out.println("Status: " + Integer.toHexString(received.getHeader().getConsistency()));
		  String s = new String(received.getMessage(), StandardCharsets.UTF_8);

		  System.out.println("Server read: " + s);

		  synchronized(queue) {
			  queue.add(new ServerEvent(server, socket, received));
			  queue.notify();
		  }
	  }
	  
	  private boolean isConnected(UserId user){
		  return connectedUsers.containsKey(user.getId());
	  }

	  public void run() {
		  ServerEvent dataEvent;

		  while(true) {
	      // Wait for data to become available
	      synchronized(queue) {
	        while(queue.isEmpty()) {
	          try {
	            queue.wait();
	          } catch (InterruptedException e) {
	          }
	        }
	        dataEvent = (ServerEvent) queue.remove(0);
	      }
	      
	      UserId sender = dataEvent.message.getHeader().getSender();
	      UserId receiver = dataEvent.message.getHeader().getReceiver();
	      
	      //Choose what to do, based on the status of the message
	      // TODO Add all possible status
	      System.out.println("Handle message");
	      //StatusByte s = StatusByte.constructStatusByte(dataEvent.message.getHeader().getConsistency());
	      byte b = dataEvent.message.getHeader().getConsistency();
	      System.out.println("Server handles status: " + Integer.toHexString(b));
	      switch(b){
	      	case 0x00:
	      		//CONNECT
	      		//save the socket in the connectedUsers HashMap
	      		System.out.println("Connected User: " + sender.getId().toString());
	      		connectedUsers.put(sender.getId(), dataEvent.socket);
	      		break;
	      	case 0x01:
	      		//DISCONNECT
	      		//remove the user from connectedUsers
	      		System.out.println("Deconnect User: " + sender.getId().toString());
	      		connectedUsers.remove(sender.getId());
	      		break;
	      	case 0x02:
	      		//DATA
	      		//Send the data to the user
	      		//TODO For now we store all messages, late we can drop them if send was successful
	      		System.out.println("DATA request from User: " + sender.getId().toString());
	      		List<Message> data = cache.getMessagesSince(receiver, 0);
	      		if(isConnected(sender)){
	      			SocketChannel socket = connectedUsers.get(sender.getId());
	      			//Send all messages
	      			for(Message m : data){
	      				dataEvent.server.send(socket, m);
	      			}
	      		}
	      		
	      		break;
	      	case 0x03:
	      		//POST
	      		System.out.println("POST request from User: " + sender.getId().toString());
	      		if(isConnected(receiver)){
	      			//User is online, forward Post
	      			SocketChannel socket = connectedUsers.get(receiver.getId());
	      			dataEvent.server.send(socket, dataEvent.message);
	      		} else {
	      			//User not online, cache the message
	      			cache.addMessage(dataEvent.message);
	      		}
	      		break;
	      	case 0x04:
	      		//SEND
	      		//Forward message to connected User
	      		System.out.println("SEND request from User: " + sender.getId().toString());
	      		if(isConnected(receiver)){
	      			System.out.println("---- User is connected");
	      			//User is online, forward message
	      			SocketChannel socket = connectedUsers.get(receiver.getId());
	      			System.out.println("---- send message to User " + receiver.getId());
	      			dataEvent.server.send(socket, dataEvent.message);
	      		} else {
	      			//Do not cache the message?!
	      			System.out.println("---- User is not connected");
	      		}
	      		break;
	      	default:
	      		break;
	      }
	    }
	  }
	}
