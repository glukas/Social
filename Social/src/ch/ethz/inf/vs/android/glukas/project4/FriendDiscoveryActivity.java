package ch.ethz.inf.vs.android.glukas.project4;

import java.util.ArrayList;
import java.util.List;

import ch.ethz.inf.vs.android.glukas.networking.FriendDiscovery;
import ch.ethz.inf.vs.android.glukas.networking.FriendDiscoveryDelegate;
import android.app.Activity;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Displays a list of nearby peers to the user
 */
public class FriendDiscoveryActivity extends Activity implements FriendDiscoveryDelegate {

	FriendDiscovery friendDiscovery;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//TODO check for bluetooth availability, and ask user to enable bluetooth if necessary
		
		// Gets the Bluetooth Adapter
		friendDiscovery = new FriendDiscovery(this, this);
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
	
	@Override
	public void onFriendsDiscoveredChanged(List<String> discoveredFriends) {
		// TODO (Samuel) display
	}
	
}
