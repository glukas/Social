package ch.ethz.inf.vs.android.glukas.project4.database;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ch.ethz.inf.vs.android.glukas.project4.User;
import ch.ethz.inf.vs.android.glukas.project4.UserId;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseContract.FriendsEntry;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseContract.UsersEntry;

/**
 * Helper class that implements all functionalities of table Friends.
 * @author alessiobaehler
 *
 */
class Friends {

	// Get the upper bound of the number of posts in the friend's wall.
	public static int getFriendPostsCount(UserId id, SQLiteDatabase db) {
		// SQL SELECT clause
		String[] projection = {UsersEntry.COUNT};
		// SQL WHERE clause
		String selection = UsersEntry.USER_ID + " == ?";
		// Arguments for selection
		String[] selectionArgs = {Utility.toSQLiteId(id).toString()};
		
		// Execute query
		Cursor cursor = db.query(UsersEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
		
		// Get and return result.
		if(cursor.moveToFirst()) {
			cursor.close();
			return cursor.getInt(0);
		} else {
			cursor.close();
			return -1;
		}
	}
	

	// Get the upper bound over the partial order of actual posts for the friend.
	public static int getFriendMaxPostsId(UserId id, SQLiteDatabase db) {
		// SQL SELECT clause
		String[] projection = {UsersEntry.MAX};
		// SQL WHERE clause
		String selection = UsersEntry.USER_ID + " == ?";
		// Arguments for selection
		String[] selectionArgs = {Utility.toSQLiteId(id).toString()};
		
		// Execute query
		Cursor cursor = db.query(UsersEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
		
		// Get and return result.
		if(cursor.moveToFirst()) {
			cursor.close();
			return cursor.getInt(0);
		} else {
			cursor.close();
			return -1;
<<<<<<< HEAD
		}
=======

>>>>>>> branch 'master' of https://github.com/glukas/Social
	}
	
	// TODO: Create a friendship relation between the user and a new friend.
	public static void putFriendship(UserId id, String username, SQLiteDatabase db) {
		
	}
	
	// Get an user name from an user id
	public static String getFriendUsername(UserId id, SQLiteDatabase db) {
		// SQL SELECT clause
		String[] projection = {UsersEntry.USERNAME};
		// SQL WHERE clause
		String selection = UsersEntry.USER_ID + " == ?";
		// Arguments for selection
		String[] selectionArgs = {Utility.toSQLiteId(id).toString()};
		
		// Execute query
		Cursor cursor = db.query(UsersEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
		
		// Get and return result.
		if(cursor.moveToFirst()) {
			cursor.close();
			return cursor.getString(0);
		} else {
			cursor.close();
			return null;
		}
	}
	
	// Get an user id from an user name (cannot ensures uniqueness)
	public static List<UserId> getFriendId(String username, SQLiteDatabase db) {
		// SQL SELECT clause
		String[] projection = {UsersEntry.USER_ID};
		// SQL WHERE clause
		String selection = UsersEntry.USERNAME + " == ?";
		// Arguments for selection
		String[] selectionArgs = {username};
		
		// Execute query
		Cursor cursor = db.query(UsersEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
		
		// Instantiate id list.
		List<UserId> idsList = new ArrayList<UserId>();
		
		// Get and return result.
		if(cursor.moveToFirst()) {
			while(!cursor.isAfterLast()) {
				idsList.add(new UserId(cursor.getBlob(0).toString()));
				cursor.moveToNext();
			}
			cursor.close();
			return idsList;
		} else {
			cursor.close();
			return null;
		}
	}
		
	/**
	 * Add a friend in the List of Friends of the user
	 * @param user
	 * @param db
	 */
	public static void putFriend(User user, SQLiteDatabase db) {
		Users.putUser(user, db);
	}
	
	/** TODO
	 * Remove friend from the List of friends & everything associated with him/her
	 * @param id
	 * @param db
	 */
	public static void deleteFriend(UserId id, SQLiteDatabase db) {
		// Delete posts
		Walls.deleteFriendWall(id, db);
		
		// SQL WHERE clause.
		String selection = FriendsEntry.USER_ID + " == ? AND " + FriendsEntry.FRIEND_ID + " == ?";

		// Arguments for selection.
		String[] selectionArgs1 = {Utility.toSQLiteId(Utility.userID).toString(), Utility.toSQLiteId(id).toString()};
		
		// Delete friendship
		db.delete(FriendsEntry.TABLE_NAME, selection, selectionArgs1);
		

		selection = UsersEntry.USER_ID + " == ?";
		String[] selectionArgs2 = {Utility.toSQLiteId(id).toString()};

		// Delete friend data
		db.delete(UsersEntry.TABLE_NAME, selection, selectionArgs2);
	}
}
