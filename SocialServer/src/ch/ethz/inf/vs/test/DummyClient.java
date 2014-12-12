package ch.ethz.inf.vs.test;

import java.io.IOException;
import java.util.Random;

import ch.ethz.inf.vs.android.glukas.project4.UserId;
import ch.ethz.inf.vs.android.glukas.project4.networking.TCPCommunicator;
import ch.ethz.inf.vs.android.glukas.project4.protocol.PublicHeader;
import ch.ethz.inf.vs.android.glukas.project4.protocol.StatusByte;
import ch.ethz.inf.vs.server.Message;

public class DummyClient implements Runnable {
	
	//Connection data
	private int port = 9000;
	private final int clientId;
	private final int clients;
	private TCPCommunicator comm;
	private UserId me;
	private UserId server;
	private Thread receiveThread;
	private boolean running = false;
	
	
	public DummyClient(int port, int id, int clients){
		this.port = port;
		this.clientId = id;
		this.clients = clients;
		this.comm = new TCPCommunicator("localhost", this.port);
		this.me = new UserId(Integer.toString(clientId));
		this.server = new UserId("0");
		running = true;
		setReceiveHandling();
		
	}
	

	private void setReceiveHandling(){
		receiveThread = new Thread() {
			@Override
			public void run() {
				while(running){
					try{
						byte[] message = comm.receiveMessage();
						onMessageReceive(message);
					} catch (IOException e) {
						if (running){
							e.printStackTrace();
						}
					} catch (Exception e){
						if (running){
							e.printStackTrace();
						}
					}
				}
			}
		};
		receiveThread.start();
	}
	
	private byte[] createPacket(PublicHeader header, byte[] message){
		//Combine header and message
		byte[] packet = new byte[header.getLength()];
		System.arraycopy(header.getbytes(), 0, packet, 0, header.getbytes().length);
		if(message != null)
			System.arraycopy(message, 0, packet, header.getbytes().length, message.length);
		return packet;
	}
	
	private static String generateString(Random rng, String characters, int length)
	{
	    char[] text = new char[length];
	    for (int i = 0; i < length; i++)
	    {
	        text[i] = characters.charAt(rng.nextInt(characters.length()));
	    }
	    return new String(text);
	}
	
	private UserId randomUser(){
		Random rnd = new Random();
		int i = rnd.nextInt(clients) + 1;
		return new UserId(Integer.toString(i));
	}
	
	private void onMessageReceive(byte[] m){
		Message received = new Message(m);
		
		//is ACK
		if(received.isEmpty && (received.getHeader().getConsistency() == 0x00 || received.getHeader().getConsistency() == 0x01)){
			System.out.println("Received ACK");
		}
		
		String message = (received.isEmpty ? "<empty>" : new String(received.getMessage()));
		System.out.println("Client " + clientId + " received message: //STATUS//" + received.getHeader().getConsistency() + "//MESSAGE//" + message);
	}
	
	private void sendConnect() throws IOException{
		PublicHeader header = new PublicHeader(PublicHeader.BYTES_LENGTH_HEADER, null, StatusByte.CONNECT.getByte(), 0, me, server );
		System.out.println("Client " + clientId + " sends message: //STATUS//" + header.getConsistency() + "//MESSAGE//<empty>");
		comm.sendMessage(createPacket(header, null));
	}
	
	private void sendDisconnect() throws IOException{
		PublicHeader header = new PublicHeader(PublicHeader.BYTES_LENGTH_HEADER, null, StatusByte.DISCONNECT.getByte(), 0, me, server );
		System.out.println("Client " + clientId + " sends message: //STATUS//" + header.getConsistency() + "//MESSAGE//<empty>");
		comm.sendMessage(createPacket(header, null));
	}
	
	private void sendPost() throws IOException{
		byte[] m = (generateString(new Random(), "abcdefghijklmnopqrstuvwxyz0123456789", 20)).getBytes();
		PublicHeader header = new PublicHeader(PublicHeader.BYTES_LENGTH_HEADER + m.length, null, StatusByte.POST.getByte(), 0, me, randomUser() );
		System.out.println("Client " + clientId + " sends POST: //STATUS//" + header.getConsistency() + "//MESSAGE//" + new String(m) + "//TO//" + header.getReceiver());
		comm.sendMessage(createPacket(header, m));
	}
	
	private void sendData() throws IOException{
		//Fetch all my data
		PublicHeader header = new PublicHeader(PublicHeader.BYTES_LENGTH_HEADER, null, StatusByte.DATA.getByte(), 0, me, me );
		System.out.println("Client " + clientId + " sends DATA: //STATUS//" + header.getConsistency() + "//MESSAGE//<empty>");
		comm.sendMessage(createPacket(header, null));
	}
	
	private void sendMessage() throws IOException{
		//Send message to myself
		byte[] m = (generateString(new Random(), "abcdefghijklmnopqrstuvwxyz0123456789", 20)).getBytes();
		PublicHeader header = new PublicHeader(PublicHeader.BYTES_LENGTH_HEADER + m.length, null, StatusByte.SEND.getByte(), 0, me, randomUser());
		System.out.println("Client " + clientId + " sends MESSAGE: //STATUS//" + header.getConsistency() + "//MESSAGE//" + new String(m) + "//TO//" + header.getReceiver());
		comm.sendMessage(createPacket(header, m));
	}
	
	private void sendUnknown(){
		
	}
	
	private void reconnect() throws IOException, InterruptedException{
			System.out.println("Client " + Integer.toString(clientId) + " performs a reconnect!");
			sendDisconnect();
			Thread.sleep((int)Math.floor(Math.random()*10000));
			System.out.println("Client " + Integer.toString(clientId) + " is back!");
			sendConnect();
			sendData();
	}

	@Override
	public void run() {
		//Send connect
		try {
			sendConnect();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		int i = 9;
		int action = 0;
		int timeout = 0;
		
		while(i != 0){
			try{
				i = (int) Math.floor(Math.random()*10);
				action = (int) Math.floor(Math.random()*4);
				timeout = (int) Math.floor(Math.random()*10000);
				System.out.println("Client " + Integer.toString(clientId) + " sleeps: " + Integer.toString(timeout/1000));
				Thread.sleep(timeout);
				
				switch(action){
				case 0:
					sendPost();
					break;
				case 1: 
					sendData();
					break;
				case 2:
					sendMessage();
					break;
				case 3:
					reconnect();
					break;
				default:
					break;
				
				}
				
			} catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
		try {
			sendDisconnect();
			Thread.sleep(1000);
			
			//Shutdown
			running = false;
			comm.finishConnection();
			System.out.println("Client " + Integer.toString(clientId) + " stopped!");
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

	}

}
