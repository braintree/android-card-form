package com.braintreepayments.cardform.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.ImageSpan;

import com.braintreepayments.cardform.utils.ViewUtils;

public class PaddedImageSpan extends ImageSpan {

    private int mResourceId;
    private int mPadding;

    public PaddedImageSpan(Context context, int resourceId) {
        super(context, resourceId);
        mResourceId = resourceId;
        mPadding = ViewUtils.dp2px(context, 8);
    }

    int getResourceId() {
        return mResourceId;
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        return super.getSize(paint, text, start, end, fm) + (2 * mPadding);
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom,
                     Paint paint) {
        super.draw(canvas, text, start, end, x + mPadding, top, y, bottom, paint);
    }
}
