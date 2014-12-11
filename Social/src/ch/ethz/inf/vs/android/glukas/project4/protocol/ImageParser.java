package ch.ethz.inf.vs.android.glukas.project4.protocol;

import java.io.ByteArrayOutputStream;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class ImageParser {

	// Parse the bitmap image to a bytearray

	public byte[] getBytesfrom(Bitmap image) {
		ByteArrayOutputStream boas = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.PNG, 100, boas);
		byte[] result = boas.toByteArray();
		return result;
	}

	// Parse the bitmap image to String
	public String getStringfrom(Bitmap image) {
		String resultstring = Base64.encodeToString(this.getBytesfrom(image),
				Base64.DEFAULT);
		return resultstring;
	}

	// Get a Bitmap from a Bytearray
	public Bitmap getBitmapfromByteArray(byte[] imagebytes) {
		Bitmap resultimage = BitmapFactory.decodeByteArray(imagebytes, 0,
				imagebytes.length);
		return resultimage;

	}

	// Get a Bitmap from a String
	public Bitmap getBitmapfromString(String imagestring) {
		byte[] imagebytes = Base64.decode(imagestring, Base64.DEFAULT);
		Bitmap resultimage = this.getBitmapfromByteArray(imagebytes);
		return resultimage;
	}
}
