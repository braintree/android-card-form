package com.braintreepayments.sample;

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
            case R.id.material_light_theme_form:
                startActivity(new Intent(this, LightThemeActivity.class));
                break;
            case R.id.material_dark_theme_form:
                startActivity(new Intent(this, DarkThemeActivity.class));
                break;
            case R.id.vibrate_permission_enabled:
                enableVibratePermission();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        updateVibrateEnabledCheckbox();
    }
}
