package com.braintreepayments.cardform.test;

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.braintreepayments.cardform.view.CardForm;

public class TestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(android.support.v7.appcompat.R.style.Theme_AppCompat);

        CardForm cardForm = new CardForm(this);
        cardForm.setId(android.R.id.custom);
        ((FrameLayout) findViewById(android.R.id.content)).addView(cardForm);
    }
}
