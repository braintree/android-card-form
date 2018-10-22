package com.braintreepayments.cardform.test;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.braintreepayments.cardform.view.CardForm;

import androidx.appcompat.app.AppCompatActivity;

public class TestActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(androidx.appcompat.R.style.Theme_AppCompat);

        CardForm cardForm = new CardForm(this);
        cardForm.setId(android.R.id.custom);
        ((FrameLayout) findViewById(android.R.id.content)).addView(cardForm);
    }
}
