package ch.ethz.inf.vs.android.glukas.networking;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothManager;
import android.content.Context;

public class FriendDiscovery implements LeScanCallback {
	
	private List<BluetoothDevice> devices = new ArrayList<BluetoothDevice>();
	private List<String> deviceNames = new ArrayList<String>();
	private Context context;
	
	private BluetoothAdapter bluetoothAdapter;
	private static UUID[] friendServices = {FriendService.friendServiceId};
	
	public FriendDiscovery(FriendDiscoveryDelegate delegate, Context context) {
		assert(context != null);
		assert(delegate != null);
		
		this.context = context;
		devices = new ArrayList<BluetoothDevice>();
		bluetoothAdapter = ((BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
	}
	
	public void resumeDiscovery() {
		bluetoothAdapter.startLeScan(friendServices, this);
	}
	
	public void pauseDiscovery() {
		bluetoothAdapter.stopLeScan(this);
	}
	
	public void requestFriendship(Peer peer) {
		
	}
	
	/**
	 * Encapsulates a peer that was discovered.
	 * Used as a handle to later initiate a friendship request.
	 */
	public class Peer {
		public final String peerUsername;
		Peer(String username) {
			peerUsername = username;
		}
		BigInteger userId;
		BluetoothDevice userDevice;
	}

	@Override
	public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
		//TODO (Lukas) is it necessary to synchronize this?
		if (!devices.contains(device)) {
			devices.add(device);
			deviceNames.add(device.getName());
			device.connectGatt(context, false, new FriendGattCallback(this));
		}
	}
	
	
}
