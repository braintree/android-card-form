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
        assertBitmapsEqual(drawables[0], null);
        assertBitmapsEqual(drawables[1], null);
        assertBitmapsEqual(drawables[2], expected);
        assertBitmapsEqual(drawables[3], null);
    }

    private static void assertBitmapsEqual(Drawable d1, Drawable d2) {
        if (d1 == null || d2 == null) {
            assertEquals(d1, d2);
        } else {
            assertEquals(shadowOf(d1).getCreatedFromResId(), shadowOf(d2).getCreatedFromResId());
        }
    }
}
