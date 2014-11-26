package ch.ethz.inf.vs.android.glukas.project4.protocol;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.SynchronousQueue;

import ch.ethz.inf.vs.android.glukas.project4.Post;
import ch.ethz.inf.vs.android.glukas.project4.User;
import ch.ethz.inf.vs.android.glukas.project4.protocol.parsing.JSONObjectFactory;
import ch.ethz.inf.vs.android.glukas.project4.security.NetworkMessage;
import ch.ethz.inf.vs.android.glukas.project4.security.SecureChannel;

/**
 * Used by the protocol to delegate heavy send of data. List of post can be added directly and
 * the Sender will take care of the parsing into NetworkMessage and of sending
 */
public class Sender extends Thread {

	private Queue<NetworkMessage> queue;
	private volatile boolean alive;
	private SecureChannel channel;
	
	/**
	 * Create a new sender with a channel where to send messages
	 * @param channel
	 */
	public Sender(SecureChannel channel) {
		queue = new SynchronousQueue<NetworkMessage>();
		this.channel = channel;
	}
	
	@Override
	public void run() {
		while(alive) {
			NetworkMessage toSend = queue.poll();
			if (toSend != null){
				channel.sendMessage(toSend);	
			} else {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Add a NetworkMessage to the queue
	 * @param msg
	 */
	public synchronized void addMessage(NetworkMessage msg) {
		queue.add(msg);
		this.notify();
	}
	
	/**
	 * Add asynchronously posts to send queue
	 * @param posts
	 * @param sender
	 * @param receiver
	 * @param isSend
	 */
	public void addPostsAsync(List<Post> posts, User sender, User receiver, boolean isSend) {
		Adder adder = new Adder(posts, sender, receiver, isSend, this);
		adder.run();
	}
	
	/**
	 * Terminate properly the sender
	 */
	public void kill() {
		alive = false;
		this.interrupt();
	}
	
	/**
	 * Used internally to add asynchronously list of posts. It parses the posts into NetworkMessage
	 */
	private class Adder implements Runnable {

		private List<Post> posts;
		private User sender;
		private User receiver;
		private boolean isSend;
		private Sender target;
		
		public Adder(List<Post> posts, User sender, User receiver, boolean isSend, Sender target) {
			this.posts = posts;
			this.sender = sender;
			this.receiver = receiver;
			this.isSend = isSend;
			this.target = target;
		}
		
		@Override
		public void run() {
			for (Post post : posts){
				String msg = JSONObjectFactory.createJSONObject(new Message(post, receiver, sender, isSend)).toString();
				PublicHeader header = new PublicHeader(0, null, StatusByte.SEND.getByte(), post.getId(), sender.getId(), receiver.getId());
				NetworkMessage networkMsg = new NetworkMessage(msg, header);
				target.addMessage(networkMsg);
			}
		}
	}
}
