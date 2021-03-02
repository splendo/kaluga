/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.base.test.flow

import com.splendo.kaluga.base.flow.takeUntilLast
import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.test.FlowableTest
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class BaseFlowableTest : FlowableTest<String>() {

    @Test
    fun testKnownValueBeforeAction() = testWithFlow {
        flow().emit("foo")
        test {
            assertEquals("foo", it, "Conflation inside the flow should preserve the set value")
        }
    }

    @Test
    fun testExceptionBeingThrown() = testWithFlow {
        action {
            flow().emit("Test")
        }
        try {
            test {
                debug("cause an exception")
                throw Exception("This is an expected error for testing.")
            }
            debug("wait for the exception..")
            action {}
            fail("No throwable was thrown, even though we caused an exception")
        } catch (t: Throwable) {
            assertEquals("This is an expected error for testing.", t.message)
            debug("We got the throwable ($t) we expected")
        }
    }

    @Test
    fun testStopFlow() = testWithFlow {

        val scope = MainScope()
        val flow = flow()
        val collectionJob = scope.async {
            flow.collect { }
        }

        flow.subscriptionCount.filter { it == 1 }.first()
        collectionJob.cancel()
        flow.subscriptionCount.filter { it == 0 }.first()
    }

    private val flow = MutableStateFlow("")
    override fun mutableSharedFlow(): MutableSharedFlow<String> {
        return flow
    }
}
