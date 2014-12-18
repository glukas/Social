package ch.ethz.inf.vs.android.glukas.project4.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import ch.ethz.inf.vs.android.glukas.project4.Post;
import ch.ethz.inf.vs.android.glukas.project4.UserId;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseContract.*;

/**
 * Helper class that implements all functionalities of table Posts.
 * @author alessiobaehler
 *
 */
class Posts {
	
	////
	//Deletions
	////
	public static void deleteUserPost(int postid, SQLiteDatabase db) {
		deleteFriendPost(postid, Utility.userId, db);
	}
	
	public static void deleteFriendPost(int postid, UserId friendid, SQLiteDatabase db) {
		// SQL WHERE clause.
		String selection =  DatabaseContract.SELECTIONS.POST_BY_ID_AND_WALL.getCommand();;
		
		// Arguments for selection.
		String[] selectionArgs = {Integer.toString(postid), Utility.toSQLiteId(friendid)};
		
		// Execute delete.
		db.delete(PostsEntry.TABLE_NAME, selection, selectionArgs);
	}
	
	////
	// Insertions
	////

	public static boolean putPost(Post post, SQLiteDatabase db) {
		return putFriendPost(post, db) >= 0;
	}
	
	public static long putFriendPost(Post post, SQLiteDatabase db) {
		// Get data.
		int id = post.getId();
		UserId poster = post.getPoster();
		UserId wallOwner = post.getWallOwner();
		String text = post.getText();
		Bitmap image = post.getImage();
		Date datetime = post.getDateTime();
		
		// Create content to insert.
		ContentValues values = new ContentValues();
		values.put(PostsEntry._ID, id);
		values.put(PostsEntry.POSTER_ID, Utility.toSQLiteId(poster));
		values.put(PostsEntry.WALL_ID, Utility.toSQLiteId(wallOwner));
		if(text != null)
			values.put(PostsEntry.TEXT, text);
		if(image != null)
			values.put(PostsEntry.IMAGE, Utility.toByteArray(image));
		if(datetime != null)
			values.put(PostsEntry.DATE_TIME, Utility.toSQLiteDate(datetime));
		
		// Insert content.
		return db.insert(PostsEntry.TABLE_NAME, null, values);
	}
	
	////
	// Selections
	////
	
	// Get all the Posts in a Wall with from as least id
	public static List<Post> getAllUserPostsFrom(int from, SQLiteDatabase db) {
		// Avoid code duplication by calling this function.
		return getAllFriendPostsFrom(Utility.userId, from, db);
	}
	

	public static Post getUserPost(int postid, SQLiteDatabase db) {
		// Avoid code duplication by calling this function.
		return getFriendPost(postid, Utility.userId, db);
	}
	
	public static boolean containsPost(int postId, UserId author, UserId wallOwner, SQLiteDatabase db) {
		String[] projection = { PostsEntry._ID};
		
		String selection = String.format("%s == ? AND %s == ? AND %s == ? ", PostsEntry._ID, PostsEntry.POSTER_ID, PostsEntry.WALL_ID);
		
		String[] selectionArgs = {Integer.toString(postId), Utility.toSQLiteId(author), Utility.toSQLiteId(wallOwner)};
		
		Cursor cursor = db.query(PostsEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
	
		boolean nonempty = cursor.moveToFirst();
		cursor.close();
		
		return nonempty;
	}
	

	public static Post getFriendPost(int postid, UserId friendid, SQLiteDatabase db) {
		// Columns to project.
		String[] projection = {	PostsEntry._ID,
								PostsEntry.POSTER_ID, 
								PostsEntry.WALL_ID,
								PostsEntry.TEXT,
								PostsEntry.IMAGE,
								PostsEntry.DATE_TIME};
		
		// SQL WHERE clause.
		String selection = DatabaseContract.SELECTIONS.POST_BY_ID_AND_WALL.getCommand();
		
		// Arguments for selection.
		String[] selectionArgs = {Integer.toString(postid), Utility.toSQLiteId(friendid)};
		
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
	
	public static List<Post> getAllFriendPostsFrom(UserId friendid, int from, SQLiteDatabase db) {
		// SQL SELECT clause.
		String[] projection = { PostsEntry._ID, 
								PostsEntry.POSTER_ID, 
								PostsEntry.WALL_ID,
								PostsEntry.TEXT, 
								PostsEntry.IMAGE, 
								PostsEntry.DATE_TIME};
		
		// SQL WHERE clause.
		String selection = PostsEntry.WALL_ID + " == ? AND " + PostsEntry._ID + " >= ?";
		
		// Arguments for selection.
		String[] selectionArgs = {Utility.toSQLiteId(friendid), Integer.toString(from)};
		
		// ORDER BY clause.
		String order = PostsEntry._ID + " ASC";
		
		// Execute query.
		Cursor cursor = db.query(PostsEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, order);
		
		// Instantiate list of post.
		List<Post> posts = new ArrayList<Post>();
		
		// Create Post objects and add them to the list.
		if(cursor.moveToFirst()) {
			while(!cursor.isAfterLast()) {
				posts.add(Utility.buildPost(cursor));
				cursor.moveToNext();
			}
		}
		cursor.close();
		return posts;
	}
	
	// Get numberPosts older than postId
	public static List<Post> getSomeLatestPosts(UserId id, int numberPosts, int postId, SQLiteDatabase db) {
		// Columns to project.
		String[] projection = {	PostsEntry._ID,
								PostsEntry.POSTER_ID, 
								PostsEntry.WALL_ID,
								PostsEntry.TEXT,
								PostsEntry.IMAGE,
								PostsEntry.DATE_TIME};
		
		// SQL WHERE clause.
		String selection = PostsEntry._ID + " <= ? AND " + PostsEntry.WALL_ID + " == ?";
		
		// Arguments for selection.
		String[] selectionArgs = {Integer.toString(postId), Utility.toSQLiteId(id)};
		
		// SQL ORDER BY clause.
		String orderBy = PostsEntry._ID + " DESC";
		
		// SQL LIMIT clause.
		String limit = Integer.toString(numberPosts);
		
		// Execute query.
		Cursor cursor = db.query(PostsEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, orderBy, limit);
		
		List<Post> older = new ArrayList<Post>();
		if(cursor.getCount() > 0 && cursor.moveToFirst()) {
			while(!cursor.isAfterLast()) {
				older.add(Utility.buildPost(cursor));
				cursor.moveToNext();
			}
		}
		cursor.close();
		return older;

	}
}
