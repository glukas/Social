package ch.ethz.inf.vs.test;

import java.io.IOException;
import java.util.Random;

import ch.ethz.inf.vs.android.glukas.project4.UserId;
import ch.ethz.inf.vs.android.glukas.project4.protocol.PublicHeader;
import ch.ethz.inf.vs.android.glukas.project4.protocol.StatusByte;
import ch.ethz.inf.vs.server.Message;

public class DummyClient_2 extends DummyClient {
	
	int sendTo = 0;

	public DummyClient_2(int port, int id, int clients, int sendTo) {
		super(port, id, clients);
		this.sendTo = sendTo;
	}
	
	
	@Override
	protected UserId randomUser(){
		return new UserId(Integer.toString(this.sendTo));
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
