package com.braintreepayments.cardform.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.braintreepayments.cardform.R;
import com.braintreepayments.cardform.utils.CardType;
import com.braintreepayments.cardform.utils.SelectableCardType;

class SupportedCardTypesAdapter extends RecyclerView.Adapter<SupportedCardTypesAdapter.SupportedCardTypesViewHolder> {

    private final SelectableCardType[] supportedCardTypes;

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
        viewHolder.getImageView().setImageResource(selectableCardType.getCardType().getFrontResource());

        viewHolder.getImageView().setContentDescription(selectableCardType.getCardType().toString());
        if (selectableCardType.isDisabled()) {
            viewHolder.getImageView().setImageAlpha(80);
        } else {
            viewHolder.getImageView().setImageAlpha(255);
        }
    }

    @Override
    public int getItemCount() {
        return supportedCardTypes.length;
    }

    void setSelected(CardType cardType) {
        for(SelectableCardType selectableCardType : supportedCardTypes) {
            selectableCardType.setDisabled(selectableCardType.getCardType() != cardType);
        }
    }
}
