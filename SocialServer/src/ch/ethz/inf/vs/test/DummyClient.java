package ch.ethz.inf.vs.test;

import java.io.IOException;
import java.util.Arrays;

import ch.ethz.inf.vs.android.glukas.project4.UserId;
import ch.ethz.inf.vs.android.glukas.project4.networking.TCPCommunicator;
import ch.ethz.inf.vs.android.glukas.project4.protocol.PublicHeader;
import ch.ethz.inf.vs.android.glukas.project4.protocol.StatusByte;

public class DummyClient implements Runnable {
	
	private int port = 9000;
	private final int clientId;
	private TCPCommunicator comm;
	
	public DummyClient(int port, int id){
		this.port = port;
		this.clientId = id;
		this.comm = new TCPCommunicator("winti.mooo.com", port);
		
		
	}

	@Override
	public void run() {
		String sentence;
		String modifiedSentence;
		byte[] received;
		PublicHeader header;
		byte[] future = new byte[] {0x00, 0x00, 0x00};
		byte consistency = 3;
		int id = 0;
		UserId sender = new UserId(Integer.toString(clientId));
		UserId recipient = new UserId(Integer.toString(0));
		int length = 0;
		byte[] message;
		byte[] packet;
		
		
		
		
		int i = 9;
		int timeout = 0;
		
		while(i != 0){
			id++;
			try{
				i = (int) Math.floor(Math.random()*10);
				timeout = (int) Math.floor(Math.random()*10000);
				System.out.println("Client " + Integer.toString(clientId) + " sleeps: " + Integer.toString(timeout/1000));
				Thread.sleep(timeout);
				
				
				sentence = Integer.toString(i);
				message = sentence.getBytes();
				length = PublicHeader.BYTES_LENGTH_HEADER + message.length;
				
				header = new PublicHeader(length, future, consistency, id, sender, recipient);
				
				//Combine header and message
				packet = new byte[length];
				System.arraycopy(header.getbytes(), 0, packet, 0, header.getbytes().length);
				System.arraycopy(message, 0, packet, header.getbytes().length, message.length);
			
				comm.sendMessage(packet);
				System.out.println("Client " + Integer.toString(clientId) + " sent: " + sentence);
				
				received = comm.receiveMessage();
				modifiedSentence = new String(received);
				
				System.out.println("Client " + Integer.toString(clientId) + " received: " + modifiedSentence);
				
				
			} catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
		try {
			
			comm.finishConnection();
			System.out.println("Client " + Integer.toString(clientId) + " stopped!");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
