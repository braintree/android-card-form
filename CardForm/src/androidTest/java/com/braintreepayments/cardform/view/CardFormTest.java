package com.braintreepayments.cardform.view;

import android.app.Activity;
import android.test.UiThreadTest;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.braintreepayments.cardform.OnCardFormValidListener;
import com.braintreepayments.cardform.R;
import com.braintreepayments.cardform.test.TestActivityTestCase;

import java.util.concurrent.atomic.AtomicInteger;

import static com.braintreepayments.cardform.test.TestCardNumbers.VISA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CardFormTest extends TestActivityTestCase {

    private Activity mMockActivity;
    private Window mMockWindow;
    private CardForm mCardForm;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        mMockWindow = mock(Window.class);
        mMockActivity = mock(Activity.class);
        when(mMockActivity.getWindow()).thenReturn(mMockWindow);
        mCardForm = (CardForm) getActivity().findViewById(android.R.id.custom);
    }

    @UiThreadTest
    public void testVisibilityIsGoneBeforeSetRequiredFieldsIsCalled() {
        assertEquals(View.GONE, mCardForm.getVisibility());
    }

    @UiThreadTest
    public void testVisibilityIsVisibleAfterSetRequiredFieldsIsCalled() {
        setRequiredFields(true, true, true, true);

        assertEquals(View.VISIBLE, mCardForm.getVisibility());
    }

    @UiThreadTest
    public void testSetRequiredFieldsSetsFlagSecure() {
        setRequiredFields(true, true, true, true);

        verify(mMockWindow).setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
    }

    @UiThreadTest
    public void testSetsIMEActionAsGoForExpirationIfCvvAndPostalAreNotPresent() {
        setRequiredFields(true, true, false, false);

        assertEquals(EditorInfo.IME_ACTION_GO,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_expiration)).getImeOptions());
    }

    @UiThreadTest
    public void testSetsIMEActionAsGoForCvvIfCvvIsPresentAndPostalIsNot() {
        setRequiredFields(true, true, true, false);

        assertEquals(EditorInfo.IME_ACTION_NEXT,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_expiration)).getImeOptions());
        assertEquals(EditorInfo.IME_ACTION_GO,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_cvv)).getImeOptions());
    }

    @UiThreadTest
    public void testSetsIMEActionAsGoForPostalAndNextForExpirationIfCvvIsNotPresent() {
        setRequiredFields(true, true, false, true);

        assertEquals(EditorInfo.IME_ACTION_NEXT,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_expiration)).getImeOptions());
        assertEquals(EditorInfo.IME_ACTION_GO,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_postal_code)).getImeOptions());
    }

    @UiThreadTest
    public void testSetsIMEActionAsGoForPostalCodeIfCvvAndPostalArePresent() {
        setRequiredFields(true, true, true, true);

        assertEquals(EditorInfo.IME_ACTION_NEXT,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_expiration)).getImeOptions());
        assertEquals(EditorInfo.IME_ACTION_NEXT,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_cvv)).getImeOptions());
        assertEquals(EditorInfo.IME_ACTION_GO,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_postal_code)).getImeOptions());
    }

    @UiThreadTest
    public void testSetEnabledSetsStateCorrectly() {
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

    @UiThreadTest
    public void testIsValidOnlyValidatesRequiredFields() {
        setRequiredFields(true, false, false, false);
        assertFalse(mCardForm.isValid());
        ((EditText) mCardForm.findViewById(R.id.bt_card_form_card_number)).setText(VISA);
        assertTrue(mCardForm.isValid());
        assertFalse(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_card_number)).isError());

        setRequiredFields(false, true, false, false);
        assertFalse(mCardForm.isValid());
        ((EditText) mCardForm.findViewById(R.id.bt_card_form_expiration)).setText("1230");
        assertTrue(mCardForm.isValid());
        assertFalse(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_expiration)).isError());

        setRequiredFields(false, false, true, false);
        assertFalse(mCardForm.isValid());
        ((EditText) mCardForm.findViewById(R.id.bt_card_form_cvv)).setText("123");
        assertTrue(mCardForm.isValid());
        assertFalse(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_cvv)).isError());

        setRequiredFields(false, false, false, true);
        assertFalse(mCardForm.isValid());
        ((EditText) mCardForm.findViewById(R.id.bt_card_form_postal_code)).setText("12345");
        assertTrue(mCardForm.isValid());
        assertFalse(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_postal_code)).isError());
    }

    @UiThreadTest
    public void testValidateSetsErrorOnFields() {
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

    @UiThreadTest
    public void testGetCardNumberReturnsCardNumber() {
        setRequiredFields(true, true, true, true);

        ((EditText) mCardForm.findViewById(R.id.bt_card_form_card_number)).setText(VISA);

        assertEquals(VISA, mCardForm.getCardNumber());
    }

    @UiThreadTest
    public void testGetExpirationMonthReturnsExpirationMonth() {
        setRequiredFields(true, true, true, true);

        ((EditText) mCardForm.findViewById(R.id.bt_card_form_expiration)).setText("1230");

        assertEquals("12", mCardForm.getExpirationMonth());
    }

    @UiThreadTest
    public void testGetExpirationYearReturnsExpirationYear() {
        setRequiredFields(true, true, true, true);

        ((EditText) mCardForm.findViewById(R.id.bt_card_form_expiration)).setText("1230");

        assertEquals("30", mCardForm.getExpirationYear());
    }

    @UiThreadTest
    public void testGetCvvReturnsCvv() {
        setRequiredFields(true, true, true, true);

        ((EditText) mCardForm.findViewById(R.id.bt_card_form_cvv)).setText("123");

        assertEquals("123", mCardForm.getCvv());
    }

    @UiThreadTest
    public void testGetPostalCodeReturnsPostalCode() {
        setRequiredFields(true, true, true, true);

        ((EditText) mCardForm.findViewById(R.id.bt_card_form_postal_code)).setText("12345");

        assertEquals("12345", mCardForm.getPostalCode());
    }

    @UiThreadTest
    public void testSetCardNumberError() {
        setRequiredFields(true, true, true, true);

        assertFalse(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_card_number)).isError());

        mCardForm.setCardNumberError();

        assertTrue(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_card_number)).isError());
        assertTrue((mCardForm.findViewById(R.id.bt_card_form_card_number)).isFocused());
    }

    @UiThreadTest
    public void testSetExpirationError() {
        setRequiredFields(false, true, true, true);

        assertFalse(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_expiration)).isError());

        mCardForm.setExpirationError();

        assertTrue(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_expiration)).isError());
        assertTrue((mCardForm.findViewById(R.id.bt_card_form_expiration)).isFocused());
    }

    @UiThreadTest
    public void testSetExpirationErrorDoesNotRequestFocusIfCardNumberIsAlreadyFocused() {
        setRequiredFields(true, true, true, true);

        mCardForm.findViewById(R.id.bt_card_form_card_number).requestFocus();
        mCardForm.setExpirationError();

        assertTrue(mCardForm.findViewById(R.id.bt_card_form_card_number).isFocused());
        assertFalse(mCardForm.findViewById(R.id.bt_card_form_expiration).isFocused());
    }

    @UiThreadTest
    public void testSetCvvError() {
        setRequiredFields(false, false, true, true);

        assertFalse(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_cvv)).isError());
        mCardForm.setCvvError();
        assertTrue(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_cvv)).isError());
        assertTrue((mCardForm.findViewById(R.id.bt_card_form_cvv)).isFocused());
    }

    @UiThreadTest
    public void testSetCvvErrorDoesNotRequestFocusIfCardNumberOrExpirationIsAlreadyFocused() {
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

    @UiThreadTest
    public void testSetPostalCodeError() {
        setRequiredFields(false, false, false, true);

        assertFalse(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_postal_code)).isError());
        mCardForm.setPostalCodeError();
        assertTrue(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_postal_code)).isError());
        assertTrue((mCardForm.findViewById(R.id.bt_card_form_postal_code)).isFocused());
    }

    @UiThreadTest
    public void testSetPostalCodeErrorDoesNotRequestFocusIfCardNumberCvvOrExpirationIsAlreadyFocused() {
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

    @UiThreadTest
    public void testOnCardFormValidListenerOnlyCalledOnValidityChange() {
        setRequiredFields(true, true, true, true);
        final AtomicInteger counter = new AtomicInteger(0);
        mCardForm.setOnCardFormValidListener(new OnCardFormValidListener() {
            @Override
            public void onCardFormValid(boolean valid) {
                counter.getAndIncrement();
            }
        });

        ((EditText) mCardForm.findViewById(R.id.bt_card_form_card_number)).setText(VISA);
        ((EditText) mCardForm.findViewById(R.id.bt_card_form_expiration)).setText("0925");
        ((EditText) mCardForm.findViewById(R.id.bt_card_form_cvv)).setText("123");
        ((EditText) mCardForm.findViewById(R.id.bt_card_form_postal_code)).setText("12345");

        assertEquals(1, counter.get());

        ((EditText) mCardForm.findViewById(R.id.bt_card_form_cvv)).setText("12");

        assertEquals(2, counter.get());
    }

    /* helpers */
    private void setRequiredFields(boolean cardNumberRequired, boolean expirationRequired,
                                   boolean cvvRequired, boolean postalCodeRequired) {
        mCardForm.setRequiredFields(mMockActivity, cardNumberRequired, expirationRequired,
                cvvRequired, postalCodeRequired, "test");
    }
}
