package com.braintreepayments.api;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

/**
 * Display a set of icons for a list of supported card types.
 */
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

    /**
     * Sets the supported {@link CardType}s on the view to display the card icons.
     *
     * @param cardTypes The {@link CardType}s to display
     */
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

    /**
     * Sets the {@link CardType} passed into this method as visually enabled.
     *
     * The remaining supported card types will become visually disabled.
     *
     * {@link #setSupportedCardTypes(CardType...)} must be called prior to using this method.
     *
     * @param cardType The {@link CardType} to set as visually enabled.
     */
    public void setSelected(@Nullable CardType cardType) {
        if (adapter != null) {
            adapter.setSelected(cardType);
            adapter.notifyDataSetChanged();
        }
    }
}
