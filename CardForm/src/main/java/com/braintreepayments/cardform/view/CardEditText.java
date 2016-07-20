package com.braintreepayments.cardform.view;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;

import com.braintreepayments.cardform.R;
import com.braintreepayments.cardform.utils.CardType;

/**
 * An {@link android.widget.EditText} that displays Card icons based on the number entered.
 */
public class CardEditText extends ErrorEditText implements TextWatcher {

    public interface OnCardTypeChangedListener {
        void onCardTypeChanged(CardType cardType);
    }

    private boolean mDisplayCardIcon = true;
    private CardType mCardType;
    private OnCardTypeChangedListener mOnCardTypeChangedListener;

    public CardEditText(Context context) {
        super(context);
        init();
    }

    public CardEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CardEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setInputType(InputType.TYPE_CLASS_NUMBER);
        setCardIcon(R.drawable.bt_card_highlighted);
        addTextChangedListener(this);
        updateCardType();
    }

    /**
     * Enable or disable showing card type icons as part of the {@link CardEditText}. Defaults to {@code true}.
     *
     * @param display {@code true} to display card type icons, {@code false} to never display card type icons.
     */
    public void setDisplayCardTypeIcon(boolean display) {
        mDisplayCardIcon = display;

        if (!mDisplayCardIcon) {
            setCardIcon(-1);
        }
    }

    /**
     * @return The {@link com.braintreepayments.cardform.utils.CardType} currently entered in
     * the {@link android.widget.EditText}
     */
    public CardType getCardType() {
        return mCardType;
    }

    /**
     * Receive a callback when the {@link com.braintreepayments.cardform.utils.CardType} changes
     * @param listener to be called when the {@link com.braintreepayments.cardform.utils.CardType}
     *  changes
     */
    public void setOnCardTypeChangedListener(OnCardTypeChangedListener listener) {
        mOnCardTypeChangedListener = listener;
    }

    @Override
    public void afterTextChanged(Editable editable) {
        Object[] paddingSpans = editable.getSpans(0, editable.length(), SpaceSpan.class);
        for (Object span : paddingSpans) {
            editable.removeSpan(span);
        }

        updateCardType();
        setCardIcon(mCardType.getFrontResource());

        if (!isRightToLeftLanguage()) {
            addSpans(editable, mCardType.getSpaceIndices());
        }

        if (mCardType.getMaxCardLength() == getSelectionStart()) {
            validate();

            if (isValid()) {
                focusNextView();
            }
        }
    }

    @Override
    public boolean isValid() {
        return mCardType.validate(getText().toString());
    }

    @Override
    public String getErrorMessage() {
        if (TextUtils.isEmpty(getText())) {
            return getContext().getString(R.string.bt_card_number_required);
        } else {
            return getContext().getString(R.string.bt_card_number_invalid);
        }
    }

    private void updateCardType() {
        CardType type = CardType.forCardNumber(getText().toString());
        if (mCardType != type) {
            mCardType = type;

            InputFilter[] filters = {new LengthFilter(mCardType.getMaxCardLength())};
            setFilters(filters);
            invalidate();

            if (mOnCardTypeChangedListener != null) {
                mOnCardTypeChangedListener.onCardTypeChanged(mCardType);
            }
        }
    }

    private void addSpans(Editable editable, int[] spaceIndices) {
        final int length = editable.length();
        for (int index : spaceIndices) {
            if (index <= length) {
                editable.setSpan(new SpaceSpan(), index - 1, index,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    private void setCardIcon(int icon) {
        if (!mDisplayCardIcon) {
            setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        } else if (isRightToLeftLanguage()) {
            setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0);
        } else {
            setCompoundDrawablesWithIntrinsicBounds(0, 0, icon, 0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
}
