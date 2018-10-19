package com.braintreepayments.cardform.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;
import android.text.Editable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.braintreepayments.cardform.CardScanningFragment;
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

import java.lang.reflect.Field;
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
    public void getCardholderNameEditText() {
        assertEquals(mCardForm.findViewById(R.id.bt_card_form_cardholder_name), mCardForm.getCardholderNameEditText());
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
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

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
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        assertDrawableIsFromResource(R.id.bt_card_form_card_number_icon, R.drawable.bt_ic_card_dark);
        assertDrawableIsFromResource(R.id.bt_card_form_cardholder_name_icon, R.drawable.bt_ic_cardholder_name_dark);
        assertDrawableIsFromResource(R.id.bt_card_form_postal_code_icon, R.drawable.bt_ic_postal_code_dark);
        assertDrawableIsFromResource(R.id.bt_card_form_mobile_number_icon, R.drawable.bt_ic_mobile_number_dark);
    }

    @Test
    public void setup_setsIconsForLightBackgrounds() {
        setActivityBackground(R.color.bt_white);
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        assertDrawableIsFromResource(R.id.bt_card_form_card_number_icon, R.drawable.bt_ic_card);
        assertDrawableIsFromResource(R.id.bt_card_form_cardholder_name_icon, R.drawable.bt_ic_cardholder_name);
        assertDrawableIsFromResource(R.id.bt_card_form_postal_code_icon, R.drawable.bt_ic_postal_code);
        assertDrawableIsFromResource(R.id.bt_card_form_mobile_number_icon, R.drawable.bt_ic_mobile_number);
    }

    @Test
    public void setCardNumberIcon_withRes_overridesDefaultIcon() {
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        mCardForm.setCardNumberIcon(R.drawable.bt_ic_amex);
        assertDrawableIsFromResource(R.id.bt_card_form_card_number_icon, R.drawable.bt_ic_amex);
    }

    @Test
    public void setPostalCodeIcon_withRes_overridesDefaultIcon() {
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        mCardForm.setPostalCodeIcon(R.drawable.bt_ic_amex);
        assertDrawableIsFromResource(R.id.bt_card_form_postal_code_icon, R.drawable.bt_ic_amex);
    }

    @Test
    public void setMobileNumberIcon_withRes_overridesDefaultIcon() {
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        mCardForm.setMobileNumberIcon(R.drawable.bt_ic_amex);
        assertDrawableIsFromResource(R.id.bt_card_form_mobile_number_icon, R.drawable.bt_ic_amex);
    }

    @Test
    public void cardNumberIsShownIfRequired() {
        mCardForm.cardRequired(true)
                .expirationRequired(false)
                .cvvRequired(false)
                .cardholderName(CardForm.FIELD_DISABLED)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        assertFieldsVisible(VISIBLE, GONE, GONE, GONE, GONE, GONE);
    }

    @Test
    public void expirationIsShownIfRequired() {
        mCardForm.cardRequired(false)
                .expirationRequired(true)
                .cvvRequired(false)
                .cardholderName(CardForm.FIELD_DISABLED)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        assertFieldsVisible(GONE, VISIBLE, GONE, GONE, GONE, GONE);
    }

    @Test
    public void cvvIsShownIfRequired() {
        mCardForm.cardRequired(false)
                .expirationRequired(false)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_DISABLED)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        assertFieldsVisible(GONE, GONE, VISIBLE, GONE, GONE, GONE);
    }

    @Test
    public void cardholderNameIsShownIfRequired() {
        mCardForm.cardRequired(false)
                .expirationRequired(false)
                .cvvRequired(false)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        assertFieldsVisible(GONE, GONE, GONE, VISIBLE, GONE, GONE);
    }

    @Test
    public void cardholderNameIsShownWhenOptional() {
        mCardForm.cardholderName(CardForm.FIELD_OPTIONAL)
                .setup(mActivity);

        assertEquals(VISIBLE, mCardForm.findViewById(R.id.bt_card_form_cardholder_name).getVisibility());
        assertEquals(VISIBLE, mCardForm.findViewById(R.id.bt_card_form_cardholder_name_icon).getVisibility());
    }

    @Test
    public void postalCodeIsShownIfRequired() {
        mCardForm.cardRequired(false)
                .expirationRequired(false)
                .cvvRequired(false)
                .cardholderName(CardForm.FIELD_DISABLED)
                .postalCodeRequired(true)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        assertFieldsVisible(GONE, GONE, GONE, GONE, VISIBLE, GONE);
    }

    @Test
    public void countryCodeMobileNumberAndMobileNumberExplanationAreShownIfRequired() {
        mCardForm.cardRequired(false)
                .expirationRequired(false)
                .cvvRequired(false)
                .cardholderName(CardForm.FIELD_DISABLED)
                .postalCodeRequired(false)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        assertFieldsVisible(GONE, GONE, GONE, GONE, GONE, VISIBLE);
        assertEquals("Make sure SMS is supported", ((TextView)mCardForm.findViewById(R.id.bt_card_form_mobile_number_explanation)).getText());
    }

    @Test
    public void mobileNumberExplanationNotShownIfMobileNumberNotRequired() {
        mCardForm.cardRequired(false)
                .expirationRequired(false)
                .cvvRequired(false)
                .cardholderName(CardForm.FIELD_DISABLED)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        assertFieldsVisible(GONE, GONE, GONE, GONE ,GONE, GONE);
    }

    @Test
    public void repeatedCallsToSetupSetCorrectVisibility() {
        mCardForm.cardRequired(false)
                .expirationRequired(false)
                .cvvRequired(false)
                .cardholderName(CardForm.FIELD_DISABLED)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        assertFieldsVisible(GONE, GONE, GONE, GONE, GONE, GONE);

        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        assertFieldsVisible(VISIBLE, VISIBLE, VISIBLE, VISIBLE, VISIBLE, VISIBLE);
    }

    @Test
    public void setsIMEActionAsGoForCardNumberIfNoOtherFieldsAreRequired() {
        mCardForm.cardRequired(true)
                .expirationRequired(false)
                .cvvRequired(false)
                .cardholderName(CardForm.FIELD_DISABLED)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        assertEquals(EditorInfo.IME_ACTION_GO,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_card_number)).getImeOptions());
    }

    @Test
    public void repeatedCallsToSetupSetsIMEActionAsGoForCardNumberIfNoOtherFieldsAreRequired() {
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(false)
                .cardholderName(CardForm.FIELD_DISABLED)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        mCardForm.cardRequired(true)
                .expirationRequired(false)
                .cvvRequired(false)
                .cardholderName(CardForm.FIELD_DISABLED)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        assertEquals(EditorInfo.IME_ACTION_GO,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_card_number)).getImeOptions());
    }

    @Test
    public void setsIMEActionAsNextForCardholderNameIfAnyOtherFieldIsRequired() {
        mCardForm.cardRequired(true)
                .expirationRequired(false)
                .cvvRequired(false)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        assertEquals(EditorInfo.IME_ACTION_NEXT,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_cardholder_name)).getImeOptions());

        mCardForm.cardRequired(false)
                .expirationRequired(true)
                .cvvRequired(false)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        assertEquals(EditorInfo.IME_ACTION_NEXT,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_cardholder_name)).getImeOptions());

        mCardForm.cardRequired(false)
                .expirationRequired(false)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        assertEquals(EditorInfo.IME_ACTION_NEXT,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_cardholder_name)).getImeOptions());

        mCardForm.cardRequired(false)
                .expirationRequired(false)
                .cvvRequired(false)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        assertEquals(EditorInfo.IME_ACTION_NEXT,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_cardholder_name)).getImeOptions());

        mCardForm.cardRequired(false)
                .expirationRequired(false)
                .cvvRequired(false)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(false)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        assertEquals(EditorInfo.IME_ACTION_NEXT,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_cardholder_name)).getImeOptions());
    }

    @Test
    public void setsIMEActionAsNextForCardNumberIfAnyOtherFieldIsRequired() {
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(false)
                .cardholderName(CardForm.FIELD_DISABLED)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        assertEquals(EditorInfo.IME_ACTION_NEXT,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_card_number)).getImeOptions());

        mCardForm.cardRequired(true)
                .expirationRequired(false)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_DISABLED)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        assertEquals(EditorInfo.IME_ACTION_NEXT,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_card_number)).getImeOptions());

        mCardForm.cardRequired(true)
                .expirationRequired(false)
                .cvvRequired(false)
                .cardholderName(CardForm.FIELD_DISABLED)
                .postalCodeRequired(true)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        assertEquals(EditorInfo.IME_ACTION_NEXT,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_card_number)).getImeOptions());

        mCardForm.cardRequired(true)
                .expirationRequired(false)
                .cvvRequired(false)
                .cardholderName(CardForm.FIELD_DISABLED)
                .postalCodeRequired(false)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        assertEquals(EditorInfo.IME_ACTION_NEXT,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_card_number)).getImeOptions());
    }

    @Test
    public void setsIMEActionAsGoForExpirationIfNoOtherFieldsRequired() {
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(false)
                .cardholderName(CardForm.FIELD_DISABLED)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        assertEquals(EditorInfo.IME_ACTION_GO,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_expiration)).getImeOptions());
    }

    @Test
    public void repeatedCallsToSetupSetsIMEActionAsGoForExpirationIfNoOtherFieldsRequired() {
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_DISABLED)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(false)
                .cardholderName(CardForm.FIELD_DISABLED)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        assertEquals(EditorInfo.IME_ACTION_GO,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_expiration)).getImeOptions());
    }

    @Test
    public void setsIMEActionAsGoForCvvIfNoOtherFieldsAreRequired() {
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_DISABLED)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        assertEquals(EditorInfo.IME_ACTION_NEXT,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_expiration)).getImeOptions());
        assertEquals(EditorInfo.IME_ACTION_GO,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_cvv)).getImeOptions());
    }

    @Test
    public void repeatedCallsToSetupSetsIMEActionAsGoForCvvIfNoOtherFieldsAreRequired() {
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_DISABLED)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        assertEquals(EditorInfo.IME_ACTION_NEXT,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_expiration)).getImeOptions());
        assertEquals(EditorInfo.IME_ACTION_GO,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_cvv)).getImeOptions());
    }

    @Test
    public void setsIMEActionAsGoForPostalAndNextForExpirationIfCvvIsNotPresent() {
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(false)
                .cardholderName(CardForm.FIELD_DISABLED)
                .postalCodeRequired(true)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        assertEquals(EditorInfo.IME_ACTION_NEXT,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_expiration)).getImeOptions());
        assertEquals(EditorInfo.IME_ACTION_GO,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_postal_code)).getImeOptions());
    }

    @Test
    public void repeatedCallsToSetupSetsIMEActionAsGoForPostalAndNextForExpirationIfCvvIsNotPresent() {
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(false)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        assertEquals(EditorInfo.IME_ACTION_NEXT,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_expiration)).getImeOptions());
        assertEquals(EditorInfo.IME_ACTION_NEXT,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_cardholder_name)).getImeOptions());
        assertEquals(EditorInfo.IME_ACTION_GO,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_postal_code)).getImeOptions());
    }

    @Test
    public void setsIMEActionAsGoForPostalCodeIfCvvAndPostalArePresent() {
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        assertEquals(EditorInfo.IME_ACTION_NEXT,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_expiration)).getImeOptions());
        assertEquals(EditorInfo.IME_ACTION_NEXT,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_cvv)).getImeOptions());
        assertEquals(EditorInfo.IME_ACTION_NEXT,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_cardholder_name)).getImeOptions());
        assertEquals(EditorInfo.IME_ACTION_GO,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_postal_code)).getImeOptions());
    }

    @Test
    public void repeatedCallsToSetupSetsIMEActionAsGoForPostalCodeIfCvvAndPostalArePresent() {
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(false)
                .cardholderName(CardForm.FIELD_DISABLED)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);


        assertEquals(EditorInfo.IME_ACTION_NEXT,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_expiration)).getImeOptions());
        assertEquals(EditorInfo.IME_ACTION_NEXT,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_cvv)).getImeOptions());
        assertEquals(EditorInfo.IME_ACTION_NEXT,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_cardholder_name)).getImeOptions());
        assertEquals(EditorInfo.IME_ACTION_GO,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_postal_code)).getImeOptions());
    }

    @Test
    public void setsIMEActionAsNextForCountryCode() {
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        assertEquals(EditorInfo.IME_ACTION_NEXT,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_country_code)).getImeOptions());
    }

    @Test
    public void setsIMEActionAsGoForMobileNumber() {
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        assertEquals(EditorInfo.IME_ACTION_GO,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_mobile_number)).getImeOptions());
    }

    @Test
    public void repeatedCallsToSetupSetsIMEActionAsGoForMobileNumber() {
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        assertEquals(EditorInfo.IME_ACTION_NEXT,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_cardholder_name)).getImeOptions());
        assertEquals(EditorInfo.IME_ACTION_NEXT,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_postal_code)).getImeOptions());
        assertEquals(EditorInfo.IME_ACTION_GO,
                ((TextView) mCardForm.findViewById(R.id.bt_card_form_mobile_number)).getImeOptions());
    }

    @Test
    public void cardNumberAdvancesToExpirationWhenCompleteAndValid() {
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(false)
                .cardholderName(CardForm.FIELD_DISABLED)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);
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
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);
        CardEditText card = mCardForm.findViewById(R.id.bt_card_form_card_number);
        card.requestFocus();

        setText(card, INVALID_VISA);

        assertTrue(card.isError());
        assertTrue(card.hasFocus());
    }

    @Test
    public void expirationAdvancesToCvvWhenComplete() {
        mCardForm.cardRequired(false)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_DISABLED)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);
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
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);
        ExpirationDateEditText expiration = mCardForm.findViewById(R.id.bt_card_form_expiration);
        expiration.requestFocus();
        assertTrue(expiration.hasFocus());

        setText(expiration, "0812");

        assertTrue(expiration.hasFocus());
    }

    @Test
    public void cvvAdvancesToPostalWhenComplete() {
        mCardForm.cardRequired(false)
                .expirationRequired(false)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_DISABLED)
                .postalCodeRequired(true)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);
        CvvEditText cvv = mCardForm.findViewById(R.id.bt_card_form_cvv);
        PostalCodeEditText postalCode = mCardForm.findViewById(R.id.bt_card_form_postal_code);
        setText(((CardEditText) mCardForm.findViewById(R.id.bt_card_form_card_number)), VISA);
        cvv.requestFocus();
        assertTrue(cvv.hasFocus());
        assertFalse(postalCode.hasFocus());

        setText(cvv, "123");

        assertTrue(postalCode.hasFocus());
    }

    public void cvvAdvancesToCardholderNameWhenComplete() {
        mCardForm.cardRequired(false)
                .expirationRequired(false)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);
        CvvEditText cvv = (CvvEditText) mCardForm.findViewById(R.id.bt_card_form_cvv);
        CardholderNameEditText cardholderName = (CardholderNameEditText) mCardForm.findViewById(R.id.bt_card_form_cardholder_name);
        setText(((CardEditText) mCardForm.findViewById(R.id.bt_card_form_card_number)), VISA);
        cvv.requestFocus();
        assertTrue(cvv.hasFocus());
        assertFalse(cardholderName.hasFocus());

        setText(cvv, "123");

        assertTrue(cardholderName.hasFocus());
    }

    @Test
    public void cvvAdvancesToPostalIfCardholderNameDisabledWhenComplete() {
        mCardForm.cardRequired(false)
                .expirationRequired(false)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_DISABLED)
                .postalCodeRequired(true)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);
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
        mCardForm.cardRequired(true)
                .expirationRequired(false)
                .cvvRequired(false)
                .cardholderName(CardForm.FIELD_DISABLED)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        setText(((CardEditText) mCardForm.findViewById(R.id.bt_card_form_card_number)), VISA);
    }

    @Test
    public void setEnabledSetsStateCorrectly() {
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        assertFieldsEnabled(true, true, true, true, true, true);

        mCardForm.setEnabled(false);
        assertFieldsEnabled(false, false, false, false, false, false);
    }

    @Test
    public void isValid_whenCardHolderNameIsOptionalAndEmpty_returnsTrue() {
        mCardForm.cardRequired(true)
                .cardholderName(CardForm.FIELD_OPTIONAL)
                .setup(mActivity);

        assertFalse(mCardForm.isValid());
        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_card_number)), VISA);
        assertTrue(mCardForm.isValid());
    }

    @Test
    public void isValid_whenCardHolderNameIsRequiredAndEmpty_returnsFalse() {
        mCardForm.cardholderName(CardForm.FIELD_REQUIRED)
                .setup(mActivity);

        assertFalse(mCardForm.isValid());
        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_cardholder_name)), " ");
        assertFalse(mCardForm.isValid());
        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_cardholder_name)), "a");
        assertTrue(mCardForm.isValid());
    }

    @Test
    public void isValidOnlyValidatesRequiredFields() {
        mCardForm.cardRequired(true)
                .expirationRequired(false)
                .cvvRequired(false)
                .cardholderName(CardForm.FIELD_DISABLED)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        assertFalse(mCardForm.isValid());
        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_card_number)), VISA);
        assertTrue(mCardForm.isValid());
        assertFalse(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_card_number)).isError());

        mCardForm.cardRequired(false)
                .expirationRequired(true)
                .cvvRequired(false)
                .cardholderName(CardForm.FIELD_DISABLED)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        assertFalse(mCardForm.isValid());
        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_expiration)), "1230");
        assertTrue(mCardForm.isValid());
        assertFalse(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_expiration)).isError());

        mCardForm.cardRequired(false)
                .expirationRequired(false)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_DISABLED)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        assertFalse(mCardForm.isValid());
        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_cvv)), "123");
        assertTrue(mCardForm.isValid());
        assertFalse(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_cvv)).isError());

        mCardForm.cardRequired(false)
                .expirationRequired(false)
                .cvvRequired(false)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        assertFalse(mCardForm.isValid());
        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_cardholder_name)), "John Doe");
        assertTrue(mCardForm.isValid());
        assertFalse(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_cardholder_name)).isError());

        mCardForm.cardRequired(false)
                .expirationRequired(false)
                .cvvRequired(false)
                .cardholderName(CardForm.FIELD_DISABLED)
                .postalCodeRequired(true)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        assertFalse(mCardForm.isValid());
        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_postal_code)), "12345");
        assertTrue(mCardForm.isValid());
        assertFalse(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_postal_code)).isError());

        mCardForm.cardRequired(false)
                .expirationRequired(false)
                .cvvRequired(false)
                .cardholderName(CardForm.FIELD_DISABLED)
                .postalCodeRequired(false)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        assertFalse(mCardForm.isValid());
        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_country_code)), "123");
        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_mobile_number)), "12345678");
        assertTrue(mCardForm.isValid());
        assertFalse(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_country_code)).isError());
        assertFalse(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_mobile_number)).isError());
    }

    @Test
    public void validateSetsErrorOnFields() {
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        assertFieldsError(false, false, false, false, false, false, false);

        mCardForm.validate();

        assertFieldsError(true, true, true, true, true, true, true);
    }

    @Test
    public void getCardNumber_returnsCardNumber() {
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_card_number)), VISA);

        assertEquals(VISA, mCardForm.getCardNumber());
    }

    @Test
    public void getExpirationMonth_returnsExpirationMonth() {
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_expiration)), "1230");

        assertEquals("12", mCardForm.getExpirationMonth());
    }

    @Test
    public void getExpirationYear_returnsExpirationYear() {
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_expiration)), "1230");

        assertEquals("30", mCardForm.getExpirationYear());
    }

    @Test
    public void getCvv_returnsCvv() {
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_cvv)), "123");

        assertEquals("123", mCardForm.getCvv());
    }

    @Test
    public void getName_returnsName() {
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_cardholder_name)), "John Doe");

        assertEquals("John Doe", mCardForm.getCardholderName());
    }

    @Test
    public void getPostalCode_returnsPostalCode() {
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_postal_code)), "12345");

        assertEquals("12345", mCardForm.getPostalCode());
    }

    @Test
    public void getCountryCode_returnsCountryCode() {
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_country_code)), "86");

        assertEquals("86", mCardForm.getCountryCode());
    }

    @Test
    public void getMobileNumber_returnsMobileNumber() {
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_mobile_number)), "5555555555");

        assertEquals("5555555555", mCardForm.getMobileNumber());
    }

    @Test
    public void setCardNumberError_setsError() {
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        assertFalse(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_card_number)).isError());

        mCardForm.setCardNumberError("Error");

        assertTrue(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_card_number)).isError());
        assertTrue((mCardForm.findViewById(R.id.bt_card_form_card_number)).isFocused());
    }

    @Test
    public void setExpirationError_setsError() {
        mCardForm.cardRequired(false)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        assertFalse(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_expiration)).isError());

        mCardForm.setExpirationError("Error");

        assertTrue(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_expiration)).isError());
        assertTrue((mCardForm.findViewById(R.id.bt_card_form_expiration)).isFocused());
    }

    @Test
    public void setExpirationError_doesNotRequestFocusIfCardNumberIsAlreadyFocused() {
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        mCardForm.findViewById(R.id.bt_card_form_card_number).requestFocus();
        mCardForm.setExpirationError("Error");

        assertTrue(mCardForm.findViewById(R.id.bt_card_form_card_number).isFocused());
        assertFalse(mCardForm.findViewById(R.id.bt_card_form_expiration).isFocused());
    }

    @Test
    public void setCvvError_setsError() {
        mCardForm.cardRequired(false)
                .expirationRequired(false)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        assertFalse(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_cvv)).isError());
        mCardForm.setCvvError("Error");
        assertTrue(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_cvv)).isError());
        assertTrue((mCardForm.findViewById(R.id.bt_card_form_cvv)).isFocused());
    }

    @Test
    public void setCvvError_doesNotRequestFocusIfCardNumberOrExpirationIsAlreadyFocused() {
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

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
    public void setCardholderNameError_setsError() {
        mCardForm.cardRequired(false)
                .expirationRequired(false)
                .cvvRequired(false)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        assertFalse(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_cardholder_name)).isError());
        mCardForm.setCardholderNameError("Error");
        assertTrue(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_cardholder_name)).isError());
        assertTrue((mCardForm.findViewById(R.id.bt_card_form_cardholder_name)).isFocused());
    }

    @Test
    public void setPostalCodeError_setsError() {
        mCardForm.cardRequired(false)
                .expirationRequired(false)
                .cvvRequired(false)
                .cardholderName(CardForm.FIELD_DISABLED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        assertFalse(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_postal_code)).isError());
        mCardForm.setPostalCodeError("Error");
        assertTrue(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_postal_code)).isError());
        assertTrue((mCardForm.findViewById(R.id.bt_card_form_postal_code)).isFocused());
    }

    @Test
    public void setPostalCodeError_doesNotRequestFocusIfCardNumberCvvOrExpirationIsAlreadyFocused() {
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

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
        mCardForm.cardRequired(false)
                .expirationRequired(false)
                .cvvRequired(false)
                .cardholderName(CardForm.FIELD_DISABLED)
                .postalCodeRequired(false)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        assertFalse(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_country_code)).isError());
        mCardForm.setCountryCodeError("Error");
        assertTrue(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_country_code)).isError());
        assertTrue((mCardForm.findViewById(R.id.bt_card_form_country_code)).isFocused());
    }

    @Test
    public void setCountryCodeError_doesNotRequestFocusIfCardNumberExpirationCvvOrCardholderNameOrPostalIsAlreadyFocused() {
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

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

        mCardForm.findViewById(R.id.bt_card_form_cardholder_name).requestFocus();
        mCardForm.setMobileNumberError("Error");
        assertTrue(mCardForm.findViewById(R.id.bt_card_form_cardholder_name).isFocused());
        assertFalse(mCardForm.findViewById(R.id.bt_card_form_postal_code).isFocused());

        mCardForm.findViewById(R.id.bt_card_form_postal_code).requestFocus();
        mCardForm.setMobileNumberError("Error");
        assertTrue(mCardForm.findViewById(R.id.bt_card_form_postal_code).isFocused());
        assertFalse(mCardForm.findViewById(R.id.bt_card_form_country_code).isFocused());
    }

    @Test
    public void setMobileNumberError_setsError() {
        mCardForm.cardRequired(false)
                .expirationRequired(false)
                .cvvRequired(false)
                .cardholderName(CardForm.FIELD_DISABLED)
                .postalCodeRequired(false)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        assertFalse(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_mobile_number)).isError());
        mCardForm.setMobileNumberError("Error");
        assertTrue(((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_mobile_number)).isError());
        assertTrue((mCardForm.findViewById(R.id.bt_card_form_mobile_number)).isFocused());
    }

    @Test
    public void setMobileNumberError_doesNotRequestFocusIfCardNumberExpirationCvvPostalOrCountryCodeIsAlreadyFocused() {
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

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

        mCardForm.findViewById(R.id.bt_card_form_cardholder_name).requestFocus();
        mCardForm.setMobileNumberError("Error");
        assertTrue(mCardForm.findViewById(R.id.bt_card_form_cardholder_name).isFocused());
        assertFalse(mCardForm.findViewById(R.id.bt_card_form_mobile_number).isFocused());

        mCardForm.findViewById(R.id.bt_card_form_postal_code).requestFocus();
        mCardForm.setMobileNumberError("Error");
        assertTrue(mCardForm.findViewById(R.id.bt_card_form_postal_code).isFocused());
        assertFalse(mCardForm.findViewById(R.id.bt_card_form_cardholder_name).isFocused());

        mCardForm.findViewById(R.id.bt_card_form_country_code).requestFocus();
        mCardForm.setMobileNumberError("Error");
        assertTrue(mCardForm.findViewById(R.id.bt_card_form_country_code).isFocused());
        assertFalse(mCardForm.findViewById(R.id.bt_card_form_mobile_number).isFocused());
    }

    @Test
    public void marksCardNumberAsErrorWhenFocusChangesAndCardNumberFailsValidation() {
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);
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
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);
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
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);
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
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);
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
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);
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
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);
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
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);
        ExpirationDateEditText expirationDateEditText =
                mCardForm.findViewById(R.id.bt_card_form_expiration);
        expirationDateEditText.requestFocus();
        assertTrue(expirationDateEditText.isFocused());
        assertFalse(expirationDateEditText.isError());

        mCardForm.findViewById(R.id.bt_card_form_card_number).requestFocus();
        assertFalse(expirationDateEditText.isFocused());
        assertFalse(expirationDateEditText.isError());
    }

    @Test
    public void doesNotMarkCvvAsErrorWhenFocusChangesAndCvvEmpty() {
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);
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
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);
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
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);
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
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);
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
        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_cardholder_name)), "John Doe");
        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_postal_code)), "12345");
        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_country_code)), "123");
        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_mobile_number)), "12345678");

        assertEquals(1, counter.get());

        setText(((EditText) mCardForm.findViewById(R.id.bt_card_form_cvv)), "12");

        assertEquals(2, counter.get());
    }

    @Test
    public void onCardTypeChangeListenerIsCalledWhenCardTypeChanges() {
        mCardForm.cardRequired(true)
                .expirationRequired(false)
                .cvvRequired(false)
                .cardholderName(CardForm.FIELD_DISABLED)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);
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
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);
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
        mCardForm.findViewById(R.id.bt_card_form_cardholder_name).requestFocus();
        mCardForm.findViewById(R.id.bt_card_form_postal_code).requestFocus();
        mCardForm.findViewById(R.id.bt_card_form_mobile_number).requestFocus();

        assertEquals(6, called.get());
    }

    @Test
    public void testDoesNotCrashWhenNoListenersAreSet() {
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        setText(((CardEditText) (mCardForm.findViewById(R.id.bt_card_form_card_number))), VISA);
    }

    @Test
    public void hintsAreDisplayed() {
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        assertTextHintIs(mCardForm.findViewById(R.id.bt_card_form_card_number), R.string.bt_form_hint_card_number);
        assertTextHintIs(mCardForm.findViewById(R.id.bt_card_form_expiration), R.string.bt_form_hint_expiration);
        assertTextHintIs(mCardForm.findViewById(R.id.bt_card_form_cvv), R.string.bt_form_hint_cvv);
        assertTextHintIs(mCardForm.findViewById(R.id.bt_card_form_cardholder_name), R.string.bt_form_hint_cardholder_name);
        assertTextHintIs(mCardForm.findViewById(R.id.bt_card_form_postal_code), R.string.bt_form_hint_postal_code);
        assertTextHintIs(mCardForm.findViewById(R.id.bt_card_form_mobile_number), R.string.bt_form_hint_mobile_number);
    }

    @Test
    public void correctCardHintsAreDisplayed() {
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

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
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        setText((EditText) mActivity.findViewById(R.id.bt_card_form_card_number), VISA);
        setText((EditText) mActivity.findViewById(R.id.bt_card_form_expiration), "1220");
        setText((EditText) mActivity.findViewById(R.id.bt_card_form_cvv), "123");
        setText((EditText) mActivity.findViewById(R.id.bt_card_form_cardholder_name), "John Doe");
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
        assertEquals("John Doe", ((EditText) mActivity.findViewById(R.id.bt_card_form_cardholder_name)).getText().toString());
        assertEquals("12345", ((EditText) mActivity.findViewById(R.id.bt_card_form_postal_code)).getText().toString());
        assertEquals("1 234-567-8", ((EditText) mActivity.findViewById(R.id.bt_card_form_mobile_number)).getText().toString());
    }

    @Test
    public void handleCardIOResponse_doesNothingIfDataIsNull() {
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        mCardForm.handleCardIOResponse(Integer.MIN_VALUE, null);

        assertEquals("", ((EditText) mActivity.findViewById(R.id.bt_card_form_card_number)).getText().toString());
        assertEquals("", ((EditText) mActivity.findViewById(R.id.bt_card_form_expiration)).getText().toString());
        assertEquals("", ((EditText) mActivity.findViewById(R.id.bt_card_form_cvv)).getText().toString());
        assertEquals("", ((EditText) mActivity.findViewById(R.id.bt_card_form_cardholder_name)).getText().toString());
        assertEquals("", ((EditText) mActivity.findViewById(R.id.bt_card_form_postal_code)).getText().toString());
        assertEquals("", ((EditText) mActivity.findViewById(R.id.bt_card_form_mobile_number)).getText().toString());
    }

    @Test
    public void handleCardIOResponse_whenResultCancelled_clearsFragment() throws Exception {
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        Field mCardScanningFragmentField = CardForm.class
                .getDeclaredField("mCardScanningFragment");
        mCardScanningFragmentField.setAccessible(true);
        mCardScanningFragmentField.set(mCardForm, new CardScanningFragment());

        mCardForm.handleCardIOResponse(Activity.RESULT_CANCELED, null);

        assertNull(mCardScanningFragmentField.get(mCardForm));
    }

    @Test
    public void handleCardIOResponse_whenResultOK_clearsFragment() throws Exception {
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        Field mCardScanningFragmentField = CardForm.class
                .getDeclaredField("mCardScanningFragment");
        mCardScanningFragmentField.setAccessible(true);
        mCardScanningFragmentField.set(mCardForm, new CardScanningFragment());
        mCardForm.handleCardIOResponse(Activity.RESULT_OK, null);

        assertNull(mCardScanningFragmentField.get(mCardForm));
    }

    @Test
    public void handleCardIOResponse_whenResultNotOkOrCancelled_retainsFragment() throws Exception {
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        Field mCardScanningFragmentField = CardForm.class
                .getDeclaredField("mCardScanningFragment");
        mCardScanningFragmentField.setAccessible(true);
        mCardScanningFragmentField.set(mCardForm, new CardScanningFragment());

        mCardForm.handleCardIOResponse(Activity.RESULT_FIRST_USER, null);

        assertNotNull(mCardScanningFragmentField.get(mCardForm));
    }

    @Test
    public void handleCardIOResponse_doesNothingIfNoCardIOResponseIsPresent() {
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);

        mCardForm.handleCardIOResponse(Integer.MIN_VALUE, new Intent());

        assertEquals("", ((EditText) mActivity.findViewById(R.id.bt_card_form_card_number)).getText().toString());
        assertEquals("", ((EditText) mActivity.findViewById(R.id.bt_card_form_expiration)).getText().toString());
        assertEquals("", ((EditText) mActivity.findViewById(R.id.bt_card_form_cvv)).getText().toString());
        assertEquals("", ((EditText) mActivity.findViewById(R.id.bt_card_form_cardholder_name)).getText().toString());
        assertEquals("", ((EditText) mActivity.findViewById(R.id.bt_card_form_postal_code)).getText().toString());
        assertEquals("", ((EditText) mActivity.findViewById(R.id.bt_card_form_mobile_number)).getText().toString());
    }

    @Test
    public void handleCardIOResponse_setsCardNumber() {
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(false)
                .cardholderName(CardForm.FIELD_DISABLED)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);
        Intent intent = new Intent()
                .putExtra(CardIOActivity.EXTRA_SCAN_RESULT, new CreditCard(VISA, 0, 0, "", "", ""));

        mCardForm.handleCardIOResponse(Activity.RESULT_OK, intent);

        assertEquals(VISA, ((EditText) mActivity.findViewById(R.id.bt_card_form_card_number)).getText().toString());
        assertTrue(mCardForm.findViewById(R.id.bt_card_form_expiration).hasFocus());
    }

    @Test
    public void handleCardIOResponse_doesNotSetCardNumberIfCardNumberNotRequired() {
        mCardForm.cardRequired(false)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);
        Intent intent = new Intent()
                .putExtra(CardIOActivity.EXTRA_SCAN_RESULT, new CreditCard(VISA, 0, 0, "", "", ""));

        mCardForm.handleCardIOResponse(Activity.RESULT_OK, intent);

        assertEquals("", ((EditText) mActivity.findViewById(R.id.bt_card_form_card_number)).getText().toString());
    }

    @Test
    public void handleCardIOResponse_setsExpirationDate() {
        mCardForm.cardRequired(false)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_DISABLED)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);
        Intent intent = new Intent()
                .putExtra(CardIOActivity.EXTRA_SCAN_RESULT, new CreditCard(VISA, 12, 2020, "", "", ""));

        mCardForm.handleCardIOResponse(Activity.RESULT_OK, intent);

        assertEquals("122020", ((EditText) mActivity.findViewById(R.id.bt_card_form_expiration)).getText().toString());
        assertTrue(mCardForm.findViewById(R.id.bt_card_form_cvv).hasFocus());
    }

    @Test
    public void handleCardIOResponse_handlesSingleDigitExpirationMonths() {
        mCardForm.cardRequired(false)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_DISABLED)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);
        Intent intent = new Intent()
                .putExtra(CardIOActivity.EXTRA_SCAN_RESULT, new CreditCard(VISA, 7, 2020, "", "", ""));

        mCardForm.handleCardIOResponse(Activity.RESULT_OK, intent);

        assertEquals("07", ((ExpirationDateEditText) mActivity.findViewById(R.id.bt_card_form_expiration)).getMonth());
        assertEquals("2020", ((ExpirationDateEditText) mActivity.findViewById(R.id.bt_card_form_expiration)).getYear());
        assertTrue(mCardForm.findViewById(R.id.bt_card_form_cvv).hasFocus());
    }

    @Test
    public void handleCardIOResponse_doesNotSetExpirationDateIfExpirationDateInvalid() {
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(false)
                .cardholderName(CardForm.FIELD_DISABLED)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);
        Intent intent = new Intent()
                .putExtra(CardIOActivity.EXTRA_SCAN_RESULT, new CreditCard(VISA, 12, 2000, "", "", ""));

        mCardForm.handleCardIOResponse(Activity.RESULT_OK, intent);

        assertEquals("", ((EditText) mActivity.findViewById(R.id.bt_card_form_expiration)).getText().toString());
        assertTrue(mCardForm.findViewById(R.id.bt_card_form_expiration).hasFocus());
    }

    @Test
    public void handleCardIOResponse_doesNotSetExpirationDateIfExpirationDateNotRequired() {
        mCardForm.cardRequired(true)
                .expirationRequired(false)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_DISABLED)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("Make sure SMS is supported")
                .setup(mActivity);
        Intent intent = new Intent()
                .putExtra(CardIOActivity.EXTRA_SCAN_RESULT, new CreditCard(VISA, 12, 2020, "", "", ""));

        mCardForm.handleCardIOResponse(Activity.RESULT_OK, intent);

        assertEquals("", ((EditText) mActivity.findViewById(R.id.bt_card_form_expiration)).getText().toString());
        assertTrue(mCardForm.findViewById(R.id.bt_card_form_cvv).hasFocus());
    }

    private void assertFieldsVisible(int cardNumberVisible, int expirationVisible, int cvvVisible, int cardholderNameVisible,
                                     int postalCodeVisible, int mobileNumberVisible) {
        assertEquals(cardNumberVisible, mCardForm.findViewById(R.id.bt_card_form_card_number_icon).getVisibility());
        assertEquals(cardNumberVisible, mCardForm.findViewById(R.id.bt_card_form_card_number).getVisibility());

        assertEquals(expirationVisible, mCardForm.findViewById(R.id.bt_card_form_expiration).getVisibility());
        assertEquals(cvvVisible, mCardForm.findViewById(R.id.bt_card_form_cvv).getVisibility());

        assertEquals(cardholderNameVisible, mCardForm.findViewById(R.id.bt_card_form_cardholder_name_icon).getVisibility());
        assertEquals(cardholderNameVisible, mCardForm.findViewById(R.id.bt_card_form_cardholder_name).getVisibility());

        assertEquals(postalCodeVisible, mCardForm.findViewById(R.id.bt_card_form_postal_code_icon).getVisibility());
        assertEquals(postalCodeVisible, mCardForm.findViewById(R.id.bt_card_form_postal_code).getVisibility());

        assertEquals(mobileNumberVisible, mCardForm.findViewById(R.id.bt_card_form_mobile_number_icon).getVisibility());
        assertEquals(mobileNumberVisible, mCardForm.findViewById(R.id.bt_card_form_country_code).getVisibility());
        assertEquals(mobileNumberVisible, mCardForm.findViewById(R.id.bt_card_form_mobile_number).getVisibility());
        assertEquals(mobileNumberVisible, mCardForm.findViewById(R.id.bt_card_form_mobile_number_explanation).getVisibility());
    }

    private void assertFieldsEnabled(boolean cardNumberEnabled, boolean expirationEnabled,
                                     boolean cvvEnabled, boolean cardholderNameEnabled, boolean postalCodeEnabled,
                                     boolean mobileNumberEnabled) {
        assertEquals(cardNumberEnabled, mCardForm.findViewById(R.id.bt_card_form_card_number).isEnabled());
        assertEquals(expirationEnabled, mCardForm.findViewById(R.id.bt_card_form_expiration).isEnabled());
        assertEquals(cvvEnabled, mCardForm.findViewById(R.id.bt_card_form_cvv).isEnabled());
        assertEquals(cardholderNameEnabled, mCardForm.findViewById(R.id.bt_card_form_cardholder_name).isEnabled());
        assertEquals(postalCodeEnabled, mCardForm.findViewById(R.id.bt_card_form_postal_code).isEnabled());
        assertEquals(mobileNumberEnabled, mCardForm.findViewById(R.id.bt_card_form_mobile_number).isEnabled());
    }

    private void assertFieldsError(boolean cardNumberEnabled, boolean expirationEnabled,
                                   boolean cvvEnabled, boolean cardholderNameEnabled, boolean postalCodeEnabled,
                                   boolean countryCodeEnabled, boolean mobileNumberEnabled) {
        assertEquals(cardNumberEnabled, ((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_card_number)).isError());
        assertEquals(expirationEnabled, ((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_expiration)).isError());
        assertEquals(cvvEnabled, ((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_cvv)).isError());
        assertEquals(cardholderNameEnabled, ((ErrorEditText) mCardForm.findViewById(R.id.bt_card_form_cardholder_name)).isError());
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
