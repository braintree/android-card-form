package com.braintreepayments.cardform.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Vibrator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class VibrationHelperTest {

    @Test
    public void vibrate_callsVibrateIfHasVibratePermissionIsTrue() {
        PackageManager packageManager = mock(PackageManager.class);
        when(packageManager.checkPermission(eq(Manifest.permission.VIBRATE), anyString()))
                .thenReturn(PackageManager.PERMISSION_GRANTED);
        Vibrator vibrator = mock(Vibrator.class);
        Context context = mock(Context.class);
        when(context.getPackageManager()).thenReturn(packageManager);
        when(context.getSystemService(Context.VIBRATOR_SERVICE)).thenReturn(vibrator);

        VibrationHelper.vibrate(context, 1000);

        verify(vibrator).vibrate(1000);
    }

    @Test
    public void vibrate_doesNotCallVibrateIfHasVibratePermissionIsFalse() {
        PackageManager packageManager = mock(PackageManager.class);
        when(packageManager.checkPermission(eq(Manifest.permission.VIBRATE), anyString()))
                .thenReturn(PackageManager.PERMISSION_DENIED);
        Vibrator vibrator = mock(Vibrator.class);
        Context context = mock(Context.class);
        when(context.getPackageManager()).thenReturn(packageManager);
        when(context.getPackageName()).thenReturn("com.braintreepayments.cardform.test");
        when(context.getSystemService(Context.VIBRATOR_SERVICE)).thenReturn(vibrator);

        VibrationHelper.vibrate(context, 1000);

        verifyZeroInteractions(vibrator);
    }

    @Test
    public void hasVibratePermission_returnsTrueIfVibratePermissionInManifest() {
        PackageManager packageManager = mock(PackageManager.class);
        when(packageManager.checkPermission(eq(Manifest.permission.VIBRATE), anyString()))
                .thenReturn(PackageManager.PERMISSION_GRANTED);
        Context context = mock(Context.class);
        when(context.getPackageManager()).thenReturn(packageManager);

        assertTrue(VibrationHelper.hasVibrationPermission(context));
    }

    @Test
    public void hasVibratePermission_returnsFalseIfVibratePermissionNotInManifest() {
        PackageManager packageManager = mock(PackageManager.class);
        when(packageManager.checkPermission(eq(Manifest.permission.VIBRATE), anyString()))
                .thenReturn(PackageManager.PERMISSION_DENIED);
        Context context = mock(Context.class);
        when(context.getPackageManager()).thenReturn(packageManager);
        when(context.getPackageName()).thenReturn("com.braintreepayments.cardform.test");

        assertFalse(VibrationHelper.hasVibrationPermission(context));
    }
}
