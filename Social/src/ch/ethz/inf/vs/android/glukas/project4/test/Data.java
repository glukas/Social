package ch.ethz.inf.vs.android.glukas.project4.test;

import ch.ethz.inf.vs.android.glukas.project4.BasicUser;
import ch.ethz.inf.vs.android.glukas.project4.UserId;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseAccess;

public class Data {

	public static final int dummyOldPostCount = 34;
	public static final int dummyPostId = 56;
	public static final int dummyNumMsg = 78;
	public static final UserId dummySenderId = new UserId("11111");
	public static final UserId dummyReceiverId = new UserId("-22222");
	public static final BasicUser dummySender = new BasicUser(dummySenderId, "Dummy Sender");
	public static final BasicUser dummyReceiver = new BasicUser(dummyReceiverId, "Dummy Receiver");
	public static final DatabaseAccess db = new DatabaseTest();
	public static final String tag = "###";
}
