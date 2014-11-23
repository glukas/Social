package ch.ethz.inf.vs.android.glukas.project4.security;

import ch.ethz.inf.vs.android.glukas.project4.protocol.PublicHeader;

public class NetworkMessage {

	public final String text;
	public final PublicHeader header;
	
	public NetworkMessage(String text, PublicHeader header) {
		this.text = text;
		this.header = header;
	}
	
}
