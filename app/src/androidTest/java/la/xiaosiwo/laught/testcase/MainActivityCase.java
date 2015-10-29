package la.xiaosiwo.laught.testcase;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import la.xiaosiwo.laught.R;

/**
 * Created by OF on 201-07-01 22:14.
 */
public class MainActivityCase extends ActivityInstrumentationTestCase2<MainActivity> {
    private String TAG = "TestCase";
    private Solo solo;
    public MainActivityCase(){
        super(MainActivity.class);
    }
    @Override
    public void setUp() throws Exception {
        // setUp() is run before a test case is started.
        // This is where the solo object is created.
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }
    @Override
    public void tearDown() throws Exception {
        // tearDown() is run after a test case has finished.
        // finishOpenedActivities() will finish all the activities that have
        // been opened during the test execution.
        solo.finishOpenedActivities();
        super.tearDown();
    }

    public void testMainActivity() throws Exception {

        solo.clickOnActionBarItem(R.id.menu_pattern_lock);
    }
}
