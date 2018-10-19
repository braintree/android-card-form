package com.braintreepayments.cardform;

import android.content.Intent;
import android.os.Bundle;

import com.braintreepayments.cardform.utils.ColorUtils;
import com.braintreepayments.cardform.view.CardForm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import io.card.payment.CardIOActivity;

public class CardScanningFragment extends Fragment {

    private static final int CARD_IO_REQUEST_CODE = 12398;
    public static final String TAG = "com.braintreepayments.cardform.CardScanningFragment";

    private CardForm mCardForm;

    public static CardScanningFragment requestScan(AppCompatActivity activity, CardForm cardForm) {
        CardScanningFragment fragment = (CardScanningFragment) activity.getSupportFragmentManager()
                .findFragmentByTag(TAG);

        if (fragment != null) {
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .remove(fragment)
                    .commit();
        }

        fragment = new CardScanningFragment();
        fragment.mCardForm = cardForm;

        activity.getSupportFragmentManager()
                .beginTransaction()
                .add(fragment, TAG)
                .commit();

        return fragment;
    }

    public void setCardForm(CardForm cardForm) {
        mCardForm = cardForm;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("resuming", false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if (savedInstanceState != null && savedInstanceState.getBoolean("resuming")) {
            return;
        }

        Intent scanIntent = new Intent(getActivity(), CardIOActivity.class)
                .putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO, true)
                .putExtra(CardIOActivity.EXTRA_USE_PAYPAL_ACTIONBAR_ICON, false)
                .putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, true)
                .putExtra(CardIOActivity.EXTRA_SUPPRESS_CONFIRMATION, true)
                .putExtra(CardIOActivity.EXTRA_SCAN_EXPIRY, true)
                .putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false)
                .putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false)
                .putExtra(CardIOActivity.EXTRA_GUIDE_COLOR,
                        ColorUtils.getColor(getActivity(), "colorAccent", R.color.bt_blue));

        startActivityForResult(scanIntent, CARD_IO_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CARD_IO_REQUEST_CODE) {
            mCardForm.handleCardIOResponse(resultCode, data);

            if (getActivity() != null) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .remove(this)
                        .commit();
            }
        }
    }
}
