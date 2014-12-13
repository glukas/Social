package ch.ethz.inf.vs.android.glukas.project4.test;

import org.junit.Test;

import ch.ethz.inf.vs.android.glukas.project4.R;
import ch.ethz.inf.vs.android.glukas.project4.protocol.ImageParser;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.AndroidTestCase;


public class ImageParsingTest extends AndroidTestCase {

		
		@Test
		public void testImageParsingBytes() {
			ImageParser imgparser = new ImageParser();
			Bitmap icon = BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.ic_launcher);
			icon = Bitmap.createScaledBitmap(icon, 500, 500, false);
			byte[] imgbytes = imgparser.getBytesfrom(icon);
			Bitmap parsedImage  = imgparser.getBitmapfromByteArray(imgbytes);
			boolean isSame = icon.sameAs(parsedImage);
			
			
			assertTrue(isSame);
			
		}
		
		
		@Test
		public void testImageParsingString() {
			ImageParser imgparser = new ImageParser();
			Bitmap icon = BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.ic_launcher);
			icon = Bitmap.createScaledBitmap(icon, 500, 500, false);
			String imgstring = imgparser.getStringfrom(icon);
			Bitmap parsedImage  = imgparser.getBitmapfromString(imgstring);
			boolean isSame = icon.sameAs(parsedImage);
			
			
			assertTrue(isSame);
			
		}
}
