package com.braintreepayments.cardform.view;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import com.braintreepayments.cardform.R;
import com.braintreepayments.cardform.utils.VibrationHelper;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.Nullable;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR1;

/**
 * Parent {@link android.widget.EditText} for storing and displaying error states.
 */
public class ErrorEditText extends TextInputEditText {

    private Animation mErrorAnimator;
    private boolean mError;
    private boolean mOptional;

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
        setupRTL();
    }

    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (lengthBefore != lengthAfter) {
            setError(null);
        }
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if(!focused && !isValid() && !TextUtils.isEmpty(getText())) {
            setError(getErrorMessage());
        }
    }

    /**
     * Sets the hint on the {@link TextInputLayout} if this view is a child of a {@link TextInputLayout}, otherwise
     * sets the hint on this {@link android.widget.EditText}.
     *
     * @param hint The string resource to use as the hint.
     */
    public void setFieldHint(int hint) {
        setFieldHint(getContext().getString(hint));
    }

    /**
     * Sets the hint on the {@link TextInputLayout} if this view is a child of a {@link TextInputLayout}, otherwise
     * sets the hint on this {@link android.widget.EditText}.
     *
     * @param hint The string value to use as the hint.
     */
    public void setFieldHint(String hint) {
        if (getTextInputLayoutParent() != null) {
            getTextInputLayoutParent().setHint(hint);
        } else {
            setHint(hint);
        }
    }

    /**
     * Request focus for the next view.
     */
    @SuppressWarnings("WrongConstant")
    public View focusNextView() {
        if (getImeActionId() == EditorInfo.IME_ACTION_GO) {
            return null;
        }

        View next;
        try {
            next = focusSearch(View.FOCUS_FORWARD);
        } catch (IllegalArgumentException e) {
            // View.FOCUS_FORWARD results in a crash in some versions of Android
            // https://github.com/braintree/braintree_android/issues/20
            next = focusSearch(View.FOCUS_DOWN);
        }
        if (next != null && next.requestFocus()) {
            return next;
        }

        return null;
    }

    /**
     * Set this {@link ErrorEditText} as optional. Optional fields are always valid and show no
     * error message.
     *
     * @param optional {@code true} to set this {@link ErrorEditText} to optional, {@code false}
     *                             to set it to required.
     */
    public void setOptional(boolean optional) {
        mOptional = optional;
    }

    /**
     * @return If this {@link ErrorEditText} is optional or not. See {@link #setOptional(boolean)}.
     */
    public boolean isOptional() {
        return mOptional;
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

        TextInputLayout textInputLayout = getTextInputLayoutParent();
        if (textInputLayout != null) {
            textInputLayout.setErrorEnabled(!TextUtils.isEmpty(errorMessage));
            textInputLayout.setError(errorMessage);
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
        if (isValid() || isOptional()) {
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

    /**
     * @return the {@link TextInputLayout} parent if present, otherwise {@code null}.
     */
    @Nullable
    public TextInputLayout getTextInputLayoutParent() {
        if (getParent() != null && getParent().getParent() instanceof TextInputLayout) {
            return (TextInputLayout) getParent().getParent();
        }

        return null;
    }

    private void setupRTL() {
        if (SDK_INT >= JELLY_BEAN_MR1) {
            if (getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
                setTextDirection(View.TEXT_DIRECTION_LTR);
                setGravity(Gravity.RIGHT);
            }
        }
    }
}
