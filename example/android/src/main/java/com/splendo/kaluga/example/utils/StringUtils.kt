package com.splendo.kaluga.example.utils

import android.content.Context

fun Context.stringByKey(key: String): String {
    val identifier = resources.getIdentifier(key, "string", packageName)
    return getString(identifier)
}