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

import com.splendo.kaluga.logging.w
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import kotlin.coroutines.CoroutineContext
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

// we use this surrogate main thread because Swing/JavaFX testing on CI is unreliable
actual open class BaseTest {

    /***********************************************************
     * PLEASE KEEP IMPLEMENTATION IN SYNC WITH ANDROID VERSION *
     ***********************************************************/

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

    @BeforeTest
    actual open fun beforeTest() {
        Dispatchers.setMain(mainDispatcher)
    }

    @AfterTest
    actual open fun afterTest() {
        // we want to avoid triggering Swing or FX
        // Dispatchers.resetMain()
        // mainDispatcher.close()
        // mainDispatcher.cancelChildren()
    }
}
