package com.braintreepayments.cardform.view;

import android.support.design.widget.TextInputLayout;

import com.braintreepayments.cardform.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;

import static junit.framework.Assert.assertEquals;
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
    public void setFieldHint_setsHintWhenParentViewIsATextInputLayout() {
        TextInputLayout parent = new TextInputLayout(RuntimeEnvironment.application);
        parent.addView(mView);

        mView.setFieldHint(R.string.bt_form_hint_cvv);

        assertEquals(parent.getHint(), "CVV");
        assertNull(mView.getHint());
    }

    @Test
    public void setFieldHint_setsHint() {
        mView.setFieldHint(R.string.bt_form_hint_cvv);

        assertEquals(mView.getHint(), "CVV");
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
