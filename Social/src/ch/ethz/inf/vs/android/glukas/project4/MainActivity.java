package ch.ethz.inf.vs.android.glukas.project4;import java.util.ArrayList;import ch.ethz.inf.vs.android.glukas.project4.RegistrationDialogFragment.RegistrationDialogFragmentDelegate;import ch.ethz.inf.vs.android.glukas.project4.networking.FriendshipRequest;import ch.ethz.inf.vs.android.glukas.project4.test.Data;import android.app.ActionBar;import android.app.ActionBar.Tab;import android.app.Fragment;import android.app.FragmentTransaction;import android.app.ListFragment;import android.content.Intent;import android.nfc.NfcAdapter;import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;import android.nfc.NfcEvent;import android.os.Bundle;import android.util.Log;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import android.widget.ListView;import android.widget.Toast;public class MainActivity extends WallActivity implements OnNdefPushCompleteCallback, UserDelegate, RegistrationDialogFragmentDelegate {	private NfcAdapter nfcAdapter;	private FriendshipRequest nextRequest;	private FriendListAdapter friendListAdapter;		private boolean mRegisterDialogShowing = false;	////	//LIFECYCLE	////		@Override	protected void onCreate(Bundle savedInstanceState) {		super.onCreate(savedInstanceState);		setContentView(R.layout.home_screen);		setUpNFCExchange();		//set up views		ActionBar actionBar = getActionBar();		actionBar.setDisplayShowHomeEnabled(false);		actionBar.setDisplayShowTitleEnabled(false);		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);		ActionBar.Tab tab1 = actionBar.newTab().setText("My Wall");		ActionBar.Tab tab2 = actionBar.newTab().setText("View Friends");		WallFragment wallFragment = new WallFragment();		wallFragment.setListAdapter(userWallAdapter);		tab1.setTabListener(new MyTabListener(wallFragment));		FriendListFragment friendListFragment = new FriendListFragment();		friendListAdapter = new FriendListAdapter(this, new ArrayList<User>());		friendListFragment.setListAdapter(friendListAdapter);		tab2.setTabListener(new MyTabListener(friendListFragment));		actionBar.addTab(tab1);		actionBar.addTab(tab2);	}	@Override	protected void onPause() {		super.onPause();	}	@Override	protected void onResume() {		checkUserRegistered();		wallOwner = mProtocol.getUser();		super.onResume();				updateFriends();	}	protected void updateFriends() {		if (mProtocol.getUser() != null) {			if (mProtocol.getFriend(Data.alice.id) == null && !Data.alice.equals(mProtocol.getUser())) {				mProtocol.putFriend(Data.alice);			}			if (mProtocol.getFriend(Data.bob.id) == null) {				mProtocol.putFriend(Data.bob);			}						/*int id = new UserId().hashCode();//Testing (add some users to begin with)			if (friendsList.size() == 0) {				friendsList = Arrays.asList(new User("Alice " + id), new User("Bob " + id), new User("Carol " + id));				for (User friend : friendsList) {					dbmanager.putFriend(friend);				}			}*/						for (User user : mProtocol.getUserMapping().values()) {				if (!mProtocol.getUser().id.equals(user.id)) {					this.friendListAdapter.add(user);				}			}		}	}		private void checkUserRegistered() {		if(mProtocol.getUser() == null && !mRegisterDialogShowing) {			// Create registration dialog			RegistrationDialogFragment dialog = new RegistrationDialogFragment(this);			dialog.show(this.getFragmentManager(), "User registration");			mRegisterDialogShowing = true;		}	}	////	//NFC (Friendship Request)	////	private void setUpNFCExchange() {		nfcAdapter = NfcAdapter.getDefaultAdapter(this);		if (nfcAdapter == null) {			Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();			return;		} else {			createNextRequest();		}	}	private void createNextRequest() {		nextRequest = new FriendshipRequest(mProtocol.getUser());		nfcAdapter.setNdefPushMessageCallback(nextRequest, this);		nfcAdapter.setOnNdefPushCompleteCallback(this, this);	}	////	//OnNdefPushCompleteCallback	////	@Override	public void onNdefPushComplete(NfcEvent event) {		Log.d(this.getClass().toString(), "onNdefPushComplete");		FriendshipRequest.setCurrentRequest(nextRequest);		createNextRequest();	}	public class FriendListFragment extends ListFragment {		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){			View view = inflater.inflate(R.layout.friend_list_tab, container, false);			return view;		}		@Override		public void onListItemClick (ListView l, View v, int position, long id) {			Intent contactDetail = new Intent(getActivity(), ContactDetailActivity.class);			BasicUser friend = ((BasicUser)this.getListAdapter().getItem(position));			Log.d(this.getClass().toString(), "clicked on user " + friend +" id : " + friend.getId());			contactDetail.putExtra(ContactDetailActivity.USERID_EXTRA, friend.id.getBytes());			startActivity(contactDetail);		}	}	public class MyTabListener implements ActionBar.TabListener {		Fragment fragment;		public MyTabListener(Fragment fragment) {			this.fragment = fragment;		}		public void onTabSelected(Tab tab, FragmentTransaction ft) {			ft.replace(R.id.home_screen, fragment);			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);		}		public void onTabUnselected(Tab tab, FragmentTransaction ft) {			ft.remove(fragment);		}		public void onTabReselected(Tab tab, FragmentTransaction ft) {			// nothing done here		}	}	////	//RegistrationDialogFragmentDelegate	////	@Override	public void onUserRegistered(String username) {		mRegisterDialogShowing = false;		if (username.trim().isEmpty()) {			Toast.makeText(getApplicationContext(), R.string.empty_username, Toast.LENGTH_LONG).show();			checkUserRegistered();		} else {			Toast.makeText(getApplicationContext(),"User " + username + " was succesfully registered.", Toast.LENGTH_LONG).show();			this.mProtocol.putUser(new User(username));			wallOwner = mProtocol.getUser();		}	}}