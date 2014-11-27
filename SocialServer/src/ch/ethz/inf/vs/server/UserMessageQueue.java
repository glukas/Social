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
	
	
	public List<Message> getMessagesSince(int clock){
		Message since = new Message(clock);
		ArrayList<Message> list = new ArrayList<Message>();
		Iterator<Message> it = messages.iterator();
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
		if(m.getHeader().getReceiver().getId() == recipient.getId()){
			messages.add(m);
		}
	}
}
