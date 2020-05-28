package com.braintreepayments.cardform.view;

import android.text.SpannableString;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class DigitsOnlyFilterTest {

    private StringBuilder stringBuilder;

    @Before
    public void beforeEach() {
        stringBuilder = new StringBuilder();
    }

    @Test
    public void filter_removesSlashesFromString() {
        DigitsOnlyFilter sut = DigitsOnlyFilter.newInstance(stringBuilder);
        CharSequence result = sut.filter("1/2and3", 0, 0, new SpannableString(""), 0, 0);
        assertEquals(result, "123");
    }

    @Test
    public void filter_removesSlashesFromMultipleStrings() {
        DigitsOnlyFilter sut = DigitsOnlyFilter.newInstance(stringBuilder);
        CharSequence result0 = sut.filter("4/5and6", 0, 0, new SpannableString(""), 0, 0);
        assertEquals(result0, "456");

        CharSequence result1 = sut.filter("7and8/9", 0, 0, new SpannableString(""), 0, 0);
        assertEquals(result1, "789");
    }
}
