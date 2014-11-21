package ch.ethz.inf.vs.server;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageBufffer {
	
	//<Receipient, Messages>
	HashMap<BigInteger, UserMessageQueue> buffer = new HashMap<BigInteger, UserMessageQueue>();
	
	public void addMessage(Message m){
		if(buffer.containsKey(m.getRecipient())){
			buffer.get(m.getRecipient()).addMessage(m);
		} else {
			UserMessageQueue queue = new UserMessageQueue(m);
			buffer.put(m.getRecipient(), queue);
		}
	}
	
	public void addMessage(byte[] bytes){
		Message m = new Message(bytes);
		this.addMessage(m);
	}
	
	public List<Message> getMessagesSince(BigInteger recipient, int clock){
		ArrayList<Message> list = new ArrayList<Message>();
		if(buffer.containsKey(recipient)){
			return buffer.get(recipient).getMessagesSince(clock);
		} else {
			//return empty list
			return list;
		}
	}

}
