package com.braintreepayments.cardform.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build.VERSION_CODES;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.braintreepayments.cardform.OnCardFormFieldFocusedListener;
import com.braintreepayments.cardform.OnCardFormSubmitListener;
import com.braintreepayments.cardform.OnCardFormValidListener;
import com.braintreepayments.cardform.R;
import com.braintreepayments.cardform.utils.CardType;
import com.braintreepayments.cardform.view.CardEditText.OnCardTypeChangedListener;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH;

public class CardForm extends LinearLayout implements
        OnCardTypeChangedListener, OnFocusChangeListener, OnClickListener, OnEditorActionListener,
        TextWatcher {

    private CardEditText mCardNumber;
    private ExpirationDateEditText mExpiration;
    private CvvEditText mCvv;
    private PostalCodeEditText mPostalCode;

    private boolean mCardNumberRequired;
    private boolean mExpirationRequired;
    private boolean mCvvRequired;
    private boolean mPostalCodeRequired;

    private boolean mValid = false;

    private OnCardFormValidListener mOnCardFormValidListener;
    private OnCardFormSubmitListener mOnCardFormSubmitListener;
    private OnCardFormFieldFocusedListener mOnCardFormFieldFocusedListener;
    private OnCardTypeChangedListener mOnCardTypeChangedListener;

    public CardForm(Context context) {
        super(context);
        init();
    }

    public CardForm(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CardForm(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(VERSION_CODES.LOLLIPOP)
    public CardForm(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.bt_card_form_fields, this);

        setVisibility(GONE);

        mCardNumber = (CardEditText) findViewById(R.id.bt_card_form_card_number);
        mExpiration = (ExpirationDateEditText) findViewById(R.id.bt_card_form_expiration);
        mCvv = (CvvEditText) findViewById(R.id.bt_card_form_cvv);
        mPostalCode = (PostalCodeEditText) findViewById(R.id.bt_card_form_postal_code);

        mCardNumber.setOnFocusChangeListener(this);
        mExpiration.setOnFocusChangeListener(this);
        mCvv.setOnFocusChangeListener(this);
        mPostalCode.setOnFocusChangeListener(this);

        mCardNumber.setOnClickListener(this);
        mExpiration.setOnClickListener(this);
        mCvv.setOnClickListener(this);
        mPostalCode.setOnClickListener(this);

        mCardNumber.setOnCardTypeChangedListener(this);
    }

    /**
     * Set the required fields for the {@link com.braintreepayments.cardform.view.CardForm}.
     * If {@link #setRequiredFields(android.app.Activity, boolean, boolean, boolean, boolean, String)}
     * is not called, the form will not be visible.
     *
     * @param activity Used to set {@link android.view.WindowManager.LayoutParams#FLAG_SECURE} to prevent screenshots
     * @param cardNumberRequired {@code true} to show and require a credit card number, {@code false} otherwise
     * @param expirationRequired {@code true} to show and require an expiration date, {@code false} otherwise
     * @param cvvRequired {@code true} to show and require a cvv, {@code false} otherwise
     * @param postalCodeRequired {@code true} to show and require a postal code, {@code false} otherwise
     * @param imeActionLabel the {@link java.lang.String} to display to the user to submit the form
     *   from the keyboard
     */
    public void setRequiredFields(Activity activity, boolean cardNumberRequired, boolean expirationRequired,
                                  boolean cvvRequired, boolean postalCodeRequired, String imeActionLabel) {
        if (SDK_INT >= ICE_CREAM_SANDWICH) {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE);
        }

        mCardNumberRequired = cardNumberRequired;
        mExpirationRequired = expirationRequired;
        mCvvRequired = cvvRequired;
        mPostalCodeRequired = postalCodeRequired;

        if (mCardNumberRequired) {
            mCardNumber.setVisibility(View.VISIBLE);
            mCardNumber.addTextChangedListener(this);

            if (mExpirationRequired) {
                mCardNumber.setNextFocusDownId(mExpiration.getId());
            } else if (mCvvRequired) {
                mCardNumber.setNextFocusDownId(mCvv.getId());
            } else if (mPostalCodeRequired) {
                mCardNumber.setNextFocusDownId(mPostalCode.getId());
            }
        } else {
            mCardNumber.setVisibility(View.GONE);
        }

        mExpiration.useExpirationDateDialogForEntry(activity, true);
        if (mExpirationRequired) {
            mExpiration.setVisibility(View.VISIBLE);
            mExpiration.addTextChangedListener(this);

            if (mCvvRequired) {
                mExpiration.setNextFocusDownId(mCvv.getId());
            } else if (mPostalCodeRequired) {
                mExpiration.setNextFocusDownId(mPostalCode.getId());
            }
        } else {
            mExpiration.setVisibility(View.GONE);
        }

        if (mCvvRequired || mPostalCodeRequired) {
            mExpiration.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        } else {
            setIMEOptionsForLastEditTestField(mExpiration, imeActionLabel);
        }

        if (mCvvRequired) {
            mCvv.setVisibility(View.VISIBLE);
            mCvv.addTextChangedListener(this);

            if (mPostalCodeRequired) {
                mCvv.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                mCvv.setNextFocusDownId(mPostalCode.getId());
            } else {
                setIMEOptionsForLastEditTestField(mCvv, imeActionLabel);
            }
        } else {
            mCvv.setVisibility(View.GONE);
        }

        if (postalCodeRequired) {
            mPostalCode.setVisibility(View.VISIBLE);
            mPostalCode.addTextChangedListener(this);
            setIMEOptionsForLastEditTestField(mPostalCode, imeActionLabel);
        } else {
            mPostalCode.setVisibility(View.GONE);
        }

        mCardNumber.setOnCardTypeChangedListener(this);

        setVisibility(VISIBLE);
    }

    private void setIMEOptionsForLastEditTestField(EditText editText, String imeActionLabel) {
        editText.setImeOptions(EditorInfo.IME_ACTION_GO);
        editText.setImeActionLabel(imeActionLabel, EditorInfo.IME_ACTION_GO);
        editText.setOnEditorActionListener(this);
    }

    /**
     * Set the listener to receive a callback when the card form becomes valid or invalid
     * @param listener to receive the callback
     */
    public void setOnCardFormValidListener(OnCardFormValidListener listener) {
        mOnCardFormValidListener = listener;
    }

    /**
     * Set the listener to receive a callback when the card form should be submitted.
     * Triggered from a keyboard by a {@link android.view.inputmethod.EditorInfo#IME_ACTION_GO} event
     * @param listener to receive the callback
     */
    public void setOnCardFormSubmitListener(OnCardFormSubmitListener listener) {
        mOnCardFormSubmitListener = listener;
    }

    /**
     * Set the listener to receive a callback when a field is focused
     * @param listener to receive the callback
     */
    public void setOnFormFieldFocusedListener(OnCardFormFieldFocusedListener listener) {
        mOnCardFormFieldFocusedListener = listener;
    }

    /**
     * Set the listener to receive a callback when the {@link com.braintreepayments.cardform.utils.CardType} changes.
     * @param listener to receive the callback
     */
    public void setOnCardTypeChangedListener(OnCardTypeChangedListener listener) {
        mOnCardTypeChangedListener = listener;
    }

    /**
     * Set {@link android.widget.EditText} fields as enabled or disabled
     * @param enabled {@code true} to enable all required fields, {@code false} to disable all
     * required fields
     */
    public void setEnabled(boolean enabled) {
        mCardNumber.setEnabled(enabled);
        mExpiration.setEnabled(enabled);
        mCvv.setEnabled(enabled);
        mPostalCode.setEnabled(enabled);
    }

    /**
     * @return {@code true} if all require fields are valid, otherwise {@code false}
     */
    public boolean isValid() {
        boolean valid = true;
        if (mCardNumberRequired) {
            valid = valid && mCardNumber.isValid();
        }
        if (mExpirationRequired) {
            valid = valid && mExpiration.isValid();
        }
        if (mCvvRequired) {
            valid = valid && mCvv.isValid();
        }
        if (mPostalCodeRequired) {
            valid = valid && mPostalCode.isValid();
        }
        return valid;
    }

    /**
     * Validate all required fields and mark invalid fields with an error indicator
     */
    public void validate() {
        if (mCardNumberRequired) {
            mCardNumber.validate();
        }
        if (mExpirationRequired) {
            mExpiration.validate();
        }
        if (mCvvRequired) {
            mCvv.validate();
        }
        if (mPostalCodeRequired) {
            mPostalCode.validate();
        }
    }

    /**
     * Set visual indicator on card number to indicate error
     */
    public void setCardNumberError() {
        if (mCardNumberRequired) {
            mCardNumber.setError(true);
            requestEditTextFocus(mCardNumber);
        }
    }

    /**
     * Set visual indicator on expiration to indicate error
     */
    public void setExpirationError() {
        if (mExpirationRequired) {
            mExpiration.setError(true);
            if (!mCardNumberRequired || !mCardNumber.isFocused()) {
                requestEditTextFocus(mExpiration);
            }
        }
    }

    /**
     * Set visual indicator on cvv to indicate error
     */
    public void setCvvError() {
        if (mCvvRequired) {
            mCvv.setError(true);
            if ((!mCardNumberRequired && !mExpirationRequired) ||
                (!mCardNumber.isFocused() && !mExpiration.isFocused())) {
                requestEditTextFocus(mCvv);
            }
        }
    }

    /**
     * Set visual indicator on postal code to indicate error
     */
    public void setPostalCodeError() {
        if (mPostalCodeRequired) {
            mPostalCode.setError(true);
            if ((!mCardNumberRequired && !mExpirationRequired && !mCvvRequired) ||
                (!mCardNumber.isFocused() && !mExpiration.isFocused() && !mCvv.isFocused())) {
                requestEditTextFocus(mPostalCode);
            }
        }
    }

    private void requestEditTextFocus(EditText editText) {
        editText.requestFocus();
        ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                .showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * Attempt to close the soft keyboard. Will have no effect if the keyboard is not open.
     */
    public void closeSoftKeyboard() {
        mCardNumber.closeSoftKeyboard();
    }

    /**
     * @return the text in the card number field
     */
    public String getCardNumber() {
        return mCardNumber.getText().toString();
    }

    /**
     * @return the 2-digit month, formatted with a leading zero if necessary from the expiration
     * field. If no month has been specified, an empty string is returned.
     */
    public String getExpirationMonth() {
        return mExpiration.getMonth();
    }

    /**
     * @return the 2- or 4-digit year depending on user input from the expiration field.
     * If no year has been specified, an empty string is returned.
     */
    public String getExpirationYear() {
        return mExpiration.getYear();
    }

    /**
     * @return the text in the cvv field
     */
    public String getCvv() {
        return mCvv.getText().toString();
    }

    /**
     * @return the text in the postal code field
     */
    public String getPostalCode() {
        return mPostalCode.getText().toString();
    }

    @Override
    public void onCardTypeChanged(CardType cardType) {
        mCvv.setCardType(cardType);

        if (mOnCardTypeChangedListener != null) {
            mOnCardTypeChangedListener.onCardTypeChanged(cardType);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus && mOnCardFormFieldFocusedListener != null) {
            mOnCardFormFieldFocusedListener.onCardFormFieldFocused(v);
        }
    }

    @Override
    public void onClick(View v) {
        if (mOnCardFormFieldFocusedListener != null) {
            mOnCardFormFieldFocusedListener.onCardFormFieldFocused(v);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        boolean valid = isValid();
        if (mValid != valid) {
            mValid = valid;
            if (mOnCardFormValidListener != null) {
                mOnCardFormValidListener.onCardFormValid(valid);
            }
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_GO && mOnCardFormSubmitListener != null) {
            mOnCardFormSubmitListener.onCardFormSubmit();
            return true;
        }
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}
}
