package ch.ethz.inf.vs.server;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

public class UserMessageQueue {
	
	private BigInteger recipient;
	private PriorityQueue<Message> messages = new PriorityQueue<Message>();
	
	public UserMessageQueue(BigInteger user){
		this.recipient = user;
	}
	
	public UserMessageQueue(Message m){
		this.recipient = m.getRecipient();
		this.messages.add(m);
	}
	
	public UserMessageQueue(byte[] bytes){
		Message m = new Message(bytes);
		this.recipient = m.getRecipient();
		this.messages.add(m);
	}
	
	
	public List<Message> getMessagesSince(int clock){
		Message since = new Message(clock);
		ArrayList<Message> list = new ArrayList<Message>();
		Iterator<Message> it = messages.iterator();
		while(it.hasNext()){
			Message next = it.next();
			if(next.getClock() >= since.getClock()){
				list.add(next);
			} else {
				break;
			}
		}
		return list;
	}
	
	public void addMessage(Message m){
		//Only add if its really the right recipient
		if(m.getRecipient() == recipient){
			messages.add(m);
		}
	}
}
