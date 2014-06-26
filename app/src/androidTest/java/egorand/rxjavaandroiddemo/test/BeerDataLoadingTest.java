package egorand.rxjavaandroiddemo.test;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import egorand.rxjavaandroiddemo.MainActivity;
import egorand.rxjavaandroiddemo.model.Beer;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.startsWith;

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

    public void testAllBeerNamesStartWithLetterB() {
        onView(withId(android.R.id.list)).check(matches(withAdapterData(withName(startsWith("B")))));
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

    private Matcher<View> withAdapterData(final Matcher<Object> dataMatcher) {
        return new TypeSafeMatcher<View>() {

            @Override
            public void describeTo(Description description) {
                description.appendText("with class name: ");
                dataMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof AdapterView)) {
                    return false;
                }
                @SuppressWarnings("rawtypes")
                Adapter adapter = ((AdapterView) view).getAdapter();
                for (int i = 0; i < adapter.getCount(); i++) {
                    if (dataMatcher.matches(adapter.getItem(i))) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    private Matcher<Object> withName(final Matcher<String> matcher) {
        return new BaseMatcher<Object>() {
            @Override public boolean matches(Object o) {
                if (!(o instanceof Beer)) {
                    return false;
                }
                Beer beer = (Beer) o;
                return matcher.matches(beer.getName());
            }

            @Override public void describeTo(Description description) {
                description.appendText("Name matches ");
                matcher.describeTo(description);
            }
        };
    }
}
