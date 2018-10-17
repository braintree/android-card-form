package com.braintreepayments.cardform.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build.VERSION_CODES;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.braintreepayments.cardform.OnCardFormSubmitListener;
import com.braintreepayments.cardform.OnCardFormValidListener;
import com.braintreepayments.cardform.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;


public class CardForm extends LinearLayout implements
    OnEditorActionListener, TextWatcher {

  private List<ErrorEditText> mVisibleEditTexts;

  private CardEditText mCardNumber;
  private ExpirationDateEditText mExpiration;
  private CardHolderNameEditText mCardHolderName;
  private CvvEditText mCvv;

  private boolean mValid = false;

  private OnCardFormValidListener mOnCardFormValidListener;
  private OnCardFormSubmitListener mOnCardFormSubmitListener;

  public CardForm(Context context) {
    super(context);
    init();
  }

  public CardForm(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public CardForm(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  @TargetApi(VERSION_CODES.LOLLIPOP)
  public CardForm(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init();
  }

  private void init() {
    setVisibility(GONE);
    setOrientation(VERTICAL);

    inflate(getContext(), R.layout.bt_card_form_fields, this);

    mCardNumber = findViewById(R.id.bt_card_form_card_number);
    mExpiration = findViewById(R.id.bt_card_form_expiration);
    mCardHolderName = findViewById(R.id.bt_card_form_card_holder_name);
    mCvv = findViewById(R.id.bt_card_form_cvv);

    mVisibleEditTexts = new ArrayList<>();

    setListeners(mCardHolderName);
    setListeners(mCardNumber);
    setListeners(mExpiration);
    setListeners(mCvv);
  }

  /**
   * @param mask if {@code true}, card number input will be masked.
   */
  public CardForm maskCardNumber(boolean mask) {
    mCardNumber.setMask(mask);
    return this;
  }

  /**
   * @param mask if {@code true}, CVV input will be masked.
   */
  public CardForm maskCvv(boolean mask) {
    mCvv.setMask(mask);
    return this;
  }

  /**
   * Sets up the card form for display to the user. If {@link #setup(android.app.Activity)} is not called,
   * the form will not be visible.
   *
   * @param activity Used to set {@link android.view.WindowManager.LayoutParams#FLAG_SECURE} to prevent screenshots
   */
  public void setup(Activity activity) {
    activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                                  WindowManager.LayoutParams.FLAG_SECURE);

    mExpiration.useDialogForExpirationDateEntry(activity, true);

    setFieldVisibility(mCardNumber);
    setFieldVisibility(mExpiration);
    setFieldVisibility(mCvv);

    TextInputEditText editText;
    for (int i = 0; i < mVisibleEditTexts.size(); i++) {
      editText = mVisibleEditTexts.get(i);
      if (i == mVisibleEditTexts.size() - 1) {
        editText.setImeOptions(EditorInfo.IME_ACTION_GO);
        editText.setImeActionLabel(null, EditorInfo.IME_ACTION_GO);
        editText.setOnEditorActionListener(this);
      } else {
        editText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        editText.setImeActionLabel(null, EditorInfo.IME_ACTION_NONE);
        editText.setOnEditorActionListener(null);
      }
    }

    setVisibility(VISIBLE);
  }

  private void setListeners(EditText editText) {
    editText.addTextChangedListener(this);
  }

  private void setViewVisibility(View view) {
    view.setVisibility(VISIBLE);
  }

  private void setFieldVisibility(ErrorEditText editText) {
    setViewVisibility(editText);
    if (editText.getTextInputLayoutParent() != null) {
      setViewVisibility(editText.getTextInputLayoutParent());
    }

    mVisibleEditTexts.add(editText);
  }

  /**
   * Set the listener to receive a callback when the card form becomes valid or invalid
   *
   * @param listener to receive the callback
   */
  public void setOnCardFormValidListener(OnCardFormValidListener listener) {
    mOnCardFormValidListener = listener;
  }

  /**
   * Set the listener to receive a callback when the card form should be submitted.
   * Triggered from a keyboard by a {@link android.view.inputmethod.EditorInfo#IME_ACTION_GO} event
   *
   * @param listener to receive the callback
   */
  public void setOnCardFormSubmitListener(OnCardFormSubmitListener listener) {
    mOnCardFormSubmitListener = listener;
  }

  /**
   * Set {@link android.widget.EditText} fields as enabled or disabled
   *
   * @param enabled {@code true} to enable all required fields, {@code false} to disable all required fields
   */
  public void setEnabled(boolean enabled) {
    mCardNumber.setEnabled(enabled);
    mExpiration.setEnabled(enabled);
    mCvv.setEnabled(enabled);
  }

  /**
   * @return {@code true} if all require fields are valid, otherwise {@code false}
   */
  public boolean isValid() {
    return mCardNumber.isValid()
        && mExpiration.isValid()
        && mCvv.isValid()
        && mCardHolderName.isValid();
  }

  /**
   * @return {@link CardEditText} view in the card form
   */
  public CardEditText getCardEditText() {
    return mCardNumber;
  }

  /**
   * @return {@link ExpirationDateEditText} view in the card form
   */
  public ExpirationDateEditText getExpirationDateEditText() {
    return mExpiration;
  }

  /**
   * @return {@link CvvEditText} view in the card form
   */
  public CvvEditText getCvvEditText() {
    return mCvv;
  }

  private void requestEditTextFocus(EditText editText) {
    editText.requestFocus();
    ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
        .showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
  }

  /**
   * @return the text in the card number field
   */
  public String getCardNumber() {
    return mCardNumber.getText().toString();
  }

  /**
   * @return the text in the card number field
   */
  public String getCardHolderName() {
    return mCardHolderName.getText().toString();
  }

  /**
   * @return the 2-digit month, formatted with a leading zero if necessary from the expiration
   * field. If no month has been specified, an empty string is returned.
   */
  public String getExpirationMonth() {
    return mExpiration.getMonth();
  }

  /**
   * @return the 2- or 4-digit year depending on user input from the expiration field.
   * If no year has been specified, an empty string is returned.
   */
  public String getExpirationYear() {
    return mExpiration.getYear();
  }

  /**
   * r
   *
   * @return the text in the cvv field
   */
  public String getCvv() {
    return mCvv.getText().toString();
  }

  @Override
  public void afterTextChanged(Editable s) {
    boolean valid = isValid();
    if (mValid != valid) {
      mValid = valid;
      if (mOnCardFormValidListener != null) {
        mOnCardFormValidListener.onCardFormValid(valid);
      }
    }
  }

  @Override
  public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
    if (actionId == EditorInfo.IME_ACTION_GO && mOnCardFormSubmitListener != null) {
      mOnCardFormSubmitListener.onCardFormSubmit();
      return true;
    }
    return false;
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {}
}
