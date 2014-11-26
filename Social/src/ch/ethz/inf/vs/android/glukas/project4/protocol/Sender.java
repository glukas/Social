package ch.ethz.inf.vs.android.glukas.project4.protocol;

import java.util.Queue;
import java.util.concurrent.SynchronousQueue;

import ch.ethz.inf.vs.android.glukas.project4.Post;

public class Sender extends Thread {

	private Queue<Post> queue;
	private volatile boolean alive;
	
	public Sender() {
		queue = new SynchronousQueue<Post>();
	}
	
	@Override
	public void run() {
		while(alive) {
			
		}
		
	}
	
	public void addPost(Post post) {
		
	}
	
	public void kill() {
		
	}

}
