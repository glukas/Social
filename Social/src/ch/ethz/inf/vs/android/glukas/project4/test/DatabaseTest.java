package ch.ethz.inf.vs.android.glukas.project4.test;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

import ch.ethz.inf.vs.android.glukas.project4.User;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseAccess;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseManager;
import android.test.AndroidTestCase;

public class DatabaseTest extends AndroidTestCase {
	
//	@Test
//	public void testUserInsertion() {
//		DatabaseAccess db = new DatabaseManager(null);	// FIXME: context can't be null!! -> sqlError
//		String username = "MeIAndMySelf";
//		User user = new User(username);
//		db.putUser(user);
//		User user2 = db.getUser();
//		boolean response = user.getUsername().equals(user2.getUsername());
//		assertTrue(response);
//	}
//	
//	@Test
//	public void testBigIntegersByteConversion() {
//		BigInteger id1 = new BigInteger("1");
//		byte[] id2 = id1.toByteArray();
//		boolean compare = id1.toString().equals(id2.toString());
//		assertTrue(compare);
//	}
	
	@Test
	public void testDatetoStringConversionConversion() {
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
		String nowString = format.format(now);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("CET"));
		calendar.setTime(now);
		String calNow = format.format(calendar.getTime());
		String answer = "2014-12-7 14:16:11";
		assertTrue(answer.equals(calNow));
	}
	
//	@Test
//	public void testStringtoDateConversionConversion() {
//		BigInteger id1 = new BigInteger("1");
//		byte[] id2 = id1.toByteArray();
//		boolean compare = id1.toString().equals(id2.toString());
//		assertTrue(compare);
//	}

}
