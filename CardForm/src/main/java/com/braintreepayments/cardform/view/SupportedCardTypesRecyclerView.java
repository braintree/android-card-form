package com.braintreepayments.cardform.view;

import android.content.Context;
import android.text.SpannableString;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.braintreepayments.cardform.utils.CardType;
import com.braintreepayments.cardform.utils.SelectableCardType;

import java.util.Arrays;

public class SupportedCardTypesRecyclerView extends RecyclerView {

    SupportedCardTypesAdapter adapter;

    public SupportedCardTypesRecyclerView(@NonNull Context context) {
        super(context);
    }

    public SupportedCardTypesRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SupportedCardTypesRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setSupportedCardTypes(CardType[] cardTypes) {
        setLayoutManager(new GridLayoutManager(getContext(), 2, LinearLayoutManager.HORIZONTAL, false));

        SelectableCardType[] selectableCardTypes = new SelectableCardType[cardTypes.length];
        for(int i = 0; i < cardTypes.length; i++) {
            selectableCardTypes[i] = new SelectableCardType(cardTypes[i]);
        }
        adapter = new SupportedCardTypesAdapter(selectableCardTypes);
        setAdapter(adapter);
//        setSelected(cardTypes);
    }

    public void setSelected(@Nullable CardType cardType) {
//        if (cardTypes == null) {
//            cardTypes = new CardType[]{};
//        }
        adapter.setSelected(cardType);
        adapter.notifyDataSetChanged();
//
//        SpannableString spannableString = new SpannableString(new String(new char[mSupportedCardTypes.size()]));
//        PaddedImageSpan span;
//        for (int i = 0; i < mSupportedCardTypes.size(); i++) {
//            span = new PaddedImageSpan(getContext(), mSupportedCardTypes.get(i).getFrontResource());
//            span.setDisabled(!Arrays.asList(cardTypes).contains(mSupportedCardTypes.get(i)));
//            spannableString.setSpan(span, i, i + 1, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }
//
//        setText(spannableString);
    }
}
