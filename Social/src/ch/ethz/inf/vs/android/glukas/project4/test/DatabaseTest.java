package ch.ethz.inf.vs.android.glukas.project4.test;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import ch.ethz.inf.vs.android.glukas.project4.Post;
import ch.ethz.inf.vs.android.glukas.project4.User;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseAccess;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseManager;
import android.content.Context;
import android.test.AndroidTestCase;
import android.util.Log;

public class DatabaseTest extends AndroidTestCase{
	
	@Test
	public void testUserInsertion() {
		DatabaseAccess db = new DatabaseManager(getTestContext());
		String username = "MeIAndMySelf";
		User user = new User(username);
		db.putUser(user);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		User user2 = db.getUser();
		boolean response = user.getUsername().equals(user2.getUsername());
		assertTrue(response);
	}
	
	@Test
	public void testInsertPost() {
		DatabaseAccess db = new DatabaseManager(getTestContext());
		Post post1 = new Post(12, Data.dummyReceiverId, Data.dummySenderId, "I'm a message", null, new Date());
		db.putFriendPost(post1, Data.dummySenderId);
		Post post2 = db.getFriendPost(12, Data.dummyReceiverId);
		Log.d("TEST DATABASE", Data.tag+" Post 2 id : "+post2.getId()+ ", PostId : "+post2.getPoster()+ ", OwnerWallId : "+post2.getWallOwner()+", content : "+post2.getText());
	}
	
	/*@Test
	public void testBigIntegersByteConversion() {
		BigInteger id1 = new BigInteger("1");
		byte[] id2 = id1.toByteArray();
		boolean compare = id1.toString().equals(id2.toString());
		assertTrue(compare);
	}*/
	
//	@Test
//	public void testBigIntegersStringConversion() {
//		BigInteger id1 = new BigInteger("1");
//		byte[] id2 = id1.toByteArray();
//		boolean compare = id1.toString().equals(id2.toString());
//		assertTrue(compare);
//	}
	
	/**
	 * @return The {@link Context} of the test project.
	 */
	private Context getTestContext()
	{
	    try
	    {
	        Method getTestContext = AndroidTestCase.class.getMethod("getTestContext");
	        return (Context) getTestContext.invoke(this);
	    }
	    catch (final Exception exception)
	    {
	        exception.printStackTrace();
	        return null;
	    }
	}

}
