package ch.ethz.inf.vs.android.glukas.project4.test;

import java.math.BigInteger;

import org.junit.Test;

import ch.ethz.inf.vs.android.glukas.project4.User;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseAccess;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseManager;
import android.test.AndroidTestCase;
import android.test.InstrumentationTestCase;

public class DatabaseTest extends InstrumentationTestCase{
	
	@Test
	public void testUserInsertion() {
		DatabaseAccess db = new DatabaseManager(getInstrumentation().getContext());
		String username = "MeIAndMySelf";
		User user = new User(username);
		db.putUser(user);
		User user2 = db.getUser();
		boolean response = user.getUsername().equals(user2.getUsername());
		assertTrue(response);
	}
	
	@Test
	public void testBigIntegersByteConversion() {
		BigInteger id1 = new BigInteger("1");
		byte[] id2 = id1.toByteArray();
		boolean compare = id1.toString().equals(id2.toString());
		assertTrue(compare);
	}
	
//	@Test
//	public void testBigIntegersStringConversion() {
//		BigInteger id1 = new BigInteger("1");
//		byte[] id2 = id1.toByteArray();
//		boolean compare = id1.toString().equals(id2.toString());
//		assertTrue(compare);
//	}

}
