package com.splendo.kaluga.example

import android.app.Application
import com.splendo.kaluga.base.ApplicationHolder

class ExampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ApplicationHolder.application = this
    }
}