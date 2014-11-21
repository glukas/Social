package ch.ethz.inf.vs.android.glukas.project4.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import ch.ethz.inf.vs.android.glukas.project4.User;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseContract.FriendsEntry;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseContract.UsersEntry;

/**
 * Helper class that implements all functionalities of table Friends.
 * @author alessiobaehler
 *
 */
class Friends {

	/** FIXME: duplicate code of putUser
	 * Add a friend in the List of Friends of the user
	 * @param user
	 * @param db
	 */
	public static void putFriend(User user, SQLiteDatabase db) {
		Users.putUser(user, db);
	}
	
	/**
	 * Remove friend from the List of friends & everything associated with him/her
	 * @param id
	 * @param db
	 */
	public static void deleteFriend(int id, SQLiteDatabase db) {
		// Delete posts
		Walls.deleteFriendWall(id, db);
		
		// SQL WHERE clause.
		String selection = FriendsEntry.USER_ID + " == ? AND " + FriendsEntry.FRIEND_ID + " == ?";

		// Arguments for selection.
		String[] selectionArgs1 = {Integer.toString(Utility.userID), Integer.toString(id)};
		
		// Delete friendship
		db.delete(FriendsEntry.TABLE_NAME, selection, selectionArgs1);
		

		selection = UsersEntry._ID + " == ?";
		String[] selectionArgs2 = {Integer.toString(id)};

		// Delete friend data
		db.delete(UsersEntry.TABLE_NAME, selection, selectionArgs2);
	}
}
