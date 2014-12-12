package ch.ethz.inf.vs.android.glukas.project4;

import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseAccess;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseManager;
import ch.ethz.inf.vs.android.glukas.project4.networking.FriendshipRequest;
import ch.ethz.inf.vs.android.glukas.project4.networking.FriendshipResponse;
import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ContactDetailActivity extends Activity {

	public static final String USERID_EXTRA = "ch.ethz.inf.vs.android.glukas.project4.USERID_EXTRA";

	TextView contactTextView;
	FriendshipResponse response;
	DatabaseAccess dbmanager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_detail);
		this.contactTextView = (TextView) findViewById(R.id.contactTextView);
		dbmanager = new DatabaseManager(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contact_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onNewIntent(Intent intent) {
		// onResume gets called after this to handle the intent
		setIntent(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();

		byte[] id = this.getIntent().getByteArrayExtra(USERID_EXTRA);
		if (id != null) {
			UserId uid = new UserId(id);
			Log.d(this.getClass().toString(), uid.toString());
			User friend = this.dbmanager.getFriend(uid);
			showFriend(friend);
		} else if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
			processIntent(getIntent());
		}


	}

	private void processIntent(Intent intent) {
		Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
		// only one message sent during the beam
		NdefMessage msg = (NdefMessage) rawMsgs[0];
		response = new FriendshipResponse(msg);
		if (response.isMatching(FriendshipRequest.getCurrentRequest())) {
			saveFriend(response);
			displayRequest(response);
		} else {
			displayFailure();
		}

	}
	// Save friend in database
	private void saveFriend(FriendshipResponse response) {
		dbmanager.putFriend(response.getSender());
	}

	private void showFriend(User friend) {
		this.getActionBar().setTitle(friend.getUsername());
		contactTextView.setText(friend.getUsername());
	}

	private void displayFailure() {
		contactTextView.setText("Friendship request failed. Please try again");
		Log.d(this.getClass().toString(), "Friend request failed.");
	}

	private void displayRequest(FriendshipResponse response) {
		// TODO (Samuel) could be nicer
		showFriend(response.getSender());

		Log.d(this.getClass().toString(), "Friend request accepted : " + response.getSender().getUsername() + " id " + response.getSender().getId());
	}
}
