package com.braintreepayments.cardform.view;

import android.content.Context;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.InputType;
import android.util.AttributeSet;

import com.braintreepayments.cardform.R;

/**
 * Input for country code. Validated for presence only due to the wide variation of country code formats worldwide.
 */
public class CountryCodeEditText extends ErrorEditText {

    public CountryCodeEditText(Context context) {
        super(context);
        init();
    }

    public CountryCodeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CountryCodeEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setInputType(InputType.TYPE_CLASS_PHONE);
        InputFilter[] filters = { new LengthFilter(4) };
        setFilters(filters);
    }

    /**
     * @return the numeric country code entered by the user
     */
    public String getCountryCode() {
        return getText().toString().replaceAll("[^\\d]", "");
    }

    @Override
    public boolean isValid() {
        return isOptional() || getText().toString().length() > 0;
    }

    @Override
    public String getErrorMessage() {
        return getContext().getString(R.string.bt_country_code_required);
    }
}
