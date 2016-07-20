package com.braintreepayments.cardform.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricGradleTestRunner.class)
public class ErrorEditTextTest {

    private ErrorEditText mView;

    @Before
    public void setup() {
        mView = new ErrorEditText(RuntimeEnvironment.application);
    }

    @Test
    public void defaultsToNoError() {
        assertFalse(mView.isError());
    }

    @Test
    public void clearsErrorOnTextChange() {
        mView.setError(null);
        mView.onTextChanged("4", 0, 0, 1);
        assertFalse(mView.isError());
    }

    @Test
    public void getErrorMessage_returnsNull() {
        assertNull(mView.getErrorMessage());
    }

    @Test
    public void setError_setsErrorWhenMessageProvided() {
        mView.setError("Error");
        assertTrue(mView.isError());
    }

    @Test
    public void setError_clearsErrorWhenMessageNullOrEmpty() {
        mView.setError("Error");
        mView.setError(null);
        assertFalse(mView.isError());

        mView.setError("Error");
        mView.setError("");
        assertFalse(mView.isError());
    }
}
