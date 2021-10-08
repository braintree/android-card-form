package com.braintreepayments.cardform.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.braintreepayments.cardform.utils.CardType;

public class SupportedCardTypesRecyclerView extends RecyclerView {

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
        SupportedCardTypesAdapter adapter = new SupportedCardTypesAdapter(cardTypes);
        setAdapter(adapter);
    }
}
