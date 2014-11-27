package ch.ethz.inf.vs.android.glukas.project4.test;

import java.util.List;

import ch.ethz.inf.vs.android.glukas.project4.BasicUser;
import ch.ethz.inf.vs.android.glukas.project4.Post;
import ch.ethz.inf.vs.android.glukas.project4.User;
import ch.ethz.inf.vs.android.glukas.project4.UserCredentials;
import ch.ethz.inf.vs.android.glukas.project4.UserId;
import ch.ethz.inf.vs.android.glukas.project4.Wall;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseAccess;

public class StaticDatabase implements DatabaseAccess {

	@Override
	public void putUser(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public User getUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getUserPostsCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getUserMaxPostsId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setUserPostsCount(int newCount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUserMaxPostsId(int newMaxPostsId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<User> getUserFriendsList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserCredentials getUserCredentials(UserId user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getFriendPostsCount(UserId id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getFriendMaxPostsId(UserId id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setFriendPostsCount(int newCount, UserId id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFriendMaxPostsId(int newMaxPostsId, UserId id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFriendsList(UserId user, List<BasicUser> friends) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<BasicUser> getFriendsList(UserId id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFriendUsername(UserId id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserId> getFriendId(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void putFriend(User friend) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteFriend(UserId id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public User getFriend(UserId id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void putUserPost(Post post) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Post> getAllUserPostsFrom(int from) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteUserPost(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Post getUserPost(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void putFriendPost(Post post, UserId id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Post getFriendPost(int postid, UserId friendid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Post> getAllFriendPostsFrom(UserId id, int from) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteFriendPost(int postid, UserId friendid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Wall getUserWall() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteUserWall() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Wall getFriendWall(UserId id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteFriendWall(UserId id) {
		// TODO Auto-generated method stub
		
	}

}
