package com.braintreepayments.sample;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class SampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
