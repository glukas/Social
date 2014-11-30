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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements
		OnNdefPushCompleteCallback, UserDelegate, SecureChannelDelegate {

	private static final String tag = "MAIN_ACTIVITY";
	private Button mConnectButton, mAddFriendButton, mViewWallButton;
	private AlertDialog.Builder mAlertBuilder;

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
		setContentView(R.layout.connection_screen);

		dbmanager = new DatabaseManager(this);

		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		if (nfcAdapter == null) {
			Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG)
					.show();
			finish();
			return;
		}

		
		
		/*
		 * mAlertBuilder = new AlertDialog.Builder(this);
		 * 
		 * mProtocol = Protocol.getInstance(new DatabaseManager(this));
		 * mProtocol.setDelegate(this); mConnectButton =
		 * (Button)findViewById(R.id.connectButton);
		 * 
		 * mConnectButton.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { mProtocol.connect(); } });
		 */
		
		
		// TODO: I think these buttons are meant to be in another Activity
		
		/* mAddFriendButton = (Button)findViewById(R.id.home_add_friend_button);
		 * 
		 * if (mAddFriendButton != null){ Log.d("not null", "not null"); } else
		 * Log.d("null","null"); mAddFriendButton.setOnClickListener(new
		 * OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { showFriendRequest(); } });
		 * 
		 * mViewWallButton = (Button)findViewById(R.id.home_wall_button);
		 * 
		 * mViewWallButton.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { showWall(); } });
		 */


		// TODO TESTING
		/*
		 * channel = new StaticSecureChannel("winti.mooo.com", 9000); db = new
		 * StaticDatabase(); channel.setDelegate(this); PublicHeader header =
		 * new PublicHeader(44, null, StatusByte.CONNECT.getByte(), 0,
		 * Data.dummySenderId, Data.serverId); channel.sendHeader(header);
		 * Log.i("DEBUG", Data.tag+"header send");
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

}
