package com.braintreepayments.cardform.test;

import java.util.Calendar;

public class TestExpirationDate {

    public static String getValidExpiration() {
        return "08" + (Calendar.getInstance().get(Calendar.YEAR) + 1);
    }
}
