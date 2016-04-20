package com.braintreepayments.cardform.utils;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;

import com.braintreepayments.cardform.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
public class ExpirationDateDialogThemeTest {

    @Test
    public void detectTheme_detectsBlackBackgroundAndReturnsLightTheme() {
        Activity activity = setupActivity(R.color.bt_black);

        ExpirationDateDialogTheme theme = ExpirationDateDialogTheme.detectTheme(activity);

        assertEquals(ExpirationDateDialogTheme.LIGHT, theme);
        assertEquals(getColor(R.color.bt_black_87), theme.getItemTextColor());
        assertEquals(getColor(R.color.bt_white_87), theme.getItemInvertedTextColor());
        assertEquals(getColor(R.color.bt_black_38), theme.getItemDisabledTextColor());
    }

    @Test
    public void detectTheme_detectsWhiteBackgroundAndReturnsDarkTheme() {
        Activity activity = setupActivity(R.color.bt_white);

        ExpirationDateDialogTheme theme = ExpirationDateDialogTheme.detectTheme(activity);

        assertEquals(ExpirationDateDialogTheme.DARK, theme);
        assertEquals(getColor(R.color.bt_white_87), theme.getItemTextColor());
        assertEquals(getColor(R.color.bt_black_87), theme.getItemInvertedTextColor());
        assertEquals(getColor(R.color.bt_white_38), theme.getItemDisabledTextColor());
    }

    @Test
    public void getSelectedItemBackground_returnsDefaultColorWhenThereIsNoAccentColor() {
        Activity activity = setupActivity(R.color.bt_white);

        ExpirationDateDialogTheme theme = ExpirationDateDialogTheme.detectTheme(activity);

        assertEquals(getColor(R.color.bt_blue), theme.getSelectedItemBackground());
    }

    private Activity setupActivity(int backgroundColor) {
        ColorDrawable colorDrawable = mock(ColorDrawable.class);
        when(colorDrawable.getColor()).thenReturn(getColor(backgroundColor));
        View rootView = mock(View.class);
        when(rootView.getBackground()).thenReturn(colorDrawable);
        View decorView = mock(View.class);
        when(decorView.getRootView()).thenReturn(rootView);
        Window window = mock(Window.class);
        when(window.getDecorView()).thenReturn(decorView);
        Activity activity = mock(Activity.class);
        when(activity.getResources()).thenReturn(RuntimeEnvironment.application.getResources());
        when(activity.getWindow()).thenReturn(window);

        return activity;
    }

    private int getColor(int color) {
        return RuntimeEnvironment.application.getResources().getColor(color);
    }
}
