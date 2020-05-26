package com.braintreepayments.cardform.view;

import android.text.InputFilter;
import android.text.Spanned;

import androidx.annotation.VisibleForTesting;

import java.util.regex.Pattern;

class DigitsOnlyFilter implements InputFilter {

    private StringBuilder stringBuilder;
    private Pattern digitsRegex;

    static DigitsOnlyFilter newInstance(int maxStringLength) {
        return newInstance(new StringBuilder(maxStringLength));
    }

    @VisibleForTesting
    static DigitsOnlyFilter newInstance(StringBuilder stringBuilder) {
        return new DigitsOnlyFilter(stringBuilder);
    }

    private DigitsOnlyFilter(StringBuilder stringBuilder) {
        this.stringBuilder = stringBuilder;
        this.digitsRegex = Pattern.compile("[0-9]");
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        // re-use string buffer to prevent unnecessary memory allocations while user is typing
        stringBuilder.setLength(0);

        int numChars = source.length();
        for (int index = 0; index < numChars; index++) {
            CharSequence c = source.subSequence(index, index + 1);
            if (digitsRegex.matcher(c).matches()) {
                stringBuilder.append(c);
            }
        }
        return stringBuilder.toString();
    }
}
