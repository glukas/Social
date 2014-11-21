package ch.ethz.inf.vs.android.glukas.project4;

import java.util.Date;
import android.graphics.Bitmap;

/**
 * This class represents a post of the user on a wall
 */

public class Post {

	private String text;
	// It would be nice if we can manage to have multiple images per post. But maybe it make things to 
	// complicate
	private Bitmap image;
	//the id is crucial for sorting, it needs to be DB-consistent. Thus, on the TODO . =)
	private int id;
	//date will not be used for sorting, but can provide friendly user content
	private Date datetime;
	// Id of the user that posted the message
	private UserId poster;
	// 
	private int postsCount;
	private int maxId;

	/**
	 * Create new Image Post
	 * @param text
	 * @param image
	 * @param datetime
	 */
	public Post(String text, Bitmap image, Date datetime, Wall wall) {
		this.id = -1;
		this.text = text;
		this.image = image;
		this.datetime = datetime;
	}
	
	/**
	 * Create new Text Post
	 * @param text
	 * @param datetime
	 */
	public Post(String text, Date datetime, Wall wall) {
		this.id = -1;
		this.text = text;
		this.image = null;
		this.datetime = datetime;
	}
	
	/**
	 * Create new Post. This constructor is used by the database to retrieve some already stored
	 * posts, not for creating new Posts.
	 * @param id
	 * @param text
	 * @param image
	 * @param datetime
	 */
	public Post(int id, String text, Bitmap image, Date datetime) {
		this.id = id;
		this.text = text;
		this.image = image;
		this.datetime = datetime;
	}
	
	// Getters.
	public int getId() {
		return this.id;
	}
	
	public String getText() {
		return this.text;
	}
	
	public Bitmap getImage() {
		return this.image;
	}
	
	public Date getDateTime() {
		return this.datetime;
	}
	
	
	public  UserId getPoster() {
		return this.poster;
	}
}
