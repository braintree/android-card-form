package com.braintreepayments.cardform.test;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.test.ActivityInstrumentationTestCase2;

public class TestActivityTestCase extends ActivityInstrumentationTestCase2<TestActivity> {

    protected Context mContext;
    protected TestActivity mActivity;

    public TestActivityTestCase() {
        super(TestActivity.class);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setUp() throws Exception {
        ((KeyguardManager) getInstrumentation().getContext().getSystemService(Context.KEYGUARD_SERVICE))
                .newKeyguardLock("TestActivity").disableKeyguard();

        mContext = getInstrumentation().getContext();
    }

    public void setupCardForm() {
        setupCardForm(true, true, true, true);
    }

    public void setupCardForm(boolean cardNumberRequired, boolean expirationRequired,
            boolean cvvRequired, boolean postalCodeRequired) {
        Intent intent = new Intent(mContext, TestActivity.class)
                .putExtra(TestActivity.SETUP_FORM, true)
                .putExtra(TestActivity.CREDIT_CARD, cardNumberRequired)
                .putExtra(TestActivity.EXPIRATION, expirationRequired)
                .putExtra(TestActivity.CVV, cvvRequired)
                .putExtra(TestActivity.POSTAL_CODE, postalCodeRequired);
        setActivityIntent(intent);

        mActivity = getActivity();

        // prevent flaky tests on CI when activity is slow to start
        SystemClock.sleep(250);
    }
}
