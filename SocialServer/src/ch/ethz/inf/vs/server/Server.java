package ch.ethz.inf.vs.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ch.ethz.inf.vs.android.glukas.project4.protocol.PublicHeader;

public class Server implements Runnable {

	private InetAddress hostAddress;
	private int port;
	
	// Used to recover from failures
	private long lastFailure = 0;
	private boolean restart = false;

	// The channel for accepting connections
	private ServerSocketChannel serverChannel;

	// Selector
	private Selector selector;

	// The buffer into which we'll read data when it's available
	private ByteBuffer headerBuffer = ByteBuffer.allocate(PublicHeader.BYTES_LENGTH_HEADER);
	//Max message size 1MB
	private ByteBuffer messageBuffer = ByteBuffer.allocate(1000000);
	private ByteBuffer outBuffer = ByteBuffer.allocate(1000000);
	
	//The worker to handle connections
	private ConnectionWorker worker;
	private Thread workerThread;
	
	//Indicates whether server is running
	protected volatile boolean isStopped    = false;

	public Server(InetAddress hostAddress, int port){
		setup(hostAddress, port);
	}
	
	private void setup(InetAddress hostAdress, int port){
		try{
			this.hostAddress = hostAddress;
			this.port = port;
			this.selector = this.initSelector();
			//Dispatch worker
			this.worker = new ConnectionWorker();
			this.workerThread = new Thread(worker);
			workerThread.start();
			
		} catch(IOException e){
			e.printStackTrace();
		}
	}

	// A list of ChangeRequest instances
	private List<ChangeRequest> changeRequests = new LinkedList<ChangeRequest>();

	// Maps a SocketChannel to a list of ByteBuffer instances
	private Map<SocketChannel, List<ByteBuffer>> pendingData = new HashMap<SocketChannel, List<ByteBuffer>>();

	@Override
	public void run() {
		while (!isStopped) {
			try {
				// Process any pending changes
				synchronized(this.changeRequests) {
					Iterator<ChangeRequest> changes = this.changeRequests.iterator();
					while (changes.hasNext()) {
						ChangeRequest change = changes.next();
						switch(change.type) {
						case ChangeRequest.CHANGEOPS:
							SelectionKey key = change.socket.keyFor(this.selector);
							if(key == null){
								System.out.println("key is null");
								break;
							}
								
							key.interestOps(change.ops);
						}
					}
					this.changeRequests.clear(); 
				}

				// Wait for event
				this.selector.select();

				// Iterate over all available keys
				Iterator<?> selectedKeys = this.selector.selectedKeys().iterator();
				while (selectedKeys.hasNext()) {
					SelectionKey key = (SelectionKey) selectedKeys.next();
					selectedKeys.remove();

					if (!key.isValid()) {
						continue;
					}

					// Check what event is available and deal with it
					if (key.isAcceptable()) {
						this.accept(key);
					} else if (key.isReadable()) {
						this.read(key);
					} else if (key.isWritable()) {
						this.write(key);
					}
				}
			} catch (Exception e) {
				if(this.lastFailure + 1000 > System.currentTimeMillis()){
					//Something is really wrong, perform reset
					System.out.println("###### RESET #######");
					workerThread.interrupt();
					setup(this.hostAddress, this.port);
				} else {
					this.lastFailure = System.currentTimeMillis();
				}
				
				e.printStackTrace();
				
			}
		}

	}

	

	public void send(SocketChannel socket, Message m) {
		byte[] data = m.getOriginal();
		synchronized (this.changeRequests) {
			System.out.println("Added message with length " + m.getHeader().getLength() + " to write queue");
			System.out.println("---Actual size " + data.length + " bytes");
			// Add data to write to queue
			synchronized (this.pendingData) {
				List<ByteBuffer> queue = this.pendingData.get(socket);
				if (queue == null) {
					queue = new ArrayList<ByteBuffer>();
					this.pendingData.put(socket, queue);
				}
				queue.add(ByteBuffer.wrap(data));
			}
			
			// Deploy change request
			this.changeRequests.add(new ChangeRequest(socket, ChangeRequest.CHANGEOPS, SelectionKey.OP_WRITE));
		}

		// wake up selector
		this.selector.wakeup();
	}

