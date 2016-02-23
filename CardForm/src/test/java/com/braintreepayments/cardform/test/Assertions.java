package com.braintreepayments.cardform.test;

import android.graphics.drawable.Drawable;

import static junit.framework.Assert.assertEquals;
import static org.robolectric.Shadows.shadowOf;

public class Assertions {

    /**
     * Assert that two bitmaps are equal
     * @param d1
     * @param d2
     */
    public static void assertBitmapsEqual(Drawable d1, Drawable d2) {
        if (d1 == null || d2 == null) {
            assertEquals(d1, d2);
        } else {
            assertEquals(shadowOf(d1).getCreatedFromResId(), shadowOf(d2).getCreatedFromResId());
        }
    }
}
