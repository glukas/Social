package ch.ethz.inf.vs.android.glukas.project4;

import java.util.List;

import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseAccess;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseManager;
import ch.ethz.inf.vs.android.glukas.project4.exceptions.FailureReason;
import ch.ethz.inf.vs.android.glukas.project4.networking.FriendshipRequest;
import ch.ethz.inf.vs.android.glukas.project4.protocol.Protocol;
import ch.ethz.inf.vs.android.glukas.project4.protocol.PublicHeader;
import ch.ethz.inf.vs.android.glukas.project4.protocol.StatusByte;
import ch.ethz.inf.vs.android.glukas.project4.protocol.parsing.MessageParser;
import ch.ethz.inf.vs.android.glukas.project4.security.NetworkMessage;
import ch.ethz.inf.vs.android.glukas.project4.security.SecureChannelDelegate;
import ch.ethz.inf.vs.android.glukas.project4.security.ZeroCredentialStorage;

//TODO TESTING
import ch.ethz.inf.vs.android.glukas.project4.test.Data;
import ch.ethz.inf.vs.android.glukas.project4.test.StaticDatabase;
import ch.ethz.inf.vs.android.glukas.project4.test.StaticSecureChannel;


import android.app.ActionBar;
import android.app.ActionBar.Tab;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements
		OnNdefPushCompleteCallback, UserDelegate, SecureChannelDelegate {

	private static final String tag = "MAIN_ACTIVITY";
	private Button mAddFriendButton, mViewWallButton, mViewFriendsButton;
	private AlertDialog.Builder mAlertBuilder;

	NfcAdapter nfcAdapter;

	FriendshipRequest nextRequest;

	Protocol mProtocol;

	DatabaseManager dbmanager;

	// TODO TESTING
	StaticDatabase db;
	StaticSecureChannel channel;

	ActionBar.Tab tab1, tab2, tab3;
	Fragment fragmentTab1 = new FragmentTab1();
	Fragment fragmentTab2 = new FragmentTab2();
	Fragment fragmentTab3 = new FragmentTab3();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_screen);

		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		tab1 = actionBar.newTab().setText("1");
		tab2 = actionBar.newTab().setText("2");
		tab3 = actionBar.newTab().setText("3");

		tab1.setTabListener(new MyTabListener(fragmentTab1));
		tab2.setTabListener(new MyTabListener(fragmentTab2));
		tab3.setTabListener(new MyTabListener(fragmentTab3));

		actionBar.addTab(tab1);
		actionBar.addTab(tab2);
		actionBar.addTab(tab3);

		dbmanager = new DatabaseManager(this);

		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		if (nfcAdapter == null) {
			Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG)
					.show();
			finish();
			return;
		}

		
		



		mAlertBuilder = new AlertDialog.Builder(this);

		mProtocol = Protocol.getInstance(new DatabaseManager(this));
		mProtocol.setDelegate(this);

		mAddFriendButton = (Button)findViewById(R.id.home_add_friend_button);
		mAddFriendButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO: Implement adding friends by username.
			}
		});

		mViewFriendsButton = (Button)findViewById(R.id.home_view_friends_button);

		mViewFriendsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showFriendsList();
			}
		});

		mViewWallButton = (Button)findViewById(R.id.home_wall_button);

		mViewWallButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showWall();
			}
		});

		createNextRequest();

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

	private void showWall() {


	}

	private void showFriendRequest() {
		mAlertBuilder.setTitle("Title");
		mAlertBuilder.setMessage("Message");

		// Set an EditText view to get user input
		final EditText input = new EditText(this);
		mAlertBuilder.setView(input);

		mAlertBuilder.setPositiveButton("Ok",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						String userName = input.getText().toString();
						mProtocol.askFriendship(userName);
					}
				});

		mAlertBuilder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.cancel();
					}
				});

		mAlertBuilder.show();
	}

	// This button will actually be another Activity later
	public void OnNFC_Click(View view) {

		createNextRequest();
		waitforResponse();

	}

	public void waitforResponse() {
		// TODO : Add timeout or do it async
		boolean waiting = true;
		while (waiting) {
			if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent()
					.getAction())) {
				processIntent(getIntent());
				waiting = false;
			}
		}
	}

	private void processIntent(Intent intent) {
		Parcelable[] rawMsgs = intent
				.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

		// only one message sent during the beam
		NdefMessage msg = (NdefMessage) rawMsgs[0];
		FriendshipRequest response = new FriendshipRequest(msg);

		saveFriend(response);
	}

	// Save friend in database
	private void saveFriend(FriendshipRequest response) {

		dbmanager.putFriend(response.getSender());


	}

	private void showFriendsList() {


	}

	private void createNextRequest() {

		nextRequest = new FriendshipRequest(dbmanager.getUser());
		nfcAdapter.setNdefPushMessageCallback(nextRequest, this);
		nfcAdapter.setOnNdefPushCompleteCallback(this, this);
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
	public void onPostReceived(Post post) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnectionFailed(FailureReason reason) {
		Log.d(tag, "Connection Failed.");
		Toast.makeText(this, R.string.connection_failed, Toast.LENGTH_LONG)
				.show();
	}

	@Override
	public void onConnectionSucceeded() {
		setContentView(R.layout.home_screen);
	}

	@Override
	public void onDisconnectionFailed(FailureReason reason) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onDisconnectionSucceeded() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onPeersDiscoverySuccess(List<User> peers) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onFriendshipAccepted() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onFriendshipDeclined() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onMessageReceived(NetworkMessage message) {
		Log.d("DEBUG", Data.tag
				+ "Message received : "
				+ MessageParser.parseMessage(message.text, message.header, db)
						.toString());

	}

	public class FragmentTab1 extends Fragment {
		public View onCreateView(LayoutInflater inflater, ViewGroup container, 
				Bundle savedInstanceState){
			View view = inflater.inflate(R.layout.view_friends, container, false);
			TextView textview = (TextView) view.findViewById(R.id.tabtextview);
			textview.setText("TEst");
			return view;
		}
	}
	
	public class FragmentTab2 extends Fragment {
		public View onCreateView(LayoutInflater inflater, ViewGroup container, 
				Bundle savedInstanceState){
			View view = inflater.inflate(R.layout.view_friends, container, false);
			TextView textview = (TextView) view.findViewById(R.id.tabtextview);
			textview.setText("TEst");
			return view;
		}
	}
	
	public class FragmentTab3 extends Fragment {
		public View onCreateView(LayoutInflater inflater, ViewGroup container, 
				Bundle savedInstanceState){
			View view = inflater.inflate(R.layout.view_friends, container, false);
			TextView textview = (TextView) view.findViewById(R.id.tabtextview);
			textview.setText("TEst");
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

}