	private void read(SelectionKey key) throws IOException {
		SocketChannel socketChannel = (SocketChannel) key.channel();

		// Clear out our read buffer so it's ready for new data
		this.headerBuffer.clear();
		this.messageBuffer.clear();

		// Attempt to read the header off the channel
		int numRead;
		try {
			numRead = socketChannel.read(this.headerBuffer);
			System.out.println(numRead + "bytes read! (Header)");
		} catch (IOException e) {
			// Connection got closed
			System.out.println("One remote connection got closed!");
			key.cancel();
			socketChannel.close();
			return;
		}

		if (numRead == -1) {
			// Remote shut down connection cleanly
			key.channel().close();
			key.cancel();
			return;
		}
		
		//Now read the message
		//Set the message Buffer limit to message size
		headerBuffer.rewind();
		int messageLength = headerBuffer.getInt();
		
		if(messageLength < 0){
			System.out.println("Bad message lenght: " + messageLength);
			return;
		}
		
		//create packet and save header in it
		byte[] packet = new byte[messageLength];
		System.arraycopy(headerBuffer.array(), 0, packet, 0, PublicHeader.BYTES_LENGTH_HEADER);
		
		System.out.println("MessageLength: " + messageLength + " bytes");
		
		//Check wether message is empty
		if(messageLength > PublicHeader.BYTES_LENGTH_HEADER){
			//Message not empty
			
			//Do not read more than the message size
			this.messageBuffer.limit(messageLength - PublicHeader.BYTES_LENGTH_HEADER);

			try {
				numRead = socketChannel.read(this.messageBuffer);
				while(numRead != messageLength - PublicHeader.BYTES_LENGTH_HEADER){
					System.out.println("Long message!");
					numRead += socketChannel.read(this.messageBuffer);
				}
				System.out.println(numRead + "bytes read! (Message)");
			} catch (IOException e) {
				// Connection got closed
				System.out.println("One remote connection got closed!");
				key.cancel();
				socketChannel.close();
				return;
			}

			if (numRead == -1) {
				// Remote shut down connection cleanly
				key.channel().close();
				key.cancel();
				return;
			} 
			
			if(messageLength - PublicHeader.BYTES_LENGTH_HEADER < 0){
				System.out.println("Bad message lenght: " + messageLength);
				return;
			}

			//Combine header and message
			System.arraycopy(messageBuffer.array(), 0, packet, PublicHeader.BYTES_LENGTH_HEADER, messageLength - PublicHeader.BYTES_LENGTH_HEADER);
		}
		
		// Hand the data off to our worker thread
		this.worker.processData(this, socketChannel, packet, numRead); 
	}


	private void write(SelectionKey key) throws IOException {
		SocketChannel socketChannel = (SocketChannel) key.channel();

		synchronized (this.pendingData) {
			System.out.println("Server writing...");
			List<ByteBuffer> queue = this.pendingData.get(socketChannel);

			// Write until there's not more data ...
			while (!queue.isEmpty()) {
				outBuffer.clear();
				//ByteBuffer buf = (ByteBuffer) queue.get(0);
				outBuffer = (ByteBuffer) queue.get(0);
				System.out.println("Server writes " + outBuffer.remaining() + " bytes");
				while(outBuffer.remaining() > 0){
					socketChannel.write(outBuffer);
				}

				queue.remove(0);
			}

			if (queue.isEmpty()) {
				//All data written, change to reading again
				key.interestOps(SelectionKey.OP_READ);
			}
			System.out.println("Server finished writing!");
		}
	}

	private void accept(SelectionKey key) throws IOException {
		System.out.println("Server accepted connection!");
		// Cast to server Socket channel
		ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();

		// Accept the connection and make it non-blocking
		SocketChannel socketChannel = serverSocketChannel.accept();
		Socket socket = socketChannel.socket();
		socketChannel.configureBlocking(false);

		// Register the new SocketChannel with our Selector
		socketChannel.register(this.selector, SelectionKey.OP_READ);
	}


	private Selector initSelector() throws IOException {
		// Create a new selector
		Selector socketSelector = SelectorProvider.provider().openSelector();

		// Create a new non-blocking server socket channel
		this.serverChannel = ServerSocketChannel.open();
		serverChannel.configureBlocking(false);

		// Bind the server socket to the specified address and port
		InetSocketAddress isa = new InetSocketAddress(this.hostAddress, this.port);
		serverChannel.socket().bind(isa);

		// Register the server socket channel, indicating an interest in 
		// accepting new connections
		serverChannel.register(socketSelector, SelectionKey.OP_ACCEPT);

		return socketSelector;
	}
	
	public synchronized void stop(){
        this.isStopped = true;
        try {
            this.serverChannel.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

	public class ChangeRequest {
		public static final int REGISTER = 1;
		public static final int CHANGEOPS = 2;

		public SocketChannel socket;
		public int type;
		public int ops;

		public ChangeRequest(SocketChannel socket, int type, int ops) {
			this.socket = socket;
			this.type = type;
			this.ops = ops;
		}
	}

}


