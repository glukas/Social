package ch.ethz.inf.vs.server;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ch.ethz.inf.vs.android.glukas.project4.UserId;

public class MessageBuffer {
	
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
	}
	
	public void addMessage(byte[] bytes){
		Message m = new Message(bytes);
		this.addMessage(m);
	}
	
	public List<Message> getMessagesSince(UserId recipient, int clock){
		BigInteger id = recipient.getId();
		ArrayList<Message> list = new ArrayList<Message>();
		if(buffer.containsKey(id)){
			list = buffer.get(id).getMessagesSince(clock);
			
			// Remove the buffer message queue if it is empty
			if(buffer.get(id).isEmpty()){
				buffer.remove(id);
			}
		}
		return list;
	}

}
