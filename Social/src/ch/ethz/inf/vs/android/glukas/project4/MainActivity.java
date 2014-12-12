package ch.ethz.inf.vs.android.glukas.project4;import java.util.ArrayList;import java.util.Arrays;import java.util.List;import ch.ethz.inf.vs.android.glukas.project4.RegistrationDialogFragment.RegistrationDialogFragmentDelegate;import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseAccess;import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseManager;import ch.ethz.inf.vs.android.glukas.project4.exceptions.FailureReason;import ch.ethz.inf.vs.android.glukas.project4.networking.FriendshipRequest;import ch.ethz.inf.vs.android.glukas.project4.protocol.Protocol;import ch.ethz.inf.vs.android.glukas.project4.protocol.ProtocolInterface;import ch.ethz.inf.vs.android.glukas.project4.protocol.parsing.MessageParser;import ch.ethz.inf.vs.android.glukas.project4.security.NetworkMessage;import ch.ethz.inf.vs.android.glukas.project4.security.SecureChannelDelegate;import ch.ethz.inf.vs.android.glukas.project4.security.ZeroCredentialStorage;//TODO TESTINGimport ch.ethz.inf.vs.android.glukas.project4.test.Data;import ch.ethz.inf.vs.android.glukas.project4.test.StaticDatabase;import ch.ethz.inf.vs.android.glukas.project4.test.StaticSecureChannel;import android.annotation.SuppressLint;import android.app.ActionBar;import android.app.ActionBar.Tab;import android.app.Activity;import android.app.AlertDialog;import android.app.Dialog;import android.app.DialogFragment;import android.app.Fragment;import android.app.FragmentTransaction;import android.app.ListFragment;import android.content.DialogInterface;import android.content.Intent;import android.graphics.Bitmap;import android.graphics.BitmapFactory;import android.nfc.NdefMessage;import android.nfc.NfcAdapter;import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;import android.nfc.NfcEvent;import android.os.Bundle;import android.os.Parcelable;import android.util.Log;import android.view.LayoutInflater;import android.view.View;import android.view.View.OnClickListener;import android.view.ViewGroup;import android.widget.ArrayAdapter;import android.widget.Button;import android.widget.EditText;import android.widget.TextView;import android.widget.Toast;@SuppressLint("ValidFragment")public class MainActivity extends Activity implements /*OnNdefPushCompleteCallback,*/ UserDelegate, RegistrationDialogFragmentDelegate {	private static final String tag = "MAIN_ACTIVITY";	NfcAdapter nfcAdapter;	FriendshipRequest nextRequest;	ProtocolInterface mProtocol;	WallPostAdapter userWallAdapter;		//TODO Once testing is done, change DatabaseManager to DatabaseAccess	DatabaseManager dbmanager;	// TODO Remove the section below	// Workaround for dbmanager not returning users:	private List<BasicUser> userList = Arrays.asList((BasicUser)new User("Alice"), new User("Bob"), new User("Carol"), new User("David"));	// Workaround for dbmanager not returning posts:	private List<Post> postList = Arrays.asList(new Post(4, new UserId(), new UserId(), "Hello World!", null, null), new Post(0, new UserId(), new UserId(), "Amazing app!!", null, null) );	// TODO Remove the section above	private FriendListAdapter friendListAdapter;		@Override	protected void onCreate(Bundle savedInstanceState) {		super.onCreate(savedInstanceState);		setContentView(R.layout.home_screen);		dbmanager = new DatabaseManager(this);				//Protocol instantiation		mProtocol = Protocol.getInstance(dbmanager);		mProtocol.setDelegate(this);		//		setUpNFCExchange();				//set up views				ActionBar actionBar = getActionBar();		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);		ActionBar.Tab tab1 = actionBar.newTab().setText("My Wall");		ActionBar.Tab tab2 = actionBar.newTab().setText("View Friends");		//ActionBar.Tab tab3 = actionBar.newTab().setText("Add Friend");				userWallAdapter = new WallPostAdapter(getApplicationContext(), new ArrayList<Post>());		WallFragment wallFragment = new WallFragment();		wallFragment.setListAdapter(userWallAdapter);				tab1.setTabListener(new MyTabListener(wallFragment));				friendListAdapter = new FriendListAdapter(this, userList);		FriendListFragment friendListFragment = new FriendListFragment();		friendListFragment.setListAdapter(friendListAdapter);		tab2.setTabListener(new MyTabListener(friendListFragment));		//tab3.setTabListener(new MyTabListener(new AddFriendFragment()));		actionBar.addTab(tab1);		actionBar.addTab(tab2);		//actionBar.addTab(tab3);			}		////	//NFC (Friendship Request)	//////	private void setUpNFCExchange() {//		nfcAdapter = NfcAdapter.getDefaultAdapter(this);//		if (nfcAdapter == null) {//			Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG)//			.show();//			finish();//			return;//		}//		createNextRequest();//	}////	private void createNextRequest() {//		nextRequest = new FriendshipRequest(dbmanager.getUser());//		//		nfcAdapter.setNdefPushMessageCallback(nextRequest, this);//		nfcAdapter.setOnNdefPushCompleteCallback(this, this);//	}//	//	//////	//OnNdefPushCompleteCallback//	//////	//	@Override//	public void onNdefPushComplete(NfcEvent event) {//		Log.d(this.getClass().toString(), "onNdefPushComplete");//		FriendshipRequest.setCurrentRequest(nextRequest);//		createNextRequest();//	}//		////	//LIFECYCLE	////	@Override	protected void onPause() {		super.onPause();	}	@Override	protected void onResume() {		super.onResume();				checkUserRegistered();		updateWall();				//TESTING		//tests insertion of posts into wall		int index = new UserId().getId().shortValue();		mProtocol.postPost(new Post(index, new UserId("1234"), new UserId("1234"), String.format("post with id : %d", index), null, null));				Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);		icon = Bitmap.createScaledBitmap(icon, 500, 500, false);		Post imagePost = new Post(index, new User("Alice").getId(), new User("Alice").getId(), "Testing image.. and now with a much longer text to see how it breaks onto the next line and stuff.. %id : " + index, icon, null);		//this.userWallAdapter.add(imagePost);	}	private void updateWall() {		//if (mProtocol.getUser() != null) {			mProtocol.getSomeUserPosts(new UserId("1234"), 20, Integer.MAX_VALUE);		//}	}	private void checkUserRegistered() {		if(mProtocol.getUser() == null) {			Log.d(tag, "No user registered");			// Create registration dialog			RegistrationDialogFragment dialog = new RegistrationDialogFragment(this);			dialog.show(this.getFragmentManager(), "lol");		} else {			Log.d(tag, "User " + dbmanager.getUser().username +" already registered");		}	}	////	//Fragment	///	public class WallFragment extends ListFragment {		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){			View view = inflater.inflate(R.layout.my_wall_tab, container, false);			return view;		}	}	public class FriendListFragment extends ListFragment {		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){			View view = inflater.inflate(R.layout.friend_list_tab, container, false);			return view;		}	}	public class AddFriendFragment extends Fragment {		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){			View view = inflater.inflate(R.layout.add_friend_tab, container, false);			return view;		}	}		public class ToDoFragment extends Fragment {		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){			View view = inflater.inflate(R.layout.todo_tab, container, false);			return view;		}	}	public class MyTabListener implements ActionBar.TabListener {		Fragment fragment;		public MyTabListener(Fragment fragment) {			this.fragment = fragment;		}		public void onTabSelected(Tab tab, FragmentTransaction ft) {			ft.replace(R.id.home_screen, fragment);		}		public void onTabUnselected(Tab tab, FragmentTransaction ft) {			ft.remove(fragment);		}		public void onTabReselected(Tab tab, FragmentTransaction ft) {			// nothing done here		}	}		////	//UserDelegate	////	@Override	public void onPostReceived(Post post) {		// TODO Discuss if this method can be non-blocking (Samuel) or if it has to be called asynch (Vincent)		// or if the lower components already calls the method asynch (Lukas - Mathias)				//TODO only add if it belongs to this users wall				this.userWallAdapter.add(post);	}	@Override	public void onConnectionFailed(FailureReason reason) {		// TODO @Samuel, from Vincent Is it of any use for you?		}		////	//RegistrationDialogFragmentDelegate	////		@Override	public void onUserRegistered(String username) {		this.dbmanager.putUser(new User(username));	}}

