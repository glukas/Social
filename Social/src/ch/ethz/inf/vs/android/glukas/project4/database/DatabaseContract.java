package ch.ethz.inf.vs.android.glukas.project4.database;

import android.provider.BaseColumns;

/**
 * Contract class. It defines the structure of the database.
 * @author alessiobaehler
 *
 */

public final class DatabaseContract {
	
	// Void construct to avoid instantiation.
	public DatabaseContract() {
		
	}
	
	// Structure of table friends.
	public static abstract class FriendsEntry implements BaseColumns {
		public static final String TABLE_NAME = "friends";
		public static final String USER_ID = "user_id";
		public static final String FRIEND_ID = "friend_id";
	}
	
	// Structure of table users.
	public static abstract class UsersEntry implements BaseColumns {
		public static final String TABLE_NAME = "users";
		public static final String NAME = "name";
//		public static final String AGE = "age";
		public static final String PRIVATE_KEY = "private_key";
		public static final String PUBLIC_KEY = "public_key";
		// ...
		
	}
	
	// Structure of table posts.
	public static abstract class PostsEntry implements BaseColumns {
		public static final String TABLE_NAME = "posts";
		public static final String WALL_ID = "wall_id";
		public static final String TEXT = "text";
		public static final String IMAGE = "image";
		public static final String DATE_TIME = "date_time";
	}

}
