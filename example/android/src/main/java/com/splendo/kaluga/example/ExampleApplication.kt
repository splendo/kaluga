package com.splendo.kaluga.example

import android.app.Application
import com.splendo.kaluga.base.ContextProvider

class ExampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ContextProvider.application = this
    }
}