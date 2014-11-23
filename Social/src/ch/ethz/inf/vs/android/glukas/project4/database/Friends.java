package ch.ethz.inf.vs.android.glukas.project4.database;

import java.math.BigInteger;
import java.util.List;

import android.content.ContentValues;
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

	// TODO: Get the upper bound of the number of posts in the friend's wall.
	public static int getFriendPostsCount(UserId id, SQLiteDatabase db) {
		return 0;
	}
	
	// TODO: Get the upper bound over the partial order of actual posts for the friend.
	public static int getFriendMaxId(UserId id, SQLiteDatabase db) {
		return 0;
	}
	
	// TODO: Create a friendship relation between the user and a new friend.
	public static void putFriendship(UserId id, String username, SQLiteDatabase db) {
		
	}
	
	// TODO: Get an user name from an user id
	public static String getFriendUsername(UserId id, SQLiteDatabase db) {
		return null;
	}
	
	// TODO: Get an user id from an user name (cannot ensures uniqueness)
	public static List<UserId> getFriendId(String username, SQLiteDatabase db) {
		return null;
	}
		
	/** FIXME: duplicate code of putUser
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
//		// Delete posts
//		Walls.deleteFriendWall(id, db);
//		
//		// SQL WHERE clause.
//		String selection = FriendsEntry.USER_ID + " == ? AND " + FriendsEntry.FRIEND_ID + " == ?";
//
//		// Arguments for selection.
//		String[] selectionArgs1 = {Utility.userID.toString(), Integer.toString(id)};
//		
//		// Delete friendship
//		db.delete(FriendsEntry.TABLE_NAME, selection, selectionArgs1);
//		
//
//		selection = UsersEntry._ID + " == ?";
//		String[] selectionArgs2 = {Integer.toString(id)};
//
//		// Delete friend data
//		db.delete(UsersEntry.TABLE_NAME, selection, selectionArgs2);
	}
}
