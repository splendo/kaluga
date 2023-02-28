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
package com.splendo.kaluga.base.test

import com.splendo.kaluga.base.utils.EmptyCompletableDeferred
import com.splendo.kaluga.base.utils.complete
import com.splendo.kaluga.test.base.SimpleFlowTest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SuperSimpleFlowTestTest : SimpleFlowTest<Int>() {
    override val flow = suspend { flowOf(1, 2, 3) }

    @Test
    // use testWithFlow to run the test
    fun test() = testWithFlow {
        val complete = EmptyCompletableDeferred()

        // tests values collected from the flow on the main thread asynchronously
        test {
            assertEquals(1, it)
        }

        test {
            assertEquals(2, it)
            delay(10)
            complete.complete()
        }
        // wait for all asynchronous tests to be completed
        action {
            assertTrue(complete.isCompleted)
        }
        test {
            assertEquals(3, it)
        }
        // no action needed, last test blocks will be run before test is complete
    }
}

class SimpleFlowTestTest : SimpleFlowTest<Int>() {

    override val flow = suspend { MutableStateFlow(1) }

    @Test
    fun testFlowActionFirst() = testWithFlow { flow ->
        action {
            (flow as MutableStateFlow).emit(2)
        }
        test {
            assertEquals(2, it)
        }
    }

    @Test
    fun testFlowTestFirst() = testWithFlow { flow ->
        test {
            assertEquals(1, it)
        }
        action {
            (flow as MutableStateFlow).emit(2)
        }
        test {
            assertEquals(2, it)
        }
    }
}
