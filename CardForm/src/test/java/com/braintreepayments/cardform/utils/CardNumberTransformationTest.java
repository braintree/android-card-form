package com.braintreepayments.cardform.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class CardNumberTransformationTest {

    CardNumberTransformation mTransform = new CardNumberTransformation();

    @Test
    public void masksCardNumber() {
        String transformed = (String) mTransform.getTransformation("4111111111111111", null);

        assertEquals("\u0000\u0000\u0000\u0000\u0000\u0000\u0000•••• 1111", transformed);
    }

    @Test
    public void doesNotMaskCardNumbersSmallerThanNineCharacters() {
        String transformed = (String) mTransform.getTransformation("41111111", null);

        assertEquals("41111111", transformed);
    }
}
