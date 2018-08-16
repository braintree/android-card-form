package com.braintreepayments.cardform.view;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;

import com.braintreepayments.cardform.R;

class CardHolderNameEditText extends ErrorEditText implements TextWatcher {

  public CardHolderNameEditText(Context context) {
    super(context);
    init();
  }

  public CardHolderNameEditText(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public CardHolderNameEditText(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  private void init() {
    setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
    addTextChangedListener(this);
  }

  @Override
  public void afterTextChanged(Editable editable) {
    validate();
  }

  @Override
  public boolean isValid() {
    return isOptional() || getText().toString().length() > 0;
  }

  @Override
  public String getErrorMessage() {
    if (TextUtils.isEmpty(getText())) {
      return getContext().getString(R.string.bt_cardholder_name_required);
    } else {
      return getContext().getString(R.string.bt_cardholder_name_invalid);
    }
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
}