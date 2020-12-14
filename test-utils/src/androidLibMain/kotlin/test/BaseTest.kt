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

package com.splendo.kaluga.test

import android.os.Build
import com.splendo.kaluga.logging.LogLevel
import com.splendo.kaluga.logging.Logger
import com.splendo.kaluga.logging.logger
import com.splendo.kaluga.logging.resetLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

actual open class BaseTest {

    // might re-enable this if needed, but to encourage cross platform behaviour it is off
    // @Rule @JvmField
    // val instantExecutorRule = InstantTaskExecutorRule()

    private val mainDispatcher: ExecutorCoroutineDispatcher by lazy { newSingleThreadContext("synthetic UI thread") }

    protected val isUnitTest: Boolean
        get() = Build.MODEL == null

    @BeforeTest
    actual open fun beforeTest() {

        if (isUnitTest) {

            Dispatchers.setMain(mainDispatcher)

            logger = object : Logger {
                override fun log(
                    level: LogLevel,
                    tag: String?,
                    throwable: Throwable?,
                    message: (() -> String)?
                ) {
                    println("$level: ${tag?.let { "[$it]" } ?: ""} ${message?.invoke() ?: ""} ${throwable?.message ?: ""}".trim())
                    throwable?.printStackTrace(System.out)
                }
            }
        }
    }

    @AfterTest
    actual open fun afterTest() {
        if (isUnitTest) {
            mainDispatcher.close()
            Dispatchers.resetMain()
            resetLogger()
        }
    }
}
