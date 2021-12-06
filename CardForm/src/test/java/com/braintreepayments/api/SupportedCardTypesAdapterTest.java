package com.braintreepayments.api;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.widget.ImageView;

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
    public void onBindViewHolder_whenCardTypeDisabled_setsAlphaSemiTransparent() {
        SelectableCardType amex = new SelectableCardType(CardType.AMEX);

        amex.setDisabled(true);

        SelectableCardType[] cardTypes = { amex };
        SupportedCardTypesAdapter sut = new SupportedCardTypesAdapter(cardTypes);

        SupportedCardTypesAdapter.SupportedCardTypesViewHolder viewHolder = mock(SupportedCardTypesAdapter.SupportedCardTypesViewHolder.class);
        ImageView imageView = mock(ImageView.class);
        when(viewHolder.getImageView()).thenReturn(imageView);

        sut.onBindViewHolder(viewHolder, 0);

        verify(imageView).setImageAlpha(80);
    }

    @Test
    public void onBindViewHolder_whenCardTypeNotDisaled_setsAlphaOpaque() {
        SelectableCardType amex = new SelectableCardType(CardType.AMEX);

        amex.setDisabled(false);

        SelectableCardType[] cardTypes = { amex };
        SupportedCardTypesAdapter sut = new SupportedCardTypesAdapter(cardTypes);

        SupportedCardTypesAdapter.SupportedCardTypesViewHolder viewHolder = mock(SupportedCardTypesAdapter.SupportedCardTypesViewHolder.class);
        ImageView imageView = mock(ImageView.class);
        when(viewHolder.getImageView()).thenReturn(imageView);

        sut.onBindViewHolder(viewHolder, 0);

        verify(imageView).setImageAlpha(255);
    }

    @Test
    public void onBindViewHolder_setsImageViewBasedOnCardType() {
        SelectableCardType amex = new SelectableCardType(CardType.AMEX);

        SelectableCardType[] cardTypes = { amex };
        SupportedCardTypesAdapter sut = new SupportedCardTypesAdapter(cardTypes);

        SupportedCardTypesAdapter.SupportedCardTypesViewHolder viewHolder = mock(SupportedCardTypesAdapter.SupportedCardTypesViewHolder.class);
        ImageView imageView = mock(ImageView.class);
        when(viewHolder.getImageView()).thenReturn(imageView);

        sut.onBindViewHolder(viewHolder, 0);

        verify(imageView).setImageResource(CardType.AMEX.getFrontResource());
    }
}
