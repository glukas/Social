package ch.ethz.inf.vs.android.glukas.project4;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a wrapper which contains all informations of a wall of a
 * particular user
 */

public class Wall {
	
	// List of all posts contained in the wall.
	private List<Post> posts;

	// Constructor
	public Wall() {
		posts = new ArrayList<Post>();
	}
	
	// Get post count.
	public int postCount() {
		return posts.size();
	}
	
	// Getters.
	public List<Post> getPosts() {
		return posts;
	}
	
	// Setters.
	public void addPost(Post newPost) {
		posts.add(newPost);
	}

	
}
