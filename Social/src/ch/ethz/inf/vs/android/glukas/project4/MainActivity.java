package ch.ethz.inf.vs.android.glukas.project4;

import ch.ethz.inf.vs.android.glukas.project4.protocol.Protocol;
import ch.ethz.inf.vs.android.glukas.project4.protocol.ProtocolDelegate;
import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	 NfcAdapter nfcAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button makeFriends = (Button)findViewById(R.id.button1);
		makeFriends.setOnClickListener(this);
		
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
	}
	
	@Override
	protected void onResume() {
		super.onResume();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
        	 Toast.makeText(this, "Friend request", Toast.LENGTH_LONG).show();
            //TODO (Lukas) respond to request
        }
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void onClick(View v) {
		//TODO this is just a quick hack
		startActivity(new Intent(this, FriendDiscoveryActivity.class));
	}
}
