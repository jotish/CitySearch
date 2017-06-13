package com.jotish.backbasecitysearch.views;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import com.jotish.backbasecitysearch.R;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivitySearchTest {

  @Rule
  public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(
      MainActivity.class);

  @Test
  public void mainActivitySearchTest() {
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }


    ViewInteraction appCompatEditText = onView(
        allOf(withId(R.id.searchText),
            withParent(withId(R.id.content)),
            isDisplayed()));
    appCompatEditText.perform(replaceText("be"), closeSoftKeyboard());

    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction textView = onView(
        allOf(withId(R.id.city_name), withText("Bellara"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.city_recycler_view),
                    0),
                0),
            isDisplayed()));
    textView.check(matches(withText("Bellara")));

    ViewInteraction textView2 = onView(
        allOf(withId(R.id.city_name), withText("Bee House"),
            childAtPosition(
                childAtPosition(
                    withId(R.id.city_recycler_view),
                    1),
                0),
            isDisplayed()));
    textView2.check(matches(withText("Bee House")));

    ViewInteraction appCompatTextView = onView(
        allOf(withId(R.id.city_name), withText("Bellara"), isDisplayed()));
    appCompatTextView.perform(click());

    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ViewInteraction textView3 = onView(
        allOf(withText("Bellara"),
            childAtPosition(
                allOf(withId(R.id.action_bar),
                    childAtPosition(
                        withId(R.id.action_bar_container),
                        0)),
                1),
            isDisplayed()));
    textView3.check(matches(withText("Bellara")));
  }

  private static Matcher<View> childAtPosition(
      final Matcher<View> parentMatcher, final int position) {

    return new TypeSafeMatcher<View>() {
      @Override
      public void describeTo(Description description) {
        description.appendText("Child at position " + position + " in parent ");
        parentMatcher.describeTo(description);
      }

      @Override
      public boolean matchesSafely(View view) {
        ViewParent parent = view.getParent();
        return parent instanceof ViewGroup && parentMatcher.matches(parent)
            && view.equals(((ViewGroup) parent).getChildAt(position));
      }
    };
  }
}
