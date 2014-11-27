package ch.ethz.inf.vs.android.glukas.project4.test;

import org.junit.Test;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseAccess;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseManager;
import ch.ethz.inf.vs.android.glukas.project4.protocol.Protocol;

public class ProtocolTest {
	
	@SuppressWarnings("unused")
	@Test
	public static void testProtocolInstantiation() {
		DatabaseAccess db = new DatabaseManager(null);
		Protocol protocol = Protocol.getInstance(null, db);
	}
}
