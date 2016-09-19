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
public class CountryCodeEditTextTest {

    private CountryCodeEditText mView;

    @Before
    public void setup() {
        mView = (CountryCodeEditText) Robolectric.setupActivity(TestActivity.class)
                .findViewById(R.id.bt_card_form_country_code);
    }

    @Test
    public void getCountryCode_returnsStrippedCountryCode() {
        mView.setText("+86");

        assertEquals("86", mView.getCountryCode());
    }

    @Test
    public void invalidIfEmpty() {
        assertFalse(mView.isValid());
    }

    @Test
    public void validIfNotEmpty() {
        mView.setText("+86");

        assertTrue(mView.isValid());
    }

    @Test
    public void validIfEmptyAndOptional() {
        mView.setOptional(true);

        assertTrue(mView.isValid());
    }

    @Test
    public void getErrorMessage_returnsErrorMessageWhenEmpty() {
        assertEquals(RuntimeEnvironment.application.getString(R.string.bt_country_code_required),
                mView.getErrorMessage());
    }
}
