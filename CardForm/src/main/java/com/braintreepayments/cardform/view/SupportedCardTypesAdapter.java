package com.braintreepayments.cardform.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.braintreepayments.cardform.R;
import com.braintreepayments.cardform.utils.CardType;
import com.braintreepayments.cardform.utils.SelectableCardType;

public class SupportedCardTypesAdapter extends RecyclerView.Adapter<SupportedCardTypesAdapter.ViewHolder> {

    private SelectableCardType[] supportedCardTypes;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            imageView = view.findViewById(R.id.bt_supported_card_icon);
        }

        public ImageView getImageView() {
            return imageView;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public SupportedCardTypesAdapter(SelectableCardType[] dataSet) {
        supportedCardTypes = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.bt_supported_card_type, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        viewHolder.getImageView().setImageResource(supportedCardTypes[position].getCardType().getFrontResource());
        if (supportedCardTypes[position].isDisabled()) {
            viewHolder.getImageView().setImageAlpha(80);
        } else {
            viewHolder.getImageView().setImageAlpha(255);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return supportedCardTypes.length;
    }

    public void setSelected(CardType cardType) {
        for(SelectableCardType selectableCardType : supportedCardTypes) {
            if(selectableCardType.getCardType() != cardType) {
                selectableCardType.setDisabled(true);
            } else {
                selectableCardType.setDisabled(false);
            }
        }
    }
}
