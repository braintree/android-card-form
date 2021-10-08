package com.braintreepayments.cardform.view;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import androidx.test.core.app.ApplicationProvider;

import com.braintreepayments.cardform.utils.CardType;

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

        // TODO: assert on card types
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