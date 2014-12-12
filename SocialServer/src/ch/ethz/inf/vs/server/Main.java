package ch.ethz.inf.vs.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
	
	private static Thread serverThread;
	private static Thread monitorThread;
	
	private static Server server;
	private static Monitor monitor;
	
	private static final int PORT = 9000;
	

	public static void main(String[] args) {
		//Output
		System.out.println("Starting Server");
		
		// Start the server thread
		server = new Server(null, PORT);
		serverThread = new Thread(server);
		serverThread.start();
		
		//Start the monitor thread
		monitor = new Monitor(server, serverThread, true);
		monitorThread = new Thread(monitor);
		monitorThread.start();
		
		
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
        
		//Kill server
        System.out.println("Stopping Server");
        try {
        	monitor.stop();
        	monitorThread.join(5000);
        } catch (InterruptedException e) {
        	e.printStackTrace();
        }
        System.out.println("Server stopped");
        System.exit(0);
	}

}
