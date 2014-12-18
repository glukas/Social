package ch.ethz.inf.vs.android.glukas.project4;

import ch.ethz.inf.vs.android.glukas.project4.RegistrationDialogFragment.RegistrationDialogFragmentDelegate;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseManager;
import ch.ethz.inf.vs.android.glukas.project4.networking.FriendshipRequest;
import ch.ethz.inf.vs.android.glukas.project4.networking.FriendshipResponse;
import ch.ethz.inf.vs.android.glukas.project4.protocol.Protocol;
import ch.ethz.inf.vs.android.glukas.project4.security.ZeroCredentialStorage;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class IncomingFriendshipActivity extends Activity implements
OnNdefPushCompleteCallback, RegistrationDialogFragmentDelegate {

	TextView usernameTextView;
	FriendshipRequest request;
	FriendshipResponse response;
	NfcAdapter nfcAdapter;
	Protocol protocol;
	
	////
	//LIFECYCLE
	////

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_incoming_friendship);
		protocol = new Protocol(new DatabaseManager(this));
		this.usernameTextView = (TextView) findViewById(R.id.usernameTextView);
	}


	@Override
	public void onNewIntent(Intent intent) {
		// onResume gets called after this to handle the intent
		setIntent(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		checkUserRegistered();//This could be the very first activity started
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
			processIntent(getIntent());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.incoming_friendship, menu);
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

	private void checkUserRegistered() {
		if(protocol.getUser() == null) {
			// Create registration dialog
			RegistrationDialogFragment dialog = new RegistrationDialogFragment(this);
			dialog.show(this.getFragmentManager(), this.getClass().toString());
		}
	}

	private void processIntent(Intent intent) {
		Parcelable[] rawMsgs = intent
				.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
		// only one message sent during the beam
		NdefMessage msg = (NdefMessage) rawMsgs[0];
		request = new FriendshipRequest(msg);

		nfcAdapter = NfcAdapter.getDefaultAdapter(this);

		// TODO better error handling
		if (nfcAdapter == null) {
			Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
			finish();
			return;
		}

		displayRequest(request);
		if (protocol.getUser() != null) {
			prepareResponse();
		}

	}

	// Save friend in database
	private void saveFriend(FriendshipRequest request) {
		final User friend = request.getSender();
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				protocol.putFriend(friend);
			}
		});
	}

	private void displayRequest(FriendshipRequest request) {
		usernameTextView.setText(request.getSender().getUsername());
		usernameTextView.setTextColor(Color.parseColor(request.getSender().getColor()));
		Log.d(this.getClass().toString(), "Friend request "
				+ request.getSender().getUsername() + " id : "
				+ request.getSender().getId());
	}

	private void prepareResponse() {
		Log.d(this.getClass().toString(), "prepare response");
		response = request.createAcceptingResponse(protocol.getUser());
		nfcAdapter.setNdefPushMessageCallback(response, this);
		nfcAdapter.setOnNdefPushCompleteCallback(this, this);
	}

	////
	//RegistrationDialogFragmentDelegate
	////

	@Override
	public void onUserRegistered(String username) {
		protocol.putUser(new User(username));
		prepareResponse();
	}

	////
	//OnNdefPushCompleteCallback
	////

	@Override
	public void onNdefPushComplete(NfcEvent event) {
		//NOTE: we only save the friend once someone (hopefully the same person) received the reply.
		//This means that the request was accepted. (by doing an exchange in the other direction)
		saveFriend(request);
		
		//Navigate to the friend's Wall, with the parent activity being 
		NavUtils.navigateUpFromSameTask(this);
		
		Intent contactDetail = new Intent(this, ContactDetailActivity.class);
		contactDetail.putExtra(ContactDetailActivity.USERID_EXTRA, request.getSender().id.getBytes());
		startActivity(contactDetail);
	}


}
