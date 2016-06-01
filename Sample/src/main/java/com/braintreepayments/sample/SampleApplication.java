package com.braintreepayments.sample;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

public class SampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}
