package com.braintreepayments.cardform.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

import com.braintreepayments.cardform.R;

public enum ExpirationDateDialogTheme {

    LIGHT(R.color.bt_black_87, R.color.bt_white_87, R.color.bt_black_38),
    DARK(R.color.bt_white_87, R.color.bt_black_87, R.color.bt_white_38);

    private int mItemTextColor;
    private int mItemInverseTextColor;
    private int mItemDisabledTextColor;

    private Context mContext;

    ExpirationDateDialogTheme(int itemTextColor, int itemInverseTextColor, int itemDisabledTextColor) {
        mItemTextColor = itemTextColor;
        mItemInverseTextColor = itemInverseTextColor;
        mItemDisabledTextColor = itemDisabledTextColor;
    }

    public static ExpirationDateDialogTheme detectTheme(Activity activity) {
        int color = activity.getResources().getColor(R.color.bt_white);
        try {
            Drawable background = activity.getWindow().getDecorView().getRootView().getBackground();
            if (background instanceof ColorDrawable) {
                color = ((ColorDrawable) background).getColor();
            }
        } catch (Exception ignored) {}

        double luminance = (0.2126 * Color.red(color)) + (0.7152 * Color.green(color)) +
                (0.0722 * Color.blue(color));
        ExpirationDateDialogTheme theme;
        if (luminance < 128) {
            theme = ExpirationDateDialogTheme.LIGHT;
        } else {
            theme = ExpirationDateDialogTheme.DARK;
        }

        theme.setContext(activity);
        return theme;
    }

    public int getItemTextColor() {
        return mContext.getResources().getColor(mItemTextColor);
    }

    public int getItemInvertedTextColor() {
        return getColor("textColorPrimaryInverse", mItemInverseTextColor);
    }

    public int getItemDisabledTextColor() {
        return mContext.getResources().getColor(mItemDisabledTextColor);
    }

    public int getSelectedItemBackground() {
        return getColor("colorAccent", R.color.bt_blue);
    }

    private int getColor(String attr, int fallbackColor) {
        TypedValue color = new TypedValue();
        try {
            int colorId = mContext.getResources().getIdentifier(attr, "attr", mContext.getPackageName());
            if (mContext.getTheme().resolveAttribute(colorId, color, true)) {
                return color.data;
            }
        } catch (Exception ignored) {}

        return mContext.getResources().getColor(fallbackColor);
    }

    private void setContext(Context context) {
        mContext = context;
    }
}

