package com.braintreepayments.cardform.utils;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

import com.braintreepayments.cardform.R;

public enum ExpirationDateDialogTheme {

    LIGHT(R.color.bt_black_87, R.color.bt_white_87, R.color.bt_black_38),
    DARK(R.color.bt_white_87, R.color.bt_black_87, R.color.bt_white_38);

    private final int mItemTextColor;
    private final int mItemInverseTextColor;
    private final int mItemDisabledTextColor;

    private int mResolvedItemTextColor;
    private int mResolvedItemInverseTextColor;
    private int mResolvedItemDisabledTextColor;
    private int mResolvedSelectedItemBackground;

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

        theme.mResolvedItemTextColor = activity.getResources().getColor(theme.mItemTextColor);
        theme.mResolvedItemInverseTextColor = getColor(activity, "textColorPrimaryInverse", theme.mItemInverseTextColor);
        theme.mResolvedItemDisabledTextColor = activity.getResources().getColor(theme.mItemDisabledTextColor);
        theme.mResolvedSelectedItemBackground = getColor(activity, "colorAccent", R.color.bt_blue);

        return theme;
    }

    public int getItemTextColor() {
        return mResolvedItemTextColor;
    }

    public int getItemInvertedTextColor() {
        return mResolvedItemInverseTextColor;
    }

    public int getItemDisabledTextColor() {
        return mResolvedItemDisabledTextColor;
    }

    public int getSelectedItemBackground() {
        return mResolvedSelectedItemBackground;
    }

    private static int getColor(Activity activity, String attr, int fallbackColor) {
        TypedValue color = new TypedValue();
        try {
            int colorId = activity.getResources().getIdentifier(attr, "attr", activity.getPackageName());
            if (activity.getTheme().resolveAttribute(colorId, color, true)) {
                return color.data;
            }
        } catch (Exception ignored) {}

        return activity.getResources().getColor(fallbackColor);
    }
}

