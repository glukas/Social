package ch.ethz.inf.vs.android.glukas.project4.test;

import java.util.Date;

import ch.ethz.inf.vs.android.glukas.project4.BasicUser;
import ch.ethz.inf.vs.android.glukas.project4.Post;
import ch.ethz.inf.vs.android.glukas.project4.User;
import ch.ethz.inf.vs.android.glukas.project4.UserId;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseAccess;

public class Data {

	public static final User alice = new User("Alice");
	public static final User bob = new User("Bob");
	public static final User eve = new User("Eve");
	
	public static final Post post1 = new Post(12, alice.getId(), bob.getId(), "1) Hello Bob!", null, new Date());
	public static final Post post2 = new Post(14, bob.getId(), alice.getId(), "2) Hello Alice, how are you?", null, new Date());
	public static final Post post3 = new Post(18, alice.getId(), bob.getId(), "3) Fine, thanks. What about you?", null, new Date());
	public static final Post post4 = new Post(19, bob.getId(), alice.getId(), "4) ...", null, new Date());

	
	public static final int dummyOldPostCount = 34;
	public static final int dummyPostId = 56;
	public static final int dummyNumMsg = 78;
	public static final UserId dummySenderId = new UserId("11111");
	public static final UserId serverId = new UserId("0");
	public static final UserId dummyReceiverId = new UserId("-22222");
	public static final BasicUser dummySender = new BasicUser(dummySenderId, "Dummy Sender");
	public static final BasicUser dummyReceiver = new BasicUser(dummyReceiverId, "Dummy Receiver");
	public static final DatabaseAccess db = new StaticDatabase();
	public static final String tag = "###";
}
