package ch.ethz.inf.vs.android.glukas.project4.database;

import java.io.ByteArrayOutputStream;
import java.util.Date;

import ch.ethz.inf.vs.android.glukas.project4.Post;
import ch.ethz.inf.vs.android.glukas.project4.UserId;
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
	public static final int MAX_BLOB_SIZE = 0;	// TODO: define # of bytes needed for a blob (images + keys + big text?)
	public static final int MAX_DATABASE_SIZE = 0;	// TODO: define # of bytes needed for the DB
	
	public static UserId userID = new UserId("0");
	
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
		bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, bos);	// TODO: adapt quality (second parameter)
		return bos.toByteArray();
	}
	
	/**
	 * Transforms a UserId into a byte[].
	 * This is needed because ids need to be stored in blobs.
	 * @param userid the UserId object to be transformed
	 * @return byte[] representation of the input
	 */
	public static final byte[] toSQLiteId(UserId userid) {
		return userid.getId().toByteArray();
	}
	
	/** TODO
	 * Java date format:
	 * @param sqlDate Date in SQLite String format
	 * @return Java Date object
	 */
	public static final Date toJavaDate(String sqlDate) {
		return null;
	}
	
	/** TODO
	 * SQL date format: 
	 * @param javaDate
	 * @return
	 */
	public static final String toSQLiteDate(Date javaDate) {
		return null;
	}
	
	/**
	 * Retrieves data from the current cursor position in order to build
	 * a Post object.
	 * @param cursor
	 * @return Post object
	 */
	public static final Post buildPost(Cursor cursor) {
		// Get id.
		int id = cursor.getInt(0);
		// Get poster id.
		UserId poster_id = new UserId(cursor.getBlob(1));
		// Get text.
		String text = cursor.getString(4);
		// Get image.
		Bitmap image = Utility.toBitmap(cursor.getBlob(5));
		// Get datetime.
		Date datetime = Utility.toJavaDate(cursor.getString(3));
		// Build adn return post.
		return new Post(id, poster_id, text, image, datetime);
	}
}
