package com.braintreepayments.cardform.test;

import android.annotation.TargetApi;
import android.app.UiAutomation;
import android.os.Build;
import android.os.SystemClock;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.CloseKeyboardAction;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import org.hamcrest.Matcher;

public class UITestHelper {

    /**
     * Closes the soft keyboard and sleeps to ensure the keyboard is fully closed.
     *
     * @return {@link android.support.test.espresso.ViewAction} instance for chaining
     */
    public static ViewAction closeSoftKeyboard() {
        return new ViewAction() {
            /**
             * The delay time to allow the soft keyboard to dismiss.
             */
            private static final long KEYBOARD_DISMISSAL_DELAY_MILLIS = 1000L;

            /**
             * The real {@link CloseKeyboardAction} instance.
             */
            private final ViewAction mCloseSoftKeyboard = new CloseKeyboardAction();

            @Override
            public Matcher<View> getConstraints() {
                return mCloseSoftKeyboard.getConstraints();
            }

            @Override
            public String getDescription() {
                return mCloseSoftKeyboard.getDescription();
            }

            @Override
            public void perform(final UiController uiController, final View view) {
                mCloseSoftKeyboard.perform(uiController, view);
                uiController.loopMainThreadForAtLeast(KEYBOARD_DISMISSAL_DELAY_MILLIS);
            }
        };
    }

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
