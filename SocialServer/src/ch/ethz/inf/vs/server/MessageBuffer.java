package ch.ethz.inf.vs.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ch.ethz.inf.vs.android.glukas.project4.UserId;

public class MessageBuffer {
	
	//<Receipient, Messages>
	HashMap<UserId, UserMessageQueue> buffer = new HashMap<UserId, UserMessageQueue>();
	
	public void addMessage(Message m){
		if(buffer.containsKey(m.getHeader().getReceiver())){
			//User already has a queue
			buffer.get(m.getHeader().getReceiver()).addMessage(m);
		} else {
			//Create new queue
			UserMessageQueue queue = new UserMessageQueue(m);
			buffer.put(m.getHeader().getReceiver(), queue);
		}
	}
	
	public void addMessage(byte[] bytes){
		Message m = new Message(bytes);
		this.addMessage(m);
	}
	
	public List<Message> getMessagesSince(UserId recipient, int clock){
		ArrayList<Message> list = new ArrayList<Message>();
		if(buffer.containsKey(recipient)){
			return buffer.get(recipient).getMessagesSince(clock);
		} else {
			//return empty list
			return list;
		}
	}

}
