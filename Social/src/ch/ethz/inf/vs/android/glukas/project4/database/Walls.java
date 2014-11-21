package ch.ethz.inf.vs.android.glukas.project4.database;

import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import ch.ethz.inf.vs.android.glukas.project4.Post;
import ch.ethz.inf.vs.android.glukas.project4.Wall;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseContract.PostsEntry;

/**
 * Helper class that implements all functionalities between tables Users and Posts.
 * @author alessiobaehler
 *
 */

class Walls {

	// TODO: Get the whole wall of the user.
	public static Wall getUserWall(SQLiteDatabase db) {
		return null;
	}
	
	// TODO: Get all the Posts in a Wall starting from id -> id or time?
	public static List<Post> getAllPostsUserFrom(int timestamp, SQLiteDatabase db) {
		return null;
	}
	
	// TODO: Get the whole Wall of a certain friend.
	public static Wall getFriendWall(int friendid, SQLiteDatabase db) {
		return null;
	}
	
	/**
	 * Delete the whole saved Wall of a certain friend. In the db structure
	 * this translates into removing all friend's posts
	 * @param friendid
	 * @param db the database
	 */
	public static void deleteFriendWall(int friendid, SQLiteDatabase db) {
		// SQL WHERE clause.
		String selection = PostsEntry.WALL_ID + " == ?";
		// 
		String[] selectionArgs = {Integer.toString(friendid)};
		db.delete(PostsEntry.TABLE_NAME, selection, selectionArgs);
	}
}
