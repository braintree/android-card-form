package com.braintreepayments.cardform.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
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
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.braintreepayments.cardform.test.Assertions.assertIconHintIs;
import static com.braintreepayments.cardform.test.Assertions.assertNoHintIcon;
import static com.braintreepayments.cardform.test.ColorTestUtils.getColor;
import static com.braintreepayments.cardform.test.TestCardNumbers.AMEX;
import static com.braintreepayments.cardform.test.TestCardNumbers.INVALID_VISA;
import static com.braintreepayments.cardform.test.TestCardNumbers.VISA;
import static com.braintreepayments.cardform.test.TestExpirationDate.getValidExpiration;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class CardFormTest {

    private ActivityController<TestActivity> mActivityController;
    private Activity mActivity;
    private CardForm mCardForm;

    @Before
    public void setup() {
        mActivityController = Robolectric.buildActivity(TestActivity.class);
        mActivity = mActivityController.setup().get();
        mCardForm = mActivity.findViewById(android.R.id.custom);
    }

    @Test
    public void getCardNumberEditText() {
        assertEquals(mCardForm.findViewById(R.id.bt_card_form_card_number), mCardForm.getCardEditText());
    }

    @Test
    public void getExpirationDateEditText() {
        assertEquals(mCardForm.findViewById(R.id.bt_card_form_expiration), mCardForm.getExpirationDateEditText());
    }

    @Test
    public void getCvvEditText() {
        assertEquals(mCardForm.findViewById(R.id.bt_card_form_cvv), mCardForm.getCvvEditText());
    }

    @Test
    public void getPostalCodeEditText() {
        assertEquals(mCardForm.findViewById(R.id.bt_card_form_postal_code), mCardForm.getPostalCodeEditText());
    }

    @Test
    public void getCountryCodeEditText() {
        assertEquals(mCardForm.findViewById(R.id.bt_card_form_country_code), mCardForm.getCountryCodeEditText());
    }

    @Test
    public void getMobileNumberEditText() {
        assertEquals(mCardForm.findViewById(R.id.bt_card_form_mobile_number), mCardForm.getMobileNumberEditText());
    }

    @Test
    public void visibilityIsGoneBeforeSetupIsCalled() {
        assertEquals(GONE, mCardForm.getVisibility());
    }

    @Test
    public void visibilityIsVisibleAfterSetupIsCalled() {
        setRequiredFields(true, true, true, true, true);

        assertEquals(VISIBLE, mCardForm.getVisibility());
    }

    @Test
    public void setup_setsFlagSecure() {
        Window window = mock(Window.class);
        mActivity = spy(mActivity);
        when(mActivity.getWindow()).thenReturn(window);

        mCardForm.setup(mActivity);

        verify(window).setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
    }

    @Test
    public void setup_setsIconsForDarkBackgrounds() {
        setActivityBackground(R.color.bt_black);
        setRequiredFields(true, true, true, true, true);

        assertDrawableIsFromResource(R.id.bt_card_form_card_number_icon, R.drawable.bt_ic_card_dark);
        assertDrawableIsFromResource(R.id.bt_card_form_postal_code_icon, R.drawable.bt_ic_postal_code_dark);
        assertDrawableIsFromResource(R.id.bt_card_form_mobile_number_icon, R.drawable.bt_ic_mobile_number_dark);
    }

    @Test
    public void setup_setsIconsForLightBackgrounds() {
        setActivityBackground(R.color.bt_white);
        setRequiredFields(true, true, true, true, true);

        assertDrawableIsFromResource(R.id.bt_card_form_card_number_icon, R.drawable.bt_ic_card);
        assertDrawableIsFromResource(R.id.bt_card_form_postal_code_icon, R.drawable.bt_ic_postal_code);
        assertDrawableIsFromResource(R.id.bt_card_form_mobile_number_icon, R.drawable.bt_ic_mobile_number);
    }

    @Test
    public void setCardNumberIcon_withRes_overridesDefaultIcon() {
        setRequiredFields(true, true, true, true, true);

        mCardForm.setCardNumberIcon(R.drawable.bt_ic_amex);
        assertDrawableIsFromResource(R.id.bt_card_form_card_number_icon, R.drawable.bt_ic_amex);
    }

    @Test
    public void setPostalCodeIcon_withRes_overridesDefaultIcon() {
        setRequiredFields(true, true, true, true, true);

        mCardForm.setPostalCodeIcon(R.drawable.bt_ic_amex);
        assertDrawableIsFromResource(R.id.bt_card_form_postal_code_icon, R.drawable.bt_ic_amex);
    }

    @Test
    public void setMobileNumberIcon_withRes_overridesDefaultIcon() {
        setRequiredFields(true, true, true, true, true);

        mCardForm.setMobileNumberIcon(R.drawable.bt_ic_amex);
        assertDrawableIsFromResource(R.id.bt_card_form_mobile_number_icon, R.drawable.bt_ic_amex);
    }

    @Test
    public void cardNumberIsShownIfRequired() {
        setRequiredFields(true, false, false, false, false);
        assertFieldsVisible(VISIBLE, GONE, GONE, GONE, GONE);
    }

    @Test
    public void expirationIsShownIfRequired() {
        setRequiredFields(false, true, false, false, false);
        assertFieldsVisible(GONE, VISIBLE, GONE, GONE, GONE);
    }

    @Test
    public void cvvIsShownIfRequired() {
        setRequiredFields(false, false, true, false, false);
        assertFieldsVisible(GONE, GONE, VISIBLE, GONE, GONE);
    }

    @Test
    public void postalCodeIsShownIfRequired() {
        setRequiredFields(false, false, false, true, false);
        assertFieldsVisible(GONE, GONE, GONE, VISIBLE, GONE);
    }

    @Test
    public void countryCodeMobileNumberAndMobileNumberExplanationAreShownIfRequired() {
        setRequiredFields(false, false, false, false, true);
        assertFieldsVisible(GONE, GONE, GONE, GONE, VISIBLE);
        assertEquals("Make sure SMS is supported", ((TextView)mCardForm.findViewById(R.id.bt_card_form_mobile_number_explanation)).getText());
    }

    @Test
    public void mobileNumberExplanationNotShownIfMobileNumberNotRequired() {
        setRequiredFields(false, false, false, false, false);
        assertFieldsVisible(GONE, GONE, GONE, GONE ,GONE);
    }

    @Test
    public void repeatedCallsToSetupSetCorrectVisibility() {
        setRequiredFields(false, false, false, false, false);
        assertFieldsVisible(GONE, GONE, GONE, GONE, GONE);

        setRequiredFields(true, true, true, true, true);
        assertFieldsVisible(VISIBLE, VISIBLE, VISIBLE, VISIBLE, VISIBLE);
    }

    @Test
    public void setsIMEActionAsGoForCardNumberIfNoOtherFieldsAreRequired() {
        setRequiredFields(true, false, false, false, false);

        assertEquals(EditorInfo.IME_ACTION_GO,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_card_number)).getImeOptions());
    }

    @Test
    public void repeatedCallsToSetupSetsIMEActionAsGoForCardNumberIfNoOtherFieldsAreRequired() {
        setRequiredFields(true, true, false, false, false);
        setRequiredFields(true, false, false, false, false);

        assertEquals(EditorInfo.IME_ACTION_GO,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_card_number)).getImeOptions());
    }

    @Test
    public void setsIMEActionAsNextForCardNumberIfAnyOtherFieldIsRequired() {
        setRequiredFields(true, true, false, false, false);
        assertEquals(EditorInfo.IME_ACTION_NEXT,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_card_number)).getImeOptions());

        setRequiredFields(true, false, true, false, false);
        assertEquals(EditorInfo.IME_ACTION_NEXT,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_card_number)).getImeOptions());

        setRequiredFields(true, false, false, true, false);
        assertEquals(EditorInfo.IME_ACTION_NEXT,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_card_number)).getImeOptions());

        setRequiredFields(true, false, false, false, true);
        assertEquals(EditorInfo.IME_ACTION_NEXT,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_card_number)).getImeOptions());
    }

    @Test
    public void setsIMEActionAsGoForExpirationIfNoOtherFieldsRequired() {
        setRequiredFields(true, true, false, false, false);

        assertEquals(EditorInfo.IME_ACTION_GO,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_expiration)).getImeOptions());
    }

    @Test
    public void repeatedCallsToSetupSetsIMEActionAsGoForExpirationIfNoOtherFieldsRequired() {
        setRequiredFields(true, true, true, false, false);
        setRequiredFields(true, true, false, false, false);

        assertEquals(EditorInfo.IME_ACTION_GO,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_expiration)).getImeOptions());
    }

    @Test
    public void setsIMEActionAsGoForCvvIfNoOtherFieldsAreRequired() {
        setRequiredFields(true, true, true, false, false);

        assertEquals(EditorInfo.IME_ACTION_NEXT,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_expiration)).getImeOptions());
        assertEquals(EditorInfo.IME_ACTION_GO,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_cvv)).getImeOptions());
    }

    @Test
    public void repeatedCallsToSetupSetsIMEActionAsGoForCvvIfNoOtherFieldsAreRequired() {
        setRequiredFields(true, true, true, true, false);
        setRequiredFields(true, true, true, false, false);

        assertEquals(EditorInfo.IME_ACTION_NEXT,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_expiration)).getImeOptions());
        assertEquals(EditorInfo.IME_ACTION_GO,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_cvv)).getImeOptions());
    }

    @Test
    public void setsIMEActionAsGoForPostalAndNextForExpirationIfCvvIsNotPresent() {
        setRequiredFields(true, true, false, true, false);

        assertEquals(EditorInfo.IME_ACTION_NEXT,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_expiration)).getImeOptions());
        assertEquals(EditorInfo.IME_ACTION_GO,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_postal_code)).getImeOptions());
    }

    @Test
    public void repeatedCallsToSetupSetsIMEActionAsGoForPostalAndNextForExpirationIfCvvIsNotPresent() {
        setRequiredFields(true, true, true, true, false);
        setRequiredFields(true, true, false, true, false);

        assertEquals(EditorInfo.IME_ACTION_NEXT,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_expiration)).getImeOptions());
        assertEquals(EditorInfo.IME_ACTION_GO,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_postal_code)).getImeOptions());
    }

    @Test
    public void setsIMEActionAsGoForPostalCodeIfCvvAndPostalArePresent() {
        setRequiredFields(true, true, true, true, false);

        assertEquals(EditorInfo.IME_ACTION_NEXT,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_expiration)).getImeOptions());
        assertEquals(EditorInfo.IME_ACTION_NEXT,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_cvv)).getImeOptions());
        assertEquals(EditorInfo.IME_ACTION_GO,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_postal_code)).getImeOptions());
    }

    @Test
    public void repeatedCallsToSetupSetsIMEActionAsGoForPostalCodeIfCvvAndPostalArePresent() {
        setRequiredFields(true, true, false, false, false);
        setRequiredFields(true, true, true, true, false);

        assertEquals(EditorInfo.IME_ACTION_NEXT,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_expiration)).getImeOptions());
        assertEquals(EditorInfo.IME_ACTION_NEXT,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_cvv)).getImeOptions());
        assertEquals(EditorInfo.IME_ACTION_GO,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_postal_code)).getImeOptions());
    }

    @Test
    public void setsIMEActionAsNextForCountryCode() {
        setRequiredFields(true, true, true, true, true);

        assertEquals(EditorInfo.IME_ACTION_NEXT,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_country_code)).getImeOptions());
    }

    @Test
    public void setsIMEActionAsGoForMobileNumber() {
        setRequiredFields(true, true, true, true, true);

        assertEquals(EditorInfo.IME_ACTION_GO,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_mobile_number)).getImeOptions());
    }

    @Test
    public void repeatedCallsToSetupSetsIMEActionAsGoForMobileNumber() {
        setRequiredFields(true, true, true, true, false);
        setRequiredFields(true, true, true, true, true);

        assertEquals(EditorInfo.IME_ACTION_NEXT,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_postal_code)).getImeOptions());
        assertEquals(EditorInfo.IME_ACTION_GO,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_mobile_number)).getImeOptions());
    }

    @Test
    public void cardNumberAdvancesToExpirationWhenCompleteAndValid() {
        setRequiredFields(true, true, false, false, false);
        CardEditText card = mCardForm.findViewById(R.id.bt_card_form_card_number);
        ExpirationDateEditText expiration = mCardForm.findViewById(R.id.bt_card_form_expiration);
        card.requestFocus();
        assertTrue(card.hasFocus());
        assertFalse(expiration.hasFocus());

        setText(card, VISA);

        assertTrue(expiration.hasFocus());
    }

    @Test
    public void cardNumberDoesNotAdvanceWhenCompleteAndInvalid() {
        setRequiredFields(true, true, true, true, true);
        CardEditText card = mCardForm.findViewById(R.id.bt_card_form_card_number);
        card.requestFocus();

        setText(card, INVALID_VISA);

        assertTrue(card.isError());
        assertTrue(card.hasFocus());
    }

    @Test
    public void expirationAdvancesToCvvWhenComplete() {
        setRequiredFields(false, true, true, false, false);
        ExpirationDateEditText expiration = mCardForm.findViewById(R.id.bt_card_form_expiration);
        CvvEditText cvv = mCardForm.findViewById(R.id.bt_card_form_cvv);
        expiration.requestFocus();
        assertTrue(expiration.hasFocus());
        assertFalse(cvv.hasFocus());

        setText(expiration, getValidExpiration());

        assertTrue(cvv.hasFocus());
    }

    @Test
    public void expirationDoesNotAdvanceWhenCompleteAndInvalid() {
        setRequiredFields(true, true, true, true, true);
        ExpirationDateEditText expiration = mCardForm.findViewById(R.id.bt_card_form_expiration);
        expiration.requestFocus();
        assertTrue(expiration.hasFocus());

        setText(expiration, "0812");

        assertTrue(expiration.hasFocus());
    }

    @Test
    public void cvvAdvancesToPostalWhenComplete() {
        setRequiredFields(false, false, true, true, false);
        CvvEditText cvv = mCardForm.findViewById(R.id.bt_card_form_cvv);
        PostalCodeEditText postalCode = mCardForm.findViewById(R.id.bt_card_form_postal_code);
        setText(((CardEditText) mCardForm.findViewById(R.id.bt_card_form_card_number)), VISA);
        cvv.requestFocus();
        assertTrue(cvv.hasFocus());
        assertFalse(postalCode.hasFocus());

        setText(cvv, "123");

        assertTrue(postalCode.hasFocus());
    }

    @Test
    public void testAdvancingDoesNotCrashWhenThereIsNotANextField() {
        setRequiredFields(true, false, false, false, false);

        setText(((CardEditText) mCardForm.findViewById(R.id.bt_card_form_card_number)), VISA);
    }

    @Test
    public void setEnabledSetsStateCorrectly() {
        setRequiredFields(true, true, true, true, true);
        assertFieldsEnabled(true, true, true, true, true);

        mCardForm.setEnabled(false);
        assertFieldsEnabled(false, false, false, false, false);
    }

    @Test
    public void isValidOnlyValidatesRequiredFields() {
        setRequiredFields(true, false, false, false, false);
        assertFalse(mCardForm.isValid());
        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_card_number)), VISA);
        assertTrue(mCardForm.isValid());
        assertFalse(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_card_number)).isError());

        setRequiredFields(false, true, false, false, false);
        assertFalse(mCardForm.isValid());
        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_expiration)), "1230");
        assertTrue(mCardForm.isValid());
        assertFalse(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_expiration)).isError());

        setRequiredFields(false, false, true, false, false);
        assertFalse(mCardForm.isValid());
        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_cvv)), "123");
        assertTrue(mCardForm.isValid());
        assertFalse(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_cvv)).isError());

        setRequiredFields(false, false, false, true, false);
        assertFalse(mCardForm.isValid());
        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_postal_code)), "12345");
        assertTrue(mCardForm.isValid());
        assertFalse(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_postal_code)).isError());

        setRequiredFields(false, false, false, false, true);
        assertFalse(mCardForm.isValid());
        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_country_code)), "123");
        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_mobile_number)), "12345678");
        assertTrue(mCardForm.isValid());
        assertFalse(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_country_code)).isError());
        assertFalse(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_mobile_number)).isError());
    }

    @Test
    public void validateSetsErrorOnFields() {
        setRequiredFields(true, true, true, true, true);

        assertFieldsError(false, false, false, false, false, false);

        mCardForm.validate();

        assertFieldsError(true, true, true, true, true, true);

        assertTrue(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_card_number)).isError());
        assertTrue(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_expiration)).isError());
        assertTrue(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_cvv)).isError());
        assertTrue(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_postal_code)).isError());
        assertTrue(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_country_code)).isError());
        assertTrue(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_mobile_number)).isError());
    }

    @Test
    public void getCardNumber_returnsCardNumber() {
        setRequiredFields(true, true, true, true, true);

        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_card_number)), VISA);

        assertEquals(VISA, mCardForm.getCardNumber());
    }

    @Test
    public void getExpirationMonth_returnsExpirationMonth() {
        setRequiredFields(true, true, true, true, true);

        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_expiration)), "1230");

        assertEquals("12", mCardForm.getExpirationMonth());
    }

    @Test
    public void getExpirationYear_returnsExpirationYear() {
        setRequiredFields(true, true, true, true, true);

        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_expiration)), "1230");

        assertEquals("30", mCardForm.getExpirationYear());
    }

    @Test
    public void getCvv_returnsCvv() {
        setRequiredFields(true, true, true, true, true);

        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_cvv)), "123");

        assertEquals("123", mCardForm.getCvv());
    }

    @Test
    public void getPostalCode_returnsPostalCode() {
        setRequiredFields(true, true, true, true, true);

        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_postal_code)), "12345");

        assertEquals("12345", mCardForm.getPostalCode());
    }

    @Test
    public void getCountryCode_returnsCountryCode() {
        setRequiredFields(true, true, true, true, true);

        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_country_code)), "86");

        assertEquals("86", mCardForm.getCountryCode());
    }

    @Test
    public void getMobileNumber_returnsMobileNumber() {
        setRequiredFields(true, true, true, true, true);

        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_mobile_number)), "5555555555");

        assertEquals("5555555555", mCardForm.getMobileNumber());
    }

    @Test
    public void setCardNumberError_setsError() {
        setRequiredFields(true, true, true, true, true);

        assertFalse(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_card_number)).isError());

        mCardForm.setCardNumberError("Error");

        assertTrue(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_card_number)).isError());
        assertTrue((mCardForm.findViewById(R.id.bt_card_form_card_number)).isFocused());
    }

    @Test
    public void setExpirationError_setsError() {
        setRequiredFields(false, true, true, true, true);

        assertFalse(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_expiration)).isError());

        mCardForm.setExpirationError("Error");

        assertTrue(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_expiration)).isError());
        assertTrue((mCardForm.findViewById(R.id.bt_card_form_expiration)).isFocused());
    }

    @Test
    public void setExpirationError_doesNotRequestFocusIfCardNumberIsAlreadyFocused() {
        setRequiredFields(true, true, true, true, true);

        mCardForm.findViewById(R.id.bt_card_form_card_number).requestFocus();
        mCardForm.setExpirationError("Error");

        assertTrue(mCardForm.findViewById(R.id.bt_card_form_card_number).isFocused());
        assertFalse(mCardForm.findViewById(R.id.bt_card_form_expiration).isFocused());
    }

    @Test
    public void setCvvError_setsError() {
        setRequiredFields(false, false, true, true, true);

        assertFalse(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_cvv)).isError());
        mCardForm.setCvvError("Error");
        assertTrue(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_cvv)).isError());
        assertTrue((mCardForm.findViewById(R.id.bt_card_form_cvv)).isFocused());
    }

    @Test
    public void setCvvError_doesNotRequestFocusIfCardNumberOrExpirationIsAlreadyFocused() {
        setRequiredFields(true, true, true, true, true);

        mCardForm.findViewById(R.id.bt_card_form_card_number).requestFocus();
        mCardForm.setCvvError("Error");
        assertTrue(mCardForm.findViewById(R.id.bt_card_form_card_number).isFocused());
        assertFalse(mCardForm.findViewById(R.id.bt_card_form_cvv).isFocused());

        mCardForm.findViewById(R.id.bt_card_form_expiration).requestFocus();
        mCardForm.setCvvError("Error");
        assertTrue(mCardForm.findViewById(R.id.bt_card_form_expiration).isFocused());
        assertFalse(mCardForm.findViewById(R.id.bt_card_form_cvv).isFocused());
    }

    @Test
    public void setPostalCodeError_setsError() {
        setRequiredFields(false, false, false, true, true);

        assertFalse(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_postal_code)).isError());
        mCardForm.setPostalCodeError("Error");
        assertTrue(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_postal_code)).isError());
        assertTrue((mCardForm.findViewById(R.id.bt_card_form_postal_code)).isFocused());
    }

    @Test
    public void setPostalCodeError_doesNotRequestFocusIfCardNumberCvvOrExpirationIsAlreadyFocused() {
        setRequiredFields(true, true, true, true, true);

        mCardForm.findViewById(R.id.bt_card_form_card_number).requestFocus();
        mCardForm.setPostalCodeError("Error");
        assertTrue(mCardForm.findViewById(R.id.bt_card_form_card_number).isFocused());
        assertFalse(mCardForm.findViewById(R.id.bt_card_form_postal_code).isFocused());

        mCardForm.findViewById(R.id.bt_card_form_expiration).requestFocus();
        mCardForm.setPostalCodeError("Error");
        assertTrue(mCardForm.findViewById(R.id.bt_card_form_expiration).isFocused());
        assertFalse(mCardForm.findViewById(R.id.bt_card_form_postal_code).isFocused());

        mCardForm.findViewById(R.id.bt_card_form_cvv).requestFocus();
        mCardForm.setPostalCodeError("Error");
        assertTrue(mCardForm.findViewById(R.id.bt_card_form_cvv).isFocused());
        assertFalse(mCardForm.findViewById(R.id.bt_card_form_postal_code).isFocused());
    }

    @Test
    public void setCountryCodeError_setsError() {
        setRequiredFields(false, false, false, false, true);

        assertFalse(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_country_code)).isError());
        mCardForm.setCountryCodeError("Error");
        assertTrue(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_country_code)).isError());
        assertTrue((mCardForm.findViewById(R.id.bt_card_form_country_code)).isFocused());
    }

    @Test
    public void setCountryCodeError_doesNotRequestFocusIfCardNumberExpirationCvvOrPostalIsAlreadyFocused() {
        setRequiredFields(true, true, true, true, true);

        mCardForm.findViewById(R.id.bt_card_form_card_number).requestFocus();
        mCardForm.setMobileNumberError("Error");
        assertTrue(mCardForm.findViewById(R.id.bt_card_form_card_number).isFocused());
        assertFalse(mCardForm.findViewById(R.id.bt_card_form_country_code).isFocused());

        mCardForm.findViewById(R.id.bt_card_form_expiration).requestFocus();
        mCardForm.setMobileNumberError("Error");
        assertTrue(mCardForm.findViewById(R.id.bt_card_form_expiration).isFocused());
        assertFalse(mCardForm.findViewById(R.id.bt_card_form_country_code).isFocused());

        mCardForm.findViewById(R.id.bt_card_form_cvv).requestFocus();
        mCardForm.setMobileNumberError("Error");
        assertTrue(mCardForm.findViewById(R.id.bt_card_form_cvv).isFocused());
        assertFalse(mCardForm.findViewById(R.id.bt_card_form_country_code).isFocused());

        mCardForm.findViewById(R.id.bt_card_form_postal_code).requestFocus();
        mCardForm.setMobileNumberError("Error");
        assertTrue(mCardForm.findViewById(R.id.bt_card_form_postal_code).isFocused());
        assertFalse(mCardForm.findViewById(R.id.bt_card_form_country_code).isFocused());
    }

    @Test
    public void setMobileNumberError_setsError() {
        setRequiredFields(false, false, false, false, true);

        assertFalse(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_mobile_number)).isError());
        mCardForm.setMobileNumberError("Error");
        assertTrue(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_mobile_number)).isError());
        assertTrue((mCardForm.findViewById(R.id.bt_card_form_mobile_number)).isFocused());
    }

    @Test
    public void setMobileNumberError_doesNotRequestFocusIfCardNumberExpirationCvvPostalOrCountryCodeIsAlreadyFocused() {
        setRequiredFields(true, true, true, true, true);

        mCardForm.findViewById(R.id.bt_card_form_card_number).requestFocus();
        mCardForm.setMobileNumberError("Error");
        assertTrue(mCardForm.findViewById(R.id.bt_card_form_card_number).isFocused());
        assertFalse(mCardForm.findViewById(R.id.bt_card_form_mobile_number).isFocused());

        mCardForm.findViewById(R.id.bt_card_form_expiration).requestFocus();
        mCardForm.setMobileNumberError("Error");
        assertTrue(mCardForm.findViewById(R.id.bt_card_form_expiration).isFocused());
        assertFalse(mCardForm.findViewById(R.id.bt_card_form_mobile_number).isFocused());

        mCardForm.findViewById(R.id.bt_card_form_cvv).requestFocus();
        mCardForm.setMobileNumberError("Error");
        assertTrue(mCardForm.findViewById(R.id.bt_card_form_cvv).isFocused());
        assertFalse(mCardForm.findViewById(R.id.bt_card_form_mobile_number).isFocused());

        mCardForm.findViewById(R.id.bt_card_form_postal_code).requestFocus();
        mCardForm.setMobileNumberError("Error");
        assertTrue(mCardForm.findViewById(R.id.bt_card_form_postal_code).isFocused());
        assertFalse(mCardForm.findViewById(R.id.bt_card_form_mobile_number).isFocused());

        mCardForm.findViewById(R.id.bt_card_form_country_code).requestFocus();
        mCardForm.setMobileNumberError("Error");
        assertTrue(mCardForm.findViewById(R.id.bt_card_form_country_code).isFocused());
        assertFalse(mCardForm.findViewById(R.id.bt_card_form_mobile_number).isFocused());
    }

    @Test
    public void marksCardNumberAsErrorWhenFocusChangesAndCardNumberFailsValidation() {
        setRequiredFields(true, true, true, true, true);
        CardEditText card = mActivity.findViewById(R.id.bt_card_form_card_number);
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
        setRequiredFields(true, true, true, true, true);
        ExpirationDateEditText expiration = mActivity.findViewById(R.id.bt_card_form_expiration);
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
        setRequiredFields(true, true, true, true, true);
        CvvEditText cvv = mCardForm.findViewById(R.id.bt_card_form_cvv);
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
        setRequiredFields(true, true, true, true, true);
        CardEditText card = mCardForm.findViewById(R.id.bt_card_form_card_number);
        CvvEditText cvv = mCardForm.findViewById(R.id.bt_card_form_cvv);

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
    public void marksMobileNumberAsErrorWhenFocusChangesAndMobileNumberNotMinimumLength() {
        setRequiredFields(true, true, true, true, true);
        MobileNumberEditText mobileNumber = mCardForm.findViewById(R.id.bt_card_form_mobile_number);
        mobileNumber.requestFocus();

        setText(mobileNumber, "1");
        assertTrue(mobileNumber.isFocused());
        assertFalse(mobileNumber.isError());

        mCardForm.findViewById(R.id.bt_card_form_card_number).requestFocus();
        assertFalse(mobileNumber.isFocused());
        assertTrue(mobileNumber.isError());
    }

    @Test
    public void doesNotMarkCardNumberAsErrorWhenFocusChangesAndCardNumberEmpty() {
        setRequiredFields(true, true, true, true, true);
        CardEditText cardEditText = mCardForm.findViewById(R.id.bt_card_form_card_number);
        cardEditText.requestFocus();
        assertTrue(cardEditText.isFocused());
        assertFalse(cardEditText.isError());

        mCardForm.findViewById(R.id.bt_card_form_expiration).requestFocus();
        assertFalse(cardEditText.isFocused());
        assertFalse(cardEditText.isError());
    }

    @Test
    public void doesNotMarkExpirationAsErrorWhenFocusChangesAndExpirationEmpty() {
        setRequiredFields(true, true, true, true, true);
        ExpirationDateEditText expirationDateEditText = mCardForm.findViewById(R.id.bt_card_form_expiration);
        expirationDateEditText.requestFocus();
        assertTrue(expirationDateEditText.isFocused());
        assertFalse(expirationDateEditText.isError());

        mCardForm.findViewById(R.id.bt_card_form_card_number).requestFocus();
        assertFalse(expirationDateEditText.isFocused());
        assertFalse(expirationDateEditText.isError());
    }

    @Test
    public void doesNotMarkCvvAsErrorWhenFocusChangesAndCvvEmpty() {
        setRequiredFields(true, true, true, true, true);
        CvvEditText cvvEditText = mCardForm.findViewById(R.id.bt_card_form_cvv);
        cvvEditText.requestFocus();
        assertTrue(cvvEditText.isFocused());
        assertFalse(cvvEditText.isError());

        mCardForm.findViewById(R.id.bt_card_form_card_number).requestFocus();
        assertFalse(cvvEditText.isFocused());
        assertFalse(cvvEditText.isError());
    }

    @Test
    public void doesNotMarkPostalCodeAsErrorWhenFocusChangesAndPostalCodeEmpty() {
        setRequiredFields(true, true, true, true, true);
        PostalCodeEditText postalCode = mCardForm.findViewById(R.id.bt_card_form_postal_code);
        postalCode.requestFocus();
        assertTrue(postalCode.isFocused());
        assertFalse(postalCode.isError());

        mCardForm.findViewById(R.id.bt_card_form_card_number).requestFocus();
        assertFalse(postalCode.isFocused());
        assertFalse(postalCode.isError());
    }

    @Test
    public void doesNotMarkMobileNumberAsErrorWhenFocusChangesAndMobileNumberEmpty() {
        setRequiredFields(true, true, true, true, true);
        MobileNumberEditText mobileNumber = mCardForm.findViewById(R.id.bt_card_form_mobile_number);
        mobileNumber.requestFocus();
        assertTrue(mobileNumber.isFocused());
        assertFalse(mobileNumber.isError());

        mCardForm.findViewById(R.id.bt_card_form_card_number).requestFocus();
        assertFalse(mobileNumber.isFocused());
        assertFalse(mobileNumber.isError());
    }

    @Test
    public void onCardFormValidListenerOnlyCalledOnValidityChange() {
        setRequiredFields(true, true, true, true, true);
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
        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_country_code)), "123");
        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_mobile_number)), "12345678");

        assertEquals(1, counter.get());

        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_cvv)), "12");

        assertEquals(2, counter.get());
    }

    @Test
    public void onCardTypeChangeListenerIsCalledWhenCardTypeChanges() {
        setRequiredFields(true, false, false, false, false);
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
        setRequiredFields(true, true, true, true, true);
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
        mCardForm.findViewById(R.id.bt_card_form_mobile_number).requestFocus();

        assertEquals(5, called.get());
    }

    @Test
    public void testDoesNotCrashWhenNoListenersAreSet() {
        setRequiredFields(true, true, true, true, true);

        setText(((CardEditText) (mCardForm.findViewById(R.id.bt_card_form_card_number))), VISA);
    }

    @Test
    public void hintsAreDisplayed() {
        setRequiredFields(true, true, true, true, true);

        assertTextHintIs(mCardForm.findViewById(R.id.bt_card_form_card_number), R.string.bt_form_hint_card_number);
        assertTextHintIs(mCardForm.findViewById(R.id.bt_card_form_expiration), R.string.bt_form_hint_expiration);
        assertTextHintIs(mCardForm.findViewById(R.id.bt_card_form_cvv), R.string.bt_form_hint_cvv);
        assertTextHintIs(mCardForm.findViewById(R.id.bt_card_form_postal_code), R.string.bt_form_hint_postal_code);
        assertTextHintIs(mCardForm.findViewById(R.id.bt_card_form_mobile_number), R.string.bt_form_hint_mobile_number);
    }

    @Test
    public void correctCardHintsAreDisplayed() {
        setRequiredFields(true, true, true, true, true);

        CardEditText card = mCardForm.findViewById(R.id.bt_card_form_card_number);

        assertNoHintIcon(card);

        setText(card, "4");
        assertIconHintIs(card, R.drawable.bt_ic_visa);

        setText(card, "51");
        assertIconHintIs(card, R.drawable.bt_ic_mastercard);

        setText(card, "37");
        assertIconHintIs(card, R.drawable.bt_ic_amex);

        setText(card, "35");
        assertIconHintIs(card, R.drawable.bt_ic_jcb);

        setText(card, "5018");
        assertIconHintIs(card, R.drawable.bt_ic_maestro);

        setText(card, "1234");
        assertIconHintIs(card, R.drawable.bt_ic_unknown);
    }

    @Test
    public void valuesAreRestored() {
        setRequiredFields(true, true, true, true, true);

        setText((EditText) mActivity.findViewById(R.id.bt_card_form_card_number), VISA);
        setText((EditText) mActivity.findViewById(R.id.bt_card_form_expiration), "1220");
        setText((EditText) mActivity.findViewById(R.id.bt_card_form_cvv), "123");
        setText((EditText) mActivity.findViewById(R.id.bt_card_form_postal_code), "12345");
        setText((EditText) mActivity.findViewById(R.id.bt_card_form_mobile_number), "12345678");

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
        assertEquals("1 234-567-8", ((EditText) mActivity.findViewById(R.id.bt_card_form_mobile_number)).getText().toString());
    }

    @Test
    public void handleCardIOResponse_doesNothingIfDataIsNull() {
        setRequiredFields(true, true, true, true, true);

        mCardForm.handleCardIOResponse(null);

        assertEquals("", ((EditText) mActivity.findViewById(R.id.bt_card_form_card_number)).getText().toString());
        assertEquals("", ((EditText) mActivity.findViewById(R.id.bt_card_form_expiration)).getText().toString());
        assertEquals("", ((EditText) mActivity.findViewById(R.id.bt_card_form_cvv)).getText().toString());
        assertEquals("", ((EditText) mActivity.findViewById(R.id.bt_card_form_postal_code)).getText().toString());
        assertEquals("", ((EditText) mActivity.findViewById(R.id.bt_card_form_mobile_number)).getText().toString());
    }

    @Test
    public void handleCardIOResponse_doesNothingIfNoCardIOResponseIsPresent() {
        setRequiredFields(true, true, true, true, true);

        mCardForm.handleCardIOResponse(new Intent());

        assertEquals("", ((EditText) mActivity.findViewById(R.id.bt_card_form_card_number)).getText().toString());
        assertEquals("", ((EditText) mActivity.findViewById(R.id.bt_card_form_expiration)).getText().toString());
        assertEquals("", ((EditText) mActivity.findViewById(R.id.bt_card_form_cvv)).getText().toString());
        assertEquals("", ((EditText) mActivity.findViewById(R.id.bt_card_form_postal_code)).getText().toString());
        assertEquals("", ((EditText) mActivity.findViewById(R.id.bt_card_form_mobile_number)).getText().toString());
    }

    @Test
    public void handleCardIOResponse_setsCardNumber() {
        setRequiredFields(true, true, false, false, false);
        Intent intent = new Intent()
                .putExtra(CardIOActivity.EXTRA_SCAN_RESULT, new CreditCard(VISA, 0, 0, "", "", ""));

        mCardForm.handleCardIOResponse(intent);

        assertEquals(VISA, ((EditText) mActivity.findViewById(R.id.bt_card_form_card_number)).getText().toString());
        assertTrue(mCardForm.findViewById(R.id.bt_card_form_expiration).hasFocus());
    }

    @Test
    public void handleCardIOResponse_doesNotSetCardNumberIfCardNumberNotRequired() {
        setRequiredFields(false, true, true, true, true);
        Intent intent = new Intent()
                .putExtra(CardIOActivity.EXTRA_SCAN_RESULT, new CreditCard(VISA, 0, 0, "", "", ""));

        mCardForm.handleCardIOResponse(intent);

        assertEquals("", ((EditText) mActivity.findViewById(R.id.bt_card_form_card_number)).getText().toString());
    }

    @Test
    public void handleCardIOResponse_setsExpirationDate() {
        setRequiredFields(false, true, true, false, false);
        Intent intent = new Intent()
                .putExtra(CardIOActivity.EXTRA_SCAN_RESULT, new CreditCard(VISA, 12, 2020, "", "", ""));

        mCardForm.handleCardIOResponse(intent);

        assertEquals("122020", ((EditText) mActivity.findViewById(R.id.bt_card_form_expiration)).getText().toString());
        assertTrue(mCardForm.findViewById(R.id.bt_card_form_cvv).hasFocus());
    }

    @Test
    public void handleCardIOResponse_handlesSingleDigitExpirationMonths() {
        setRequiredFields(false, true, true, false, false);
        Intent intent = new Intent()
                .putExtra(CardIOActivity.EXTRA_SCAN_RESULT, new CreditCard(VISA, 7, 2020, "", "", ""));

        mCardForm.handleCardIOResponse(intent);

        assertEquals("07", ((ExpirationDateEditText) mActivity.findViewById(R.id.bt_card_form_expiration)).getMonth());
        assertEquals("2020", ((ExpirationDateEditText) mActivity.findViewById(R.id.bt_card_form_expiration)).getYear());
        assertTrue(mCardForm.findViewById(R.id.bt_card_form_cvv).hasFocus());
    }

    @Test
    public void handleCardIOResponse_doesNotSetExpirationDateIfExpirationDateInvalid() {
        setRequiredFields(true, true, false, false, false);
        Intent intent = new Intent()
                .putExtra(CardIOActivity.EXTRA_SCAN_RESULT, new CreditCard(VISA, 12, 2000, "", "", ""));

        mCardForm.handleCardIOResponse(intent);

        assertEquals("", ((EditText) mActivity.findViewById(R.id.bt_card_form_expiration)).getText().toString());
        assertTrue(mCardForm.findViewById(R.id.bt_card_form_expiration).hasFocus());
    }

    @Test
    public void handleCardIOResponse_doesNotSetExpirationDateIfExpirationDateNotRequired() {
        setRequiredFields(true, false, true, false, false);
        Intent intent = new Intent()
                .putExtra(CardIOActivity.EXTRA_SCAN_RESULT, new CreditCard(VISA, 12, 2020, "", "", ""));

        mCardForm.handleCardIOResponse(intent);

        assertEquals("", ((EditText) mActivity.findViewById(R.id.bt_card_form_expiration)).getText().toString());
        assertTrue(mCardForm.findViewById(R.id.bt_card_form_cvv).hasFocus());
    }

    private void setRequiredFields(boolean cardNumberRequired, boolean expirationRequired, boolean cvvRequired,
                                   boolean postalCodeRequired, boolean mobileNumberRequired) {
        mCardForm.cardRequired(cardNumberRequired)
                .expirationRequired(expirationRequired)
                .cvvRequired(cvvRequired)
                .postalCodeRequired(postalCodeRequired)
                .mobileNumberRequired(mobileNumberRequired)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);
    }

    private void assertFieldsVisible(int cardNumberVisible, int expirationVisible, int cvvVisible,
                                     int postalCodeVisible, int mobileNumberVisible) {
        assertEquals(cardNumberVisible, mCardForm.findViewById(R.id.bt_card_form_card_number_icon).getVisibility());
        assertEquals(cardNumberVisible, mCardForm.findViewById(R.id.bt_card_form_card_number).getVisibility());

        assertEquals(expirationVisible, mCardForm.findViewById(R.id.bt_card_form_expiration).getVisibility());
        assertEquals(cvvVisible, mCardForm.findViewById(R.id.bt_card_form_cvv).getVisibility());

        assertEquals(postalCodeVisible, mCardForm.findViewById(R.id.bt_card_form_postal_code_icon).getVisibility());
        assertEquals(postalCodeVisible, mCardForm.findViewById(R.id.bt_card_form_postal_code).getVisibility());

        assertEquals(mobileNumberVisible, mCardForm.findViewById(R.id.bt_card_form_mobile_number_icon).getVisibility());
        assertEquals(mobileNumberVisible, mCardForm.findViewById(R.id.bt_card_form_country_code).getVisibility());
        assertEquals(mobileNumberVisible, mCardForm.findViewById(R.id.bt_card_form_mobile_number).getVisibility());
        assertEquals(mobileNumberVisible, mCardForm.findViewById(R.id.bt_card_form_mobile_number_explanation).getVisibility());
    }

    private void assertFieldsEnabled(boolean cardNumberEnabled, boolean expirationEnabled,
                                     boolean cvvEnabled, boolean postalCodeEnabled,
                                     boolean mobileNumberEnabled) {
        assertEquals(cardNumberEnabled, mCardForm.findViewById(R.id.bt_card_form_card_number).isEnabled());
        assertEquals(expirationEnabled, mCardForm.findViewById(R.id.bt_card_form_expiration).isEnabled());
        assertEquals(cvvEnabled, mCardForm.findViewById(R.id.bt_card_form_cvv).isEnabled());
        assertEquals(postalCodeEnabled, mCardForm.findViewById(R.id.bt_card_form_postal_code).isEnabled());
        assertEquals(mobileNumberEnabled, mCardForm.findViewById(R.id.bt_card_form_mobile_number).isEnabled());
    }

    private void assertFieldsError(boolean cardNumberEnabled, boolean expirationEnabled,
                                   boolean cvvEnabled, boolean postalCodeEnabled,
                                   boolean countryCodeEnabled, boolean mobileNumberEnabled) {
        assertEquals(cardNumberEnabled, ((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_card_number)).isError());
        assertEquals(expirationEnabled, ((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_expiration)).isError());
        assertEquals(cvvEnabled, ((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_cvv)).isError());
        assertEquals(postalCodeEnabled, ((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_postal_code)).isError());
        assertEquals(postalCodeEnabled, ((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_postal_code)).isError());
        assertEquals(countryCodeEnabled, ((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_country_code)).isError());
        assertEquals(mobileNumberEnabled, ((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_mobile_number)).isError());
    }

    private void setText(EditText editText, String text) {
        editText.setText("");
        Editable editable = editText.getText();
        for (char c : text.toCharArray()) {
            editable.append(c);
        }
    }

    private static void assertTextHintIs(View view, int resourceId) {
        assertEquals(RuntimeEnvironment.application.getString(resourceId),
                ((TextInputLayout) view.getParent().getParent()).getHint());
    }

    private void setActivityBackground(int backgroundColor) {
        mActivity = spy(mActivity);
        ColorDrawable colorDrawable = mock(ColorDrawable.class);
        when(colorDrawable.getColor()).thenReturn(getColor(backgroundColor));
        View rootView = mock(View.class);
        when(rootView.getBackground()).thenReturn(colorDrawable);
        View decorView = mock(View.class);
        when(decorView.getRootView()).thenReturn(rootView);
        Window window = mock(Window.class);
        when(window.getDecorView()).thenReturn(decorView);
        when(mActivity.getResources()).thenReturn(RuntimeEnvironment.application.getResources());
        when(mActivity.getWindow()).thenReturn(window);
    }

    private void assertDrawableIsFromResource(int view, int resourceId) {
        Drawable drawable = ((ImageView) mCardForm.findViewById(view)).getDrawable();
        assertEquals(resourceId, shadowOf(drawable).getCreatedFromResId());
    }
}
