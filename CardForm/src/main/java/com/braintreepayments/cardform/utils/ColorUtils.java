package com.braintreepayments.cardform.utils;

import android.app.Activity;
import android.util.TypedValue;

public class ColorUtils {

    public static int getColor(Activity activity, String attr, int fallbackColor) {
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
