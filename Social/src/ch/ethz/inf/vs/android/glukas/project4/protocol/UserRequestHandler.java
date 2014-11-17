package ch.ethz.inf.vs.android.glukas.project4.protocol;

import ch.ethz.inf.vs.android.glukas.project4.Post;
import ch.ethz.inf.vs.android.glukas.project4.exceptions.DatabaseException;
import ch.ethz.inf.vs.android.glukas.project4.exceptions.NetworkException;

/**
 * Handle requests of the user
 */
public class UserRequestHandler implements UserDelegate {

	/*
	 * TODO : 	-create UserRequest object, stores everything needed in DB
	 * 			-create JSONObject from created request
	 * 			-pass JSONObject to Security Layer
	 */
	
	@Override
	public void connect() throws NetworkException {
	}

	@Override
	public void disconnect() throws NetworkException {
	}

	@Override
	public void postPost(Post post) throws DatabaseException {
	}

	@Override
	public void getUserWall(String DistUsername) throws NetworkException {
	}

	@Override
	public void askFriendship(String DistUsername) throws NetworkException {
	}

	@Override
	public void searchUser(String DistUsername) throws NetworkException {
	}

}
