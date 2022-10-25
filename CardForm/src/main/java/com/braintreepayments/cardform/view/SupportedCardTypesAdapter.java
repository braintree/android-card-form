package com.braintreepayments.cardform.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.recyclerview.widget.RecyclerView;

import com.braintreepayments.api.CardType;
import com.braintreepayments.cardform.R;
import com.braintreepayments.cardform.utils.CardDescriptor;
import com.braintreepayments.cardform.utils.CardParser;
import com.braintreepayments.cardform.utils.SelectableCardType;

class SupportedCardTypesAdapter extends RecyclerView.Adapter<SupportedCardTypesAdapter.SupportedCardTypesViewHolder> {

    private final int OPAQUE = 255;
    private final int SEMI_TRANSPARENT = 80;

    private final SelectableCardType[] supportedCardTypes;

    private CardParser cardParser = new CardParser();

    static class SupportedCardTypesViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;

        public SupportedCardTypesViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.bt_supported_card_icon);
        }

        public ImageView getImageView() {
            return imageView;
        }
    }

    SupportedCardTypesAdapter(SelectableCardType[] cardTypes) {
        supportedCardTypes = cardTypes;
    }

    @NonNull
    @Override
    public SupportedCardTypesViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.bt_supported_card_type, viewGroup, false);

        return new SupportedCardTypesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SupportedCardTypesViewHolder viewHolder, final int position) {
        SelectableCardType selectableCardType = supportedCardTypes[position];

        CardDescriptor cardDescriptor = cardParser.getDescriptor(selectableCardType.getCardType());
        viewHolder.getImageView().setImageResource(cardDescriptor.getFrontResource());

        viewHolder.getImageView().setContentDescription(selectableCardType.getCardType().toString());
        if (selectableCardType.isDisabled()) {
            viewHolder.getImageView().setImageAlpha(SEMI_TRANSPARENT);
        } else {
            viewHolder.getImageView().setImageAlpha(OPAQUE);
        }
    }

    @Override
    public int getItemCount() {
        return supportedCardTypes.length;
    }

    @VisibleForTesting
    SelectableCardType[] getSupportedCardTypes() {
        return supportedCardTypes;
    }

    void setSelected(CardType cardType) {
        if (supportedCardTypes != null) {
            for (SelectableCardType selectableCardType : supportedCardTypes) {
                selectableCardType.setDisabled(selectableCardType.getCardType() != cardType);
            }
        }
    }
}
