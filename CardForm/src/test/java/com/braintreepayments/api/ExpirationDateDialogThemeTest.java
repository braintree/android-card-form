package com.braintreepayments.api;

import android.app.Activity;

import com.braintreepayments.api.cardform.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static com.braintreepayments.api.test.ColorTestUtils.getColor;
import static com.braintreepayments.api.test.ColorTestUtils.setupActivity;
import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class ExpirationDateDialogThemeTest {

    @Test
    public void detectTheme_detectsBlackBackgroundAndReturnsDarkTheme() {
        Activity activity = setupActivity(R.color.bt_black);

        ExpirationDateDialogTheme theme = ExpirationDateDialogTheme.detectTheme(activity);

        assertEquals(ExpirationDateDialogTheme.DARK, theme);
        assertEquals(getColor(R.color.bt_white_87), theme.getItemTextColor());
        assertEquals(getColor(R.color.bt_black_87), theme.getItemInvertedTextColor());
        assertEquals(getColor(R.color.bt_white_38), theme.getItemDisabledTextColor());
    }

    @Test
    public void detectTheme_detectsWhiteBackgroundAndReturnsLightTheme() {
        Activity activity = setupActivity(R.color.bt_white);

        ExpirationDateDialogTheme theme = ExpirationDateDialogTheme.detectTheme(activity);

        assertEquals(ExpirationDateDialogTheme.LIGHT, theme);
        assertEquals(getColor(R.color.bt_black_87), theme.getItemTextColor());
        assertEquals(getColor(R.color.bt_white_87), theme.getItemInvertedTextColor());
        assertEquals(getColor(R.color.bt_black_38), theme.getItemDisabledTextColor());
    }

    @Test
    public void getSelectedItemBackground_returnsDefaultColorWhenThereIsNoAccentColor() {
        Activity activity = setupActivity(R.color.bt_white);

        ExpirationDateDialogTheme theme = ExpirationDateDialogTheme.detectTheme(activity);

        assertEquals(getColor(R.color.bt_blue), theme.getSelectedItemBackground());
    }
}
