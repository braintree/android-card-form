package com.braintreepayments.cardform.view;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import androidx.test.core.app.ApplicationProvider;

import com.braintreepayments.api.CardType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class AccessibleSupportedCardTypesViewTest {

    @Test
    public void setSupportedCardTypes_addsAllCardTypes() {
        AccessibleSupportedCardTypesView sut = new AccessibleSupportedCardTypesView(ApplicationProvider.getApplicationContext());

        sut.setSupportedCardTypes(CardType.VISA, CardType.MASTERCARD, CardType.DISCOVER,
                CardType.AMEX, CardType.DINERS_CLUB, CardType.JCB, CardType.MAESTRO, CardType.UNIONPAY,
                CardType.HIPER, CardType.HIPERCARD);

        assertEquals(10, sut.getAdapter().getItemCount());

        assertEquals(CardType.VISA, sut.adapter.getSupportedCardTypes()[0].getCardType());
        assertEquals(CardType.MASTERCARD, sut.adapter.getSupportedCardTypes()[1].getCardType());
        assertEquals(CardType.DISCOVER, sut.adapter.getSupportedCardTypes()[2].getCardType());
        assertEquals(CardType.AMEX, sut.adapter.getSupportedCardTypes()[3].getCardType());
        assertEquals(CardType.DINERS_CLUB, sut.adapter.getSupportedCardTypes()[4].getCardType());
        assertEquals(CardType.JCB, sut.adapter.getSupportedCardTypes()[5].getCardType());
        assertEquals(CardType.MAESTRO, sut.adapter.getSupportedCardTypes()[6].getCardType());
        assertEquals(CardType.UNIONPAY, sut.adapter.getSupportedCardTypes()[7].getCardType());
        assertEquals(CardType.HIPER, sut.adapter.getSupportedCardTypes()[8].getCardType());
        assertEquals(CardType.HIPERCARD, sut.adapter.getSupportedCardTypes()[9].getCardType());
    }

    @Test
    public void setSupportedCardTypes_handlesNull() {
        AccessibleSupportedCardTypesView sut = new AccessibleSupportedCardTypesView(ApplicationProvider.getApplicationContext());

        sut.setSupportedCardTypes((CardType[]) null);
        assertEquals(0, sut.getAdapter().getItemCount());
    }

    @Test
    public void setSelected_notifiesAdapter() {
        AccessibleSupportedCardTypesView sut = new AccessibleSupportedCardTypesView(ApplicationProvider.getApplicationContext());

        SupportedCardTypesAdapter adapter = mock(SupportedCardTypesAdapter.class);
        sut.adapter = adapter;

        sut.setSelected(CardType.VISA);

        verify(adapter).setSelected(CardType.VISA);
        verify(adapter).notifyDataSetChanged();
    }

    @Test
    public void setSelected_withoutSettingSupportedCardTypes_doesNothing() {
        AccessibleSupportedCardTypesView sut = new AccessibleSupportedCardTypesView(ApplicationProvider.getApplicationContext());

        sut.setSelected(CardType.VISA);

        assertNull(sut.getAdapter());
    }
}