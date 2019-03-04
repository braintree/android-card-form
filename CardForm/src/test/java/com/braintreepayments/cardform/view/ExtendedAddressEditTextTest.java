package com.braintreepayments.cardform.view;

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
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class ExtendedAddressEditTextTest {

    private ExtendedAddressEditText mView;

    @Before
    public void setup() {
        mView = Robolectric.setupActivity(TestActivity.class)
                .findViewById(R.id.bt_card_form_extended_address);
    }

    @Test
    public void invalidIfEmpty() {
        assertFalse(mView.isValid());
    }

    @Test
    public void validIfNotEmpty() {
        mView.setText("Apt 2");
        assertTrue(mView.isValid());
    }

    @Test
    public void getErrorMessage_returnsErrorMessageWhenEmpty() {
        assertEquals(RuntimeEnvironment.application.getString(R.string.bt_extended_address_required),
                mView.getErrorMessage());
    }
}
