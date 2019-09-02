package com.splendo.mpp.test

import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

// Android Studio will shown an error when parsing this file because it's also defined in the unit test module
// To clear the error close the file and restart Android Studio ¯\_(ツ)_/¯
actual class GlobalTestListener {

    private val mainDispatcher: ExecutorCoroutineDispatcher = newSingleThreadContext("UI thread")

    actual fun beforeTest() {
        Dispatchers.setMain(mainDispatcher)
    }

    actual fun afterTest() {
        Dispatchers.resetMain()
        mainDispatcher.close()
    }
}