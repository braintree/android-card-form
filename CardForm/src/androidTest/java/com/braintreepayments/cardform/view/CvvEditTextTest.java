package com.braintreepayments.cardform.view;

import android.test.UiThreadTest;
import android.text.Editable;

import com.braintreepayments.cardform.R;
import com.braintreepayments.cardform.test.TestActivityTestCase;
import com.braintreepayments.cardform.utils.CardType;

public class CvvEditTextTest extends TestActivityTestCase {
    private CvvEditText mView;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        setupCardForm();
        mView = (CvvEditText) mActivity.findViewById(R.id.bt_card_form_cvv);
        assertNotNull(mView);
    }

    @UiThreadTest
    public void testDefaultLimitIs3() {
        type("123").assertTextIs("123");
        type("4").assertTextIs("123");
    }

    @UiThreadTest
    public void testCustomLimits() {
        for (CardType type : CardType.values()) {
            mView.setCardType(type);
            if (type == CardType.AMEX) {
                type("1234").assertTextIs("1234");
                type("5").assertTextIs("1234");
            } else {
                type("123").assertTextIs("123");
                type("4").assertTextIs("123");
            }
            clearText();
        }
    }

    private CvvEditTextTest type(String text) {
        Editable editable = mView.getText();
        for (char c : text.toCharArray()) {
            editable.append(c);
        }
        return this;
    }

    private void clearText() {
        mView.getText().clear();
    }

    private CvvEditTextTest assertTextIs(String expected) {
        assertEquals(expected, mView.getText().toString());
        return this;
    }

}
