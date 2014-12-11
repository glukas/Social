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
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseContract.PostsEntry;
import ch.ethz.inf.vs.android.glukas.project4.database.Definitions.POSTS_ENTRY;

/**
 * Helper class that implements all functionalities of table Posts.
 * @author alessiobaehler
 *
 */
class Posts implements PostsInterface{
	
	////
	//Deletions
	////
	@Override
	public void deleteUserPost(int postid, SQLiteDatabase db) {
		deleteFriendPost(postid, Utility.userID, db);
	}
	
	@Override
	public void deleteFriendPost(int postid, UserId friendid, SQLiteDatabase db) {
		// SQL WHERE clause.
		String selection = Definitions.SELECTIONS.POST_BY_ID_AND_WALL.getCommand();
		
		// Arguments for selection.
		String[] selectionArgs = {Integer.toString(postid), Utility.toSQLiteId(friendid)};
		
		// Execute delete.
		db.delete(PostsEntry.TABLE_NAME, selection, selectionArgs);
	}
	
	////
	// Insertions
	////

	@Override
	public void putUserPost(Post post, SQLiteDatabase db) {
		// Avoid code duplication by calling this function.
		putFriendPost(post, Utility.userID, db);
	}
	
	@Override
	public void putFriendPost(Post post, UserId friendid, SQLiteDatabase db) {
		// Get data.
		int id = post.getId();
		UserId poster = post.getPoster();
		String text = post.getText();
		Bitmap image = post.getImage();
		Date datetime = post.getDateTime();
		
		// Create content to insert.
		ContentValues values = new ContentValues();
		values.put(POSTS_ENTRY._ID.getStr(), id);
		values.put(POSTS_ENTRY.POSTER_ID.getStr(), Utility.toSQLiteId(poster));
		values.put(POSTS_ENTRY.WALL_ID.getStr(), Utility.toSQLiteId(friendid));
		if(text != null)
			values.put(POSTS_ENTRY.TEXT.getStr(), text);
		if(image != null)
			values.put(POSTS_ENTRY.IMAGE.getStr(), Utility.toByteArray(image));
		
		if(datetime != null)
			values.put(POSTS_ENTRY.DATE_TIME.getStr(), Utility.toSQLiteDate(datetime));
		
		// Insert content.
		db.insert(POSTS_ENTRY.TABLE_NAME.getStr(), null, values);
	}
	
	////
	// Selections
	////
	
	// Get all the Posts in a Wall with from as least id
	public List<Post> getAllUserPostsFrom(int from, SQLiteDatabase db) {
		// Avoid code duplication by calling this function.
		return getAllFriendPostsFrom(Utility.userID, from, db);
	}
	
	@Override
	public Post getUserPost(int postid, SQLiteDatabase db) {
		// Avoid code duplication by calling this function.
		return getFriendPost(postid, Utility.userID, db);
	}
	
	@Override
	public Post getFriendPost(int postid, UserId friendid, SQLiteDatabase db) {
		// Columns to project.
		String[] projection = {	POSTS_ENTRY._ID.getStr(),
								POSTS_ENTRY.POSTER_ID.getStr(), 
								POSTS_ENTRY.WALL_ID.getStr(),
								POSTS_ENTRY.TEXT.getStr(),
								POSTS_ENTRY.IMAGE.getStr(),
								POSTS_ENTRY.DATE_TIME.getStr(),};
		
		// SQL WHERE clause.
		String selection = Definitions.SELECTIONS.POST_BY_ID_AND_WALL.getCommand();
		
		// Arguments for selection.
		String[] selectionArgs = {Integer.toString(postid), Utility.toSQLiteId(friendid)};
		
		// Execute query.
		Cursor cursor = db.query(POSTS_ENTRY.TABLE_NAME.getStr(), projection, selection, selectionArgs, null, null, null);

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
	
	@Override
	public List<Post> getAllFriendPostsFrom(UserId friendid, int from, SQLiteDatabase db) {
		// SQL SELECT clause.
		String[] projection = {PostsEntry._ID, PostsEntry.POSTER_ID, PostsEntry.TEXT, PostsEntry.IMAGE, PostsEntry.DATE_TIME};
		
		// SQL WHERE clause.
		String selection = PostsEntry.WALL_ID + " == ? AND " + PostsEntry.DATE_TIME + " > ?";
		
		// Arguments for selection.
		String[] selectionArgs = {Utility.toSQLiteId(friendid), Integer.toString(from)};
		
		// ORDER BY clause.
		String order = PostsEntry._ID + " DESC";
		
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
	
	// Get numberPosts older than postId
	public List<Post> getSomeLatestPosts(UserId id, int numberPosts, int postId, SQLiteDatabase db) {
		// SQL SELECT clause.
		String[] projection = null;
		
		// SQL WHERE clause.
		String selection = PostsEntry._ID + " > ? AND" + PostsEntry.WALL_ID + " == ?";
		
		// Arguments for selection.
		String[] selectionArgs = {Integer.toString(postId), Utility.toSQLiteId(id)};
		
		// SQL ORDER BY clause.
		String orderBy = PostsEntry._ID + " DESC";
		
		// SQL LIMIT clause.
		String limit = Integer.toString(numberPosts);
		
		// Execute query.
		Cursor cursor = db.query(PostsEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, orderBy, limit);
		
		List<Post> older = new ArrayList<Post>();
		if(cursor.moveToFirst()) {
			while(!cursor.isAfterLast()) {
				older.add(Utility.buildPost(cursor));
				cursor.moveToNext();
			}
			return older;
		}
		else
			return null;
	}
}
