package ch.ethz.inf.vs.android.glukas.project4.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import android.test.suitebuilder.TestSuiteBuilder;

/**
 * A test suite containing all tests for the application
 */
public class AllTests extends TestSuite {
	
    public static Test suite() {
        return new TestSuiteBuilder(AllTests.class).includeAllPackagesUnderHere().build();
    }
}
