package com.braintreepayments.cardform.view;

import android.text.Editable;

import com.braintreepayments.cardform.R;
import com.braintreepayments.cardform.test.TestActivity;
import com.braintreepayments.cardform.utils.CardType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricGradleTestRunner.class)
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

    /* helpers */
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
