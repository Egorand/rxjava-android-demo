package egorand.rxjavaandroiddemo.test;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.view.ViewGroup;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import egorand.rxjavaandroiddemo.MainActivity;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;

public class BeerDataLoadingTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public BeerDataLoadingTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    public void testDataIsLoadedFromInternetAndTheListViewIsNotEmpty() {
        onView(withId(android.R.id.list)).check(matches(hasChildren()));
    }

    private Matcher<View> hasChildren() {
        return new BaseMatcher<View>() {
            @Override public boolean matches(Object o) {
                ViewGroup viewGroup = (ViewGroup) o;
                return viewGroup.getChildCount() > 0;
            }

            @Override public void describeTo(Description description) {
                description.appendText("Children count should be non-zero");
            }
        };
    }
}
