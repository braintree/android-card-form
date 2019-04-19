package com.braintreepayments.cardform.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatCheckBox;

/**
 * Sets an initial CheckBox checked state that is overwritten when restoring this view.
 */
public class InitialValueCheckBox extends AppCompatCheckBox {
    private static final String EXTRA_SUPER_STATE = "com.braintreepayments.cardform.view.InitialValueCheckBox.EXTRA_SUPER_STATE";
    private static final String EXTRA_CHECKED_VALUE = "com.braintreepayments.cardform.view.InitialValueCheckBox.EXTRA_CHECKED_VALUE";

    private boolean mRestored;

    public InitialValueCheckBox(Context context) {
        super(context);
    }

    public InitialValueCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InitialValueCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_SUPER_STATE, superState);
        bundle.putBoolean(EXTRA_CHECKED_VALUE, isChecked());

        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        Bundle bundle = (Bundle)state;

        super.onRestoreInstanceState(bundle.getParcelable(EXTRA_SUPER_STATE));

        setChecked(bundle.getBoolean(EXTRA_CHECKED_VALUE));

        mRestored = true;
    }

    /**
     * Sets the initial value for the CheckBox checked state.
     *
     * @param checked the CheckBox checked state.
     */
    public void setInitiallyChecked(boolean checked) {
        if (!mRestored) {
            setChecked(checked);
        }
    }
}
