package ch.ethz.inf.vs.server;

import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

import ch.ethz.inf.vs.android.glukas.project4.protocol.StatusByte;

public class ConnectionWorker implements Runnable {
	  private List<ServerEvent> queue = new LinkedList<ServerEvent>();
	  
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
	      System.out.println("Handle message");
	      //StatusByte s = StatusByte.constructStatusByte(dataEvent.message.getHeader().getConsistency());
	      byte b = dataEvent.message.getHeader().getConsistency();
	      System.out.println("Server handles status: " + Integer.toHexString(b));
	      switch(b){
	      	case 0x00:
	      		break;
	      	case 0x01:
	      		break;
	      	case 0x02:
	      		break;
	      	case 0x03:
	      		dataEvent.server.send(dataEvent.socket, dataEvent.message);
	      		break;
	      	case 0x04:
	      		break;
	      	default:
	      		break;
	      }
	    }
	  }
	}
