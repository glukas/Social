package ch.ethz.inf.vs.android.glukas.project4.database;

class Utility {
	
	// SQLite constants.
	public static final String NULL_TYPE = "NULL";	// The value is a NULL value.
	public static final String INTEGER_TYPE = "INTEGER";	// The value is a signed integer, stored in 1, 2, 3, 4, 6, or 8 bytes depending on the magnitude of the value.
	public static final String REAL_TYPE = "REAL";	// The value is a floating point value, stored as an 8-byte IEEE floating point number.
	public static final String TEXT_TYPE = "TEXT";	// The value is a text string, stored using the database encoding (UTF-8, UTF-16BE or UTF-16LE).
	public static final String BLOB_TYPE = "BLOB";	// The value is a blob of data, stored exactly as it was input.
	
	public static final String CREATE_TABLE = "CREATE TABLE";
	public static final String DROP_TABLE = "DROP TABLE";
	public static final String IF_EXISTS = "IF EXISTS";
	
	// Implementation constants.
	public static final int MAX_BLOB_SIZE = 0;	// TODO: define # of bytes needed for a blob (images + keys + big text?)
	public static final int MAX_DATABASE_SIZE = 0;	// TODO: define # of bytes needed for the DB
	
	
}
