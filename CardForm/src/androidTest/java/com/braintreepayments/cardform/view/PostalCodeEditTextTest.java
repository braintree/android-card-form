package com.braintreepayments.cardform.view;

import android.test.AndroidTestCase;

public class PostalCodeEditTextTest extends AndroidTestCase {

    private PostalCodeEditText view;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        view = new PostalCodeEditText(getContext());
    }

    public void testInvalidIfEmpty() {
        assertFalse(view.isValid());
    }

    public void testValidIfNotEmpty() {
        view.setText("12345");
        assertTrue(view.isValid());
    }
}
