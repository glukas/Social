package ch.ethz.inf.vs.android.glukas.project4.database;

import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import ch.ethz.inf.vs.android.glukas.project4.Post;
import ch.ethz.inf.vs.android.glukas.project4.Wall;

/**
 * Helper class that implements all functionalities between tables Users and Posts.
 * @author alessiobaehler
 *
 */

class Walls {

	// TODO: Get the whole wall of the user.
	public Wall getUserWall(SQLiteDatabase db) {
		return null;
	}
	
	// TODO: Get all the Posts in a Wall starting from id -> id or time?
	public List<Post> getAllPostsUserFrom(int timestamp, SQLiteDatabase db) {
		return null;
	}
	
	// TODO: Get the whole Wall of a certain friend.
	public Wall getFriendWall(int friendid, SQLiteDatabase db) {
		return null;
	}
	
	// TODO: Delete the whole saved Wall of a certain friend
	public void deleteFriendWall(int friendid, SQLiteDatabase db) {
		
	}
}
