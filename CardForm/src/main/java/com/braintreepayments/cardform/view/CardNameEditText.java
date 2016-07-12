package com.braintreepayments.cardform.view;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by dhaval on 12/07/16.
 */
public class CardNameEditText extends FloatingLabelEditText {
    //name needs to have a minimum lenth of 3
    private static final int DEFAULT_MIN_LENGTH = 3;

    public CardNameEditText(Context context) {
        super(context);
    }

    public CardNameEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CardNameEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean isValid() {
        return getText().toString().length() >= DEFAULT_MIN_LENGTH;
    }
}
