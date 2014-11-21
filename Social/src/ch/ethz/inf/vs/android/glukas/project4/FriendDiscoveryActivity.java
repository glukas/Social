package ch.ethz.inf.vs.android.glukas.project4;

import java.util.List;

import ch.ethz.inf.vs.android.glukas.project4.networking.FriendRequestMessageCallback;

import android.app.Activity;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class FriendDiscoveryActivity extends Activity  {

	FriendRequestMessageCallback NfcCallback;
	
	////
	//ACTIVITY
	////
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_discovery);
		NfcAdapter adapter = NfcAdapter.getDefaultAdapter(this);
		NfcCallback = new FriendRequestMessageCallback();
		adapter.setNdefPushMessageCallback(NfcCallback, this);
		adapter.setOnNdefPushCompleteCallback(NfcCallback, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.friend_discovery, menu);
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
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}

}
