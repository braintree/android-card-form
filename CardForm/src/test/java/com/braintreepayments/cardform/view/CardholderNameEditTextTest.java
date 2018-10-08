package com.braintreepayments.cardform.view;

import com.braintreepayments.cardform.R;
import com.braintreepayments.cardform.test.TestActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import edu.emory.mathcs.backport.java.util.Collections;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class CardholderNameEditTextTest {

    private CardholderNameEditText mView;

    @Before
    public void setup() {
        mView = Robolectric.setupActivity(TestActivity.class)
                .findViewById(R.id.bt_card_form_cardholder_name);
    }

    @Test
    public void invalidIfEmpty() {
        assertFalse(mView.isValid());
    }

    @Test
    public void validIfNotEmpty() {
        mView.setText("John Doe");
        assertTrue(mView.isValid());
    }

    @Test
    public void validIfEmptyAndOptional() {
        mView.setOptional(true);

        assertTrue(mView.isValid());
    }

    @Test
    public void hasMaxAllowedLength() {
        int maxLength = 255;
        String justRight = String.join("", Collections.nCopies(maxLength, "a"));
        String tooLong = justRight + "a";

        mView.setText(justRight);
        assertEquals(maxLength, mView.getText().length());

        mView.setText(tooLong);
        assertEquals(maxLength, mView.getText().length());
    }

    @Test
    public void getErrorMessage_returnsErrorMessageWhenEmpty() {
        assertEquals(RuntimeEnvironment.application.getString(R.string.bt_cardholder_name_required), mView.getErrorMessage());
    }
}
