package com.braintreepayments.cardform.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import androidx.annotation.Nullable;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.widget.TextView;

import com.braintreepayments.cardform.utils.CardType;

import java.util.Arrays;
import java.util.List;

/**
 * Display a set of icons for a list of supported card types.
 */
@SuppressLint("AppCompatCustomView")
public class SupportedCardTypesView extends TextView {

    private List<CardType> mSupportedCardTypes;

    public SupportedCardTypesView(Context context) {
        super(context);
    }

    public SupportedCardTypesView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SupportedCardTypesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SupportedCardTypesView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * Sets the supported {@link CardType}s on the view to display the card icons.
     *
     * @param cardTypes The {@link CardType}s to display
     */
    public void setSupportedCardTypes(@Nullable CardType... cardTypes) {
        if (cardTypes == null) {
            cardTypes = new CardType[]{};
        }

        mSupportedCardTypes = Arrays.asList(cardTypes);
        setSelected(cardTypes);
    }

    public void setSelected(@Nullable CardType... cardTypes) {
        if (cardTypes == null) {
            cardTypes = new CardType[]{};
        }

        SpannableString spannableString = new SpannableString(new String(new char[mSupportedCardTypes.size()]));
        PaddedImageSpan span;
        for (int i = 0; i < mSupportedCardTypes.size(); i++) {
            span = new PaddedImageSpan(getContext(), mSupportedCardTypes.get(i).getFrontResource());
            span.setDisabled(!Arrays.asList(cardTypes).contains(mSupportedCardTypes.get(i)));
            spannableString.setSpan(span, i, i + 1, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        setText(spannableString);
    }
}
