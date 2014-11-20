package ch.ethz.inf.vs.server;

import java.nio.channels.SocketChannel;

class ServerEvent {
	public Server server;
	public SocketChannel socket;
	public Message message;
	
	public ServerEvent(Server server, SocketChannel socket, Message m) {
		this.server = server;
		this.socket = socket;
		this.message = m;
	}
}