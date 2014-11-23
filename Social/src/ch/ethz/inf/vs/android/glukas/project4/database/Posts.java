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
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseContract.PostsEntry;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseContract.UsersEntry;

/**
 * Helper class that implements all functionalities of table Posts.
 * @author alessiobaehler
 *
 */
class Posts {

	/** FIXME
	 * Update the wall of the user with the given post.
	 * SQL code: 
	 * @param post the post to insert
	 * @param db the SQLiteDatabase
	 */
	public static void putUserPost(Post post, SQLiteDatabase db) {
		// Avoid code duplication by calling this function.
//		putFriendPost(post, Utility.userID, db);
	}
	
	/** FIXME
	 * Get a certain post from the user's wall.
	 * SQL query: SELECT p_id, p.text, p.image, p.date_time, p.id FROM users u, posts p WHERE u._id == Utility.userID AND u._id == p.wall_id AND p._id == id
	 * @param postid the id of the post to retrieve
	 * @param db SQLliteDatabase to query
	 * @return a Post object if it was found, else null
	 */
	public static Post getUserPost(int postid, SQLiteDatabase db) {
		// Avoid code duplication by calling this function.
//		return getFriendPost(postid, Utility.userID, db);
		return null;
	}
	
	/** FIXME
	 * Delete a certain post from the user's wall.
	 * @param id the id of the post to delete
	 * @param db SQLliteDatabase to query
	 */
	public static void deleteUserPost(int postid, SQLiteDatabase db) {
//		deleteFriendPost(postid, Utility.userID, db);
	}

	// TODO: Get all the Posts in a Wall starting from timestamp
	public static List<Post> getAllUserPostsFrom(int timestamp, SQLiteDatabase db) {
		String[] projection = {PostsEntry._ID, PostsEntry.TEXT, PostsEntry.IMAGE, PostsEntry.DATE_TIME};
		String selection = PostsEntry.WALL_ID + " == ? AND " + PostsEntry.DATE_TIME + " > ?";
		String[] selectionArgs = {Utility.userID.toString(), Integer.toString(timestamp)};
		
		Cursor cursor = db.query(PostsEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
		
		List<Post> posts = new ArrayList<Post>();
		
		// Create Post object to return.
		if(cursor.moveToFirst()) {
			do {
				int id = cursor.getInt(0);
				String text = cursor.getString(2);
				Bitmap image = Utility.toBitmap(cursor.getBlob(3));
				Date datetime = Utility.toJavaDate(cursor.getString(4));	// still to implement
				posts.add(new Post(id, text, image, datetime));
			} while(cursor.moveToNext());
			
			// Close cursor
			cursor.close();
			return posts;
		}
		else {
			cursor.close();
			return null;
		}	
	}
	

	/** FIXME
	 * Update the wall of a friend whose wall is saved on our phone.
	 * @param post
	 * @param friendid
	 * @param db
	 */
	public static void putFriendPost(Post post, int friendid, SQLiteDatabase db) {
		// Get data.
		String text = post.getText();
		Bitmap image = post.getImage();
		Date datetime = post.getDateTime();
		
		// Transform bitmap to byte[]
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.PNG, 100, bos);	// TODO: adapt quality (second parameter)
		byte[] bArray = bos.toByteArray();
		
		// Convert datetime into string format accepted by SQLite
		// TODO: use simpledateformat
		String dtNew = 
				Integer.toString(datetime.getYear()) + "-" + Integer.toString(datetime.getMonth()) + 
				"-" + Integer.toString(datetime.getDay()) + " " + Integer.toString(datetime.getHours()) + 
				":" + Integer.toString(datetime.getMinutes()) + ":" + Integer.toString(datetime.getSeconds());
		
		// Create content to insert.
		ContentValues values = new ContentValues();
		values.put(PostsEntry.TEXT, text);
		values.put(PostsEntry.IMAGE, bArray);
		values.put(PostsEntry.WALL_ID, friendid);
		values.put(PostsEntry.DATE_TIME, dtNew);
		
		// Insert content.
		db.insert(PostsEntry.TABLE_NAME, null, values);
	}
	
	/** FIXME
	 * Get a certain Post from a certain friend.
	 * @param postid
	 * @param friendid
	 * @param db
	 * @return
	 */
	public static Post getFriendPost(int postid, int friendid, SQLiteDatabase db) {
		// Columns to project.
		String[] projection = {PostsEntry._ID, PostsEntry.TEXT, PostsEntry.IMAGE, PostsEntry.DATE_TIME};
		
		// SQL WHERE clause.
		String selection = PostsEntry._ID + " == ? AND" + PostsEntry.WALL_ID + " == ?";
		
		// Arguments for selection.
		String[] selectionArgs = {Integer.toString(postid), Integer.toString(friendid)};
		
		// Execute query.
		Cursor cursor = db.query(PostsEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

		// Create Post object to return.
		if(cursor.moveToFirst()) {
			int id = cursor.getInt(0);
			String text = cursor.getString(2);
			Bitmap image = Utility.toBitmap(cursor.getBlob(3));
			Date datetime = Utility.toJavaDate(cursor.getString(4));	// still to implement
			// Close cursor
			cursor.close();
			return new Post(id, text, image, datetime);
		}
		else {
			cursor.close();
			return null;
		}	
	}
	
	/** FIXME
	 * Get all Posts of a certain friend starting at a certain time/timestamp.
	 * @param timestamp
	 * @param friendid
	 * @param db
	 * @return
	 */
	public static List<Post> getAllFriendPostsfrom(Date timestamp, int friendid, SQLiteDatabase db) {
		return null;
	}
	
	/** FIXME
	 * Delete a certain Post of a certain friend.
	 * @param postid
	 * @param friendid
	 * @param db
	 */
	public static void deleteFriendPost(int postid, int friendid, SQLiteDatabase db) {
		// SQL WHERE clause.
		String selection = PostsEntry._ID + " == ? AND" + PostsEntry.WALL_ID + " == ?";
		
		// Arguments for selection.
		String[] selectionArgs = {Integer.toString(postid), Integer.toString(friendid)};
		
		// Execute delete.
		db.delete(PostsEntry.TABLE_NAME, selection, selectionArgs);
	}
}
