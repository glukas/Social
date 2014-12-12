package ch.ethz.inf.vs.android.glukas.project4.database;

import java.util.ArrayList;
import java.util.List;

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
	
	public static enum CREATE_TABLE {
		USERS("CREATE TABLE users (" +
				BaseColumns._ID +" INTEGER, " +
				"user_id TEXT, " +
				"username TEXT, " +
				"is_friend INTEGER," +
				"count INTEGER, " +
				"max INTEGER, " +
				"broadcast_enc_key BLOB, " +
				"broadcast_auth_key BLOB, " +
				"PRIMARY KEY(user_id)" +
				");"),
		
		POSTS("CREATE TABLE posts (" +
				BaseColumns._ID+" INTEGER, " +
				"poster_id TEXT, " +
				"wall_id TEXT, " +
				"date_time TEXT, " +
				"text TEXT, " +
				"image BLOB, " +
				"PRIMARY KEY("+BaseColumns._ID+", wall_id), " +
				"FOREIGN KEY (poster_id) REFERENCES users(user_id) ON DELETE SET NULL," +
				"FOREIGN KEY (wall_id) REFERENCES users(user_id) ON DELETE CASCADE" +
				");");
			
		private String command;
		
		CREATE_TABLE(String s){
			command = s;
		}
		
		public String getCommand() {
			return command;
		}
	}
	
	public static enum DROP_TABLE_IF_EXIST {
		DROP_TABLE_USERS_IF_EXIST("DROP TABLE IF EXISTS users;"),
		DROP_TABLE_POSTS_IF_EXIST("DROP TABLE IF EXISTS posts;");
		
		private String command;
		
		DROP_TABLE_IF_EXIST(String s){
			command = s;
		}
		
		public String getCommand() {
			return command;
		}
	}
	
	
	// Structure of table users.
	public static abstract class UsersEntry implements BaseColumns {
		public static final String TABLE_NAME = "users";
		public static final String USER_ID = "user_id";
		public static final String USERNAME = "username";
		// Distinguishes the user itself from its friends
		public static final String IS_FRIEND = "is_friend";
		public static final String COUNT = "count";
		public static final String MAX = "max";
//		public static final String AGE = "age";
		public static final String BROADCAST_ENC_KEY = "broadcast_enc_key";
		public static final String BROADCAST_AUTH_KEY = "broadcast_auth_key";
		// ...
		
	}
	
	// Structure of table posts.
	public static abstract class PostsEntry implements BaseColumns {
		public static final String TABLE_NAME = "posts";
		public static final String WALL_ID = "wall_id";
		public static final String TEXT = "text";
		public static final String IMAGE = "image";
		public static final String DATE_TIME = "date_time";
		public static final String POSTER_ID = "poster_id";
	}

}
