package ch.ethz.inf.vs.android.glukas.project4;

import java.util.ArrayList;
import java.util.List;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseAccess;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseManager;
import ch.ethz.inf.vs.android.glukas.project4.exceptions.FailureReason;
import ch.ethz.inf.vs.android.glukas.project4.networking.FriendshipRequest;
import ch.ethz.inf.vs.android.glukas.project4.protocol.Protocol;
import ch.ethz.inf.vs.android.glukas.project4.protocol.ProtocolDelegate;
import ch.ethz.inf.vs.android.glukas.project4.protocol.parsing.MessageParser;
import ch.ethz.inf.vs.android.glukas.project4.security.NetworkMessage;
import ch.ethz.inf.vs.android.glukas.project4.security.SecureChannelDelegate;
import ch.ethz.inf.vs.android.glukas.project4.security.ZeroCredentialStorage;

//TODO TESTING
import ch.ethz.inf.vs.android.glukas.project4.test.Data;
import ch.ethz.inf.vs.android.glukas.project4.test.StaticDatabase;
import ch.ethz.inf.vs.android.glukas.project4.test.StaticSecureChannel;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class MainActivity extends Activity implements OnNdefPushCompleteCallback, SecureChannelDelegate, UserDelegate {

	private static final String tag = "MAIN_ACTIVITY";

	NfcAdapter nfcAdapter;

	FriendshipRequest nextRequest;

	ProtocolDelegate mProtocol;

	//TODO Once testing is done, change DatabaseManager to DatabaseAccess
	DatabaseManager dbmanager;

	// TODO Remove the section below

	// Workaround for dbmanager not returning users:
	private ArrayList<BasicUser> userList = new ArrayList<BasicUser>();
	// Workaround for dbmanager not returning posts:
	private ArrayList<Post> postList = new ArrayList<Post>();

	// TODO Remove the section above

	// TODO TESTING
	StaticDatabase db;
	StaticSecureChannel channel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_screen);

		dbmanager = new DatabaseManager(this);
		
		//Protocol instantiation
		mProtocol = Protocol.getInstance(dbmanager);
		mProtocol.setDelegate(this);
		
		// Insert static test data
		dbmanager.initializeTest(getApplicationContext());

		if(mProtocol.getUser() == null) {
			Log.d(tag, "No user registered");
			// Create registration dialog
			RegistrationDialogFragment dialog = new RegistrationDialogFragment();
			dialog.show(this.getFragmentManager(), "lol");
		}
		else {
			Log.d(tag, "User " + dbmanager.getUser().username +" already registered");
		}
		
//		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
//		if (nfcAdapter == null) {
//			Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG)
//			.show();
//			finish();
//			return;
//		}

		// TODO Remove the section below

		// Workaround for dbmanager not returning users:
		userList.add(new User("Alice"));
		userList.add(new User("Bob"));
		userList.add(new User("Eve"));
		userList.add(new User("Benjamin"));
		userList.add(new User("Steve"));
		userList.add(new User("Carlos"));
		userList.add(new User("Sven"));
		userList.add(new User("Mathias"));
		userList.add(new User("Bradley"));
		
		
		// Workaround for dbmanager not returning posts:
//		post
		// TODO Remove the section above

		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		ActionBar.Tab tab1 = actionBar.newTab().setText("My Wall");
		ActionBar.Tab tab2 = actionBar.newTab().setText("View Friends");
		ActionBar.Tab tab3 = actionBar.newTab().setText("Add Friend");
//		// TODO: remove this tab after testing phase
//		ActionBar.Tab tab4 = actionBar.newTab().setText("DB state");
//		// TODO: remove this tab after testing phase
//		ActionBar.Tab tab5 = actionBar.newTab().setText("DB tester");
		
		tab1.setTabListener(new MyTabListener(new WallFragment()));
		tab2.setTabListener(new MyTabListener(new FriendListFragment()));
		tab3.setTabListener(new MyTabListener(new AddFriendFragment()));
