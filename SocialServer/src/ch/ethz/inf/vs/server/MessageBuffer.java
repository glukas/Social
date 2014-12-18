package ch.ethz.inf.vs.server;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import ch.ethz.inf.vs.android.glukas.project4.UserId;

public class MessageBuffer {
	
	private int size = 0;
	//Only to have reference to the oldest messages
	private LinkedList<Message> inserted = new LinkedList<Message>();
	
	//<Receipient, Messages>
	HashMap<BigInteger, UserMessageQueue> buffer = new HashMap<BigInteger, UserMessageQueue>();
	
	public void addMessage(Message m){
		if(buffer.containsKey(m.getHeader().getReceiver().getId())){
			//User already has a queue
			buffer.get(m.getHeader().getReceiver().getId()).addMessage(m);
		} else {
			//Create new queue
			UserMessageQueue queue = new UserMessageQueue(m);
			buffer.put(m.getHeader().getReceiver().getId(), queue);
		}
		
		this.inserted.push(m);
		this.size++;
	}
	
	public void addMessage(byte[] bytes){
		Message m = new Message(bytes);
		this.addMessage(m);
		this.inserted.push(m);
		this.size++;
	}
	
	public List<Message> getMessagesSince(UserId recipient, int clock){
		BigInteger id = recipient.getId();
		ArrayList<Message> list = new ArrayList<Message>();
		if(buffer.containsKey(id)){
			list = buffer.get(id).getMessagesSince(clock);
			// Remove the retrieved messages
			buffer.get(id).removeMessages(list);
			this.size -= list.size();
			// Remove the buffer message queue if it is empty
			if(buffer.get(id).isEmpty()){
				buffer.remove(id);
			}
		}
		return list;
	}
	
	public List<Message> getAllMessages(UserId recipient){
		BigInteger id = recipient.getId();
		ArrayList<Message> list = new ArrayList<Message>();
		if(buffer.containsKey(id)){
			list = buffer.get(id).getMessagesSince(0);
		}
		return list;
	}
	
	public int size(){
		return this.size;
	}
	
	public void removeOldestMessages(int count){
		for(int i = 0; i < count; i++){
			Message m = inserted.removeLast();
			buffer.get(m.getHeader().getReceiver().getId()).removeMessages(m);
		}
	}

}
