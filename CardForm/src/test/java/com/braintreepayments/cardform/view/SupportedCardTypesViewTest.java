package com.braintreepayments.cardform.view;

import android.text.SpannableString;

import com.braintreepayments.cardform.utils.CardType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricGradleTestRunner.class)
public class SupportedCardTypesViewTest {

    @Test
    public void setSupportedCardTypes_addsAllCardTypes() {
        SupportedCardTypesView supportedCardTypesView = new SupportedCardTypesView(RuntimeEnvironment.application);

        supportedCardTypesView.setSupportedCardTypes(CardType.VISA, CardType.MASTERCARD, CardType.DISCOVER,
                CardType.AMEX, CardType.DINERS_CLUB, CardType.JCB, CardType.MAESTRO, CardType.UNION_PAY);

        List<PaddedImageSpan> allSpans = Arrays.asList(new SpannableString(supportedCardTypesView.getText())
                .getSpans(0, supportedCardTypesView.length(), PaddedImageSpan.class));
        assertEquals(CardType.VISA.getFrontResource(), allSpans.get(0).getResourceId());
        assertEquals(CardType.MASTERCARD.getFrontResource(), allSpans.get(1).getResourceId());
        assertEquals(CardType.DISCOVER.getFrontResource(), allSpans.get(2).getResourceId());
        assertEquals(CardType.AMEX.getFrontResource(), allSpans.get(3).getResourceId());
        assertEquals(CardType.DINERS_CLUB.getFrontResource(), allSpans.get(4).getResourceId());
        assertEquals(CardType.JCB.getFrontResource(), allSpans.get(5).getResourceId());
        assertEquals(CardType.MAESTRO.getFrontResource(), allSpans.get(6).getResourceId());
        assertEquals(CardType.UNION_PAY.getFrontResource(), allSpans.get(7).getResourceId());
    }
}
