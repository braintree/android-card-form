package com.braintreepayments.cardform.test;

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.braintreepayments.cardform.view.CardForm;

public class TestActivity extends Activity {

    public static final String SETUP_FORM = "setup_form";
    public static final String CREDIT_CARD = "credit_card";
    public static final String EXPIRATION = "expiration";
    public static final String CVV = "cvv";
    public static final String POSTAL_CODE = "postal_code";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CardForm cardForm = new CardForm(this);
        cardForm.setId(android.R.id.custom);

        if (getIntent().getBooleanExtra(SETUP_FORM, false)) {
            cardForm.setRequiredFields(this,
                    getIntent().getBooleanExtra(CREDIT_CARD, true),
                    getIntent().getBooleanExtra(EXPIRATION, true),
                    getIntent().getBooleanExtra(CVV, true),
                    getIntent().getBooleanExtra(POSTAL_CODE, true),
                    "Purchase");
        }

        ((FrameLayout) findViewById(android.R.id.content)).addView(cardForm);
    }
}
