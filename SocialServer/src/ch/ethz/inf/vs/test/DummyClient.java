package ch.ethz.inf.vs.test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class DummyClient implements Runnable {
	
	private int port = 8080;
	private final int clientId;
	
	public DummyClient(int port, int id){
		this.port = port;
		this.clientId = id;
	}

	@Override
	public void run() {
		String sentence;
		String modifiedSentence;
		Socket clientSocket = null;
		DataOutputStream outToServer = null;
		BufferedReader inFromServer = null;
		try{
			clientSocket = new Socket("winti.mooo.com", port);
			outToServer = new DataOutputStream(clientSocket.getOutputStream());
			inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		int i = 9;
		int timeout = 0;
		
		while(i != 0){
			
			try{
				i = (int) Math.floor(Math.random()*10);
				timeout = (int) Math.floor(Math.random()*10000);
				System.out.println("Client " + Integer.toString(clientId) + " sleeps: " + Integer.toString(timeout/1000));
				Thread.sleep(timeout);
				
				
				//sentence = "ping from " + Integer.toString(clientId);
				sentence = Integer.toString(i);
				outToServer.writeBytes(sentence);
				
				System.out.println("Client " + Integer.toString(clientId) + " sent: " + sentence);
				modifiedSentence = inFromServer.readLine();
				
				System.out.println("Client " + Integer.toString(clientId) + " received: " + modifiedSentence);
				

				
				
				
				
			} catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
		try {
			
			sentence = Integer.toString(-1);
			outToServer.writeBytes(sentence);
			outToServer.close();
			inFromServer.close();
			clientSocket.close();
			System.out.println("Client " + Integer.toString(clientId) + " stopped!");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
