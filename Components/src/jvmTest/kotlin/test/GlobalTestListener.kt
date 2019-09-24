package com.splendo.mpp.test

actual class GlobalTestListener {
    actual fun beforeTest() {
        // required for running headless on CI
        val props = System.getProperties().setProperty("javax.accessibility.assistive_technologies", "")
    }

    actual fun afterTest() {
    }

}