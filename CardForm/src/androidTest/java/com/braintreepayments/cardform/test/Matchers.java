package com.braintreepayments.cardform.test;

import android.content.res.Resources;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.internal.matchers.TypeSafeMatcher;

public class Matchers {

    /**
     * Returns a matcher that matches {@link View}s based on resource ids. Note: Android resource ids
     * are not guaranteed to be unique. You may have to pair this matcher with another one to
     * guarantee a unique view selection.
     *
     * @param resourceId the resource id to match
     */
    public static Matcher<View> withId(final int resourceId) {
        return new TypeSafeMatcher<View>() {
            private String resourceName = null;

            @Override
            public void describeTo(Description description) {
                description.appendText("with resource id: ");
                description.appendValue(resourceId);
                if (null != resourceName) {
                    description.appendText(" resource name: ");
                    description.appendText(resourceName);
                }
            }

            @Override
            public boolean matchesSafely(View view) {
                if (null == resourceName) {
                    try {
                        resourceName = view.getResources().getResourceEntryName(resourceId);
                    } catch (Resources.NotFoundException ignored) {
                        /* view could be from a context unaware of the resource id. */
                    }
                }
                return resourceId == view.getId();
            }
        };
    }

    /**
     * Returns a matcher that matches {@link android.widget.TextView}s based on their hints.
     * Note: You are not guaranteed to match a unique view.
     * You may have to pair this matcher with another one to guarantee a unique view selection.
     *
     * @param resourceId the resource id of a {@link String} to match the hints
     */
    public static Matcher<View> withHint(final int resourceId) {
        return new BoundedMatcher<View, TextView>(TextView.class) {
            private String resourceName = null;
            private String expectedHint = null;

            @Override
            public void describeTo(Description description) {
                description.appendText("with hint from resource id: ");
                description.appendValue(resourceId);
                if (null != resourceName) {
                    description.appendText(" with hint from resource: ");
                    description.appendText(resourceName);
                }
                if (null != expectedHint) {
                    description.appendText(" value: ");
                    description.appendText(expectedHint);
                }
            }

            @Override
            protected boolean matchesSafely(TextView textView) {
                if (null == expectedHint) {
                    try {
                        expectedHint = textView.getResources().getString(resourceId);
                        resourceName = textView.getResources().getResourceEntryName(resourceId);
                    } catch (Resources.NotFoundException ignored) {
                        /* view could be from a context unaware of the resource id. */
                    }
                }
                if (null != expectedHint) {
                    return expectedHint.equals(textView.getHint());
                } else {
                    return false;
                }
            }
        };
    }
}
