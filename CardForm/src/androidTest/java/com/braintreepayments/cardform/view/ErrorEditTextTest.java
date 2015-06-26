package com.braintreepayments.cardform.view;

import android.test.UiThreadTest;

import com.braintreepayments.cardform.R;
import com.braintreepayments.cardform.test.TestActivityTestCase;

public class ErrorEditTextTest extends TestActivityTestCase {

    private ErrorEditText mView;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        setupCardForm();
        mView = (ErrorEditText) mActivity.findViewById(R.id.bt_card_form_card_number);
        assertNotNull(mView);
    }

    @UiThreadTest
    public void testIsErrorIsTrueWhenErrorIsSet() {
        mView.setError(true);
        assertTrue(mView.isError());
    }

    @UiThreadTest
    public void testDefaultsToNoError() {
        assertFalse(mView.isError());
    }

    @UiThreadTest
    public void testClearsErrorStateOnSetErrorFalse() {
        mView.setError(false);
        assertFalse(mView.isError());
    }

    @UiThreadTest
    public void testClearsErrorOnTextChange() {
        mView.setError(true);
        mView.onTextChanged("4", 0, 0, 1);
        assertFalse(mView.isError());
    }
}
