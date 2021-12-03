package com.braintreepayments.api;

import android.app.Activity;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static com.braintreepayments.api.test.ColorTestUtils.setupActivity;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import com.braintreepayments.api.cardform.R;

@RunWith(RobolectricTestRunner.class)
public class ViewUtilsTest {

    @Test
    public void isDarkBackground_detectsBlackBackgroundAndReturnsTrue() {
        Activity activity = setupActivity(R.color.bt_black);

        assertTrue(ViewUtils.isDarkBackground(activity));
    }

    @Test
    public void isDarkBackground_detectsWhiteBackgroundAndReturnsFalse() {
        Activity activity = setupActivity(R.color.bt_white);

        assertFalse(ViewUtils.isDarkBackground(activity));
    }
}
