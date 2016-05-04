package com.braintreepayments.cardform.utils;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.braintreepayments.cardform.R;

import java.util.ArrayList;
import java.util.List;

public class ExpirationDateItemAdapter extends ArrayAdapter<String> {

    private ExpirationDateDialogTheme mTheme;
    private ShapeDrawable mSelectedItemBackground;
    private AdapterView.OnItemClickListener mOnItemClickListener;
    private int mSelectedPosition = -1;
    private List<Integer> mDisabledPositions = new ArrayList<>();

    public ExpirationDateItemAdapter(Context context, int resource) {
        super(context, resource);
    }

    public ExpirationDateItemAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public ExpirationDateItemAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
    }

    public ExpirationDateItemAdapter(Context context, int resource, int textViewResourceId, String[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public ExpirationDateItemAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
    }

    public ExpirationDateItemAdapter(Context context, int resource, int textViewResourceId, List<String> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public ExpirationDateItemAdapter(Context context, ExpirationDateDialogTheme theme, List<String> objects) {
        super(context, R.layout.bt_expiration_date_item, objects);
        mTheme = theme;

        float radius = context.getResources().getDimension(R.dimen.bt_expiration_date_item_selected_background_radius);
        float[] radii = new float[] { radius, radius, radius, radius, radius, radius, radius, radius };
        mSelectedItemBackground = new ShapeDrawable(new RoundRectShape(radii, null, null));
        mSelectedItemBackground.getPaint().setColor(mTheme.getSelectedItemBackground());
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public void setSelected(int position) {
        mSelectedPosition = position;
        notifyDataSetChanged();
    }

    public void setDisabled(List<Integer> disabledPositions) {
        mDisabledPositions = disabledPositions;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setEnabled(true);
        if (mSelectedPosition == position) {
            view.setBackgroundDrawable(mSelectedItemBackground);
            view.setTextColor(mTheme.getItemInvertedTextColor());
        } else {
            view.setBackgroundResource(android.R.color.transparent);

            if (mDisabledPositions.contains(position)) {
                view.setTextColor(mTheme.getItemDisabledTextColor());
                view.setEnabled(false);
            } else {
                view.setTextColor(mTheme.getItemTextColor());
            }
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedPosition = position;
                notifyDataSetChanged();
                VibrationHelper.vibrate(getContext(), 10);

                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(null, v, position, position);
                }
            }
        });

        return view;
    }
}
