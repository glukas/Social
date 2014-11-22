package ch.ethz.inf.vs.android.glukas.project4;

import ch.ethz.inf.vs.android.glukas.project4.networking.FriendshipOutgoingRequest;
import ch.ethz.inf.vs.android.glukas.project4.protocol.Protocol;
import ch.ethz.inf.vs.android.glukas.project4.protocol.ProtocolDelegate;
import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	NfcAdapter nfcAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        
		FriendshipOutgoingRequest request = new FriendshipOutgoingRequest();
		nfcAdapter.setNdefPushMessageCallback(request, this);
		nfcAdapter.setOnNdefPushCompleteCallback(request, this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
}
