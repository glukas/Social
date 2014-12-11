package ch.ethz.inf.vs.android.glukas.project4.database;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.text.*;

import ch.ethz.inf.vs.android.glukas.project4.Post;
import ch.ethz.inf.vs.android.glukas.project4.User;
import ch.ethz.inf.vs.android.glukas.project4.UserCredentials;
import ch.ethz.inf.vs.android.glukas.project4.UserId;
import ch.ethz.inf.vs.android.glukas.project4.Wall;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

class Utility {
	
	// SQLite constants.
	public static final String NULL_TYPE = "NULL";	// The value is a NULL value.
	public static final String INTEGER_TYPE = "INTEGER";	// The value is a signed integer, stored in 1, 2, 3, 4, 6, or 8 bytes depending on the magnitude of the value.
	public static final String REAL_TYPE = "REAL";	// The value is a floating point value, stored as an 8-byte IEEE floating point number.
	public static final String TEXT_TYPE = "TEXT";	// The value is a text string, stored using the database encoding (UTF-8, UTF-16BE or UTF-16LE).
	public static final String BLOB_TYPE = "BLOB";	// The value is a blob of data, stored exactly as it was input.
	
	public static final String CREATE_TABLE = "CREATE TABLE";
	public static final String DROP_TABLE = "DROP TABLE";
	public static final String NOT_NULL = "NOT NULL";
	public static final String IF_EXISTS = "IF EXISTS";
	public static final String PRIMARY_KEY = "PRIMARY KEY";
	public static final String FOREIGN_KEY = "FOREIGN KEY";
	public static final String REFERENCES = "REFERENCES";
	public static final String ON_DELETE = "ON DELETE";
	public static final String CASCADE = "CASCADE";
	public static final String SET_NULL = "SET NULL";
	public static final String NO_ACTION = "NO ACTION";
	
	// Implementation constants.
	// TODO: think about making them dynamic
	public static final int MAX_BLOB_SIZE = 0;	// TODO: define # of bytes needed for a blob (max 400kB?)
	public static final int MAX_DATABASE_SIZE = 0;	// TODO: define max byte size the database can reach (dynamic?)
	
	public static UserId userId;
	
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
		bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, bos);	// TODO: adapt quality (second parameter)
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
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return format.parse(sqlDate);
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
		// Get id.
		int id = cursor.getInt(0);
		// Get poster id.
		UserId poster_id = new UserId(cursor.getBlob(1));
		// Get text.
		String text = cursor.getString(2);
		// Get image.
		Bitmap image;
		if(cursor.getBlob(3) != null)
			image = Utility.toBitmap(cursor.getBlob(3));
		else
			image = null;
		// Get datetime.
		Date datetime;
		if(cursor.getString(4) != null)
			datetime = Utility.toJavaDate(cursor.getString(4));
		else
			datetime = null;
		// Build and return post.
		return new Post(id, poster_id, text, image, datetime);
	}
	
}
