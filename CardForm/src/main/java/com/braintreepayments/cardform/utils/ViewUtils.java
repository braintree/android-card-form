package com.braintreepayments.cardform.utils;

import android.content.Context;
import android.util.TypedValue;

public class ViewUtils {

    public static int dp2px(Context context, float dp) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics()));
    }
}
