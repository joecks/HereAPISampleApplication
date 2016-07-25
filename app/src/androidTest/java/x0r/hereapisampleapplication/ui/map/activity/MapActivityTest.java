package x0r.hereapisampleapplication.ui.map.activity;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import com.here.android.mpa.mapping.MapMarker;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import x0r.hereapisampleapplication.R;
import x0r.hereapisampleapplication.ui.map.presenter.MapPresenter;
import x0r.hereapisampleapplication.ui.placesearch.activity.PlaceSearchActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.Visibility.GONE;
import static android.support.test.espresso.matcher.ViewMatchers.Visibility.VISIBLE;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;


public class MapActivityTest {

    @Rule
    public ActivityTestRule<MapActivity> activityRule = new ActivityTestRule<>(MapActivity.class, false, false);

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    MapPresenter mockMapPresenter;

    @Before
    public void setUp() throws Exception {
        final MapActivity mapActivity = activityRule.launchActivity(null);
        mapActivity.setPresenter(mockMapPresenter);
    }

    @Test
    public void testControlsAreVisible() {
        onView(withId(R.id.start_point)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.end_point)).check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void testOnStartPointSelectClick() throws Exception {
        Intents.init();
        onView(withId(R.id.start_point)).perform(click());

        intended(hasComponent(PlaceSearchActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void testOnEndPointSelectClick() throws Exception {
        Intents.init();
        onView(withId(R.id.end_point)).perform(click());

        intended(hasComponent(PlaceSearchActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void testOnPointClearClick() throws Exception {

        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            activityRule.getActivity().setStartPointClearButtonVisible(true);
            activityRule.getActivity().setEndPointClearButtonVisible(true);
        });

        onView(withId(R.id.start_point_clear)).perform(click());
        onView(withId(R.id.end_point_clear)).perform(click());

        Mockito.verify(mockMapPresenter).onStartPointClearClick();
        Mockito.verify(mockMapPresenter).onEndPointClearClick();
    }

    @Test
    public void testGetMapMarketFrom() throws Exception {
        MapMarker mapMarker = activityRule.getActivity().getMapMarketFrom(1.0, 2.0, R.drawable.ic_map_point_start);

        assertNotNull(mapMarker);
        assertNotNull(mapMarker.getCoordinate());
        assertEquals(mapMarker.getCoordinate().getLatitude(), 1.0);
        assertEquals(mapMarker.getCoordinate().getLongitude(), 2.0);
        assertNotNull(mapMarker.getIcon());
        assertTrue(mapMarker.getIcon().isValid());
    }

    @Test
    public void testSetStartPointText() throws Exception {
        onView(withId(R.id.start_point_text)).check(matches(withText("")));

        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> activityRule.getActivity().setStartPointText("Test Text"));

        onView(withId(R.id.start_point_text))
                .check(matches(ViewMatchers.withText(String.format(activityRule.getActivity().getString(R.string.start_point_text_formatted), "Test Text"))));
    }

    @Test
    public void testSetEndPointText() throws Exception {
        onView(withId(R.id.end_point_text)).check(matches(withText("")));

        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> activityRule.getActivity().setEndPointText("Test Text"));

        onView(withId(R.id.end_point_text))
                .check(matches(ViewMatchers.withText(String.format(activityRule.getActivity().getString(R.string.end_point_text_formatted), "Test Text"))));
    }

    @Test
    public void testSetStartPointClearButtonVisible() throws Exception {
        onView(withId(R.id.start_point_clear)).check(matches(withEffectiveVisibility(GONE)));

        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            activityRule.getActivity().setStartPointClearButtonVisible(true);
        });

        onView(withId(R.id.start_point_clear)).check(matches(withEffectiveVisibility(VISIBLE)));
    }

    @Test
    public void testSetEndPointClearButtonVisible() throws Exception {
        onView(withId(R.id.end_point_clear)).check(matches(withEffectiveVisibility(GONE)));

        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            activityRule.getActivity().setEndPointClearButtonVisible(true);
        });

        onView(withId(R.id.end_point_clear)).check(matches(withEffectiveVisibility(VISIBLE)));
    }

    @Test
    public void testSetControlsVisible() throws Exception {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            activityRule.getActivity().setControlsVisible(true);
        });

        onView(withId(R.id.start_point)).check(matches(withEffectiveVisibility(VISIBLE)));
        onView(withId(R.id.end_point)).check(matches(withEffectiveVisibility(VISIBLE)));

        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            activityRule.getActivity().setControlsVisible(false);
        });

        onView(withId(R.id.start_point)).check(matches(withEffectiveVisibility(GONE)));
        onView(withId(R.id.end_point)).check(matches(withEffectiveVisibility(GONE)));
    }

    @Test
    public void testShowMsgBar() throws Exception {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            activityRule.getActivity().showMsgBar(R.string.app_name, true);
        });

        onView(ViewMatchers.withText(R.string.app_name)).check(matches(isCompletelyDisplayed()));
    }
}