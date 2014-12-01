package ch.ethz.inf.vs.android.glukas.project4;

import java.util.ArrayList;
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

public class MainActivity extends Activity implements
		OnNdefPushCompleteCallback, SecureChannelDelegate {

	private static final String tag = "MAIN_ACTIVITY";

	NfcAdapter nfcAdapter;

	FriendshipRequest nextRequest;

	Protocol mProtocol;

	DatabaseManager dbmanager;

	// TODO TESTING
	StaticDatabase db;
	StaticSecureChannel channel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_screen);

		
		dbmanager = new DatabaseManager(this);
		dbmanager.putUser(new User("Dummyname"));
		if (dbmanager.getUser() == null) {
			Log.d("User null", "User null");
		}
		else {
			Log.d("User not null", "User not null");
			
		}
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		if (nfcAdapter == null) {
			Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG)
					.show();
			finish();
			return;
		}
		
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		ActionBar.Tab tab1 = actionBar.newTab().setText("My Wall");
		ActionBar.Tab tab2 = actionBar.newTab().setText("View Friends");
		ActionBar.Tab tab3 = actionBar.newTab().setText("Add Friend");

		tab1.setTabListener(new MyTabListener(new WallFragment()));
		tab2.setTabListener(new MyTabListener(new FriendListFragment()));
		tab3.setTabListener(new MyTabListener(new AddFriendFragment()));

		actionBar.addTab(tab1);
		actionBar.addTab(tab2);
		actionBar.addTab(tab3);

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
	public void onMessageReceived(NetworkMessage message) {
		Log.d("DEBUG", Data.tag
				+ "Message received : "
				+ MessageParser.parseMessage(message.text, message.header, db)
						.toString());

	}

	public class WallFragment extends Fragment {
		public View onCreateView(LayoutInflater inflater, ViewGroup container, 
				Bundle savedInstanceState){
			View view = inflater.inflate(R.layout.my_wall_tab, container, false);
			return view;
		}
	}
	
	public class FriendListFragment extends ListFragment {
		public View onCreateView(LayoutInflater inflater, ViewGroup container, 
				Bundle savedInstanceState){
			View view = inflater.inflate(R.layout.friend_list_tab, container, false);
			User myself = dbmanager.getUser();
			
			if (myself == null) {
				Toast.makeText(getApplicationContext(), "dbmanager.getUser() returned null!", Toast.LENGTH_LONG).show();
				return view;
			}
			
			ArrayList<BasicUser> users = new ArrayList<BasicUser>(dbmanager.getFriendsList(myself.id));
			ArrayAdapter<BasicUser> userAdapter = new ArrayAdapter<BasicUser>(getApplicationContext(), R.layout.friend_list_row, R.id.userName, users);
			setListAdapter(userAdapter);
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
