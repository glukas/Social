package ch.ethz.inf.vs.android.glukas.project4.networking;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.NfcEvent;
import android.util.Log;

public class FriendRequestMessageCallback implements CreateNdefMessageCallback, OnNdefPushCompleteCallback {

	private static final String APPLICATION_NAME = "ch.ethz.inf.vs.android.glukas.project4";
	
	@Override
	public NdefMessage createNdefMessage(NfcEvent event) {
		
		NdefMessage message = new NdefMessage(NdefRecord.createApplicationRecord(APPLICATION_NAME));
		return message;
	}

	@Override
	public void onNdefPushComplete(NfcEvent event) {
		// TODO Auto-generated method stub
		Log.d(this.getClass().toString(), "onNdefPushComplete");
	}

}
