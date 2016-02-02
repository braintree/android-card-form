package com.braintreepayments.cardform.test;

import android.annotation.TargetApi;
import android.app.UiAutomation;
import android.os.Build;
import android.os.SystemClock;
import android.test.ActivityInstrumentationTestCase2;

public class UITestHelper {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static void rotateToLandscape(ActivityInstrumentationTestCase2<?> testCase) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return;
        }

        rotateTo(UiAutomation.ROTATION_FREEZE_90, testCase);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static void rotateToPortrait(ActivityInstrumentationTestCase2<?> testCase) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return;
        }

        rotateTo(UiAutomation.ROTATION_FREEZE_0, testCase);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static void rotateTo(int direction, ActivityInstrumentationTestCase2<?> testCase) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return;
        }

        UiAutomation automation = testCase.getInstrumentation().getUiAutomation();
        automation.setRotation(UiAutomation.ROTATION_UNFREEZE);
        automation.setRotation(direction);

        SystemClock.sleep(1000);
    }
}
