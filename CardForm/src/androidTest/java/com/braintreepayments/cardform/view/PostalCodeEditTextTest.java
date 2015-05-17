package com.braintreepayments.cardform.view;

import android.test.UiThreadTest;

import com.braintreepayments.cardform.R;
import com.braintreepayments.cardform.test.TestActivityTestCase;

public class PostalCodeEditTextTest extends TestActivityTestCase {

    private PostalCodeEditText mView;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        setupCardForm();
        mView = (PostalCodeEditText) mActivity.findViewById(R.id.bt_card_form_postal_code);
        assertNotNull(mView);
    }

    @UiThreadTest
    public void testInvalidIfEmpty() {
        assertFalse(mView.isValid());
    }

    @UiThreadTest
    public void testValidIfNotEmpty() {
        mView.setText("12345");
        assertTrue(mView.isValid());
    }
}
