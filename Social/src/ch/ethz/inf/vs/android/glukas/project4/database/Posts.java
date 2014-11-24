package ch.ethz.inf.vs.android.glukas.project4.database;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import ch.ethz.inf.vs.android.glukas.project4.Post;
import ch.ethz.inf.vs.android.glukas.project4.UserId;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseContract.PostsEntry;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseContract.UsersEntry;

/**
 * Helper class that implements all functionalities of table Posts.
 * @author alessiobaehler
 *
 */
class Posts {

	/**
	 * Update the wall of the user with the given post.
	 * SQL code: 
	 * @param post the post to insert
	 * @param db the SQLiteDatabase
	 */
	public static void putUserPost(Post post, SQLiteDatabase db) {
		// Avoid code duplication by calling this function.
		putFriendPost(post, Utility.userID, db);
	}
	
	// Get all the Posts in a Wall with from as least id
	public static List<Post> getAllUserPostsFrom(int from, SQLiteDatabase db) {
		// Avoid code duplication by calling this function.
		return getAllFriendPostsFrom(Utility.userID, from, db);
	}
	
	/**
	 * Delete a certain post from the user's wall.
	 * @param id the id of the post to delete
	 * @param db SQLliteDatabase to query
	 */
	public static void deleteUserPost(int postid, SQLiteDatabase db) {
		deleteFriendPost(postid, Utility.userID, db);
	}
	
	/**
	 * Get a certain post from the user's wall.
	 * SQL query: SELECT p_id, p.text, p.image, p.date_time, p.id FROM users u, posts p WHERE u._id == Utility.userID AND u._id == p.wall_id AND p._id == id
	 * @param postid the id of the post to retrieve
	 * @param db SQLliteDatabase to query
	 * @return a Post object if it was found, else null
	 */
	public static Post getUserPost(int postid, SQLiteDatabase db) {
		// Avoid code duplication by calling this function.
		return getFriendPost(postid, Utility.userID, db);
	}

	/**
	 * Update the wall of a friend whose wall is saved on our phone.
	 * @param post
	 * @param friendid
	 * @param db
	 */
	public static void putFriendPost(Post post, UserId friendid, SQLiteDatabase db) {
		// Get data.
		int id = post.getId();
		UserId poster = post.getPoster();
		String text = post.getText();
		Bitmap image = post.getImage();
		Date datetime = post.getDateTime();
		
		// Create content to insert.
		ContentValues values = new ContentValues();
		values.put(PostsEntry._ID, id);
		values.put(PostsEntry.POSTER_ID, Utility.toSQLiteId(poster));
		values.put(PostsEntry.TEXT, text);
		values.put(PostsEntry.IMAGE, Utility.toByteArray(image));
		values.put(PostsEntry.WALL_ID, Utility.toSQLiteId(friendid));
		values.put(PostsEntry.DATE_TIME, Utility.toSQLiteDate(datetime));
		
		// Insert content.
		db.insert(PostsEntry.TABLE_NAME, null, values);
	}
	
	/**
	 * Get a certain Post from a certain friend.
	 * @param postid
	 * @param friendid
	 * @param db
	 * @return
	 */
	public static Post getFriendPost(int postid, UserId friendid, SQLiteDatabase db) {
		// Columns to project.
		String[] projection = {PostsEntry._ID, PostsEntry.POSTER_ID, PostsEntry.TEXT, PostsEntry.IMAGE, PostsEntry.DATE_TIME};
		
		// SQL WHERE clause.
		String selection = PostsEntry._ID + " == ? AND" + PostsEntry.WALL_ID + " == ?";
		
		// Arguments for selection.
		String[] selectionArgs = {Integer.toString(postid), Utility.toSQLiteId(friendid).toString()};
		
		// Execute query.
		Cursor cursor = db.query(PostsEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

		// Create Post object to return.
		if(cursor.moveToFirst()) {
			// Build message.
			Post post = Utility.buildPost(cursor);
			// Close cursor
			cursor.close();
			return post;
		}
		else {
			// Close cursor
			cursor.close();
			return null;
		}	
	}
	
	/**
	 * Get all Posts of a certain friend with id at least from.
	 * @param timestamp
	 * @param friendid
	 * @param db
	 * @return
	 */
	public static List<Post> getAllFriendPostsFrom(UserId friendid, int from, SQLiteDatabase db) {
		// SQL SELECT clause.
		String[] projection = {PostsEntry._ID, PostsEntry.POSTER_ID, PostsEntry.TEXT, PostsEntry.IMAGE, PostsEntry.DATE_TIME};
		
		// SQL WHERE clause.
		String selection = PostsEntry.WALL_ID + " == ? AND " + PostsEntry.DATE_TIME + " > ?";
		
		// Arguments for selection.
		String[] selectionArgs = {Utility.toSQLiteId(friendid).toString(), Integer.toString(from)};
		
		// ORDER BY clause.
		String order = PostsEntry._ID + " DESC";
		
		// Execute query.
		Cursor cursor = db.query(PostsEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, order);
		
		// Instantiate list of post.
		List<Post> posts = new ArrayList<Post>();
		
		// Create Post objects and add them to the list.
		if(cursor.moveToFirst()) {
			do {
				posts.add(Utility.buildPost(cursor));
			} while(cursor.moveToNext());
			
			// Close cursor
			cursor.close();
			return posts;
		}
		else {
			// Close cursor.
			cursor.close();
			return null;
		}	
	}
	
	/**
	 * Delete a certain Post of a certain friend.
	 * @param postid
	 * @param friendid
	 * @param db
	 */
	public static void deleteFriendPost(int postid, UserId friendid, SQLiteDatabase db) {
		// SQL WHERE clause.
		String selection = PostsEntry._ID + " == ? AND" + PostsEntry.WALL_ID + " == ?";
		
		// Arguments for selection.
		String[] selectionArgs = {Integer.toString(postid), Utility.toSQLiteId(friendid).toString()};
		
		// Execute delete.
		db.delete(PostsEntry.TABLE_NAME, selection, selectionArgs);
	}
}
