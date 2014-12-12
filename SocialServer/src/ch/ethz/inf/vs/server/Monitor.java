package ch.ethz.inf.vs.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import ch.ethz.inf.vs.android.glukas.project4.UserId;
import ch.ethz.inf.vs.android.glukas.project4.networking.TCPCommunicator;
import ch.ethz.inf.vs.android.glukas.project4.protocol.PublicHeader;
import ch.ethz.inf.vs.android.glukas.project4.protocol.StatusByte;

public class Monitor implements Runnable {
	
	private volatile boolean running = false;
	private Server server;
	private Thread serverThread;
	private Runtime rt;
	
	//The port the server listens
	private int port = 9000; 
	
	//TCPCommunicator to test the connection
	private TCPCommunicator conn;
	
	public Monitor(Server server,  Thread serverThread, boolean serverRunning){
		running = true;
		this.server = server;
		this.serverThread = serverThread;
		
		//need to start the server?
		if(!serverRunning){
			startServer();
		}
		
		//Get runtime
		rt = Runtime.getRuntime();
		
		
	}
	
	@Override
	public void run() {
		int i = 0;
		//Do while running
		while(running){
			
			try {
				//Check for memory
				System.out.println("##### Check Memory #####");
				if(getFreeMemory() < 20.0){
					System.out.println("##### Run GC #####");
					rt.gc();
				}

				//Check for connection
				i = 0;
				while(i < 5 && !serverAlive()){
					if(i == 4){
						//Server is not responding
						restartServer();
					}
					i++;
				}

				Thread.sleep(5000);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Return the free space in percentage [0,100]
	 * @return
	 */
	private float getFreeMemory(){
		float totalMemory = rt.totalMemory()/(1024*1024);
		float freeMemory = rt.freeMemory()/(1024*1024);
		float freePercent = (freeMemory/totalMemory)*100;
		System.out.println("##### " + totalMemory + "MB total memory #####");
		System.out.println("##### " + freeMemory + "MB free memory #####");
		System.out.println("##### " + rt.maxMemory()/(1024*1024) + "MB mx memory #####");
		System.out.println("##### " + freePercent + "% free memory #####");
		return freePercent;
	}
	
	private boolean serverAlive(){
		// Setup connection
		try {
			conn = new TCPCommunicator("localhost", port);
			
			//Set timeout
			conn.setTimeout(5000);
			
			//Send connect
			PublicHeader header = new PublicHeader(PublicHeader.BYTES_LENGTH_HEADER, null, StatusByte.CONNECT.getByte(), 0, new UserId("0"), new UserId("0") );
			conn.sendMessage(header.getbytes());
			
			//wait for answer
			byte[] receivedBytes = conn.receiveMessage();
			Message received = new Message(receivedBytes);
			
			//Close connection in any case
			conn.finishConnection();
			
			//is ACK
			if(received.isEmpty && (received.getHeader().getConsistency() == 0x00 || received.getHeader().getConsistency() == 0x01)){
				//Everything ok
				return true;
			} else {
				//Received wrong answer
				return false;
			}
			
		} catch (UnknownHostException e) {
			//e.printStackTrace();
			return false;
		} catch (IOException e) {
			//e.printStackTrace();
			return false;
		}
	}
	
	public void stop() throws InterruptedException{
		this.running = false;
		stopServer();
	}
	
	public void restartServer() throws InterruptedException{
		//kill the running server
		server.stop();
		serverThread.join(2000);
		
		//start the new server
		startServer();
		
	}
	
	public void startServer(){
		//Start a new server
		server = new Server(null, port);
		serverThread = new Thread(server);
		serverThread.start();
	}
	
	public void stopServer() throws InterruptedException{
		server.stop();
		serverThread.join(2000);
	}

}
