package ch.ethz.inf.vs.android.glukas.project4;

import java.util.Date;
import java.util.List;

import android.graphics.Bitmap;

/**
 * This class represents a post of the user on a wall
 */

public class Post {

	// author: Alessio
	// just to begin
	// Post's id.
//	int id;
	
	// Id of the wall it belongs to
//	int wallId;
	
	// Text of the post
	String text;
	
	// Image Binary
	// Image of the post. Let max 1 or more?
	Bitmap image;

	// Id of the Post
	public int id;

	// Lamport timestamp instead of real time
	// they are unique for each user
	public int timestamp;
	
	// Real time or timestamp? (Young)
	// Date and time when the message was sent/received
//	Date datetime;

	public Date datetime;

	public Post(String text, Bitmap image, int id, int timestamp) {
		this.text = text;
		this.image = image;
		this.id = id;
		this.timestamp = timestamp;
	}
	
	// metadata: visited times, ...
	
	// Constructor.
	public Post(int id, String text, Bitmap image, Date datetime) {
		this.id = id;
//		this.wallId = wallId;
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
}
