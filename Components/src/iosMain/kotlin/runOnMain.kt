package com.splendo.mpp

import platform.darwin.*

actual fun runOnMain(block: () -> Unit) {
    dispatch_async(dispatch_get_main_queue(), block)
}