package ch.ethz.inf.vs.android.glukas.project4;

import ch.ethz.inf.vs.android.glukas.networking.FriendService;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

	private Intent friendService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button makeFriends = (Button)findViewById(R.id.button1);
		makeFriends.setOnClickListener(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		friendService = new Intent(this, FriendService.class);
		startService(friendService);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		stopService(friendService);
	}

	@Override
	public void onClick(View v) {
		//TODO this is just a quick hack
		startActivity(new Intent(this, FriendDiscoveryActivity.class));
	}
}
