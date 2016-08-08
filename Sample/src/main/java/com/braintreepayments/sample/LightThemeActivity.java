package com.braintreepayments.sample;

import android.view.Menu;

public class LightThemeActivity extends BaseCardFormActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        if (mCardForm.isCardScanningAvailable()) {
            getMenuInflater().inflate(R.menu.card_io, menu);
        }

        return true;
    }
}
