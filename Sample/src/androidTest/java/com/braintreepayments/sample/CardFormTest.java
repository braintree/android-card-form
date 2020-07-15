package com.braintreepayments.sample;

import androidx.test.core.app.ActivityScenario;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static com.lukekorth.deviceautomator.AutomatorAssertion.visible;
import static com.lukekorth.deviceautomator.DeviceAutomator.onDevice;
import static com.lukekorth.deviceautomator.UiObjectMatcher.withText;

@RunWith(AndroidJUnit4.class)
public class CardFormTest {

    @Test(timeout = 60000)
    public void cardForm_hasAllFields() {
        ActivityScenario<LightThemeActivity> scenario = ActivityScenario.launch(LightThemeActivity.class);
        onDevice(withText("Cardholder Name")).check(visible(true));
        onDevice(withText("Card Number")).check(visible(true));
        onDevice(withText("Expiration Date")).check(visible(true));
        onDevice(withText("CVV")).check(visible(true));
        onDevice(withText("Postal Code")).check(visible(true));
        onDevice(withText("Country Code")).check(visible(true));
        onDevice(withText("Mobile Number")).check(visible(true));
    }
}
