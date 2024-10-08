package com.braintreepayments.sample;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

        // Support Edge-to-Edge layout in Android 15
        // Ref: https://developer.android.com/develop/ui/views/layout/edge-to-edge#cutout-insets
        View navHostView = findViewById(R.id.content);
        ViewCompat.setOnApplyWindowInsetsListener(navHostView, (v, insets) -> {
            @WindowInsetsCompat.Type.InsetsType int insetTypeMask =
                    WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout();
            Insets bars = insets.getInsets(insetTypeMask);
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });
    }

    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.material_light_theme_form) {
            startActivity(new Intent(this, LightThemeActivity.class));
        } else if (viewId == R.id.material_dark_theme_form) {
            startActivity(new Intent(this, DarkThemeActivity.class));
        } else if (viewId == R.id.vibrate_permission_enabled) {
            enableVibratePermission();
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
