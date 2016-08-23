package com.braintreepayments.cardform.test;

import android.graphics.drawable.Drawable;
import android.widget.EditText;

import org.robolectric.RuntimeEnvironment;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.robolectric.Shadows.shadowOf;

public class Assertions {

    public static void assertNoHintIcon(EditText editText) {
        assertNull(editText.getCompoundDrawables()[0]);
        assertNull(editText.getCompoundDrawables()[1]);
        assertNull(editText.getCompoundDrawables()[2]);
        assertNull(editText.getCompoundDrawables()[3]);
    }

    public static void assertIconHintIs(EditText editText, int resourceId) {
        Drawable expected;
        if (resourceId == 0) {
            expected = null;
        } else {
            expected = RuntimeEnvironment.application.getResources().getDrawable(resourceId);
        }

        Drawable[] drawables = editText.getCompoundDrawables();
        assertBitmapsEqual(null, drawables[0]);
        assertBitmapsEqual(null, drawables[1]);
        assertBitmapsEqual(expected, drawables[2]);
        assertBitmapsEqual(null, drawables[3]);
    }

    private static void assertBitmapsEqual(Drawable expected, Drawable actual) {
        if (expected == null || actual == null) {
            assertEquals(expected, actual);
        } else {
            assertEquals(shadowOf(expected).getCreatedFromResId(), shadowOf(actual).getCreatedFromResId());
        }
    }
}
