package com.braintreepayments.cardform.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.braintreepayments.cardform.R;
import com.braintreepayments.cardform.utils.VibrationHelper;
import com.braintreepayments.cardform.utils.ViewUtils;

/**
 * Parent {@link android.widget.EditText} for storing and displaying error states.
 */
public class ErrorEditText extends EditText {

    private Paint mPaint;
    private int mPaddingBottom;
    private int mActiveUnderlineThickness;
    private int mInactiveUnderlineThickness;
    private Animation mErrorAnimator;
    private boolean mError;
    private int mFocusedColor;
    private int mInactiveColor;
    private int mErrorColor;

    public ErrorEditText(Context context) {
        super(context);
        init();
    }

    public ErrorEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ErrorEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaddingBottom = ViewUtils.dp2px(getContext(), 8);
        mActiveUnderlineThickness = ViewUtils.dp2px(getContext(), 2);
        mInactiveUnderlineThickness = ViewUtils.dp2px(getContext(), 1);
        mErrorAnimator = AnimationUtils.loadAnimation(getContext(), R.anim.bt_error_animation);
        mError = false;

        mInactiveColor = getResources().getColor(R.color.bt_light_gray);
        mErrorColor = getResources().getColor(R.color.bt_red);

        TypedValue colorAccentTypedValue = new TypedValue();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getContext().getTheme().resolveAttribute(android.R.attr.colorAccent, colorAccentTypedValue, true);
            mFocusedColor = colorAccentTypedValue.data;
        } else {
            try {
                int colorPrimaryId = getResources().getIdentifier("colorAccent", "attr", getContext().getPackageName());
                getContext().getTheme().resolveAttribute(colorPrimaryId, colorAccentTypedValue, true);
                mFocusedColor = colorAccentTypedValue.data;
            } catch (Exception e) {
                mFocusedColor = 0;
            }

            if (mFocusedColor == 0) {
                mFocusedColor = getResources().getColor(R.color.bt_blue);
            }
        }

        TypedArray themeArray = getContext().getTheme().obtainStyledAttributes(new int[]{android.R.attr.editTextColor});
        try {
            int index = 0;
            int defaultColourValue = 0;
            setTextColor(themeArray.getColor(index, defaultColourValue));
        } catch (RuntimeException ignored) {
            setTextColor(getResources().getColor(R.color.bt_black));
        } finally {
            themeArray.recycle();
        }
    }

    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        setError(false);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if(!focused && !isValid()) {
            setError(true);
        }
    }

    /**
     * Request focus for the next view.
     */
    public View focusNextView() {
        if (getImeActionId() == EditorInfo.IME_ACTION_GO) {
            return null;
        }

        View next = focusSearch(View.FOCUS_DOWN);
        if (next != null && next.requestFocus()) {
            return next;
        }

        return null;
    }

    /**
     * @return the current error state of the {@link android.widget.EditText}
     */
    public boolean isError() {
        return mError;
    }

    /**
     * Controls the error state of this {@link ErrorEditText} and sets a visual indication that the
     * {@link ErrorEditText} contains an error.
     *
     * @param error {@code true} to mark this {@link ErrorEditText} as an error, {@code false} to
     *                          mark it as valid.
     */
    public void setError(boolean error) {
        mError = error;

        if (mErrorAnimator != null && error) {
            startAnimation(mErrorAnimator);
            VibrationHelper.vibrate(getContext(), 10);
        }
    }

    /**
     * Override this method validation logic
     *
     * @return {@code true}
     */
    public boolean isValid() {
        return true;
    }

    /**
     * Check if the {@link ErrorEditText} is valid and set the correct error state and visual
     * indication on it.
     */
    public void validate() {
        if (isValid()) {
            setError(false);
        } else {
            setError(true);
        }
    }

    /**
     * Attempt to close the soft keyboard. Will have no effect if the keyboard is not open.
     */
    public void closeSoftKeyboard() {
        ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(getWindowToken(), 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int endX = getRight();
        int startY = getBottom() - mPaddingBottom - mActiveUnderlineThickness;

        if (mError) {
            mPaint.setColor(mErrorColor);
            canvas.drawRect(0, startY, endX, startY + mActiveUnderlineThickness, mPaint);
        } else if (!isEnabled()) {
            mPaint.setColor(mInactiveColor & 0x00ffffff | 0x44000000);
            canvas.drawRect(0, startY, endX, startY + mInactiveUnderlineThickness, mPaint);
        } else if (hasFocus()) {
            mPaint.setColor(mFocusedColor);
            canvas.drawRect(0, startY, endX, startY + mActiveUnderlineThickness, mPaint);
        } else {
            mPaint.setColor(mInactiveColor & 0x00ffffff | 0x1E000000);
            canvas.drawRect(0, startY, endX, startY + mInactiveUnderlineThickness, mPaint);
        }
    }

    protected int getFocusedColor() {
        return mFocusedColor;
    }

    protected int getInactiveColor() {
        return mInactiveColor;
    }

    protected int getErrorColor() {
        return mErrorColor;
    }
}
