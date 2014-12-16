package ch.ethz.inf.vs.android.glukas.project4;

import java.util.Date;

import ch.ethz.inf.vs.android.glukas.project4.database.Utility;
import ch.ethz.inf.vs.android.glukas.project4.exceptions.UnknowRequestType;
import ch.ethz.inf.vs.android.glukas.project4.protocol.Message;
import ch.ethz.inf.vs.android.glukas.project4.protocol.Message.MessageType;
import android.graphics.Bitmap;

/**
 * This class represents a post of the user on a wall
 */

public class Post implements Comparable<Post> {

	private final String text;
	// It would be nice if we can manage to have multiple images per post. But maybe it make things to 
	// complicate
	private Bitmap image;
	//the id is crucial for sorting.
	private final int id;
	//date will not be used for sorting, but can provide friendly user content
	private Date datetime;
	// Id of the user that posted the message
	private final UserId poster;
	// Id of the user that owns the wall where the post is
	private final UserId wallOwner;
	
	/**
	 * Database and user interface point of view.
	 * Create new Post. This constructor is used by the database to retrieve some already stored
	 * posts, not for creating new Posts.
	 * @param id
	 * @param text
	 * @param image
	 * @param datetime
	 */
	public Post(int id, UserId Poster, UserId wallOwner, String text, Bitmap image, Date datetime) {
		this.id = id;
		this.wallOwner = wallOwner;
		this.poster = Poster;
		this.text = text;
		this.image = image;
		this.datetime = datetime;
	}
	
	/**
	 * Network point of view
	 * Create a new Post
	 * @param msg
	 */
	public Post(Message msg) {
		this.poster = msg.getSender();
		this.wallOwner = msg.getReceiver();
		this.id = msg.getId();
		this.text = msg.getMessage();
		if (msg.getPayload().length == 0) {
			this.image = null;
		} else {
			this.image = Utility.toBitmap(msg.getPayload());
		}
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getText() {
		return this.text;
	}
	
	public Bitmap getImage() {
		return image;
	}
	
	public Date getDateTime() {
		return this.datetime;
	}
	
	public  UserId getPoster() {
		return this.poster;
	}
	
	public UserId getWallOwner() {
		return this.wallOwner;
	}
	
	/**
	 * returns positive if this post has a lower id, and negative if this post has a higher id
	 * poster ids decide ties
	 */
	@Override
	public int compareTo(Post another) {
		if (this.id < another.id) {
			return 1;
		} else if (this.id > another.id) {
			return -1;
		} else {
			return this.poster.getId().compareTo(another.poster.getId());
		}
	}
	
	@Override
	public boolean equals(Object other) {
		return (other instanceof Post) && (this.compareTo((Post) other) == 0);
	}
	
	@Override
	public int hashCode() {
		return this.id ^ poster.getId().intValue();
	}
	
	@Override
	public String toString() {
		return "post: " + this.text + ", " + this.id;
	}
}
