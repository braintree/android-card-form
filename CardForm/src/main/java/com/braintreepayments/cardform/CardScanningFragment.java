package com.braintreepayments.cardform;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import com.braintreepayments.cardform.utils.ColorUtils;
import com.braintreepayments.cardform.view.CardForm;

import io.card.payment.CardIOActivity;

public class CardScanningFragment extends Fragment {

    private static final int CARD_IO_REQUEST_CODE = 12398;
    private static final String TAG = "com.braintreepayments.cardform.CardScanningFragment";

    private CardForm mCardForm;

    public static void requestScan(Activity activity, CardForm cardForm) {
        CardScanningFragment fragment = (CardScanningFragment) activity.getFragmentManager().findFragmentByTag(TAG);

        if (fragment != null) {
            activity.getFragmentManager()
                    .beginTransaction()
                    .remove(fragment)
                    .commit();
        }

        fragment = new CardScanningFragment();
        fragment.mCardForm = cardForm;

        activity.getFragmentManager()
                .beginTransaction()
                .add(fragment, TAG)
                .commit();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

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

            if (getActivity() != null) {
                getActivity().getFragmentManager()
                        .beginTransaction()
                        .remove(this)
                        .commit();
            }
        }
    }
}
