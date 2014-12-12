package ch.ethz.inf.vs.android.glukas.project4.database;

import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseContract.PostsEntry;
import android.provider.BaseColumns;

public class Definitions {
	
	/**
	 * Enumerate all selections query made by the database
	 */
	public enum SELECTIONS {
		
		POST_BY_ID_AND_WALL( PostsEntry._ID + " == ? AND " + PostsEntry.POSTER_ID + " == ?");
		
		private String command;
		
		SELECTIONS(String s){
			command = s;
		}
		
		public String getCommand() {
			return command;
		}
	}
	
	/**
	 * Enumerate all creations of table made by the database
	 */
	public enum CREATE_TABLE {
//		FRIENDS("CREATE TABLE friends (" +
//				BaseColumns._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
//				"friend_id_1 TEXT, " +
//				"friend_id_2 TEXT, " +
//				"FOREIGN KEY(friend_id_1) REFERENCES users(user_id) ON DELETE CASCADE, " +
//				"FOREIGN KEY(friend_id_2) REFERENCES users(user_id) ON DELETE CASCADE" +
//				");"),
				
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
				
//		APP_OWNER("CREATE TABLE appowner (" +
//				BaseColumns._ID+" INTEGER, " +
//				"user_id TEXT, " +
//				"PRIMARY KEY(user_id)"+
//				");");
			
		private String command;
		
		CREATE_TABLE(String s){
			command = s;
		}
		
		public String getCommand() {
			return command;
		}
	}
	
	/**
	 * Enumerate all drops of table made by the database
	 */
	public enum DROP_TABLE_IF_EXIST {
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
}
