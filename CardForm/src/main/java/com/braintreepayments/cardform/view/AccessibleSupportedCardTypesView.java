package com.braintreepayments.cardform.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.braintreepayments.cardform.utils.CardType;
import com.braintreepayments.cardform.utils.SelectableCardType;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

public class AccessibleSupportedCardTypesView extends RecyclerView {

    @VisibleForTesting
    SupportedCardTypesAdapter adapter;

    public AccessibleSupportedCardTypesView(@NonNull Context context) {
        super(context);
    }

    public AccessibleSupportedCardTypesView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AccessibleSupportedCardTypesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setSupportedCardTypes(@Nullable CardType... cardTypes) {
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext(), FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.CENTER);
        setLayoutManager(layoutManager);

        if (cardTypes == null) {
            cardTypes = new CardType[]{};
        }
        SelectableCardType[] selectableCardTypes = new SelectableCardType[cardTypes.length];
        for(int i = 0; i < cardTypes.length; i++) {
            selectableCardTypes[i] = new SelectableCardType(cardTypes[i]);
        }
        adapter = new SupportedCardTypesAdapter(selectableCardTypes);
        setAdapter(adapter);
    }

    public void setSelected(@Nullable CardType cardType) {
        if (adapter != null) {
            adapter.setSelected(cardType);
            adapter.notifyDataSetChanged();
        }
    }
}
