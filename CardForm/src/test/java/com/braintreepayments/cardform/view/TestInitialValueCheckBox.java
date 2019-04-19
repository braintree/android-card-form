package com.braintreepayments.cardform.view;

import android.os.Parcelable;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class TestInitialValueCheckBox {
    @Test
    public void setInitiallyChecked_setsChecked() {
        InitialValueCheckBox initialValueCheckBox = new InitialValueCheckBox(RuntimeEnvironment.application);

        initialValueCheckBox.setInitiallyChecked(true);
        assertTrue(initialValueCheckBox.isChecked());

        initialValueCheckBox.setInitiallyChecked(false);
        assertFalse(initialValueCheckBox.isChecked());
    }

    @Test
    public void setInitiallyChecked_whenRestoring_usesConsumerValue() {
        InitialValueCheckBox initialValueCheckBox = new InitialValueCheckBox(RuntimeEnvironment.application);

        initialValueCheckBox.setInitiallyChecked(true);
        assertTrue(initialValueCheckBox.isChecked());

        Parcelable parcelable = initialValueCheckBox.onSaveInstanceState();

        initialValueCheckBox.setInitiallyChecked(false);
        assertFalse(initialValueCheckBox.isChecked());

        initialValueCheckBox.onRestoreInstanceState(parcelable);

        assertTrue(initialValueCheckBox.isChecked());
    }

    @Test
    public void setInitiallyChecked_afterRestore_Noops() {
        InitialValueCheckBox initialValueCheckBox = new InitialValueCheckBox(RuntimeEnvironment.application);

        initialValueCheckBox.setInitiallyChecked(true);
        assertTrue(initialValueCheckBox.isChecked());

        Parcelable parcelable = initialValueCheckBox.onSaveInstanceState();
        initialValueCheckBox.onRestoreInstanceState(parcelable);

        initialValueCheckBox.setInitiallyChecked(false);

        assertTrue(initialValueCheckBox.isChecked());
    }
}
