package com.braintreepayments.cardform.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import com.braintreepayments.cardform.R;
import com.braintreepayments.cardform.utils.VibrationHelper;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR1;

/**
 * Parent {@link android.widget.EditText} for storing and displaying error states.
 */
public class ErrorEditText extends TextInputEditText {

    private Animation mErrorAnimator;
    private boolean mError;

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
        mErrorAnimator = AnimationUtils.loadAnimation(getContext(), R.anim.bt_error_animation);
        mError = false;
    }

    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        setError(null);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if(!focused && !isValid() && !TextUtils.isEmpty(getText())) {
            setError(getErrorMessage());
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
     * @param errorMessage the error message to display to the user. {@code null} will remove any error message displayed.
     */
    public void setError(@Nullable String errorMessage) {
        mError = !TextUtils.isEmpty(errorMessage);

        if (getParent() instanceof TextInputLayout) {
            ((TextInputLayout) getParent()).setError(errorMessage);
        }

        if (mErrorAnimator != null && mError) {
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
     * Override this method to display error messages
     *
     * @return {@link String} error message to display.
     */
    public String getErrorMessage() {
        return null;
    }

    /**
     * Check if the {@link ErrorEditText} is valid and set the correct error state and visual
     * indication on it.
     */
    public void validate() {
        if (isValid()) {
            setError(null);
        } else {
            setError(getErrorMessage());
        }
    }

    /**
     * Attempt to close the soft keyboard. Will have no effect if the keyboard is not open.
     */
    public void closeSoftKeyboard() {
        ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(getWindowToken(), 0);
    }

    @TargetApi(JELLY_BEAN_MR1)
    protected boolean isRightToLeftLanguage() {
        if (SDK_INT >= JELLY_BEAN_MR1) {
            if (getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
                return true;
            }
        }
        return false;
    }
}
