package ch.ethz.inf.vs.android.glukas.project4;

import java.util.Date;

import ch.ethz.inf.vs.android.glukas.project4.exceptions.UnknowRequestType;
import ch.ethz.inf.vs.android.glukas.project4.protocol.Message;
import ch.ethz.inf.vs.android.glukas.project4.protocol.Message.MessageType;
import android.graphics.Bitmap;

/**
 * This class represents a post of the user on a wall
 */

public class Post {

	private String text;
	// It would be nice if we can manage to have multiple images per post. But maybe it make things to 
	// complicate
	private Bitmap image;
	private String imageLink;
	//the id is crucial for sorting.
	private int id;
	//date will not be used for sorting, but can provide friendly user content
	private Date datetime;
	// Id of the user that posted the message
	private UserId poster;
	private PostType type;
	
	/**
	 * Create new Post. This constructor is used by the database to retrieve some already stored
	 * posts, not for creating new Posts.
	 * @param id
	 * @param text
	 * @param image
	 * @param datetime
	 */
	public Post(int id, UserId Poster, String text, Bitmap image, Date datetime) {
		this.id = id;
		this.poster = Poster;
		this.text = text;
		if (image == null) {
			type = PostType.TEXT;
		} else {
			type = PostType.PICTURE;
		}
		this.image = image;
		this.datetime = datetime;
	}
	
	public Post(Message msg, int id) {
		MessageType type = msg.getRequestType();
		if (type.equals(MessageType.POST_PICTURE)) {
			this.id = msg.getId();
			this.text = msg.getMessage();
			this.image = null;
			this.imageLink = msg.getHttpLink();
		} else if (type.equals(MessageType.POST_TEXT)) {
			this.id = msg.getId();
			this.text = msg.getMessage();
			this.image = null;
			this.imageLink = "";
		} else {
			throw new UnknowRequestType(type);
		}
	}
	
	// Getters.
	public String getImageLink() {
		return this.imageLink;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getText() {
		return this.text;
	}
	
	public Bitmap getImage() {
		if (type.equals(PostType.TEXT)){
			return null;
		} else {
			return image;
		}
	}
	
	public Date getDateTime() {
		return this.datetime;
	}
	
	public  UserId getPoster() {
		return this.poster;
	}
	
	public PostType getType() {
		return type;
	}

	/**
	 * Used to differentiate between different kinds of posts
	 */
	public enum PostType{
		PICTURE,
		TEXT;
	}
}
