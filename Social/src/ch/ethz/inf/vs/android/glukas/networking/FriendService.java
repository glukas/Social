package ch.ethz.inf.vs.android.glukas.networking;

import java.util.UUID;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.bluetooth.*;

/**
 * This service listens for incoming friend request over BLUETOOTH and forwards requests to some user-visible component
 */
public class FriendService extends Service {

	//BluetoothGattServer gattServer;
	BluetoothManager bluetoothManager;
	BluetoothAdapter bluetoothAdapter;
	FriendGattServerCallback friendCallback;
	
	public static final UUID friendServiceId = new UUID(1110, 10101);//TODO: proper UUID (http://www.ietf.org/rfc/rfc4122.txt)
	
	//The Advertisement characteristic presents this peers profile-message to others, so that they can decide to friend him or her
	public static final UUID profileAdvertisementCharacteristicId = new UUID(1110, 90101);//TODO: proper UUID (http://www.ietf.org/rfc/rfc4122.txt)

	/**
	 * @param profileMessage the string that other peers can see when discovering this peer
	 */
	public FriendService(String profileMessage) {
		bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
		bluetoothAdapter = bluetoothManager.getAdapter();
		if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
			throw new RuntimeException("TODO : handle no bluetooth");
		}
		
		BluetoothGattServer gattServer = bluetoothManager.openGattServer(getApplicationContext(), friendCallback);
		gattServer.addService(constructFriendGattService(profileMessage));
		
		friendCallback = new FriendGattServerCallback(gattServer);
	}
	
	static private BluetoothGattService constructFriendGattService(String profileMessage) {
		BluetoothGattService service = new BluetoothGattService(friendServiceId, BluetoothGattService.SERVICE_TYPE_PRIMARY);
		
		service.addCharacteristic(constructProfileAdvertisementCharacteristic(profileMessage));
		
		return service;
	}

	private static BluetoothGattCharacteristic constructProfileAdvertisementCharacteristic(String profileMessage) {
		
		int properties = BluetoothGattCharacteristic.PROPERTY_READ;
		int permissions = BluetoothGattCharacteristic.PERMISSION_READ;
		BluetoothGattCharacteristic peerAdvertisement = new BluetoothGattCharacteristic(profileAdvertisementCharacteristicId, properties, permissions);
		peerAdvertisement.setValue(profileMessage);
		
		return peerAdvertisement;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
}
