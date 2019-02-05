package com.braintreepayments.cardform.view;

import android.content.Context;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.InputType;
import android.util.AttributeSet;

import com.braintreepayments.cardform.R;

/**
 * Input for address line 1. Validated for presence only.
 */
public class AddressLine1EditText extends ErrorEditText {

    public AddressLine1EditText(Context context) {
        super(context);
        init();
    }

    public AddressLine1EditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AddressLine1EditText(Context context, AttributeSet attrs, int defStyle) {
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
        return isOptional() || !getText().toString().trim().isEmpty();
    }

    @Override
    public String getErrorMessage() {
        return getContext().getString(R.string.bt_address_line1_required);
    }
}
