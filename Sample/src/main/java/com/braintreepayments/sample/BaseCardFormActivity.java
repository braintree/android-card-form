package com.braintreepayments.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.braintreepayments.cardform.OnCardFormSubmitListener;
import com.braintreepayments.cardform.utils.CardType;
import com.braintreepayments.cardform.view.CardForm;
import com.braintreepayments.cardform.view.SupportedCardTypesView;

public class BaseCardFormActivity extends Activity implements OnCardFormSubmitListener {

    private CardForm mCardForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_form);

        SupportedCardTypesView supportedCardTypesView = (SupportedCardTypesView) findViewById(R.id.supported_card_types);
        supportedCardTypesView.setSupportedCardTypes(CardType.VISA, CardType.MASTERCARD, CardType.DISCOVER,
                CardType.AMEX, CardType.DINERS_CLUB, CardType.JCB, CardType.MAESTRO, CardType.UNION_PAY);

        mCardForm = (CardForm) findViewById(R.id.card_form);
        mCardForm.setRequiredFields(this, true, true, true, true, getString(R.string.purchase));
        mCardForm.setOnCardFormSubmitListener(this);
    }

    @Override
    public void onCardFormSubmit() {
        if (mCardForm.isValid()) {
            Toast.makeText(this, R.string.valid, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.invalid, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        if (mCardForm.isCardScanningAvailable()) {
            getMenuInflater().inflate(R.menu.card_io, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.card_io_item) {
            mCardForm.scanCard(this);
            return true;
        }

        return false;
    }
}
