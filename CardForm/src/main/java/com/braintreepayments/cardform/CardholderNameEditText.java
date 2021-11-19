package com.braintreepayments.cardform;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.InputType;
import android.util.AttributeSet;

import java.util.regex.Pattern;

/**
 * Input for cardholder name. Validated for presence only.
 */
public class CardholderNameEditText extends ErrorEditText {

    private static final Pattern sensitiveDataRegex = Pattern.compile("^[\\d\\s-]+$");

    public CardholderNameEditText(Context context) {
        super(context);
        init();
    }

    public CardholderNameEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CardholderNameEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setInputType(InputType.TYPE_CLASS_TEXT);
        InputFilter[] filters = { new LengthFilter(255) };
        setFilters(filters);
    }

    @Override
    public boolean isValid() {
        if (isOptional()) {
            return hasValidCardholderNameText();
        } else {
            return !isTextEmpty() && hasValidCardholderNameText();
        }
    }

    private boolean isTextEmpty() {
        return getText().toString().trim().isEmpty();
    }

    private boolean hasValidCardholderNameText() {
        Editable text = getText();
        if (text != null) {
            return !sensitiveDataRegex.matcher(text).matches();
        }
        // empty text does not contain sensitive data
        return true;
    }

    @Override
    public String getErrorMessage() {
        return getContext().getString(R.string.bt_cardholder_name_required);
    }
}
