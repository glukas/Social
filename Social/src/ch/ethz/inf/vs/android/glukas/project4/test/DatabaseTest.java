package ch.ethz.inf.vs.android.glukas.project4.test;

import org.junit.Test;

import ch.ethz.inf.vs.android.glukas.project4.User;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseAccess;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseManager;
import android.test.AndroidTestCase;

public class DatabaseTest extends AndroidTestCase {
	
	@Test
	public void testUserInsertion() {
		DatabaseAccess db = new DatabaseManager(null);
		String username = "MeIAndMySelf";
		User user = new User(username);
		db.putUser(user);
		User user2 = db.getUser();
		boolean response = user.getUsername().equals(user2.getUsername());
		assertTrue(response);
	}

}
