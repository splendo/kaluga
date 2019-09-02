package com.splendo.mpp

import android.os.Handler
import android.os.Looper
import kotlinx.coroutines.CoroutineScope

var mainHandler = Handler(Looper.getMainLooper())

actual fun runOnMain(block: () -> Unit) {
    mainHandler.post(block)
}