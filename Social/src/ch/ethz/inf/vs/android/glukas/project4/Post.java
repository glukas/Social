package ch.ethz.inf.vs.android.glukas.project4;

import java.util.Date;

import android.graphics.Bitmap;

/**
 * This class represents a post of the user on a wall
 */

public class Post {
	
	// author: Alessio
	// just to begin
	// Post's id.
	int id;
	
	// Id of the wall it belongs to
//	int wallId;
	
	// Text of the post
	String text;
	
	// Image of the post. Let max 1 or more?
	Bitmap image;
	
	// Date and time when the message was sent/received
	Date datetime;
	
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
