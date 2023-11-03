package com.braintreepayments.sample;

import static com.braintreepayments.sample.BaseCardFormActivity.SUPPORTED_CARD_TYPES;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;

import com.braintreepayments.cardform.view.CardForm;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class SampleActivity extends AppCompatActivity {
    private static final int VIBRATE_PERMISSION_REQUEST = 10;

    private CheckBox mVibrateEnabledCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_activity_layout);

        mVibrateEnabledCheckbox = findViewById(R.id.vibrate_permission_enabled);

        updateVibrateEnabledCheckbox();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.material_default_theme_form:
                startActivity(new Intent(this, DefaultThemeActivity.class));
                break;
            case R.id.material_custom_theme_form:
                startActivity(new Intent(this, CustomThemeActivity.class));
                break;
            case R.id.vibrate_permission_enabled:
                enableVibratePermission();
            case R.id.bottom_sheet_form:
                displayBottomSheet();
            default:
                break;
        }
    }

    private void updateVibrateEnabledCheckbox() {
        mVibrateEnabledCheckbox.setChecked(
                ContextCompat.checkSelfPermission(this, Manifest.permission.VIBRATE)
                        == PackageManager.PERMISSION_GRANTED);
    }

    private void enableVibratePermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.VIBRATE},
                VIBRATE_PERMISSION_REQUEST);
    }

    private void displayBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.card_form);
        CardForm cardForm = bottomSheetDialog.findViewById(R.id.card_form);
        cardForm.cardRequired(true)
                .maskCardNumber(true)
                .maskCvv(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .saveCardCheckBoxChecked(true)
                .saveCardCheckBoxVisible(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .mobileNumberExplanation("Make sure SMS is enabled for this mobile number")
                .actionLabel(getString(R.string.purchase))
                .setSupportedCardTypes(SUPPORTED_CARD_TYPES)
                .setup(this);
        bottomSheetDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        updateVibrateEnabledCheckbox();
    }
}
