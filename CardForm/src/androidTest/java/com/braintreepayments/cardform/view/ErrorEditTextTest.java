package com.braintreepayments.cardform.view;

import android.graphics.drawable.Drawable;
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
        mView.setError();
        assertTrue(mView.isError());
    }

    @UiThreadTest
    public void testDefautlsToNoError() {
        assertFalse(mView.isError());
    }

    @UiThreadTest
    public void testClearsErrorStateOnClearError() {
        mView.setError();
        mView.clearError();
        assertFalse(mView.isError());
    }

    @UiThreadTest
    public void testUsesErrorSelectorWhenErrorIsSet() {
        Drawable startingDrawable = mView.getBackground();
        mView.setError();
        assertNotSame(startingDrawable, mView.getBackground());
    }

    @UiThreadTest
    public void testUsesDefaultSelectorWhenErrorIsCleared() {
        mView.setError();
        Drawable errorDrawable = mView.getBackground();
        mView.clearError();
        assertNotSame(errorDrawable, mView.getBackground());
    }

    @UiThreadTest
    public void testClearsErrorOnTextChange() {
        mView.setError();
        Drawable errorDrawable = mView.getBackground();
        mView.onTextChanged("4", 0, 0, 1);
        assertNotSame(errorDrawable, mView.getBackground());
        assertFalse(mView.isError());
    }
}
