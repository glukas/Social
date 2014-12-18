package ch.ethz.inf.vs.android.glukas.project4.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import ch.ethz.inf.vs.android.glukas.project4.protocol.PublicHeader;

public class TCPCommunicator {
	
	private final int port;
	private final String address;
	private Socket socket;
	
	
	private int receiveTimeout = 0;
	
	
	private DataInputStream in;
	private DataOutputStream out;

	/**
	 * Constructor
	 * @throws IOException 
	 */
	public TCPCommunicator(String address, int port) {
		this.port = port;
		this.address = address;

		setupConnection();
	}

	/**
	 * Constructor
	 * @throws IOException 
	 */
	public TCPCommunicator(String address, int port, int receiveBufSize) {
		this.port = port;
		this.address = address;

		setupConnection();
	}

	/**
	 * Close the connection properly
	 * @throws IOException
	 */
	public void finishConnection() throws IOException{
		if (socket != null)
			socket.close();
		if (in != null) 
			in.close();
		if (out != null) 
			out.close();
	}


	////
	//Connection set up
	////

	private void setupConnection(){
		try {
			socket = new Socket(address, port);
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	

	////
	//Send and receive requests, responses
	////

	/**
	 * This function should be used to send a message to the server
	 * @param message The message as byte array
	 * @throws Exception if any error occurs, like there is no network
	 */
	public void sendMessage(byte[] message) throws IOException {
		out.write(message);
	}


	/**
	 * Receive method that first reads the fix sized PublicHeader and then the message
	 * @return byte array representing message received
	 * @throws IOException
	 */
	public byte[] receiveMessage() throws IOException {
		//Initialize locals
		byte[] headerBytes = new byte[PublicHeader.BYTES_LENGTH_HEADER];
		byte[] messageBytes;
		PublicHeader header;
		
		//read the header
		in.readFully(headerBytes);
		header = new PublicHeader(ByteBuffer.wrap(headerBytes));
		
		//Create packet and save header in it
		byte[] packet = new byte[header.getLength()];
		System.arraycopy(header.getbytes(), 0, packet, 0, header.getbytes().length);
		
		//Check for empty message
		if(header.getLength() > PublicHeader.BYTES_LENGTH_HEADER){
			//Message not empty
			//set message based on header
			messageBytes = new byte[header.getLength() - PublicHeader.BYTES_LENGTH_HEADER];
			in.readFully(messageBytes);


			//Combine header and message
			System.arraycopy(messageBytes, 0, packet, header.getbytes().length, messageBytes.length);
		}
		
		return packet;
	}

	////
	//Getters and setters
	////


	/**
	 * @return port of the remote host
	 */
	public int getRemotePort(){
		return port;
	}

	/**
	 * Gets the local port
	 * @return local port, or -2 if socket is null, -1 if socket is closed, 0 if socket is unbound
	 */
	public int getLocalPort(){
		if (socket != null) {
			return socket.getLocalPort();
		} else{
			return -2;
		}
	}

	public String getAddress(){
		return address;
	}


	public void setTimeout(int timeout) throws SocketException{
		this.receiveTimeout = timeout;
		socket.setSoTimeout(this.receiveTimeout);
	}

	public int getTimeout(){
		return receiveTimeout;
	}
}
