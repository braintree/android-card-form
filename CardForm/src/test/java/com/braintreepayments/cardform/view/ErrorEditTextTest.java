package com.braintreepayments.cardform.view;

import com.braintreepayments.cardform.R;
import com.braintreepayments.cardform.test.TestActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricGradleTestRunner.class)
public class ErrorEditTextTest {

    private ErrorEditText mView;

    @Before
    public void setup() {
        mView = (ErrorEditText) Robolectric.setupActivity(TestActivity.class)
                .findViewById(R.id.bt_card_form_card_number);
    }

    @Test
    public void isErrorIsTrueWhenErrorIsSet() {
        mView.setError(true);
        assertTrue(mView.isError());
    }

    @Test
    public void defaultsToNoError() {
        assertFalse(mView.isError());
    }

    @Test
    public void clearsErrorStateOnSetErrorFalse() {
        mView.setError(false);
        assertFalse(mView.isError());
    }

    @Test
    public void clearsErrorOnTextChange() {
        mView.setError(true);
        mView.onTextChanged("4", 0, 0, 1);
        assertFalse(mView.isError());
    }
}
