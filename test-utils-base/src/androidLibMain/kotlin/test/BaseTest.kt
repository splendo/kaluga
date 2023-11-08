/*

Copyright 2022 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.test.base

import android.os.Build
import com.splendo.kaluga.logging.LogLevel
import com.splendo.kaluga.logging.Logger
import com.splendo.kaluga.logging.logger
import com.splendo.kaluga.logging.resetLogger
import com.splendo.kaluga.logging.w
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import kotlin.coroutines.CoroutineContext
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

actual open class BaseTest {

    /*******************************************************
     * PLEASE KEEP IMPLEMENTATION IN SYNC WITH JVM VERSION *
     *******************************************************/

    // might re-enable this if needed, but to encourage cross platform behaviour it is off
    // @Rule @JvmField
    // val instantExecutorRule = InstantTaskExecutorRule()

    private val mainDispatcher: CoroutineDispatcher by lazy {
        var mainThread: Thread? = null
        val factory = ThreadFactory { r ->
            Thread(r, "synthetic UI thread").also {
                it.uncaughtExceptionHandler =
                    Thread.UncaughtExceptionHandler { _, e -> w(throwable = e) { "error in synthetic main thread: $e" } }
                mainThread = it
            }
        }

        val executor: ExecutorService = Executors.newSingleThreadExecutor(factory)
        val d = executor.asCoroutineDispatcher()
        // Implement proper immediate support
        object : CoroutineDispatcher() {
            override fun dispatch(context: CoroutineContext, block: Runnable) {
                d.dispatch(context, block)
            }
            override fun isDispatchNeeded(context: CoroutineContext): Boolean = Thread.currentThread() != mainThread
        }
    }

    protected val isUnitTest: Boolean
        get() = Build.MODEL == null

    @BeforeTest
    actual open fun beforeTest() {
        if (isUnitTest) {
            // Disabled as it gives issues as of Kotlin 1.8
            // DebugProbes.install() // coroutine debugging

            Dispatchers.setMain(mainDispatcher)

            logger = object : Logger {
                override fun log(level: LogLevel, tag: String?, throwable: Throwable?, message: (() -> String)?) {
                    println("$level: ${tag?.let { "[$it]" } ?: ""} ${message?.invoke() ?: ""} ${throwable?.message ?: ""}".trim())
                    throwable?.printStackTrace(System.out)
                }
            }
        }
    }

    @AfterTest
    actual open fun afterTest() {
        if (isUnitTest) {
            Thread.sleep(50) // reseting the main dispatcher too early can lead to strange issues
            Dispatchers.resetMain()
            resetLogger()
        }
    }
}
