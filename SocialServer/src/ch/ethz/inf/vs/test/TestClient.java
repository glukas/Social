package ch.ethz.inf.vs.test;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TestClient {
	
	public static void main(String[] args) {
		int clients = 1;
		ExecutorService threadPool = Executors.newCachedThreadPool();
		//Start some dummy clients
		System.out.println("How many clients?");
		clients = Integer.parseInt(System.console().readLine());

		for(int i = 0; i < clients; i++){
			threadPool.execute(
					//client ids from 1...clients, 0 is the server
					new DummyClient(9000, i+1, clients)
					);
		}
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter 'kill' to terminate the server!\n");
        String input = "";
        while(!input.equals("kill")){
			try {
				input = reader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				input = "";
			}
        }
        
        threadPool.shutdownNow();
        try {
			threadPool.awaitTermination(1,TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.exit(0);
	}

}
