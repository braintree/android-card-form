package com.braintreepayments.cardform.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.braintreepayments.cardform.OnCardFormFieldFocusedListener;
import com.braintreepayments.cardform.OnCardFormValidListener;
import com.braintreepayments.cardform.R;
import com.braintreepayments.cardform.test.TestActivity;
import com.braintreepayments.cardform.utils.CardType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.util.ActivityController;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

import static com.braintreepayments.cardform.test.Assertions.assertBitmapsEqual;
import static com.braintreepayments.cardform.test.TestCardNumbers.AMEX;
import static com.braintreepayments.cardform.test.TestCardNumbers.INVALID_VISA;
import static com.braintreepayments.cardform.test.TestCardNumbers.VISA;
import static com.braintreepayments.cardform.test.TextExpirationDate.getValidExpiration;
import static com.ibm.icu.impl.Assert.fail;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
public class CardFormTest {

    private ActivityController<TestActivity> mActivityController;
    private Activity mActivity;
    private CardForm mCardForm;

    @Before
    public void setup() {
        mActivityController = Robolectric.buildActivity(TestActivity.class);
        mActivity = mActivityController.setup().get();
        mCardForm = (CardForm) mActivity.findViewById(android.R.id.custom);
    }

    @Test
    public void visibilityIsGoneBeforeSetRequiredFieldsIsCalled() {
        assertEquals(View.GONE, mCardForm.getVisibility());
    }

    @Test
    public void visibilityIsVisibleAfterSetRequiredFieldsIsCalled() {
        setRequiredFields(true, true, true, true);

        assertEquals(View.VISIBLE, mCardForm.getVisibility());
    }

    @Test
    public void setRequiredFieldsSetsFlagSecure() {
        Window window = mock(Window.class);
        mActivity = spy(mActivity);
        when(mActivity.getWindow()).thenReturn(window);
        mCardForm.setRequiredFields(mActivity, true, true, true, true, "test");

        verify(window).setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
    }

    @Test
    public void cardNumberIsShownIfRequired() {
        setRequiredFields(true, false, false, false);

        assertEquals(View.VISIBLE, mCardForm.findViewById(R.id.bt_card_form_card_number).getVisibility());
        assertEquals(View.GONE, mCardForm.findViewById(R.id.bt_card_form_expiration).getVisibility());
        assertEquals(View.GONE, mCardForm.findViewById(R.id.bt_card_form_cvv).getVisibility());
        assertEquals(View.GONE, mCardForm.findViewById(R.id.bt_card_form_postal_code).getVisibility());
    }

    @Test
    public void expirationIsShownIfRequired() {
        setRequiredFields(false, true, false, false);

        assertEquals(View.GONE, mCardForm.findViewById(R.id.bt_card_form_card_number).getVisibility());
        assertEquals(View.VISIBLE, mCardForm.findViewById(R.id.bt_card_form_expiration).getVisibility());
        assertEquals(View.GONE, mCardForm.findViewById(R.id.bt_card_form_cvv).getVisibility());
        assertEquals(View.GONE, mCardForm.findViewById(R.id.bt_card_form_postal_code).getVisibility());
    }

    @Test
    public void cvvIsShownIfRequired() {
        setRequiredFields(false, false, true, false);

        assertEquals(View.GONE, mCardForm.findViewById(R.id.bt_card_form_card_number).getVisibility());
        assertEquals(View.GONE, mCardForm.findViewById(R.id.bt_card_form_expiration).getVisibility());
        assertEquals(View.VISIBLE, mCardForm.findViewById(R.id.bt_card_form_cvv).getVisibility());
        assertEquals(View.GONE, mCardForm.findViewById(R.id.bt_card_form_postal_code).getVisibility());
    }

    @Test
    public void postalCodeIsShownIfRequired() {
        setRequiredFields(false, false, false, true);

        assertEquals(View.GONE, mCardForm.findViewById(R.id.bt_card_form_card_number).getVisibility());
        assertEquals(View.GONE, mCardForm.findViewById(R.id.bt_card_form_expiration).getVisibility());
        assertEquals(View.GONE, mCardForm.findViewById(R.id.bt_card_form_cvv).getVisibility());
        assertEquals(View.VISIBLE, mCardForm.findViewById(R.id.bt_card_form_postal_code).getVisibility());
    }

