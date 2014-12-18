package ch.ethz.inf.vs.android.glukas.project4.database;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.text.*;
import ch.ethz.inf.vs.android.glukas.project4.Post;
import ch.ethz.inf.vs.android.glukas.project4.UserId;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Utility {
	
	// Implementation constants.
	// TODO: think about making them dynamic
	public static final int MAX_BLOB_SIZE = 0;	// TODO: define # of bytes needed for a blob (max 400kB?)
	public static final int MAX_DATABASE_SIZE = 0;	// TODO: define max byte size the database can reach (dynamic?)
	
	public static UserId userId = new UserId("-1");
	
	private static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
	
	/**
	 * Transforms a byte[] representing an image into a bitmap.
	 * @param blobImage byte[] representing an image.
	 * @return bitmap image
	 */
	public static final Bitmap toBitmap(byte[] blobImage) {
		return BitmapFactory.decodeByteArray(blobImage, 0, blobImage.length);
	}
	
	/**
	 * Transforms a Bitmap into a byte[].
	 * @param bitmapImage
	 * @return
	 */
	public static final byte[] toByteArray(Bitmap bitmapImage) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		boolean success = bitmapImage.compress(Bitmap.CompressFormat.JPEG, 80, bos);
		if (!success) throw new RuntimeException();
		return bos.toByteArray();
	}
	
	/**
	 * Transforms a UserId into a byte[].
	 * This is needed because ids need to be stored in blobs.
	 * @param userid the UserId object to be transformed
	 * @return byte[] representation of the input
	 */
	public static final String toSQLiteId(UserId userid) {
		return userid.getId().toString();
	}
	
	/**
	 * Java date format: 
	 * @param sqlDate Date in SQLite String format
	 * @return Java Date object
	 */
	public static final Date toJavaDate(String sqlDate) {
		try {
			return dateFormatter.parse(sqlDate);
		}
		catch(ParseException e) {
			return null;
		}
	}
	
	/** FIXME: general use of Calendar instead of Date?
	 * SQL string date format: YYYY-MM-DD HH:MM:SS
	 * @param javaDate
	 * @return
	 */
	public static final String toSQLiteDate(Date javaDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("CET"));
		calendar.setTime(javaDate);
		return dateFormatter.format(calendar.getTime());
	}
	
	/**
	 * Retrieves data from the current cursor position in order to build
	 * a Post object.
	 * @param cursor with following projection: id, poster_id, text, image, datetime 
	 * @return Post object
	 */
	public static final Post buildPost(Cursor cursor) {
		/*for (int i = 0; i < 3; i++){
			Log.d("DATABASE DEBUG", "col " + i + " # "+cursor.getString(i));
		}*/
		// Get id.
		int id = cursor.getInt(0);
		// Get poster id.
		UserId poster_id = new UserId(cursor.getString(1));
		
		UserId wall_owner_id = new UserId(cursor.getString(2));
		// Get text.
		String text = cursor.getString(3);
		// Get image.
		Bitmap image = null;
		if(cursor.getBlob(4) != null) {
			image = Utility.toBitmap(cursor.getBlob(4));
		}
		// Get datetime.
		Date datetime = null;
		if(cursor.getString(5) != null) {
			datetime = Utility.toJavaDate(cursor.getString(5));
		}
		// Build and return post.
		return new Post(id, poster_id, wall_owner_id, text, image, datetime);
	}
	
}
