package com.braintreepayments.cardform.view;

import android.support.design.widget.TextInputLayout;

import com.braintreepayments.cardform.R;
import com.braintreepayments.cardform.test.TestActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class ErrorEditTextTest {

    private ErrorEditText mView;

    @Before
    public void setup() {
        mView = new ErrorEditText(RuntimeEnvironment.application);
    }

    @Test
    public void setFieldHint_setsHintWhenParentViewIsATextInputLayout() {
        mView = (CardEditText) Robolectric.setupActivity(TestActivity.class)
                .findViewById(R.id.bt_card_form_card_number);

        mView.setFieldHint(R.string.bt_form_hint_cvv);

        assertEquals("CVV", ((TextInputLayout) mView.getParent().getParent()).getHint());
        assertEquals("CVV", mView.getHint());
    }

    @Test
    public void setFieldHint_setsHintStringWhenParentViewIsATextInputLayout() {
        mView = (CardEditText) Robolectric.setupActivity(TestActivity.class)
                .findViewById(R.id.bt_card_form_card_number);

        mView.setFieldHint("CVV");

        assertEquals("CVV", ((TextInputLayout) mView.getParent().getParent()).getHint());
        assertEquals("CVV", mView.getHint());
    }

    @Test
    public void setFieldHint_setsHint() {
        mView.setFieldHint(R.string.bt_form_hint_cvv);

        assertEquals(mView.getHint(), "CVV");
    }

    @Test
    public void setFieldHint_setsHintString() {
        mView.setFieldHint("CVV");

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
    public void isOptional_defaultsToFalse() {
        assertFalse(mView.isOptional());
    }

    @Test
    public void setOptional() {
        assertFalse(mView.isOptional());

        mView.setOptional(true);
        assertTrue(mView.isOptional());

        mView.setOptional(false);
        assertFalse(mView.isOptional());
    }

    @Test
    public void getErrorMessage_returnsNull() {
        assertNull(mView.getErrorMessage());
    }

    @Test
    public void setError_setsErrorWhenParentViewIsATextInputLayout() {
        mView = (CardEditText) Robolectric.setupActivity(TestActivity.class)
                .findViewById(R.id.bt_card_form_card_number);

        mView.setError("Error");

        assertEquals("Error", ((TextInputLayout) mView.getParent().getParent()).getError());
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

    @Test
    public void validate_showsErrorMessage() {
        mView = new ErrorEditText(RuntimeEnvironment.application) {
            @Override
            public boolean isValid() {
                return false;
            }

            @Override
            public String getErrorMessage() {
                return "Error";
            }
        };

        mView.validate();

        assertTrue(mView.isError());
    }

    @Test
    public void validate_doesNotShowErrorMessageIfOptional() {
        mView = new ErrorEditText(RuntimeEnvironment.application) {
            @Override
            public boolean isValid() {
                return false;
            }

            @Override
            public String getErrorMessage() {
                return "Error";
            }
        };
        mView.setOptional(true);

        mView.validate();

        assertFalse(mView.isError());
    }

    @Test
    public void getTextInputLayoutParent_returnsTextInputLayout() {
        mView = (CardEditText) Robolectric.setupActivity(TestActivity.class)
                .findViewById(R.id.bt_card_form_card_number);

        assertNotNull(mView.getTextInputLayoutParent());
    }

    @Test
    public void getTextInputLayoutParent_returnsNullIfParentNotPresent() {
        assertNull(mView.getTextInputLayoutParent());
    }
}
