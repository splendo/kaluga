package com.splendo.kaluga.base

import android.app.Application
import android.content.Context

class ContextProvider {
    companion object {
        var application: Application? = null

        val context: Context
            get() {
                val context = application
                checkNotNull(context) { "Application was never set" }
                return context
            }
    }
}