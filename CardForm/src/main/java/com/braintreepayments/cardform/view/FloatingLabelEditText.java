package com.braintreepayments.cardform.view;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Looper;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH;
import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR1;

/**
 * {@link android.widget.EditText} for displaying floating hints when text has been entered.
 */
public class FloatingLabelEditText extends ErrorEditText {

    private static final int ANIMATION_DURATION_MILLIS = 300;

    private TextPaint mHintPaint = new TextPaint();
    private ValueAnimator mHintAnimator;
    private ValueAnimator mFocusColorAnimator;
    private ValueAnimator mInactiveColorAnimator;
    private ValueAnimator mAlphaAnimator;

    private float mAnimatedHintHeight = -1;
    private int mAnimatedHintColor;
    private int mHintAlpha;

    private int mPreviousTextLength;

    protected boolean mRightToLeftLanguage;

    public FloatingLabelEditText(Context context) {
        super(context);
        init();
    }

    public FloatingLabelEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FloatingLabelEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mRightToLeftLanguage = isRightToLeftLanguage();
        mPreviousTextLength = getText().length();

        final float textSize = getTextSize();
        mHintAnimator = ValueAnimator.ofFloat(textSize * 1.75f, textSize);
        mHintAnimator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimatedHintHeight = (Float) animation.getAnimatedValue();
                invalidate();
            }
        });
        mHintAnimator.setDuration(ANIMATION_DURATION_MILLIS);

        AnimatorUpdateListener animatorUpdateListener = new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimatedHintColor = (Integer) animation.getAnimatedValue();
                invalidate();
            }
        };

        mFocusColorAnimator = ValueAnimator.ofInt(getInactiveColor(), getPrimaryColor());
        mFocusColorAnimator.setEvaluator(new ArgbEvaluator());
        mFocusColorAnimator.addUpdateListener(animatorUpdateListener);
        mFocusColorAnimator.setDuration(ANIMATION_DURATION_MILLIS);

        mInactiveColorAnimator = ValueAnimator.ofInt(getPrimaryColor(), getInactiveColor());
        mInactiveColorAnimator.setEvaluator(new ArgbEvaluator());
        mInactiveColorAnimator.addUpdateListener(animatorUpdateListener);
        mInactiveColorAnimator.setDuration(ANIMATION_DURATION_MILLIS);

        mAlphaAnimator = ValueAnimator.ofInt(0, 255);
        mAlphaAnimator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mHintAlpha = (Integer) animation.getAnimatedValue();
                invalidate();
            }
        });
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (Looper.myLooper() != null) {
            if (focused) {
                mFocusColorAnimator.start();
            } else {
                mInactiveColorAnimator.start();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (getText().length() > 0) {
            mHintPaint.setColor(mAnimatedHintColor);
            mHintPaint.setTextSize(getPaint().getTextSize() * 3 / 4);
            mHintPaint.setAlpha(mHintAlpha);

            canvas.drawText(getHint().toString(), 0, mAnimatedHintHeight, mHintPaint);
        }
    }

    @Override
    @TargetApi(ICE_CREAM_SANDWICH)
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (SDK_INT >= ICE_CREAM_SANDWICH && Looper.myLooper() != null) {
            if (mPreviousTextLength == 0 && text.length() > 0 && !mHintAnimator.isStarted()) {
                mHintAnimator.start();
                mFocusColorAnimator.start();
                mAlphaAnimator.start();
            }
        }
        mPreviousTextLength = text.length();
    }

    @TargetApi(JELLY_BEAN_MR1)
    private boolean isRightToLeftLanguage() {
        if (SDK_INT >= JELLY_BEAN_MR1) {
            if(getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
                return true;
            }
        }
        return false;
    }
}
