package com.splendo.mpp

actual fun runOnMain(block: () -> Unit) {
    block()
}