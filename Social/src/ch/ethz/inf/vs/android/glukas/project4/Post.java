package ch.ethz.inf.vs.android.glukas.project4;

/**
 * This class represents a post of the user on a wall
 */

public class Post {

	// author: Alessio
	// just to begin

	// Text
	public String text;

	// Image Binary
	public String image;

	// Id of the Post
	public int id;

	// Lamport timestamp instead of real time
	// they are unique for each user
	public int timestamp;

	public Post(String text, String image, int id, int timestamp) {
		this.text = text;
		this.image = image;
		this.id = id;
		this.timestamp = timestamp;

	}

	// metadata: date/time, visited times, ...

}
