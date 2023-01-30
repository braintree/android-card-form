package com.braintreepayments.api;

import android.content.Context;
import android.graphics.Rect;
import androidx.core.widget.TextViewCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;

import com.braintreepayments.api.cardform.R;

/**
 * An {@link android.widget.EditText} that displays Card icons based on the number entered.
 */
public class CardEditText extends ErrorEditText implements TextWatcher {

    public interface OnCardTypeChangedListener {
        void onCardTypeChanged(CardType cardType);
    }

    private boolean displayCardIcon = true;
    private boolean mask = false;
    private CardAttributes cardAttributes = CardAttributes.EMPTY;
    private OnCardTypeChangedListener onCardTypeChangedListener;
    private TransformationMethod savedTransformationMethod;

    private CardParser cardParser = new CardParser();

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
        setCardIcon(R.drawable.bt_ic_unknown);
        addTextChangedListener(this);
        updateCardType();
        savedTransformationMethod = getTransformationMethod();
    }

    /**
     * Enable or disable showing card type icons as part of the {@link CardEditText}. Defaults to
     * {@code true}.
     *
     * @param display {@code true} to display card type icons, {@code false} to never display card
     *                            type icons.
     */
    public void displayCardTypeIcon(boolean display) {
        displayCardIcon = display;

        if (!displayCardIcon) {
            setCardIcon(-1);
        }
    }

    /**
     * @return The {@link CardType} currently entered in
     * the {@link android.widget.EditText}
     */
    public CardType getCardType() {
        return cardAttributes.getCardType();
    }

    /**
     * @param mask if {@code true}, all but the last four digits of the card number will be masked when
     * focus leaves the card field. Uses {@link CardNumberTransformation}, transforming the number from
     * something like "4111111111111111" to "•••• 1111".
     */
    public void setMask(boolean mask) {
        this.mask = mask;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);

        if (focused) {
            unmaskNumber();

            if (getText().toString().length() > 0) {
                setSelection(getText().toString().length());
            }
        } else if (mask && isValid()) {
            maskNumber();
        }
    }

    /**
     * Receive a callback when the {@link CardType} changes
     * @param listener to be called when the {@link CardType}
     *  changes
     */
    public void setOnCardTypeChangedListener(OnCardTypeChangedListener listener) {
        onCardTypeChangedListener = listener;
    }

    @Override
    public void afterTextChanged(Editable editable) {
        Object[] paddingSpans = editable.getSpans(0, editable.length(), SpaceSpan.class);
        for (Object span : paddingSpans) {
            editable.removeSpan(span);
        }

        updateCardType();

        setCardIcon(cardAttributes.getFrontResource());
        addSpans(editable, cardAttributes.getSpaceIndices());

        if (cardAttributes.getMaxCardLength() == getSelectionStart()) {
            validate();

            if (isValid()) {
                focusNextView();
            } else {
                unmaskNumber();
            }
        } else if (!hasFocus()) {
            if (mask) {
                maskNumber();
            }
        }
    }

    @Override
    public boolean isValid() {
        return isOptional() || cardParser.validate(getText().toString());
    }

    @Override
    public String getErrorMessage() {
        if (TextUtils.isEmpty(getText())) {
            return getContext().getString(R.string.bt_card_number_required);
        } else {
            return getContext().getString(R.string.bt_card_number_invalid);
        }
    }

    private void maskNumber() {
        if (!(getTransformationMethod() instanceof CardNumberTransformation)) {
            savedTransformationMethod = getTransformationMethod();

            setTransformationMethod(new CardNumberTransformation());
        }
    }

    private void unmaskNumber() {
        if (getTransformationMethod() != savedTransformationMethod) {
            setTransformationMethod(savedTransformationMethod);
        }
    }

    private void updateCardType() {
        CardAttributes attrs = cardParser.parseCardAttributes(getText().toString());

        if (cardAttributes.getCardType() != attrs.getCardType()) {
            cardAttributes = attrs;

            InputFilter[] filters = { new LengthFilter(cardAttributes.getMaxCardLength()) };
            setFilters(filters);
            invalidate();

            if (onCardTypeChangedListener != null) {
                onCardTypeChangedListener.onCardTypeChanged(cardAttributes.getCardType());
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
        if (!displayCardIcon || getText().length() == 0) {
            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(this, 0, 0, 0, 0);
        } else {
            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(this, 0, 0, icon, 0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
}
