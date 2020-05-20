package com.braintreepayments.cardform.view;

import android.text.InputFilter;
import android.text.Spanned;

import androidx.annotation.VisibleForTesting;

class RemoveSlashesFilter implements InputFilter {

    private StringBuilder stringBuilder;

    static RemoveSlashesFilter newInstance(int maxStringLength) {
        return newInstance(new StringBuilder(maxStringLength));
    }

    @VisibleForTesting
    static RemoveSlashesFilter newInstance(StringBuilder stringBuilder) {
        return new RemoveSlashesFilter(stringBuilder);
    }

    private RemoveSlashesFilter(StringBuilder stringBuilder) {
        this.stringBuilder = stringBuilder;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        // re-use string buffer to prevent unnecessary memory allocations while user is typing
        stringBuilder.setLength(0);

        int numChars = source.length();
        for (int index = 0; index < numChars; index++) {
            char c = source.charAt(index);
            if (source.charAt(index) != '/') {
                stringBuilder.append(c);
            }
        }
        return stringBuilder.toString();
    }
}
