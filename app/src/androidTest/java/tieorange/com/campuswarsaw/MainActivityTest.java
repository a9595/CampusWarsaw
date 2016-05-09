package tieorange.com.campuswarsaw;

import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by tieorange on 09/05/16.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    public static final String EXISTING_EVENT_NAME = "Private Equity Academy";
    @Rule
    public final ActivityRule<MainActivity> main =
            new ActivityRule<>(MainActivity.class);

    @Test
    public void shouldBeAbleToLaunchMainScreen() {
        onView(withText(EXISTING_EVENT_NAME)).check(ViewAssertions.matches(isDisplayed()));
    }
}
