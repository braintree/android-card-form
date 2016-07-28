package com.braintreepayments.cardform.view;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;

import com.braintreepayments.cardform.R;
import com.braintreepayments.cardform.test.TestActivity;
import com.braintreepayments.cardform.utils.CardType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class CvvEditTextTest {

    private CvvEditText mView;

    @Before
    public void setup() {
        mView = (CvvEditText) Robolectric.setupActivity(TestActivity.class)
                .findViewById(R.id.bt_card_form_cvv);
    }

    @Test
    public void defaultLimitIs3() {
        type("123").assertTextIs("123");
        type("4").assertTextIs("123");
    }

    @Test
    public void customLimits() {
        for (CardType type : CardType.values()) {
            mView.setCardType(type);
            if (type == CardType.AMEX) {
                type("1234").assertTextIs("1234");
                type("5").assertTextIs("1234");
            } else {
                type("123").assertTextIs("123");
                type("4").assertTextIs("123");
            }
            mView.getText().clear();
        }
    }

    @Test
    public void allowsEmptyCvvForUnionPay() {
        mView.setCardType(CardType.UNIONPAY);
        mView.getText().clear();

        assertTrue(mView.isValid());
    }

    @Test
    public void showsTheCorrectCvvTextHintsForAllCardTypes() {
        for (CardType type : CardType.values()) {
            mView.setCardType(type);

            assertEquals(RuntimeEnvironment.application.getString(type.getSecurityCodeName()),
                    ((TextInputLayout) mView.getParent()).getHint());
        }
    }

    @Test
    public void getErrorMessage_returnsErrorMessageWhenEmpty() {
        assertEquals(RuntimeEnvironment.application.getString(R.string.bt_cvv_required), mView.getErrorMessage());
    }

    @Test
    public void getErrorMessage_returnsErrorMessageWhenNotEmpty() {
        type("4");

        assertEquals(RuntimeEnvironment.application.getString(R.string.bt_cvv_invalid), mView.getErrorMessage());
    }

    private CvvEditTextTest type(String text) {
        Editable editable = mView.getText();
        for (char c : text.toCharArray()) {
            editable.append(c);
        }
        return this;
    }

    private void assertTextIs(String expected) {
        assertEquals(expected, mView.getText().toString());
    }
}
