package ch.ethz.inf.vs.android.glukas.networking;

import java.util.ArrayList;
import java.util.List;


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
	
	public FriendDiscovery(FriendDiscoveryDelegate delegate, Context context) {
		assert(context != null);
		assert(delegate != null);
		
		this.context = context;
		devices = new ArrayList<BluetoothDevice>();
		bluetoothAdapter = ((BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
	}
	
	public void resumeDiscovery() {
		bluetoothAdapter.startLeScan(this);
	}
	
	public void pauseDiscovery() {
		bluetoothAdapter.stopLeScan(this);
	}

	@Override
	public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
		if (!devices.contains(device)) {
			devices.add(device);
			deviceNames.add(device.getName());
			//device.connectGatt(context, false, new FriendGattCallback(this));//TODO
		}
	}
	
	
}
