package com.braintreepayments.cardform.utils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Calendar;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class DateValidatorTest {

    private DateValidator mValidator;

    @Before
    public void setup() {
        // Tests assume the current date is May 7th, 2014, unless otherwise stated.
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2014);
        calendar.set(Calendar.MONTH, Calendar.MAY);
        calendar.set(Calendar.DAY_OF_MONTH, 7);

        mValidator = new DateValidator(calendar);
    }

    @Test
    public void monthIsRequired() {
        assertInvalid("","18");
        assertInvalid("","2018");
    }

    @Test
    public void yearIsRequired() {
        assertInvalid("01","");
    }

    @Test
    public void leadingZeroNotRequiredForMonth() {
        assertValid("7","18");
    }

    @Test
    public void leadingZeroRequiredForYear() {
        assertInvalid("11","7");
    }

    @Test
    public void monthEdgeCases() {
        assertInvalid("00","18");
        assertInvalid("13","18");

        assertValid("01","18");
        assertValid("12","18");
    }

    @Test
    public void pastMonthInCurrentYearIsInvalid() {
        assertInvalid("04","14");
    }

    @Test
    public void currentMonthAndYearIsValid() {
        assertValid("05","14");
    }

    @Test
    public void futureMonthInCurrentYearIsValid() {
        assertValid("06","14");
    }

    @Test
    public void yearInPastIsInvalid() {
        assertInvalid("05","13");
        assertInvalid("05","2013");
    }

    @Test
    public void yearTooFarInTheFutureIsInvalid() {
        assertInvalid("05","36");
        assertInvalid("05","2036");
    }

    @Test
    public void yearWrapping() {
        Calendar endOfCenturyCalendar = Calendar.getInstance();
        endOfCenturyCalendar.set(Calendar.YEAR, 2095);
        endOfCenturyCalendar.set(Calendar.MONTH, Calendar.NOVEMBER);
        DateValidator endOfCenturyValidator = new DateValidator(endOfCenturyCalendar);

        assertTrue(endOfCenturyValidator.isValidHelper("01","01"));
        assertTrue(endOfCenturyValidator.isValidHelper("01","05"));
        assertTrue(endOfCenturyValidator.isValidHelper("01","96"));
        assertFalse(endOfCenturyValidator.isValidHelper("01","94"));

        // the following assertions use the regular validator, where the year is set to 2014. Years
        // with prefix-zeros should now fail.
        assertInvalid("01","01");
        assertInvalid("01","05");
        assertInvalid("01","94");
    }

    /* helpers */
    private void assertValid(String month, String year) {
        assertTrue(mValidator.isValidHelper(month, year));
    }

    private void assertInvalid(String month, String year) {
        assertFalse(mValidator.isValidHelper(month, year));
    }
}
