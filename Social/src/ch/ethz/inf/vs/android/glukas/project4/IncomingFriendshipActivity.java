package ch.ethz.inf.vs.android.glukas.project4;

import ch.ethz.inf.vs.android.glukas.project4.networking.FriendshipRequest;
import ch.ethz.inf.vs.android.glukas.project4.networking.FriendshipResponse;
import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class IncomingFriendshipActivity extends Activity implements OnNdefPushCompleteCallback {

	TextView usernameTextView;
	FriendshipRequest request;
	FriendshipResponse response;
	NfcAdapter nfcAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_incoming_friendship);
		this.usernameTextView = (TextView) findViewById(R.id.usernameTextView);
		
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
	
    @Override
    public void onNewIntent(Intent intent) {
        // onResume gets called after this to handle the intent
        setIntent(intent);
    }
	
	@Override
	protected void onResume() {
		super.onResume();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
        	processIntent(getIntent());
        }
	}
	
	private void processIntent(Intent intent) {
		Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        // only one message sent during the beam
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        request = new FriendshipRequest(msg);
        //TODO (Vincent?/Young?/Samuel?) replace with this device's user
        response = request.createAcceptingResponse(new User(new UserId("0"), "Bob", null, null));
 
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		//TODO better error handling
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        
		nfcAdapter.setNdefPushMessageCallback(response, this);
		nfcAdapter.setOnNdefPushCompleteCallback(this, this);

        displayRequest(request);
	}

	private void displayRequest(FriendshipRequest request) {
		usernameTextView.setText(request.getSender().getUsername());
    	Log.d(this.getClass().toString(), "Friend request " + request.getSender().getUsername() + " id : " + request.getSender().getId());
	}

	@Override
	public void onNdefPushComplete(NfcEvent event) {
		//TODO (Lukas) we would actually have to make sure that the correct recipient was reached
		Log.d(this.getClass().toString(), "successfully friended");
	}
	
	/*
	@Override
	public void onClick(View v) {
		//Ask user to invoke android beam (this can be done automatically since API version 21)
	}*/

}
