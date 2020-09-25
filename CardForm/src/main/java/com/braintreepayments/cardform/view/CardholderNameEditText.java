package com.braintreepayments.cardform.view;

import android.content.Context;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.braintreepayments.cardform.R;
import com.braintreepayments.cardform.utils.CardType;

/**
 * Input for cardholder name. Validated for presence only.
 */
public class CardholderNameEditText extends ErrorEditText {

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
        String cardholderNameText = getText().toString().trim();
        boolean isCardNumber = TextUtils.isDigitsOnly(cardholderNameText) && CardType.isLuhnValid(cardholderNameText);
        boolean isValidCardholderName = !cardholderNameText.isEmpty() && !isCardNumber;
        return isOptional() || isValidCardholderName;
    }

    @Override
    public String getErrorMessage() {
        return getContext().getString(R.string.bt_cardholder_name_required);
    }
}
