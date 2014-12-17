package ch.ethz.inf.vs.test;

import java.io.IOException;
import java.util.Random;

import ch.ethz.inf.vs.android.glukas.project4.UserId;
import ch.ethz.inf.vs.android.glukas.project4.protocol.PublicHeader;
import ch.ethz.inf.vs.android.glukas.project4.protocol.StatusByte;
import ch.ethz.inf.vs.server.Message;

public class DummyClient_3 extends DummyClient {
	
	int sendTo = 0;
	boolean send = true;

	public DummyClient_3(int port, int id, int clients, int sendTo, boolean send) {
		super(port, id, clients);
		this.sendTo = sendTo;
		this.send = send;
	}
	
	
	@Override
	protected UserId randomUser(){
		return new UserId(Integer.toString(this.sendTo));
	}
	
	

	@Override
	public void run() {

		if(send){
			//Send connect
			try {
				sendConnect();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			try {
				//Send a post
				sendPost();
				Thread.sleep(2000);
				sendPost();
				Thread.sleep(2000);
				sendPost();
				Thread.sleep(2000);
				sendPost();
				Thread.sleep(2000);
				sendPost();
				Thread.sleep(2000);

				sendDisconnect();

				Thread.sleep(10000);
			} catch (IOException | InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else {
			
			//Send connect
			try {
				Thread.sleep(10000);
				sendConnect();
				Thread.sleep(2000);
			} catch (IOException | InterruptedException e1) {
				e1.printStackTrace();
			}

			try {
				//Send a post
				sendData();
				Thread.sleep(2000);
				

				
			} catch (IOException | InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
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
