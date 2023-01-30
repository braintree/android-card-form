package com.braintreepayments.api;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.util.AttributeSet;

import com.braintreepayments.api.cardform.R;

/**
 * An {@link android.widget.EditText} that displays a CVV hint for a given Card type when focused.
 */
public class CvvEditText extends ErrorEditText implements TextWatcher {

    private static final int DEFAULT_MAX_LENGTH = 3;

    private CardAttributes cardAttributes;

    public CvvEditText(Context context) {
        super(context);
        init();
    }

    public CvvEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CvvEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setInputType(InputType.TYPE_CLASS_NUMBER);
        setFilters(new InputFilter[]{new LengthFilter(DEFAULT_MAX_LENGTH)});
        addTextChangedListener(this);
    }

    /**
     * Sets the card type associated with the security code type. {@link CardType#AMEX} has a
     * different icon and length than other card types. Typically handled through
     * {@link CardEditText.OnCardTypeChangedListener#onCardTypeChanged(CardType)}.
     *
     * @param cardType Type of card represented by the current value of card number input.
     */
    public void setCardType(CardType cardType) {
        cardAttributes = CardAttributes.forCardType(cardType);

        InputFilter[] filters = { new LengthFilter(cardAttributes.getSecurityCodeLength()) };
        setFilters(filters);

        setFieldHint(cardAttributes.getSecurityCodeName());

        invalidate();
    }

    /**
     * @param mask if {@code true}, this field will be masked.
     */
    public void setMask(boolean mask) {
        if (mask) {
            setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            setTransformationMethod(SingleLineTransformationMethod.getInstance());
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (cardAttributes == null) {
            return;
        }

        if (cardAttributes.getSecurityCodeLength() == editable.length() && getSelectionStart() == editable.length()) {
            validate();

            if (isValid()) {
                focusNextView();
            }
        }
    }

    @Override
    public boolean isValid() {
        return isOptional() || getText().toString().length() == getSecurityCodeLength();
    }

    @Override
    public String getErrorMessage() {
        String securityCodeName;
        if (cardAttributes == null) {
            securityCodeName = getContext().getString(R.string.bt_cvv);
        } else {
            securityCodeName = getContext().getString(cardAttributes.getSecurityCodeName());
        }

        if (TextUtils.isEmpty(getText())) {
            return getContext().getString(R.string.bt_cvv_required, securityCodeName);
        } else {
            return getContext().getString(R.string.bt_cvv_invalid, securityCodeName);
        }
    }

    private int getSecurityCodeLength() {
        if (cardAttributes == null) {
            return DEFAULT_MAX_LENGTH;
        } else {
            return cardAttributes.getSecurityCodeLength();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
}
