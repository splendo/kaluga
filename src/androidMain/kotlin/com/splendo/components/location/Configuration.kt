package com.splendo.components.location

import android.app.Application
import android.content.Context

actual data class Configuration (
    val context: Context
) {
    actual companion object {
        actual val default = Configuration(context = instance!!)
    }
}

private var instance: Application? = null
private fun Application.onCreate() {
    instance = this
    println(" 🗿 - app onCreate")
}