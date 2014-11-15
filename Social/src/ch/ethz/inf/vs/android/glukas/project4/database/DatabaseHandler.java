package ch.ethz.inf.vs.android.glukas.project4.database;

import ch.ethz.inf.vs.android.glukas.project4.Post;
import ch.ethz.inf.vs.android.glukas.project4.User;
import ch.ethz.inf.vs.android.glukas.project4.Wall;
import android.content.Context;
import android.database.sqlite.*;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

/**
 * Provides the interface with the database.
 * @author alessiobaehler
 * @comment these will be the basic functionalities, more will be added.
 */

public abstract class DatabaseHandler extends SQLiteOpenHelper {

	public DatabaseHandler(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	// USER MANAGEMENT
	/**
	 * 
	 * @param user
	 */
	public abstract void putUser(User user);
	
	public abstract User getUser(int id);
	
//	update single fields necessary?
	
	public abstract void deleteUser(int id);
	
	// WALLS MANAGEMENT
	
	public abstract void putWall(Wall wall);
	
	public abstract User getWall(int id);
	
	public abstract void deleteWall(int id);
	
	// FRIENDS MANAGEMENT
	
	public abstract void putFriend(User user);
	
	public abstract User getFriend(int id);
	
	public abstract void deleteFriend(int id);
	
	// POSTS MANAGEMENT
	
	public abstract void putPost(Post post);
	
	public abstract User getPost(int id);
	
	public abstract void deletePost(int id);
	
	

}