//		tab4.setTabListener(new MyTabListener(new ToDoFragment()));
//		tab5.setTabListener(new MyTabListener(new ToDoFragment()));


		actionBar.addTab(tab1);
		actionBar.addTab(tab2);
		actionBar.addTab(tab3);
//		actionBar.addTab(tab4);
//		actionBar.addTab(tab5);
		
		//mProtocol = Protocol.getInstance(new DatabaseManager(this));
		//mProtocol.setDelegate(this);

		//TODO TESTING
		/*
		channel = new StaticSecureChannel("winti.mooo.com", 9000);
		db = new StaticDatabase();
		channel.setDelegate(this);
		PublicHeader header = new PublicHeader(44, null, StatusByte.CONNECT.getByte(), 0, Data.dummySenderId, Data.serverId);
		channel.sendHeader(header);
		Log.i("DEBUG", Data.tag+"header send");
		 */
	}




	private void createNextRequest() {

		//nextRequest = new FriendshipRequest(dbmanager.getUser());

		nextRequest = new FriendshipRequest(new User("Alice"));

//		nfcAdapter.setNdefPushMessageCallback(nextRequest, this);
//		nfcAdapter.setOnNdefPushCompleteCallback(this, this);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void onNdefPushComplete(NfcEvent event) {
		Log.d(this.getClass().toString(), "onNdefPushComplete");
		FriendshipRequest.setCurrentRequest(nextRequest);
		createNextRequest();
	}


	@Override
	public void onMessageReceived(NetworkMessage message) {
		Log.d("DEBUG", Data.tag
				+ "Message received : "
				+ MessageParser.parseMessage(message.getText(), message.header, db)
				.toString());

	}

	public class WallFragment extends ListFragment {
		public View onCreateView(LayoutInflater inflater, ViewGroup container, 
				Bundle savedInstanceState){
			View view = inflater.inflate(R.layout.my_wall_tab, container, false);
			Wall myWall = mProtocol.getUserWall();
			User myself = mProtocol.getUser();

			if (myself == null) {
				Toast.makeText(getApplicationContext(), "dbmanager.getUser() returned null!", Toast.LENGTH_LONG).show();
				return view;
			}
			
			if(myWall == null) {
				Toast.makeText(getApplicationContext(), "dbmanager.getUserWall() returned null! Using dummy posts...", Toast.LENGTH_LONG).show();
				WallPostAdapter postAdapter = new WallPostAdapter(getApplicationContext(), postList);
				setListAdapter(postAdapter);
				return view;
			}
			else {
				Toast.makeText(getApplicationContext(), "dbmanager.getUserWall() returned something!", Toast.LENGTH_LONG).show();
				List<Post> posts = myWall.getPosts();
				WallPostAdapter userAdapter = new WallPostAdapter(getApplicationContext(), posts);
				setListAdapter(userAdapter);
			}
			
			return view;
		}
	}

	public class FriendListFragment extends ListFragment {
		public View onCreateView(LayoutInflater inflater, ViewGroup container, 
				Bundle savedInstanceState){
			View view = inflater.inflate(R.layout.friend_list_tab, container, false);
			User myself = mProtocol.getUser();

			if (myself == null) {
				Toast.makeText(getApplicationContext(), "dbmanager.getUser() returned null! Using dummy friends...", Toast.LENGTH_LONG).show();
				ArrayAdapter<BasicUser> userAdapter = new ArrayAdapter<BasicUser>(getApplicationContext(), R.layout.friend_list_row, R.id.userName, userList);
				setListAdapter(userAdapter);
				return view;
			}

			List<BasicUser> myFriends = mProtocol.getFriendsList(myself.id);
			if(myFriends == null) {
				Toast.makeText(getApplicationContext(), "dbmanager.getFriendsList() still doesn't work (Alessio)! Using dummy friends...", Toast.LENGTH_LONG).show();
				ArrayAdapter<BasicUser> userAdapter = new ArrayAdapter<BasicUser>(getApplicationContext(), R.layout.friend_list_row, R.id.userName, userList);
				setListAdapter(userAdapter);
				return view;
			}
			else {
				ArrayList<BasicUser> users = new ArrayList<BasicUser>();
				ArrayAdapter<BasicUser> userAdapter = new ArrayAdapter<BasicUser>(getApplicationContext(), R.layout.friend_list_row, R.id.userName, users);
				setListAdapter(userAdapter);
			}
			return view;
		}
	}

	public class AddFriendFragment extends Fragment {
		public View onCreateView(LayoutInflater inflater, ViewGroup container, 
				Bundle savedInstanceState){
			View view = inflater.inflate(R.layout.add_friend_tab, container, false);
			createNextRequest();
			return view;
		}
	}
	
	public class ToDoFragment extends Fragment {
		public View onCreateView(LayoutInflater inflater, ViewGroup container, 
				Bundle savedInstanceState){
			View view = inflater.inflate(R.layout.todo_tab, container, false);
			createNextRequest();
			return view;
		}
	}

	public class MyTabListener implements ActionBar.TabListener {
		Fragment fragment;

		public MyTabListener(Fragment fragment) {
			this.fragment = fragment;
		}

		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			ft.replace(R.id.home_screen, fragment);
		}

		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			ft.remove(fragment);
		}

		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// nothing done here
		}
	}
	
	// Fragment layout for the registration dialog
	public class RegistrationDialogFragment extends DialogFragment {

		 @Override
	    public AlertDialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the Builder class for convenient dialog construction
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        
	        // Get the layout inflater
	        LayoutInflater inflater = getActivity().getLayoutInflater();

	        // Inflate and set the layout for the dialog
	        // Pass null as the parent view because its going in the dialog layout
//	        builder.setView(inflater.inflate(R.layout.dialog_registration, null));
	        final EditText input = new EditText(getActivity());
	        builder.setView(input);
	        
	        // Set dialog title
	        builder.setTitle(R.string.registration_title);
	        
	        // Set positive button
	        builder.setPositiveButton(R.string.register_button, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       // Retrieve data from textboxes, create new User and call putUser
	                	   String username = input.getEditableText().toString(); // TODO: check for null
	                	   Toast.makeText(getActivity(), username + " was succesfully registered", Toast.LENGTH_LONG).show();
	                	   // Create new user object
	                	   User user = new User(username);
	                	   // Insert user in the database
	                	   dbmanager.putUser(user);
	                   }
	               });
	        
	        // Set negative button
	        builder.setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       // TODO: User cancelled registration dialog, what should be done?
	                   }
	               });
	        
	        // Create the AlertDialog object and return it
	        return builder.create();
	    }

	}

	@Override
	public void onPostReceived(Post post) {
		// TODO Also a List<Post> received?
		// TODO Discuss if this method can be non-blocking (Samuel) or if it has to be called asynch (Vincent)
		// or if the lower components already calls the method asynch (Lukas - Mathias)
	}

	@Override
	public void onConnectionFailed(FailureReason reason) {
		// TODO @Samuel, from Vincent Is it of any use for you?	
	}

	@Override
	public void onConnectionSucceeded() {
		// TODO @Samuel, from Vincent Is it of any use for you?
	}

	@Override
	public void onDisconnectionFailed(FailureReason reason) {
		// TODO @Samuel, from Vincent Is it of any use for you?
	}
	
	@Override
	public void onDisconnectionSucceeded() {
		// TODO @Samuel, from Vincent Is it of any use for you?
	}
	
	@Override
	public void onPeersDiscoverySuccess(List<User> peers) {
		// TODO NOT USED NOW (remote friendship mechanism)
	}

	@Override
	public void onFriendshipAccepted() {
		// TODO NOT USED NOW (remote friendship mechanism)
	}

	@Override
	public void onFriendshipDeclined() {
		// TODO NOT USED NOW (remote friendship mechanism)
	}
}
