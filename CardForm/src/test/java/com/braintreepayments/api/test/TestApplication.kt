package com.braintreepayments.api.test

import android.app.Application
import androidx.appcompat.R

class TestApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        setTheme(R.style.Theme_AppCompat)
    }
}