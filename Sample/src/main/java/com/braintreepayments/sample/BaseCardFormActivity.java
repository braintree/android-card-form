package com.braintreepayments.sample;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.braintreepayments.cardform.OnCardFormSubmitListener;
import com.braintreepayments.cardform.utils.CardType;
import com.braintreepayments.cardform.view.CardEditText;
import com.braintreepayments.cardform.view.CardForm;
import com.braintreepayments.cardform.view.SupportedCardTypesView;

public class BaseCardFormActivity extends AppCompatActivity implements OnCardFormSubmitListener {

  private static final CardType[] SUPPORTED_CARD_TYPES = {CardType.VISA, CardType.MASTERCARD, CardType.DISCOVER,
      CardType.AMEX, CardType.DINERS_CLUB, CardType.JCB, CardType.MAESTRO, CardType.UNIONPAY};

  protected CardForm mCardForm;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.card_form);

    mCardForm = findViewById(R.id.card_form);
    mCardForm
        .maskCardNumber(true)
        .maskCvv(true)
        .setup(this);
    mCardForm.setOnCardFormSubmitListener(this);

    // Warning: this is for development purposes only and should never be done outside of this example app.
    // Failure to set FLAG_SECURE exposes your app to screenshots allowing other apps to steal card information.
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
  }

  @Override
  public void onCardFormSubmit() {
    if (mCardForm.isValid()) {
      Toast.makeText(this, R.string.valid, Toast.LENGTH_SHORT).show();
    } else {
      mCardForm.validate();
      Toast.makeText(this, R.string.invalid, Toast.LENGTH_SHORT).show();
    }
  }

}
