package ch.ethz.inf.vs.android.glukas.project4.database;

import java.util.Date;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import ch.ethz.inf.vs.android.glukas.project4.Post;
import ch.ethz.inf.vs.android.glukas.project4.UserId;
import ch.ethz.inf.vs.android.glukas.project4.Wall;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseContract.PostsEntry;

/**
 * Helper class that implements all functionalities between tables Users and Posts.
 * @author alessiobaehler
 *
 */

class Walls {

	// Get the whole wall of the user.
	public static Wall getUserWall(SQLiteDatabase db) {
		return getFriendWall(Utility.userID, db);
	}
	
	// Delete user's wall.
	public static void deleteUserWall(SQLiteDatabase db) {
		deleteFriendWall(Utility.userID, db);
	}
	
	// Get the whole Wall of a certain friend.
	public static Wall getFriendWall(UserId friendid, SQLiteDatabase db) {
		// SQL SELECT clause
		String[] projection = {PostsEntry._ID, PostsEntry.POSTER_ID, PostsEntry.TEXT, PostsEntry.IMAGE, PostsEntry.DATE_TIME};
		// SQL WHERE clause
		String selection = PostsEntry.WALL_ID + " == ?";
		
		// Arguments for selection.
		String[] selectionArgs = {Utility.toSQLiteId(friendid)};
		
		// SQL ORDER BY clause.
		String order = PostsEntry._ID + " DESC";
		
		// Execute query.
		Cursor cursor = db.query(PostsEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, order);
		
		// Instantiate Wall object
		Wall wall = new Wall();
		
		// Add posts to wall
		if(cursor.moveToFirst()) {
			while(!cursor.isAfterLast()) {
				// Add post to wall.
				wall.posts.add(Utility.buildPost(cursor));
				cursor.moveToNext();
			}
			// Close cursor
			cursor.close();
			
			// Return wall
			return wall;
		} else {
			// Close cursor
			cursor.close();
			return null;
		}
	}
	
	/**
	 * Delete the whole saved Wall of a certain friend. In the db structure
	 * this translates into removing all friend's posts
	 * @param friendid
	 * @param db the database
	 */
	public static void deleteFriendWall(UserId friendid, SQLiteDatabase db) {
		// SQL WHERE clause.
		String selection = PostsEntry.WALL_ID + " == ?";
		
		// Arguments for selection. They must be the string representation
		// of the byte array corresponding to the id.
		String[] selectionArgs = {Utility.toSQLiteId(friendid)};
		
		// Execute delete.
		db.delete(PostsEntry.TABLE_NAME, selection, selectionArgs);
	}
}
