package ch.ethz.inf.vs.android.glukas.networking;

import android.bluetooth.*;
import android.util.Log;

/**
 * Responds to friendship discovery queries by some other peer.
 */
class FriendGattServerCallback extends BluetoothGattServerCallback{

	public final BluetoothGattServer gattServer;
	
	public FriendGattServerCallback(BluetoothGattServer gattServer) {
		this.gattServer = gattServer;
	}

	@Override
	public void onCharacteristicReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattCharacteristic characteristic) {
		if (characteristic.equals(FriendService.profileAdvertisementCharacteristicId)) {
			if (offset != 0) {
				gattServer.sendResponse(device, requestId, BluetoothGatt.GATT_INVALID_OFFSET, offset, new byte[0]);
			}
			Log.d(this.getClass().toString(), "read characteristic " + characteristic.getStringValue(0));
			//TODO: maybe we actually need to pass in the strings value directly here
			gattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, characteristic.getValue());
		} else {
			Log.e(this.getClass().toString(), "unkown gatt characteristic read request");
			gattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, new byte[0]);
		}
	}
	
}
