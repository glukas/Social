package ch.ethz.inf.vs.server;

import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import ch.ethz.inf.vs.android.glukas.project4.UserId;
import ch.ethz.inf.vs.server.MessageBuffer;

public class ConnectionWorker implements Runnable {
	  private List<ServerEvent> queue = new LinkedList<ServerEvent>();
	  private HashMap<UserId, SocketChannel> connectedUsers = new HashMap<UserId, SocketChannel>();
	  private MessageBuffer cache = new MessageBuffer();
	  
	  public void processData(Server server, SocketChannel socket, byte[] data, int count) {
		  //Create Message out of the byte array
		  Message received = new Message(data);
		  byte[] dataCopy = new byte[count];
		  System.arraycopy(data, 0, dataCopy, 0, count);
		  System.out.println("Status: " + Integer.toHexString(received.getHeader().getConsistency()));
		  String s = new String(dataCopy, StandardCharsets.UTF_8);

		  System.out.println("Server read: " + s);

		  synchronized(queue) {
			  queue.add(new ServerEvent(server, socket, received));
			  queue.notify();
		  }
	  }
	  
	  private boolean isConnected(UserId user){
		  return connectedUsers.containsKey(user);
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
	      		connectedUsers.put(sender, dataEvent.socket);
	      		break;
	      	case 0x01:
	      		//DISCONNECT
	      		//remove the user from connectedUsers
	      		connectedUsers.remove(sender);
	      		break;
	      	case 0x02:
	      		//DATA
	      		//Send the data to the user
	      		//TODO For now we store all messages, late we can drop them if send was successful
	      		List<Message> data = cache.getMessagesSince(receiver, 0);
	      		if(isConnected(sender)){
	      			SocketChannel socket = connectedUsers.get(sender);
	      			//Send all messages
	      			for(Message m : data){
	      				dataEvent.server.send(socket, m);
	      			}
	      		}
	      		
	      		break;
	      	case 0x03:
	      		//POST
	      		if(isConnected(receiver)){
	      			//User is online, forward Post
	      			SocketChannel socket = connectedUsers.get(receiver);
	      			dataEvent.server.send(socket, dataEvent.message);
	      		} else {
	      			//User not online, cache the message
	      			cache.addMessage(dataEvent.message);
	      		}
	      		break;
	      	case 0x04:
	      		//SEND
	      		//Forward message to connected User
	      		if(isConnected(receiver)){
	      			//User is online, forward message
	      			SocketChannel socket = connectedUsers.get(receiver);
	      			dataEvent.server.send(socket, dataEvent.message);
	      		} else {
	      			//Do not cache the message?!
	      		}
	      		break;
	      	default:
	      		break;
	      }
	    }
	  }
	}
