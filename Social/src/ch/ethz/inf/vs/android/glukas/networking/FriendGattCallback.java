package ch.ethz.inf.vs.android.glukas.networking;

import ch.ethz.inf.vs.android.glukas.networking.FriendDiscovery.Peer;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

public class FriendGattCallback extends BluetoothGattCallback {

	public FriendGattCallback(FriendDiscovery friendDiscovery) {
		
	}

	@Override
    // New services discovered
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
			for (BluetoothGattService service : gatt.getServices()) {
				
				if (service.getUuid().equals(FriendService.friendServiceId)) {
					boolean success = gatt.readCharacteristic(service.getCharacteristic(FriendService.profileAdvertisementCharacteristicId));
					if (!success) {
						gatt.close();
						Log.e(this.getClass().toString(), "read failed");
					}
					
				}
			}
        } else {
        	//TODO (Lukas)
        	Log.e(this.getClass().toString(), "onServiceDiscovered failed");
        }
    }

    @Override
    // Result of a characteristic read operation
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
        	
        	if (characteristic.getUuid().equals(FriendService.profileAdvertisementCharacteristicId)) {
        		String profileMessage = characteristic.getStringValue(0);
        		Log.d(this.getClass().toString(), profileMessage);
        		BluetoothDevice device = gatt.getDevice();
        		//TODO (Vincent/Young) process message
        		Peer peer;//= new Peer(...)
        		//TODO (Lukas) Callback to FriendDiscovery (using some handler)
        		
        	}
        } else {
        	//TODO (Lukas)
        }
        gatt.close();
    }
	
}
