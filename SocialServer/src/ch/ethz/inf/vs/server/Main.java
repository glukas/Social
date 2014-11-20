package ch.ethz.inf.vs.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

	public static void main(String[] args) {
		//ThreadPoolServer server = new ThreadPoolServer(9000);
		Server server = new Server(null, 9000);
		System.out.println("Starting Server");
		new Thread(server).start();
		
		
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
        
		
		System.out.println("Stopping Server");
		server.stop();
		System.out.println("Server stopped");
		System.exit(0);
	}

}
