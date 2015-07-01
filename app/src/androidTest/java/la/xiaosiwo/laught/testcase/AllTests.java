package la.xiaosiwo.laught.testcase;

import android.test.suitebuilder.TestSuiteBuilder;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Created by OF on 23:07.
 */
public class AllTests extends TestSuite {
    public static Test suite(){
        return new TestSuiteBuilder(AllTests.class).includeAllPackagesUnderHere().build();
    }
}
