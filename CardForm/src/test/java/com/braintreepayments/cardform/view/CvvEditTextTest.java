package com.braintreepayments.cardform.view;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertSame;

import android.text.Editable;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;

import com.braintreepayments.api.CardType;
import com.braintreepayments.cardform.R;
import com.braintreepayments.cardform.test.TestActivity;
import com.braintreepayments.cardform.utils.CardDescriptor;
import com.braintreepayments.cardform.utils.CardParser;
import com.google.android.material.textfield.TextInputLayout;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

@RunWith(RobolectricTestRunner.class)
public class CvvEditTextTest {

    private CvvEditText mView;
    private CardParser cardParser = new CardParser();

    @Before
    public void setup() {
        mView = Robolectric.setupActivity(TestActivity.class)
                .findViewById(R.id.bt_card_form_cvv);
    }

    @Test
    public void defaultLimitIs3() {
        type("123").assertTextIs("123");
        type("4").assertTextIs("123");
    }

    @Test
    public void customLimits() {
        for (CardType type : CardType.values()) {
            mView.setCardType(type);
            if (type == CardType.AMEX) {
                type("1234").assertTextIs("1234");
                type("5").assertTextIs("1234");
            } else {
                type("123").assertTextIs("123");
                type("4").assertTextIs("123");
            }
            mView.getText().clear();
        }
    }

    @Test
    public void hintDefaultsToCvv() {
        assertEquals(((TextInputLayout) mView.getParent().getParent()).getHint(),
                RuntimeEnvironment.application.getString(R.string.bt_cvv));
    }

    @Test
    public void hintChangesForCardType() {
        for (CardType cardType : CardType.values()) {
            mView.setCardType(cardType);

            CardDescriptor descriptor = cardParser.getDescriptor(cardType);
            assertEquals(RuntimeEnvironment.application.getString(descriptor.getSecurityCodeName()),
                    mView.getTextInputLayoutParent().getHint());
        }
    }

    @Test
    public void onInit_setsInputTypeToNumberAndKeepsDefaultTransformationMethod() {
        assertEquals(InputType.TYPE_CLASS_NUMBER, mView.getInputType());
        assertSame(mView.getTransformationMethod(), SingleLineTransformationMethod.getInstance());
    }

    @Test
    public void setMaskToTrue_appliesPasswordTransformationMethod() {
        mView.setMask(true);
        assertSame(mView.getTransformationMethod(), PasswordTransformationMethod.getInstance());
    }

    @Test
    public void setMaskToFalse_doesNotApplyPasswordTransformationMethod() {
        mView.setMask(false);
        assertSame(mView.getTransformationMethod(), SingleLineTransformationMethod.getInstance());
    }

    @Test
    public void isValid_returnsFalseForInvalidCvv() {
        assertFalse(mView.isValid());
    }

    @Test
    public void isValid_returnsTrueForValidCvv() {
        type("123");

        assertTrue(mView.isValid());
    }

    @Test
    public void isValid_returnsTrueForInvalidCvvWhenOptional() {
        mView.setOptional(true);

        assertTrue(mView.isValid());
    }

    @Test
    public void getErrorMessage_returnsErrorMessageForNoCardTypeWhenEmpty() {
        String expectedMessage = RuntimeEnvironment.application.getString(R.string.bt_cvv_required,
                RuntimeEnvironment.application.getString(R.string.bt_cvv));

        assertEquals(expectedMessage, mView.getErrorMessage());
    }

    @Test
    public void getErrorMessage_returnsCorrectErrorMessageForCardTypeWhenEmpty() {
        for (CardType cardType : CardType.values()) {
            mView.setCardType(cardType);

            CardDescriptor descriptor = cardParser.getDescriptor(cardType);
            String expectedMessage = RuntimeEnvironment.application.getString(R.string.bt_cvv_required,
                    RuntimeEnvironment.application.getString(descriptor.getSecurityCodeName()));
            assertEquals(expectedMessage, mView.getErrorMessage());
        }
    }

    @Test
    public void getErrorMessage_returnsErrorMessageForNoCardTypeWhenNotEmpty() {
        type("4");
        String expectedMessage = RuntimeEnvironment.application.getString(R.string.bt_cvv_invalid,
                RuntimeEnvironment.application.getString(R.string.bt_cvv));

        assertEquals(expectedMessage, mView.getErrorMessage());
    }

    @Test
    public void getErrorMessage_returnsCorrectErrorMessageForCardTypeWhenNotEmpty() {
        type("4");

        for (CardType cardType : CardType.values()) {
            mView.setCardType(cardType);

            CardDescriptor descriptor = cardParser.getDescriptor(cardType);
            String expectedMessage = RuntimeEnvironment.application.getString(R.string.bt_cvv_invalid,
                    RuntimeEnvironment.application.getString(descriptor.getSecurityCodeName()));
            assertEquals(expectedMessage, mView.getErrorMessage());
        }
    }

    private CvvEditTextTest type(String text) {
        Editable editable = mView.getText();
        for (char c : text.toCharArray()) {
            editable.append(c);
        }
        return this;
    }

    private void assertTextIs(String expected) {
        assertEquals(expected, mView.getText().toString());
    }
}
