package com.braintreepayments.cardform.view;

import android.text.SpannableStringBuilder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class ExpirationDateTextWatcherTest {

    @Test
    public void afterTextChanged_whenStringIsEmpty_doesNothing() {
        ExpirationDateTextWatcher sut = ExpirationDateTextWatcher.newInstance();

        SpannableStringBuilder editable = new SpannableStringBuilder("");
        sut.afterTextChanged(editable);
        assertEquals(editable.toString(), "");
    }

    @Test
    public void afterTextChanged_addsLeadingZeroByDefault() {

        List<List<String>> assertionPairs = Arrays.asList(
            Arrays.asList("2", "02"),
            Arrays.asList("3", "03"),
            Arrays.asList("4", "04"),
            Arrays.asList("5", "05"),
            Arrays.asList("6", "06"),
            Arrays.asList("7", "07"),
            Arrays.asList("8", "08"),
            Arrays.asList("9", "09")
        );

        for (List<String> pair : assertionPairs) {
            String input = pair.get(0);
            String expectedOutput = pair.get(1);

            ExpirationDateTextWatcher sut = ExpirationDateTextWatcher.newInstance();
            SpannableStringBuilder editable = new SpannableStringBuilder(input);

            sut.afterTextChanged(editable);
            assertEquals(editable.toString(), expectedOutput);
        }
    }

    @Test
    public void afterTextChanged_whenLeadingCharIsZero_doesNothing() {
        ExpirationDateTextWatcher sut = ExpirationDateTextWatcher.newInstance();

        SpannableStringBuilder editable = new SpannableStringBuilder("0");
        sut.afterTextChanged(editable);
        assertEquals(editable.toString(), "0");
    }

    @Test
    public void afterTextChanged_whenLeadingCharIsOne_doesNothingByDefault() {
        ExpirationDateTextWatcher sut = ExpirationDateTextWatcher.newInstance();
        SpannableStringBuilder editable = new SpannableStringBuilder("1");

        sut.afterTextChanged(editable);
        assertEquals(editable.toString(), "1");
    }

    @Test
    public void afterTextChanged_whenFirstTwoCharactersAreValidMonth_doesNothing() {

        List<List<String>> assertionPairs = Arrays.asList(
                Arrays.asList("01", "01"),
                Arrays.asList("02", "02"),
                Arrays.asList("03", "03"),
                Arrays.asList("04", "04"),
                Arrays.asList("05", "05"),
                Arrays.asList("06", "06"),
                Arrays.asList("07", "07"),
                Arrays.asList("08", "08"),
                Arrays.asList("09", "09"),
                Arrays.asList("10", "10"),
                Arrays.asList("11", "11"),
                Arrays.asList("12", "12")
        );

        for (List<String> pair : assertionPairs) {
            String input = pair.get(0);
            String expectedOutput = pair.get(1);

            ExpirationDateTextWatcher sut = ExpirationDateTextWatcher.newInstance();
            SpannableStringBuilder editable = new SpannableStringBuilder(input);

            sut.afterTextChanged(editable);
            assertEquals(editable.toString(), expectedOutput);
        }
    }

    @Test
    public void afterTextChanged_whenFirstTwoCharactersAreInvalidMonth_addsSlash() {

        List<List<String>> assertionPairs = Arrays.asList(
                Arrays.asList("13", "01/3"),
                Arrays.asList("14", "01/4"),
                Arrays.asList("15", "01/5"),
                Arrays.asList("16", "01/6"),
                Arrays.asList("17", "01/7"),
                Arrays.asList("18", "01/8"),
                Arrays.asList("19", "01/9")
        );

        for (List<String> pair : assertionPairs) {
            String input = pair.get(0);
            String expectedOutput = pair.get(1);

            ExpirationDateTextWatcher sut = ExpirationDateTextWatcher.newInstance();
            SpannableStringBuilder editable = new SpannableStringBuilder(input);

            sut.afterTextChanged(editable);
            assertEquals(editable.toString(), expectedOutput);
        }
    }

    @Test
    public void afterTextChanged_whenValidMonthEnteredWithSlash_doesNothing() {

        List<List<String>> assertionPairs = Arrays.asList(
                Arrays.asList("01/", "01/"),
                Arrays.asList("02/", "02/"),
                Arrays.asList("03/", "03/"),
                Arrays.asList("04/", "04/"),
                Arrays.asList("05/", "05/"),
                Arrays.asList("06/", "06/"),
                Arrays.asList("07/", "07/"),
                Arrays.asList("08/", "08/"),
                Arrays.asList("09/", "09/"),
                Arrays.asList("10/", "10/"),
                Arrays.asList("11/", "11/"),
                Arrays.asList("12/", "12/")
        );

        for (List<String> pair : assertionPairs) {
            String input = pair.get(0);
            String expectedOutput = pair.get(1);

            ExpirationDateTextWatcher sut = ExpirationDateTextWatcher.newInstance();
            SpannableStringBuilder editable = new SpannableStringBuilder(input);

            sut.afterTextChanged(editable);
            assertEquals(editable.toString(), expectedOutput);
        }
    }

    @Test
    public void afterTextChanged_whenTypingDateString_doesNothing() {

        List<List<String>> assertionPairs = Arrays.asList(
                Arrays.asList("01/2", "01/2"),
                Arrays.asList("01/20", "01/20"),
                Arrays.asList("01/202", "01/202"),
                Arrays.asList("01/2020", "01/2020")
        );

        for (List<String> pair : assertionPairs) {
            String input = pair.get(0);
            String expectedOutput = pair.get(1);

            ExpirationDateTextWatcher sut = ExpirationDateTextWatcher.newInstance();
            SpannableStringBuilder editable = new SpannableStringBuilder(input);

            sut.afterTextChanged(editable);
            assertEquals(editable.toString(), expectedOutput);
        }
    }
}
