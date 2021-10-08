package com.braintreepayments.cardform.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.braintreepayments.cardform.utils.CardType;
import com.braintreepayments.cardform.utils.SelectableCardType;

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
        // Limit view to 5 card icons in one row
        int rows;
        if (cardTypes.length % 5 == 0) {
            rows = cardTypes.length / 5;
        } else {
            rows = (cardTypes.length / 5) + 1;
        }
        setLayoutManager(new GridLayoutManager(getContext(), rows, LinearLayoutManager.HORIZONTAL, false));

        SelectableCardType[] selectableCardTypes = new SelectableCardType[cardTypes.length];
        for(int i = 0; i < cardTypes.length; i++) {
            selectableCardTypes[i] = new SelectableCardType(cardTypes[i]);
        }
        adapter = new SupportedCardTypesAdapter(selectableCardTypes);
        setAdapter(adapter);
    }

    public void setSelected(@Nullable CardType cardType) {
        adapter.setSelected(cardType);
        adapter.notifyDataSetChanged();
    }
}
