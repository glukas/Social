package ch.ethz.inf.vs.android.glukas.project4.database;

import java.util.Date;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import ch.ethz.inf.vs.android.glukas.project4.Post;
import ch.ethz.inf.vs.android.glukas.project4.UserId;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseContract.PostsEntry;

/**
 * Helper class that implements all functionalities between tables Users and Posts.
 * @author alessiobaehler
 *
 */

class Walls {
	
	// Delete user's wall.
	public static void deleteUserWall(SQLiteDatabase db) {
		deleteFriendWall(Utility.userId, db);
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