    @Test
    public void repeatedCallsToSetRequiredFieldsSetCorrectVisibility() {
        setRequiredFields(false, false, false, false);
        assertEquals(View.GONE, mCardForm.findViewById(R.id.bt_card_form_card_number).getVisibility());
        assertEquals(View.GONE, mCardForm.findViewById(R.id.bt_card_form_expiration).getVisibility());
        assertEquals(View.GONE, mCardForm.findViewById(R.id.bt_card_form_cvv).getVisibility());
        assertEquals(View.GONE, mCardForm.findViewById(R.id.bt_card_form_postal_code).getVisibility());

        setRequiredFields(true, true, true, true);
        assertEquals(View.VISIBLE, mCardForm.findViewById(R.id.bt_card_form_card_number).getVisibility());
        assertEquals(View.VISIBLE, mCardForm.findViewById(R.id.bt_card_form_expiration).getVisibility());
        assertEquals(View.VISIBLE, mCardForm.findViewById(R.id.bt_card_form_cvv).getVisibility());
        assertEquals(View.VISIBLE, mCardForm.findViewById(R.id.bt_card_form_postal_code).getVisibility());
    }

    @Test
    public void setsIMEActionAsGoForExpirationIfCvvAndPostalAreNotPresent() {
        setRequiredFields(true, true, false, false);

        assertEquals(EditorInfo.IME_ACTION_GO,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_expiration)).getImeOptions());
    }

    @Test
    public void setsIMEActionAsGoForCvvIfCvvIsPresentAndPostalIsNot() {
        setRequiredFields(true, true, true, false);

        assertEquals(EditorInfo.IME_ACTION_NEXT,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_expiration)).getImeOptions());
        assertEquals(EditorInfo.IME_ACTION_GO,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_cvv)).getImeOptions());
    }

    @Test
    public void setsIMEActionAsGoForPostalAndNextForExpirationIfCvvIsNotPresent() {
        setRequiredFields(true, true, false, true);

        assertEquals(EditorInfo.IME_ACTION_NEXT,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_expiration)).getImeOptions());
        assertEquals(EditorInfo.IME_ACTION_GO,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_postal_code)).getImeOptions());
    }

    @Test
    public void setsIMEActionAsGoForPostalCodeIfCvvAndPostalArePresent() {
        setRequiredFields(true, true, true, true);

        assertEquals(EditorInfo.IME_ACTION_NEXT,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_expiration)).getImeOptions());
        assertEquals(EditorInfo.IME_ACTION_NEXT,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_cvv)).getImeOptions());
        assertEquals(EditorInfo.IME_ACTION_GO,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_postal_code)).getImeOptions());
    }

    @Test
    public void setsFocusDownIdForCardNumberIfExpirationIsNextField() {
        setRequiredFields(true, true, false, false);

        assertEquals(R.id.bt_card_form_expiration,
                mCardForm.findViewById(R.id.bt_card_form_card_number).getNextFocusDownId());
    }

    @Test
    public void repeatedCallsToSetRequiredFieldsSetsFocusDownIdForCardNumberIfExpirationIsNextField() {
        setRequiredFields(true, false, true, true);
        setRequiredFields(true, true, false, false);

        assertEquals(R.id.bt_card_form_expiration,
                mCardForm.findViewById(R.id.bt_card_form_card_number).getNextFocusDownId());
    }

    @Test
    public void setsFocusDownIdForCardNumberIfCvvIsNextField() {
        setRequiredFields(true, false, true, false);

        assertEquals(R.id.bt_card_form_cvv, mCardForm.findViewById(R.id.bt_card_form_card_number).getNextFocusDownId());
    }

    @Test
    public void repeatedCallsToSetRequiredFieldsSetsFocusDownIdForCardNumberIfCvvIsNextField() {
        setRequiredFields(true, true, true, true);
        setRequiredFields(true, false, true, false);

        assertEquals(R.id.bt_card_form_cvv, mCardForm.findViewById(R.id.bt_card_form_card_number).getNextFocusDownId());
    }

    @Test
    public void setsFocusDownIdForCardNumberIfPostalIsNextField() {
        setRequiredFields(true, false, false, true);

        assertEquals(R.id.bt_card_form_postal_code,
                mCardForm.findViewById(R.id.bt_card_form_card_number).getNextFocusDownId());
    }

    @Test
    public void repeatedCallsToSetRequiredFieldsSetsFocusDownIdForCardNumberIfPostalIsNextField() {
        setRequiredFields(true, true, true, true);
        setRequiredFields(true, false, false, true);

        assertEquals(R.id.bt_card_form_postal_code,
                mCardForm.findViewById(R.id.bt_card_form_card_number).getNextFocusDownId());
    }

    @Test
    public void doesNotSetFocusDownIdForCardNumberIfNoNextField() {
        setRequiredFields(true, false, false, false);

        assertEquals(View.NO_ID, mCardForm.findViewById(R.id.bt_card_form_card_number).getNextFocusDownId());
    }

    @Test
    public void repeatedCallsToSetRequiredFieldsDoesNotSetFocusDownIdForCardNumberIfNoNextField() {
        setRequiredFields(true, true, true, true);
        setRequiredFields(true, false, false, false);

        assertEquals(View.NO_ID, mCardForm.findViewById(R.id.bt_card_form_card_number).getNextFocusDownId());
    }

    @Test
    public void setsFocusDownIdForExpirationIfCvvIsNextField() {
        setRequiredFields(true, true, true, false);

        assertEquals(R.id.bt_card_form_cvv,
                mCardForm.findViewById(R.id.bt_card_form_expiration).getNextFocusDownId());
    }

    @Test
    public void repeatedCallsToSetRequiredFieldsSetsFocusDownIdForExpirationIfCvvIsNextField() {
        setRequiredFields(true, true, false, true);
        setRequiredFields(true, true, true, false);

        assertEquals(R.id.bt_card_form_cvv,
                mCardForm.findViewById(R.id.bt_card_form_expiration).getNextFocusDownId());
    }

    @Test
    public void setsFocusDownIdForExpirationIfPostalIsNextField() {
        setRequiredFields(true, true, false, true);

        assertEquals(R.id.bt_card_form_postal_code,
                mCardForm.findViewById(R.id.bt_card_form_expiration).getNextFocusDownId());
    }

    @Test
    public void repeatedCallsToSetRequiredFieldsSetsFocusDownIdForExpirationIfPostalIsNextField() {
        setRequiredFields(true, true, true, true);
        setRequiredFields(true, true, false, true);

        assertEquals(R.id.bt_card_form_postal_code,
                mCardForm.findViewById(R.id.bt_card_form_expiration).getNextFocusDownId());
    }

    @Test
    public void doesNotSetFocusDownIdForExpirationIfNoNextField() {
        setRequiredFields(true, true, false, false);

        assertEquals(View.NO_ID, mCardForm.findViewById(R.id.bt_card_form_expiration).getNextFocusDownId());
    }

    @Test
    public void repeatedCallsToSetRequiredFieldsDoesNotSetFocusDownIdForExpirationIfNoNextField() {
        setRequiredFields(true, true, true, true);
        setRequiredFields(true, true, false, false);

        assertEquals(View.NO_ID, mCardForm.findViewById(R.id.bt_card_form_expiration).getNextFocusDownId());
    }

    @Test
    public void setsFocusDownIdForCvvIfPostalIsNextField() {
        setRequiredFields(true, true, true, true);

        assertEquals(R.id.bt_card_form_postal_code,
                mCardForm.findViewById(R.id.bt_card_form_cvv).getNextFocusDownId());
    }

    @Test
    public void repeatedCallsToSetRequiredFieldsSetsFocusDownIdForCvvIfPostalIsNextField() {
        setRequiredFields(true, true, true, false);
        setRequiredFields(true, true, true, true);

        assertEquals(R.id.bt_card_form_postal_code,
                mCardForm.findViewById(R.id.bt_card_form_cvv).getNextFocusDownId());
    }

    @Test
    public void doesNotSetFocusDownIdForCvvIfNoNextField() {
        setRequiredFields(true, true, true, false);

        assertEquals(View.NO_ID, mCardForm.findViewById(R.id.bt_card_form_cvv).getNextFocusDownId());
    }

    @Test
    public void repeatedCallsToSetRequiredFieldsDoesNotSetFocusDownIdForCvvIfNoNextField() {
        setRequiredFields(true, true, true, true);
        setRequiredFields(true, true, true, false);

        assertEquals(View.NO_ID, mCardForm.findViewById(R.id.bt_card_form_cvv).getNextFocusDownId());
    }

    @Test
    public void cardNumberAdvancesToExpirationWhenCompleteAndValid() {
        setRequiredFields(true, true, true, true);
        CardEditText card = (CardEditText) mCardForm.findViewById(R.id.bt_card_form_card_number);
        ExpirationDateEditText expiration = (ExpirationDateEditText) mCardForm.findViewById(R.id.bt_card_form_expiration);
        card.requestFocus();
        assertTrue(card.hasFocus());
        assertFalse(expiration.hasFocus());

        setText(card, VISA);

        assertTrue(expiration.hasFocus());
    }

    @Test
    public void cardNumberDoesNotAdvanceWhenCompleteAndInvalid() {
        setRequiredFields(true, true, true, true);
        CardEditText card = (CardEditText) mCardForm.findViewById(R.id.bt_card_form_card_number);
        card.requestFocus();

        setText(card, INVALID_VISA);

        assertTrue(card.isError());
        assertTrue(card.hasFocus());
    }

    @Test
    public void expirationAdvancesToCvvWhenComplete() {
        setRequiredFields(true, true, true, true);
        ExpirationDateEditText expiration = (ExpirationDateEditText) mCardForm.findViewById(R.id.bt_card_form_expiration);
        CvvEditText cvv = (CvvEditText) mCardForm.findViewById(R.id.bt_card_form_cvv);
        expiration.requestFocus();
        assertTrue(expiration.hasFocus());
        assertFalse(cvv.hasFocus());

        setText(expiration, getValidExpiration());

        assertTrue(cvv.hasFocus());
    }

    @Test
    public void expirationDoesNotAdvanceWhenCompleteAndInvalid() {
        setRequiredFields(true, true, true, true);
        ExpirationDateEditText expiration = (ExpirationDateEditText) mCardForm.findViewById(R.id.bt_card_form_expiration);
        expiration.requestFocus();
        assertTrue(expiration.hasFocus());

        setText(expiration, "0812");

        assertTrue(expiration.hasFocus());
    }

    @Test
    public void testCvvAdvancesToPostalWhenComplete() {
        setRequiredFields(true, true, true, true);
        CvvEditText cvv = (CvvEditText) mCardForm.findViewById(R.id.bt_card_form_cvv);
        PostalCodeEditText postalCode = (PostalCodeEditText) mCardForm.findViewById(R.id.bt_card_form_postal_code);
        setText(((CardEditText) mCardForm.findViewById(R.id.bt_card_form_card_number)), VISA);
        cvv.requestFocus();
        assertTrue(cvv.hasFocus());
        assertFalse(postalCode.hasFocus());

        setText(cvv, "123");

        assertTrue(postalCode.hasFocus());
    }

    @Test
    public void testAdvancingDoesNotCrashWhenThereIsNotANextField() {
        setRequiredFields(true, false, false, false);

        setText(((CardEditText) mCardForm.findViewById(R.id.bt_card_form_card_number)), VISA);
    }

    @Test
    public void setEnabledSetsStateCorrectly() {
        setRequiredFields(true, true, true, true);

        assertTrue(mCardForm.findViewById(R.id.bt_card_form_card_number).isEnabled());
        assertTrue(mCardForm.findViewById(R.id.bt_card_form_expiration).isEnabled());
        assertTrue(mCardForm.findViewById(R.id.bt_card_form_cvv).isEnabled());
        assertTrue(mCardForm.findViewById(R.id.bt_card_form_postal_code).isEnabled());

        mCardForm.setEnabled(false);

        assertFalse(mCardForm.findViewById(R.id.bt_card_form_card_number).isEnabled());
        assertFalse(mCardForm.findViewById(R.id.bt_card_form_expiration).isEnabled());
        assertFalse(mCardForm.findViewById(R.id.bt_card_form_cvv).isEnabled());
        assertFalse(mCardForm.findViewById(R.id.bt_card_form_postal_code).isEnabled());
    }

    @Test
    public void isValidOnlyValidatesRequiredFields() {
        setRequiredFields(true, false, false, false);
        assertFalse(mCardForm.isValid());
        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_card_number)), VISA);
        assertTrue(mCardForm.isValid());
        assertFalse(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_card_number)).isError());

        setRequiredFields(false, true, false, false);
        assertFalse(mCardForm.isValid());
        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_expiration)), "1230");
        assertTrue(mCardForm.isValid());
        assertFalse(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_expiration)).isError());

        setRequiredFields(false, false, true, false);
        assertFalse(mCardForm.isValid());
        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_cvv)), "123");
        assertTrue(mCardForm.isValid());
        assertFalse(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_cvv)).isError());

        setRequiredFields(false, false, false, true);
        assertFalse(mCardForm.isValid());
        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_postal_code)), "12345");
        assertTrue(mCardForm.isValid());
        assertFalse(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_postal_code)).isError());
    }

    @Test
    public void validateSetsErrorOnFields() {
        setRequiredFields(true, true, true, true);

        assertFalse(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_card_number)).isError());
        assertFalse(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_expiration)).isError());
        assertFalse(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_cvv)).isError());
        assertFalse(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_postal_code)).isError());

        mCardForm.validate();

        assertTrue(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_card_number)).isError());
        assertTrue(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_expiration)).isError());
        assertTrue(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_cvv)).isError());
        assertTrue(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_postal_code)).isError());
    }

    @Test
    public void getCardNumberReturnsCardNumber() {
        setRequiredFields(true, true, true, true);

        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_card_number)), VISA);

        assertEquals(VISA, mCardForm.getCardNumber());
    }

    @Test
    public void getExpirationMonthReturnsExpirationMonth() {
        setRequiredFields(true, true, true, true);

        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_expiration)), "1230");

        assertEquals("12", mCardForm.getExpirationMonth());
    }

    @Test
    public void getExpirationYearReturnsExpirationYear() {
        setRequiredFields(true, true, true, true);

        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_expiration)), "1230");

        assertEquals("30", mCardForm.getExpirationYear());
    }

    @Test
    public void getCvvReturnsCvv() {
        setRequiredFields(true, true, true, true);

        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_cvv)), "123");

        assertEquals("123", mCardForm.getCvv());
    }

    @Test
    public void getPostalCodeReturnsPostalCode() {
        setRequiredFields(true, true, true, true);

        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_postal_code)), "12345");

        assertEquals("12345", mCardForm.getPostalCode());
    }

    @Test
    public void setCardNumberError() {
        setRequiredFields(true, true, true, true);

        assertFalse(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_card_number)).isError());

        mCardForm.setCardNumberError();

        assertTrue(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_card_number)).isError());
        assertTrue((mCardForm.findViewById(R.id.bt_card_form_card_number)).isFocused());
    }

    @Test
    public void setExpirationError() {
        setRequiredFields(false, true, true, true);

        assertFalse(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_expiration)).isError());

        mCardForm.setExpirationError();

        assertTrue(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_expiration)).isError());
        assertTrue((mCardForm.findViewById(R.id.bt_card_form_expiration)).isFocused());
    }

    @Test
    public void setExpirationErrorDoesNotRequestFocusIfCardNumberIsAlreadyFocused() {
        setRequiredFields(true, true, true, true);

        mCardForm.findViewById(R.id.bt_card_form_card_number).requestFocus();
        mCardForm.setExpirationError();

        assertTrue(mCardForm.findViewById(R.id.bt_card_form_card_number).isFocused());
        assertFalse(mCardForm.findViewById(R.id.bt_card_form_expiration).isFocused());
    }

    @Test
    public void setCvvError() {
        setRequiredFields(false, false, true, true);

        assertFalse(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_cvv)).isError());
        mCardForm.setCvvError();
        assertTrue(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_cvv)).isError());
        assertTrue((mCardForm.findViewById(R.id.bt_card_form_cvv)).isFocused());
    }

    @Test
    public void setCvvErrorDoesNotRequestFocusIfCardNumberOrExpirationIsAlreadyFocused() {
        setRequiredFields(true, true, true, true);

        mCardForm.findViewById(R.id.bt_card_form_card_number).requestFocus();
        mCardForm.setCvvError();
        assertTrue(mCardForm.findViewById(R.id.bt_card_form_card_number).isFocused());
        assertFalse(mCardForm.findViewById(R.id.bt_card_form_cvv).isFocused());

        mCardForm.findViewById(R.id.bt_card_form_expiration).requestFocus();
        mCardForm.setCvvError();
        assertTrue(mCardForm.findViewById(R.id.bt_card_form_expiration).isFocused());
        assertFalse(mCardForm.findViewById(R.id.bt_card_form_cvv).isFocused());
    }

    @Test
    public void setPostalCodeError() {
        setRequiredFields(false, false, false, true);

        assertFalse(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_postal_code)).isError());
        mCardForm.setPostalCodeError();
        assertTrue(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_postal_code)).isError());
        assertTrue((mCardForm.findViewById(R.id.bt_card_form_postal_code)).isFocused());
    }

    @Test
    public void setPostalCodeErrorDoesNotRequestFocusIfCardNumberCvvOrExpirationIsAlreadyFocused() {
        setRequiredFields(true, true, true, true);

        mCardForm.findViewById(R.id.bt_card_form_card_number).requestFocus();
        mCardForm.setPostalCodeError();
        assertTrue(mCardForm.findViewById(R.id.bt_card_form_card_number).isFocused());
        assertFalse(mCardForm.findViewById(R.id.bt_card_form_postal_code).isFocused());

        mCardForm.findViewById(R.id.bt_card_form_expiration).requestFocus();
        mCardForm.setPostalCodeError();
        assertTrue(mCardForm.findViewById(R.id.bt_card_form_expiration).isFocused());
        assertFalse(mCardForm.findViewById(R.id.bt_card_form_postal_code).isFocused());

        mCardForm.findViewById(R.id.bt_card_form_cvv).requestFocus();
        mCardForm.setPostalCodeError();
        assertTrue(mCardForm.findViewById(R.id.bt_card_form_cvv).isFocused());
        assertFalse(mCardForm.findViewById(R.id.bt_card_form_postal_code).isFocused());
    }

    @Test
    public void marksCardNumberAsErrorWhenFocusChangesAndCardNumberFailsValidation() {
        setRequiredFields(true, true, true, true);
        CardEditText card = (CardEditText) mActivity.findViewById(R.id.bt_card_form_card_number);
        card.requestFocus();

        setText(card, "4");
        assertTrue(card.isFocused());
        assertFalse(card.isError());

        mCardForm.findViewById(R.id.bt_card_form_expiration).requestFocus();
        assertFalse(card.isFocused());
        assertTrue(card.isError());
    }

    @Test
    public void marksExpirationAsErrorWhenFocusChangesAndExpirationFailsValidation() {
        setRequiredFields(true, true, true, true);
        ExpirationDateEditText expiration = (ExpirationDateEditText) mActivity.findViewById(R.id.bt_card_form_expiration);
        expiration.requestFocus();

        setText(expiration, "1");
        assertTrue(expiration.isFocused());
        assertFalse(expiration.isError());

        mCardForm.findViewById(R.id.bt_card_form_card_number).requestFocus();
        assertFalse(expiration.isFocused());
        assertTrue(expiration.isError());
    }

    @Test
    public void marksCvvAsErrorWhenFocusChangesAndCvvNotProperLength() {
        setRequiredFields(true, true, true, true);
        CvvEditText cvv = (CvvEditText) mCardForm.findViewById(R.id.bt_card_form_cvv);
        cvv.requestFocus();

        setText(cvv, "1");
        assertTrue(cvv.isFocused());
        assertFalse(cvv.isError());

        mCardForm.findViewById(R.id.bt_card_form_card_number).requestFocus();
        assertFalse(cvv.isFocused());
        assertTrue(cvv.isError());
    }

    @Test
    public void marksCvvAsErrorWhenCardChangesToAmex() {
        setRequiredFields(true, true, true, true);
        CardEditText card = (CardEditText) mCardForm.findViewById(R.id.bt_card_form_card_number);
        CvvEditText cvv = (CvvEditText) mCardForm.findViewById(R.id.bt_card_form_cvv);

        setText(cvv, "111");
        card.requestFocus();
        assertFalse(cvv.isFocused());
        assertFalse(cvv.isError());

        setText(card, AMEX);
        cvv.requestFocus();
        card.requestFocus();
        assertFalse(cvv.isFocused());
        assertTrue(cvv.isError());
    }

    @Test
    public void marksPostalCodeAsErrorWhenFocusChangesAndPostalCodeBlank() {
        setRequiredFields(true, true, true, true);
        PostalCodeEditText postalCode = (PostalCodeEditText) mCardForm.findViewById(R.id.bt_card_form_postal_code);
        postalCode.requestFocus();
        assertTrue(postalCode.isFocused());
        assertFalse(postalCode.isError());

        mCardForm.findViewById(R.id.bt_card_form_card_number).requestFocus();
        assertFalse(postalCode.isFocused());
        assertTrue(postalCode.isError());
    }

    @Test
    public void onCardFormValidListenerOnlyCalledOnValidityChange() {
        setRequiredFields(true, true, true, true);
        final AtomicInteger counter = new AtomicInteger(0);
        mCardForm.setOnCardFormValidListener(new OnCardFormValidListener() {
            @Override
            public void onCardFormValid(boolean valid) {
                counter.getAndIncrement();
            }
        });

        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_card_number)), VISA);
        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_expiration)), "0925");
        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_cvv)), "123");
        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_postal_code)), "12345");

        assertEquals(1, counter.get());

        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_cvv)), "12");

        assertEquals(2, counter.get());
    }

    @Test
    public void onCardTypeChangeListenerIsCalledWhenCardTypeChanges() {
        setRequiredFields(true, false, false, false);
        final AtomicBoolean wasCalled = new AtomicBoolean(false);
        mCardForm.setOnCardTypeChangedListener(new CardEditText.OnCardTypeChangedListener() {
            @Override
            public void onCardTypeChanged(CardType cardType) {
                assertEquals(CardType.VISA, cardType);
                wasCalled.set(true);
            }
        });

        setText((EditText) mCardForm.findViewById(R.id.bt_card_form_card_number), VISA);

        assertTrue(wasCalled.get());
    }

    @Test
    public void onFormFieldFocusedListenerIsCalledWhenFieldIsFocused() {
        setRequiredFields(true, true, true, true);
        final AtomicInteger called = new AtomicInteger(0);
        mCardForm.setOnFormFieldFocusedListener(new OnCardFormFieldFocusedListener() {
            @Override
            public void onCardFormFieldFocused(View field) {
                called.getAndIncrement();
            }
        });

        mCardForm.findViewById(R.id.bt_card_form_card_number).requestFocus();
        mCardForm.findViewById(R.id.bt_card_form_expiration).requestFocus();
        mCardForm.findViewById(R.id.bt_card_form_cvv).requestFocus();
        mCardForm.findViewById(R.id.bt_card_form_postal_code).requestFocus();

        assertEquals(4, called.get());
    }

    @Test
    public void testDoesNotCrashWhenNoListenersAreSet() {
        setRequiredFields(true, true, true, true);

        setText(((CardEditText) (mCardForm.findViewById(R.id.bt_card_form_card_number))), VISA);
    }

    @Test
    public void testHintsAreDisplayed() {
        setRequiredFields(true, true, true, true);

        assertTextHintIs(((EditText) mCardForm.findViewById(R.id.bt_card_form_card_number)),
                R.string.bt_form_hint_card_number);
        assertTextHintIs(((EditText) mCardForm.findViewById(R.id.bt_card_form_expiration)),
                R.string.bt_form_hint_expiration);
        assertTextHintIs(((EditText) mCardForm.findViewById(R.id.bt_card_form_cvv)),
                R.string.bt_form_hint_cvv);
        assertTextHintIs(((EditText) mCardForm.findViewById(R.id.bt_card_form_postal_code)),
                R.string.bt_form_hint_postal_code);
    }

    @Test
    public void correctCardHintsAreDisplayed() {
        setRequiredFields(true, true, true, true);

        CardEditText card = (CardEditText) mCardForm.findViewById(R.id.bt_card_form_card_number);

        assertIconHintIs(card, R.drawable.bt_card_highlighted);

        setText(card, "4");
        assertIconHintIs(card, R.drawable.bt_visa);

        setText(card, "51");
        assertIconHintIs(card, R.drawable.bt_mastercard);

        setText(card, "37");
        assertIconHintIs(card, R.drawable.bt_amex);

        setText(card, "35");
        assertIconHintIs(card, R.drawable.bt_jcb);

        setText(card, "5018");
        assertIconHintIs(card, R.drawable.bt_maestro);

        setText(card, "1234");
        assertIconHintIs(card, R.drawable.bt_card_highlighted);
    }

    @Test
    public void testCvvHintsShowAndDisappearOnClick() {
        setRequiredFields(true, true, true, true);
        CvvEditText cvv = (CvvEditText) mCardForm.findViewById(R.id.bt_card_form_cvv);
        assertFalse(cvv.hasFocus());

        assertIconHintIs(cvv, 0);

        cvv.requestFocus();
        assertIconHintIs(cvv, R.drawable.bt_cvv_highlighted);

        mCardForm.findViewById(R.id.bt_card_form_card_number).requestFocus();
        assertFalse(cvv.hasFocus());
        assertIconHintIs(cvv, 0);
    }

    @Test
    public void valuesAreRestored() {
        setRequiredFields(true, true, true, true);

        setText((EditText) mActivity.findViewById(R.id.bt_card_form_card_number), VISA);
        setText((EditText) mActivity.findViewById(R.id.bt_card_form_expiration), "1220");
        setText((EditText) mActivity.findViewById(R.id.bt_card_form_cvv), "123");
        setText((EditText) mActivity.findViewById(R.id.bt_card_form_postal_code), "12345");

        Bundle bundle = new Bundle();

        mActivityController.saveInstanceState(bundle)
                .pause()
                .stop()
                .destroy();

        mActivityController = Robolectric.buildActivity(TestActivity.class)
                .setup(bundle);
        mActivity = mActivityController.get();

        assertEquals(VISA, ((EditText) mActivity.findViewById(R.id.bt_card_form_card_number)).getText().toString());
        assertEquals("1220", ((EditText) mActivity.findViewById(R.id.bt_card_form_expiration)).getText().toString());
        assertEquals("123", ((EditText) mActivity.findViewById(R.id.bt_card_form_cvv)).getText().toString());
        assertEquals("12345", ((EditText) mActivity.findViewById(R.id.bt_card_form_postal_code)).getText().toString());
    }

    @Test
    public void handleCardIOResponse_doesNothingIfDataIsNull() {
        setRequiredFields(true, true, true, true);

        mCardForm.handleCardIOResponse(null);

        assertEquals("", ((EditText) mActivity.findViewById(R.id.bt_card_form_card_number)).getText().toString());
        assertEquals("", ((EditText) mActivity.findViewById(R.id.bt_card_form_expiration)).getText().toString());
        assertEquals("", ((EditText) mActivity.findViewById(R.id.bt_card_form_cvv)).getText().toString());
        assertEquals("", ((EditText) mActivity.findViewById(R.id.bt_card_form_postal_code)).getText().toString());
    }

    @Test
    public void handleCardIOResponse_doesNothingIfNoCardIOResponseIsPresent() {
        setRequiredFields(true, true, true, true);

        mCardForm.handleCardIOResponse(new Intent());

        assertEquals("", ((EditText) mActivity.findViewById(R.id.bt_card_form_card_number)).getText().toString());
        assertEquals("", ((EditText) mActivity.findViewById(R.id.bt_card_form_expiration)).getText().toString());
        assertEquals("", ((EditText) mActivity.findViewById(R.id.bt_card_form_cvv)).getText().toString());
        assertEquals("", ((EditText) mActivity.findViewById(R.id.bt_card_form_postal_code)).getText().toString());
    }

    @Test
    public void handleCardIOResponse_setsCardNumber() {
        setRequiredFields(true, true, true, true);
        Intent intent = new Intent()
                .putExtra(CardIOActivity.EXTRA_SCAN_RESULT, new CreditCard(VISA, 0, 0, "", "", ""));

        mCardForm.handleCardIOResponse(intent);

        assertEquals(VISA, ((EditText) mActivity.findViewById(R.id.bt_card_form_card_number)).getText().toString());
        assertTrue(mCardForm.findViewById(R.id.bt_card_form_expiration).hasFocus());
    }

    @Test
    public void handleCardIOResponse_doesNotSetCardNumberIfCardNumberNotRequired() {
        setRequiredFields(false, true, true, true);
        Intent intent = new Intent()
                .putExtra(CardIOActivity.EXTRA_SCAN_RESULT, new CreditCard(VISA, 0, 0, "", "", ""));

        mCardForm.handleCardIOResponse(intent);

        assertEquals("", ((EditText) mActivity.findViewById(R.id.bt_card_form_card_number)).getText().toString());
    }

    @Test
    public void handleCardIOResponse_setsExpirationDate() {
        setRequiredFields(true, true, true, true);
        Intent intent = new Intent()
                .putExtra(CardIOActivity.EXTRA_SCAN_RESULT, new CreditCard(VISA, 12, 2020, "", "", ""));

        mCardForm.handleCardIOResponse(intent);

        assertEquals("122020", ((EditText) mActivity.findViewById(R.id.bt_card_form_expiration)).getText().toString());
        assertTrue(mCardForm.findViewById(R.id.bt_card_form_cvv).hasFocus());
    }

    @Test
    public void handleCardIOResponse_doesNotSetExpirationDateIfExpirationDateInvalid() {
        setRequiredFields(true, true, true, true);
        Intent intent = new Intent()
                .putExtra(CardIOActivity.EXTRA_SCAN_RESULT, new CreditCard(VISA, 12, 2000, "", "", ""));

        mCardForm.handleCardIOResponse(intent);

        assertEquals("", ((EditText) mActivity.findViewById(R.id.bt_card_form_expiration)).getText().toString());
        assertTrue(mCardForm.findViewById(R.id.bt_card_form_expiration).hasFocus());
    }

    @Test
    public void handleCardIOResponse_doesNotSetExpirationDateIfExpirationDateNotRequired() {
        setRequiredFields(true, false, true, true);
        Intent intent = new Intent()
                .putExtra(CardIOActivity.EXTRA_SCAN_RESULT, new CreditCard(VISA, 12, 2020, "", "", ""));

        mCardForm.handleCardIOResponse(intent);

        assertEquals("", ((EditText) mActivity.findViewById(R.id.bt_card_form_expiration)).getText().toString());
        assertTrue(mCardForm.findViewById(R.id.bt_card_form_cvv).hasFocus());
    }

    /* helpers */
    private void setRequiredFields(boolean cardNumberRequired, boolean expirationRequired, boolean cvvRequired,
                                   boolean postalCodeRequired) {
        mCardForm.setRequiredFields(mActivity, cardNumberRequired, expirationRequired,
                cvvRequired, postalCodeRequired, "test");
    }

    private void setText(EditText editText, String text) {
        editText.setText("");
        Editable editable = editText.getText();
        for (char c : text.toCharArray()) {
            editable.append(c);
        }
    }

    private static void assertTextHintIs(EditText editText, int resourceId) {
        try {
            Field field = TextView.class.getDeclaredField("mHint");
            field.setAccessible(true);
            assertEquals(RuntimeEnvironment.application.getString(resourceId), (String) field.get(editText));
        } catch (NoSuchFieldException e) {
            fail(e.getMessage());
        } catch (IllegalAccessException e) {
            fail(e.getMessage());
        }
    }

    private static void assertIconHintIs(EditText editText, int resourceId) {
        Drawable expected;
        if (resourceId == 0) {
            expected = null;
        } else {
            expected = RuntimeEnvironment.application.getResources().getDrawable(resourceId);
        }

        Drawable[] drawables = editText.getCompoundDrawables();
        assertBitmapsEqual(drawables[0], null);
        assertBitmapsEqual(drawables[1], null);
        assertBitmapsEqual(drawables[2], expected);
        assertBitmapsEqual(drawables[3], null);
    }
}
