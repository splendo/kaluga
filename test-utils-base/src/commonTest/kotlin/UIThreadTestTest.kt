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

import com.splendo.kaluga.base.utils.EmptyCompletableDeferred
import com.splendo.kaluga.base.utils.complete
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.time.Duration.Companion.seconds

class SimpleUIThreadTestTest : SimpleUIThreadTest() {

    @Test
    fun testSimpleUIThreadTest() = testOnUIThread {
        withTimeout(2.seconds) {
            val e = EmptyCompletableDeferred()
            launch(Dispatchers.Main) {
                e.complete()
            }
            e.await()
        }
    }
}

class UIThreadTestTest : UIThreadTest<UIThreadTestTest.MyTestContext>() {
    class MyTestContext(coroutineScope: CoroutineScope) :
        TestContext,
        CoroutineScope by coroutineScope {
        var myContext = "myContext"
    }

    override val createTestContext: suspend (scope: CoroutineScope) -> MyTestContext = { MyTestContext(it) }

    @Test
    fun testUIThreadTest() = testOnUIThread {
        assertEquals("myContext", myContext)
        myContext = "someContext"
    }

    @Test
    fun testCanceling() = testOnUIThread(cancelScopeAfterTest = true) {
        launch {
            // normally this would hang the test
            while (true) {
                delay(2.seconds)
            }
        }
    }

    @Test
    fun testExceptionWhenCanceling() {
        // a custom cancel exception is thrown and caught by the method, but ours is exception is not a cancel exception
        assertFailsWith<IllegalArgumentException> {
            testOnUIThread(cancelScopeAfterTest = true) {
                throw IllegalArgumentException("generic exception")
            }
        }

        // a custom cancel exception is thrown and caught by the method, but ours is a different instance
        assertFailsWith<CancellationException> {
            testOnUIThread(cancelScopeAfterTest = true) {
                throw CancellationException("some cancel exception")
            }
        }
    }
}
