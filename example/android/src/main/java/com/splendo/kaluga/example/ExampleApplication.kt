package com.splendo.kaluga.example

import android.app.Application
import com.splendo.kaluga.base.ApplicationHolder
import com.splendo.kaluga.example.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ExampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ApplicationHolder.application = this

        startKoin {
            androidContext(this@ExampleApplication)
            modules(viewModelModule)
        }
    }
}