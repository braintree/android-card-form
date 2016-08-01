package com.braintreepayments.cardform.utils;

import android.app.Activity;

import com.braintreepayments.cardform.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static com.braintreepayments.cardform.test.ColorTestUtils.setupActivity;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

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
