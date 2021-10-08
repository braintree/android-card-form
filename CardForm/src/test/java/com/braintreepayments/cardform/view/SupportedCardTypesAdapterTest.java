package com.braintreepayments.cardform.view;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;

import com.braintreepayments.cardform.utils.CardType;
import com.braintreepayments.cardform.utils.SelectableCardType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class SupportedCardTypesAdapterTest {

    @Test
    public void setSelected_disablesNonSelectedCardTypes() {
        SelectableCardType visa = new SelectableCardType(CardType.VISA);
        SelectableCardType amex = new SelectableCardType(CardType.AMEX);

        SelectableCardType[] cardTypes = { amex, visa };
        SupportedCardTypesAdapter sut = new SupportedCardTypesAdapter(cardTypes);

        sut.setSelected(CardType.VISA);
        assertTrue(amex.isDisabled());
        assertFalse(visa.isDisabled());
    }

    @Test
    public void setSelected_handlesNull() {
        SelectableCardType visa = new SelectableCardType(CardType.VISA);
        SelectableCardType amex = new SelectableCardType(CardType.AMEX);

        SelectableCardType[] cardTypes = { amex, visa };
        SupportedCardTypesAdapter sut = new SupportedCardTypesAdapter(cardTypes);

        try {
            sut.setSelected(null);
        } catch (NullPointerException e) {
            fail("Should handle null");
        }
    }

    @Test
    public void onBindViewHolder_setsAlphaBasedOnDisabledStatus() {
        // TODO: implement
    }

    @Test
    public void onBindViewHolder_setsImageViewBasedOnCardType() {
        // TODO: implement
    }
}
