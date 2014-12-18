package ch.ethz.inf.vs.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

import ch.ethz.inf.vs.android.glukas.project4.UserId;

public class UserMessageQueue {
	
	private UserId recipient;
	private PriorityQueue<Message> messages = new PriorityQueue<Message>();

	
	public UserMessageQueue(Message m){
		this.recipient = m.getHeader().getReceiver();
		this.messages.add(m);
	}
	
	public UserMessageQueue(byte[] bytes){
		Message m = new Message(bytes);
		this.recipient = m.getHeader().getReceiver();
		this.messages.add(m);
	}
	
	public boolean isEmpty(){
		return messages.isEmpty();
	}
	
	public void removeMessages(List<Message> toRemove){
		synchronized(this.messages){
			this.messages.removeAll(toRemove);
		}
		
	}
	
	public void removeMessages(Message toRemove){
		synchronized(this.messages){
			this.messages.remove(toRemove);
		}
		
	}
	
	
	public ArrayList<Message> getMessagesSince(int clock){
		Message since = new Message(clock);
		ArrayList<Message> list = new ArrayList<Message>();
		Iterator<Message> it = messages.iterator();
		System.out.println("---- " + messages.size() + " messages in buffer!");
		while(it.hasNext()){
			Message next = it.next();
			if(next.getHeader().getMessageId() >= since.getHeader().getMessageId()){
				list.add(next);
			} else {
				break;
			}
		}
		return list;
	}
	
	public void addMessage(Message m){
		//Only add if its really the right recipient
		if(m.getHeader().getReceiver().getId().equals(recipient.getId())){
			System.out.println("---- Added message to queue of user " + recipient.getId() + "!");
			messages.add(m);
		}
	}
}
