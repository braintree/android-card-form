package com.braintreepayments.cardform.view;

import android.content.Context;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.InputType;
import android.util.AttributeSet;

import com.braintreepayments.cardform.R;

/**
 * Input for extended address. Validated for presence only.
 */
public class ExtendedAddressEditText extends ErrorEditText {

    public ExtendedAddressEditText(Context context) {
        super(context);
        init();
    }

    public ExtendedAddressEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ExtendedAddressEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setInputType(InputType.TYPE_CLASS_TEXT);
        InputFilter[] filters = { new LengthFilter(255) };
        setFilters(filters);
    }
}
