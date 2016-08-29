package com.braintreepayments.cardform.view;

import android.text.SpannableString;

import com.braintreepayments.cardform.utils.CardType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class SupportedCardTypesViewTest {

    @Test
    public void setSupportedCardTypes_addsAllCardTypes() {
        SupportedCardTypesView supportedCardTypesView = new SupportedCardTypesView(RuntimeEnvironment.application);

        supportedCardTypesView.setSupportedCardTypes(CardType.VISA, CardType.MASTERCARD, CardType.DISCOVER,
                CardType.AMEX, CardType.DINERS_CLUB, CardType.JCB, CardType.MAESTRO, CardType.UNIONPAY);

        List<PaddedImageSpan> allSpans = Arrays.asList(new SpannableString(supportedCardTypesView.getText())
                .getSpans(0, supportedCardTypesView.length(), PaddedImageSpan.class));
        assertEquals(CardType.VISA.getFrontResource(), allSpans.get(0).getResourceId());
        assertEquals(CardType.MASTERCARD.getFrontResource(), allSpans.get(1).getResourceId());
        assertEquals(CardType.DISCOVER.getFrontResource(), allSpans.get(2).getResourceId());
        assertEquals(CardType.AMEX.getFrontResource(), allSpans.get(3).getResourceId());
        assertEquals(CardType.DINERS_CLUB.getFrontResource(), allSpans.get(4).getResourceId());
        assertEquals(CardType.JCB.getFrontResource(), allSpans.get(5).getResourceId());
        assertEquals(CardType.MAESTRO.getFrontResource(), allSpans.get(6).getResourceId());
        assertEquals(CardType.UNIONPAY.getFrontResource(), allSpans.get(7).getResourceId());
    }

    @Test
    public void setSupportedCardTypes_handlesNull() {
        SupportedCardTypesView supportedCardTypesView = new SupportedCardTypesView(RuntimeEnvironment.application);

        supportedCardTypesView.setSupportedCardTypes((CardType[]) null);

        List<PaddedImageSpan> allSpans = Arrays.asList(new SpannableString(supportedCardTypesView.getText())
                .getSpans(0, supportedCardTypesView.length(), PaddedImageSpan.class));
        assertEquals(0, allSpans.size());
    }

    @Test
    public void setSelectedCardTypes_disablesNonSelectedCardTypes() {
        SupportedCardTypesView supportedCardTypesView = new SupportedCardTypesView(RuntimeEnvironment.application);
        supportedCardTypesView.setSupportedCardTypes(CardType.VISA, CardType.MASTERCARD, CardType.DISCOVER,
                CardType.AMEX, CardType.DINERS_CLUB, CardType.JCB, CardType.MAESTRO, CardType.UNIONPAY);

        supportedCardTypesView.setSelected(CardType.VISA);

        List<PaddedImageSpan> allSpans = Arrays.asList(new SpannableString(supportedCardTypesView.getText())
                .getSpans(0, supportedCardTypesView.length(), PaddedImageSpan.class));
        assertFalse(allSpans.get(0).isDisabled());
        assertTrue(allSpans.get(1).isDisabled());
        assertTrue(allSpans.get(2).isDisabled());
        assertTrue(allSpans.get(3).isDisabled());
        assertTrue(allSpans.get(4).isDisabled());
        assertTrue(allSpans.get(5).isDisabled());
        assertTrue(allSpans.get(6).isDisabled());
        assertTrue(allSpans.get(7).isDisabled());
    }

    @Test
    public void setSelectedCardTypes_handlesNull() {
        SupportedCardTypesView supportedCardTypesView = new SupportedCardTypesView(RuntimeEnvironment.application);
        supportedCardTypesView.setSupportedCardTypes(CardType.VISA, CardType.MASTERCARD, CardType.DISCOVER,
                CardType.AMEX, CardType.DINERS_CLUB, CardType.JCB, CardType.MAESTRO, CardType.UNIONPAY);

        supportedCardTypesView.setSelected((CardType[]) null);

        List<PaddedImageSpan> allSpans = Arrays.asList(new SpannableString(supportedCardTypesView.getText())
                .getSpans(0, supportedCardTypesView.length(), PaddedImageSpan.class));
        assertTrue(allSpans.get(0).isDisabled());
        assertTrue(allSpans.get(1).isDisabled());
        assertTrue(allSpans.get(2).isDisabled());
        assertTrue(allSpans.get(3).isDisabled());
        assertTrue(allSpans.get(4).isDisabled());
        assertTrue(allSpans.get(5).isDisabled());
        assertTrue(allSpans.get(6).isDisabled());
        assertTrue(allSpans.get(7).isDisabled());
    }
}
