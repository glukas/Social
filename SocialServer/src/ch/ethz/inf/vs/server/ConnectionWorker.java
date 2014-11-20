package ch.ethz.inf.vs.server;

import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

import ch.ethz.inf.vs.server.Server.Status;

public class ConnectionWorker implements Runnable {
	  private List<ServerEvent> queue = new LinkedList<ServerEvent>();
	  
	  public void processData(Server server, SocketChannel socket, byte[] data, int count) {
		  //Create Message out of the byte array
		  Message received = new Message(data);
		  byte[] dataCopy = new byte[count];
		  System.arraycopy(data, 0, dataCopy, 0, count);

		  String s = new String(dataCopy, StandardCharsets.UTF_8);

		  System.out.println("Server read: " + s);

		  synchronized(queue) {
			  queue.add(new ServerEvent(server, socket, received));
			  queue.notify();
		  }
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
	      
	      //Choose what to do, based on the status of the message
	      // TODO Add all possible status
	      byte s = dataEvent.message.getStatus();
	      switch(s){
	      	case Status.INTERNAL:
	      		break;
	      	case Status.MESSAGE:
	      		break;
	      	case Status.PING:
	      		dataEvent.server.send(dataEvent.socket, dataEvent.message);
	      		break;
	      	default:
	      		break;
	      }
	    }
	  }
	}
