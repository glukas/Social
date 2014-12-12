package ch.ethz.inf.vs.android.glukas.project4.test;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.lang.reflect.Method;
import java.util.ArrayList;
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

//	@Test
//	public void testUserInsertion() {
//		DatabaseAccess db = new DatabaseManager(null);	// FIXME: context can't be null!! -> sqlError
//		String username = "MeIAndMySelf";
//		User user = new User(username);
//		db.putUser(user);
//		User user2 = db.getUser();
//		boolean response = user.getUsername().equals(user2.getUsername());
//		assertTrue(response);
//	}
//	
//	@Test
//	public void testBigIntegersByteConversion() {
//		BigInteger id1 = new BigInteger("1");
//		byte[] id2 = id1.toByteArray();
//		boolean compare = id1.toString().equals(id2.toString());
//		assertTrue(compare);
//	}
	
	@Test
	public void testUserInsertion() {
		DatabaseAccess db = new DatabaseManager(getTestContext());
		String username = "MeIAndMySelf";
		User user = new User(username);
		db.putUser(user);
		User user2 = db.getUser();
		boolean response = user.getUsername().equals(user2.getUsername());
		boolean response2 = user.getId().equals(user2.getId());
		assertTrue(response && response2);
	}
	
	@Test
	public void testDatetoStringConversionConversion() {
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
		String nowString = format.format(now);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("CET"));
		calendar.setTime(now);
		String calNow = format.format(calendar.getTime());
		String answer = "2014-12-7 14:16:11";
		assertTrue(answer.equals(calNow));
	}
	
	public void testInsertPost() {
		DatabaseAccess db = new DatabaseManager(getTestContext());
		Post post1 = new Post(12, Data.dummyReceiverId, Data.dummySenderId, "I'm a message", null, new Date());
		db.putFriendPost(post1, Data.dummySenderId);
		Post post2 = db.getFriendPost(12, Data.dummyReceiverId);
		assertEquals(post1, post2);
		assertEquals(post1.getText(), post2.getText());
		//Log.d("TEST DATABASE", Data.tag+" Post 2 id : "+post2.getId()+ ", PostId : "+post2.getPoster()+ ", OwnerWallId : "+post2.getWallOwner()+", content : "+post2.getText());
	}
	
	@Test
	public void testGetSomePosts() {
		DatabaseAccess db = new DatabaseManager(getTestContext());
		List<Post> listPostInsert = new ArrayList<Post>();
		Post post1 = new Post(1, Data.dummyReceiverId, Data.dummySenderId, "I'm a message1", null, new Date());
		Post post2 = new Post(2, Data.dummyReceiverId, Data.dummySenderId, "I'm a message2", null, new Date());
		Post post3 = new Post(3, Data.dummyReceiverId, Data.dummySenderId, "I'm a message3", null, new Date());
		Post post4 = new Post(4, Data.dummyReceiverId, Data.dummySenderId, "I'm a message4", null, new Date());
		listPostInsert.add(post1);
		listPostInsert.add(post2);
		listPostInsert.add(post3);
		listPostInsert.add(post4);
		db.putFriendPost(post1, Data.dummySenderId);
		db.putFriendPost(post2, Data.dummySenderId);
		db.putFriendPost(post3, Data.dummySenderId);
		db.putFriendPost(post4, Data.dummySenderId);
		List<Post> listPost = db.getSomeLatestPosts(Data.dummySenderId, 4, 4);
		assertTrue(listPost.size() == 4);
		int i = 0;
		for (Post p : listPost){
			assertEquals(listPostInsert.get(3-i), p);
			assertEquals(listPostInsert.get(3-i).getText(), p.getText());
			//Log.d("", "###"+p.getText()+" "+p.getId()+ " "+p.getWallOwner());
			i++;
		}
	}
	
	/*@Test
	public void testBigIntegersByteConversion() {
		BigInteger id1 = new BigInteger("1");
		byte[] id2 = id1.toByteArray();
		boolean compare = id1.toString().equals(id2.toString());
		assertTrue(compare);
	}*/
	
//	@Test
//	public void testStringtoDateConversionConversion() {
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
