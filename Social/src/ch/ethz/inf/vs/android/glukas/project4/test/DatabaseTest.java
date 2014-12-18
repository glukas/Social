package ch.ethz.inf.vs.android.glukas.project4.test;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.TimeZone;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ch.ethz.inf.vs.android.glukas.project4.Post;
import ch.ethz.inf.vs.android.glukas.project4.User;
import ch.ethz.inf.vs.android.glukas.project4.UserCredentials;
import ch.ethz.inf.vs.android.glukas.project4.UserId;
import ch.ethz.inf.vs.android.glukas.project4.Wall;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseAccess;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseManager;
import ch.ethz.inf.vs.android.glukas.project4.database.Utility;
import android.content.Context;
import android.test.AndroidTestCase;
import android.util.Log;

public class DatabaseTest extends AndroidTestCase{
	
	// USERS
	
	@Test
	public void testUserInsertion() {
		DatabaseManager db = cleanDB();
	
		db.putUser(Data.alice);
		User user = db.getUser();
		assertEquals(Data.alice, user);
	}
	
	@Test
	public void testUserPostCount() {
		DatabaseManager db = cleanDB();
		
		db.putUser(Data.alice);
		
		int newCount = 10;
		db.setUserPostsCount(newCount);
		int returned = db.getUserPostsCount();
		
		assertEquals(newCount, returned);
	}
	
	@Test
	public void testUserMaxPost() {
		DatabaseManager db = cleanDB();
		
		db.putUser(Data.alice);
		
		int newMaxCount = 20;
		db.setUserPostsCount(newMaxCount);
		int returned = db.getUserPostsCount();
		
		assertEquals(newMaxCount, returned);
	}
	
	@Test
	public void testUserCredentials() {
		DatabaseManager db = cleanDB();
		
		db.putUser(Data.alice);
		
		UserCredentials returned = db.getUserCredentials(Data.alice.getId());
		byte[] temp1 = Data.alice.getCredentials().broadcastAuthenticationKey;
		byte[] temp2 = Data.alice.getCredentials().broadcastEncryptionKey;
		
		assertEquals(Data.alice.getId(), returned.userId);
		int i = 0;
		for(byte elem : temp1) {
			assertEquals(elem, returned.broadcastAuthenticationKey[i]);
			i++;
		}
		i = 0;
		for(byte elem : temp2) {
			assertEquals(elem, returned.broadcastEncryptionKey[i]);
			i++;
		}
	}
	
	@Test
	public void testGetUserFriends() {
		DatabaseManager db = cleanDB();
		
		db.putUser(Data.alice);
		
		List<User> friends = new ArrayList<User>();
		friends.add(Data.bob);
		friends.add(Data.eve);
		
		for(User friend : friends)
			db.putFriend(friend);
		
		List<User> returned = db.getUserFriendsList();
		
		assertTrue(returned.size() == friends.size());
		int i = 0;
		for(User friend : returned) {
			assertEquals(friend.getId(), friends.get(i).getId());
			assertEquals(friend.getUsername(), friends.get(i).getUsername());
			i++;
		}
		
	}
	
	// POSTS

	/*public void testInsertFriendPost() {
		DatabaseManager db = cleanDB();
		
		db.putUser(Data.alice);
		db.putFriend(Data.bob);
		
		db.putPost(Data.post2);
		
		Post returned = db.getFriendPost(Data.post2.getId(), Data.post2.getPoster());

		assertEquals(Data.post2, returned);
//		Log.d("TEST DATABASE", Data.tag+" Post 2 id : "+post2.getId()+ ", PostId : "+post2.getPoster()+ ", OwnerWallId : "+post2.getWallOwner()+", content : "+post2.getText());
	}*/
	
	/*
	@Test
	public void testDeleteUserPost() {
		DatabaseManager db = cleanDB();
		
		db.putUser(Data.alice);
		
		db.putPost(Data.post1);
		
		db.deleteUserPost(Data.post1.getId());
		
		assertTrue(db.getUserPost(Data.post1.getId()) == null);
	}*/

	@Test
	public void testGetSomePosts() {
		DatabaseManager db = cleanDB();
		
		db.putUser(Data.alice);
		db.putFriend(Data.bob);
		
		List<Post> listPostInsert = new ArrayList<Post>();		
		listPostInsert.add(Data.post1);
		listPostInsert.add(Data.post3);
		
		for(Post post : listPostInsert)
			db.putPost(post);

		List<Post> listPost = db.getSomeLatestPosts(Data.bob.getId(), 4, 30);
		Collections.sort(listPost, new postIdComparator());
		assertTrue(listPost.size() == listPostInsert.size());
		
		int i = 0;
		for(Post post : listPost) {
			Post inserted = listPostInsert.get(i);
			assertEquals(post.getId(), inserted.getId());
			assertEquals(post.getText(), inserted.getText());
			assertEquals(post.getPoster(), inserted.getPoster());
			i++;
		}
	}
	
	// WALLS
	
	@Test
	public void testWall() {
		DatabaseManager db = cleanDB();
		
		db.putUser(Data.alice);
		
		List<Post> posts = new ArrayList<Post>();
		posts.add(Data.post1);
		posts.add(Data.post3);
		
		for(Post post : posts)
			db.putPost(post);
	
		Wall returned = db.getUserWall();
		List<Post> returnedPosts = returned.getPosts();
		Collections.sort(returnedPosts, new postIdComparator());
		
		int i = 0;
		for(Post post : returned.getPosts()) {
			Post inserted = posts.get(i);
			assertEquals(post.getId(), inserted.getId());
			assertEquals(post.getText(), inserted.getText());
			assertEquals(post.getPoster(), inserted.getPoster());
			i++;
		}
		
		db.deleteUserWall();
		
		assertTrue(db.getUserWall() != null);
		assertTrue(db.getUserWall().getPosts().isEmpty());
	}
	
	// FRIENDS
	
	@Test
	public void testGetFriendUsername() {
		DatabaseManager db = cleanDB();
		
		db.putFriend(Data.bob);
		
		assertEquals(Data.bob.getUsername(), db.getFriendUsername(Data.bob.getId()));
	}
	
	/*
	@Test
	public void testDeleteFriend() {
		DatabaseManager db = cleanDB();
		
		db.putFriend(Data.bob);
		
		db.deleteFriend(Data.bob.getId());
		
		assertTrue(db.getFriend(Data.bob.getId()) == null);
	}*/
	
	
	
	// UTILITY
	
	@Test
	public void testDatetoStringConversionConversion() {
		Date now = new Date();
		Date now2 = Utility.toJavaDate(Utility.toSQLiteDate(now));
		assertTrue(now.toString().equals(now2.toString()));
	}
	
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
	
	/**
	 * Get a db instance and cleans it
	 * We need to clean up the db using it, because there's a single
	 * instance and thus there could be conflicts with the previous data
	 * e.g. putUser
	 * @return the database instance
	 */
	private DatabaseManager cleanDB() {
		DatabaseManager db = new DatabaseManager(getTestContext());
		db.resetDB();
		return db;
	}
	
	// Assmues there aren't two posts with the same id!
	private class postIdComparator implements Comparator<Post> {
		@Override
		public int compare(Post a, Post b) {
			return a.getId() < b.getId() ? -1 : 1;
		}
	}
}
