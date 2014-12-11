package ch.ethz.inf.vs.android.glukas.project4.database;

import java.util.List;

import ch.ethz.inf.vs.android.glukas.project4.Post;
import ch.ethz.inf.vs.android.glukas.project4.UserId;
import android.database.sqlite.SQLiteDatabase;

public interface PostsInterface {
	
	/**
	 * Delete a certain post from the user's wall.
	 * @param id the id of the post to delete
	 * @param db SQLliteDatabase to query
	 */
	public void deleteUserPost(int postid, SQLiteDatabase db);
	
	/**
	 * Delete a certain Post of a certain friend.
	 * @param postid
	 * @param friendid
	 * @param db
	 */
	public void deleteFriendPost(int postid, UserId friendid, SQLiteDatabase db);
	
	/**
	 * Update the wall of the user with the given post.
	 * SQL code: 
	 * @param post the post to insert
	 * @param db the SQLiteDatabase
	 */
	public void putUserPost(Post post, SQLiteDatabase db);
	
	/**
	 * Get a certain post from the user's wall.
	 * SQL query: SELECT p_id, p.text, p.image, p.date_time, p.id FROM users u, posts p WHERE u._id == Utility.userID AND u._id == p.wall_id AND p._id == id
	 * @param postid the id of the post to retrieve
	 * @param db SQLliteDatabase to query
	 * @return a Post object if it was found, else null
	 */
	public Post getUserPost(int postid, SQLiteDatabase db);
	
	/**
	 * Update the wall of a friend whose wall is saved on our phone.
	 * @param post
	 * @param friendid
	 * @param db
	 */
	public void putFriendPost(Post post, UserId friendid, SQLiteDatabase db);
	
	/**
	 * Get a certain Post from a certain friend.
	 * @param postid
	 * @param friendid
	 * @param db
	 * @return
	 */
	public Post getFriendPost(int postid, UserId friendid, SQLiteDatabase db);
	
	/**
	 * Get all Posts of a certain friend with id at least from.
	 * @param timestamp
	 * @param friendid
	 * @param db
	 * @return
	 */
	public List<Post> getAllFriendPostsFrom(UserId friendid, int from, SQLiteDatabase db);
	
	public List<Post> getSomeLatestPosts(UserId id, int numberPosts, int postId, SQLiteDatabase db);
	
	public List<Post> getAllUserPostsFrom(int from, SQLiteDatabase db);
}
