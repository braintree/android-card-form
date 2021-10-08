package com.braintreepayments.cardform.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.braintreepayments.cardform.R;
import com.braintreepayments.cardform.utils.CardType;
import com.braintreepayments.cardform.utils.SelectableCardType;

public class SupportedCardTypesAdapter extends RecyclerView.Adapter<SupportedCardTypesAdapter.ViewHolder> {

    private final SelectableCardType[] supportedCardTypes;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.bt_supported_card_icon);
        }

        public ImageView getImageView() {
            return imageView;
        }
    }

    public SupportedCardTypesAdapter(SelectableCardType[] dataSet) {
        supportedCardTypes = dataSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.bt_supported_card_type, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        SelectableCardType selectableCardType = supportedCardTypes[position];
        viewHolder.getImageView().setImageResource(selectableCardType.getCardType().getFrontResource());

        String descripion = selectableCardType.getCardType().toString();

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

    public void setSelected(CardType cardType) {
        for(SelectableCardType selectableCardType : supportedCardTypes) {
            selectableCardType.setDisabled(selectableCardType.getCardType() != cardType);
        }
    }
}
