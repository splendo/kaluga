package com.splendo.kaluga.test
/*

Copyright 2019 Splendo Consulting B.V. The Netherlands

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

*/

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