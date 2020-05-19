package com.braintreepayments.cardform.view;

import android.text.SpannableString;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class RemoveSlashesFilterTest {

    private StringBuilder stringBuilder;

    @Before
    public void beforeEach() {
        stringBuilder = new StringBuilder();
    }

    @Test
    public void filter_removesSlashesFromString() {
        RemoveSlashesFilter sut = RemoveSlashesFilter.newInstance(stringBuilder);
        CharSequence result = sut.filter("one/two/three", 0, 0, new SpannableString(""), 0, 0);
        assertEquals(result, "onetwothree");
    }

    @Test
    public void filter_removesSlashesFromMultipleStrings() {
        RemoveSlashesFilter sut = RemoveSlashesFilter.newInstance(stringBuilder);
        CharSequence result0 = sut.filter("four/five/six", 0, 0, new SpannableString(""), 0, 0);
        assertEquals(result0, "fourfivesix");

        CharSequence result1 = sut.filter("seven/eight/nine", 0, 0, new SpannableString(""), 0, 0);
        assertEquals(result1, "seveneightnine");
    }
}
