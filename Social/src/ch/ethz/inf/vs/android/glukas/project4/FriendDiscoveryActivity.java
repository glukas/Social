package ch.ethz.inf.vs.android.glukas.project4;

import java.util.List;

import ch.ethz.inf.vs.android.glukas.networking.FriendDiscovery;
import ch.ethz.inf.vs.android.glukas.networking.FriendDiscovery.Peer;
import ch.ethz.inf.vs.android.glukas.networking.FriendDiscoveryDelegate;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class FriendDiscoveryActivity extends Activity implements FriendDiscoveryDelegate {

	private FriendDiscovery friendDiscovery;
	
	////
	//ACTIVITY
	////
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_discovery);
		
		friendDiscovery = new FriendDiscovery(this, this);
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
		friendDiscovery.pauseDiscovery();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		friendDiscovery.resumeDiscovery();
	}
	
	////
	//FRIEND DISCOVERY DELEGATE
	////
	
	@Override
	public void onPeersDiscoveredChanged(List<Peer> discoveredPeers) {
		// TODO (Samuel) display
	}

	@Override
	public void onFriendshipRequestAccepted(Peer peer) {
		// TODO (Samuel) display
	}
}
