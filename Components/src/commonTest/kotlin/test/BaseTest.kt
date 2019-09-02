package com.splendo.mpp.test

import kotlin.test.AfterTest
import kotlin.test.BeforeTest

/*
 * Android Studio at this time thinks this class has too many implementations because of the test and androidTest source sets.
 * To clear the error close the file and restart Android Studio ¯\_(ツ)_/¯
 */
expect class GlobalTestListener() {
    fun beforeTest()
    fun afterTest()
}

open class BaseTest {
    private val testListener = GlobalTestListener()

    @BeforeTest
    fun beforeTest() {
        testListener.beforeTest()
    }

    @AfterTest
    fun afterTest() {
        testListener.afterTest()
    }

}