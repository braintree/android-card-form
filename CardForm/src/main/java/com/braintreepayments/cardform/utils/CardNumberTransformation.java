package com.braintreepayments.cardform.utils;

import android.graphics.Rect;
import android.text.method.TransformationMethod;
import android.view.View;

import java.util.Arrays;

/**
 * A transformation that masks card numbers up to the last 4 digits. For example, it will transform
 * "4111111111111111" to "●●●● 1111". This can be used to mask card numbers in an {@link android.widget.EditText}.
 */
public class CardNumberTransformation implements TransformationMethod {

    private static final String FOUR_DOTS = "••••";

    @Override
    public CharSequence getTransformation(final CharSequence source, View view) {
        if (source.length() >= 9) {
            StringBuilder result = new StringBuilder()
                    .append(FOUR_DOTS)
                    .append(" ")
                    .append(source.subSequence(source.length() - 4, source.length()));

            char[] padding = new char[source.length() - result.length()];
            Arrays.fill(padding, Character.MIN_VALUE);
            result.insert(0, padding);

            return result.toString();
        }

        return source;
    }

    @Override
    public void onFocusChanged(View view, CharSequence sourceText, boolean focused, int direction, Rect previouslyFocusedRect) {}
}

