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

import com.splendo.kaluga.flow.BaseFlowable
import com.splendo.kaluga.runBlocking
import com.splendo.kaluga.log.debug
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class BaseFlowableTest : FlowableTest<String>() {

    override val flowable = BaseFlowable<String>()

    // doesn't test BaseFlowable directly, but shows some the working of some coroutine principles used in the class
    // unignore if you want to experiment more
    @Ignore
    @Test
    fun testConflatedFlow() = runBlocking {
        val c = ConflatedBroadcastChannel<String>()
        c.send("Foo")
        var r: String? = null
        var flow: Flow<String>?
        MainScope().launch {
            flow = c.asFlow()
            flow!!.collect {
                r = it
            }
            delay(500)
            r = "FooBar"
        }
        delay(100)
        assertEquals("Foo", r)
        c.send("Bar")
        delay(100)
        assertEquals("Bar", r)
        delay(100)
        c.cancel()
        delay(1000)
        assertEquals("FooBar", r)
    }

    @Test
    fun testKnownValueBeforeAction() = runBlockingWithFlow {
        flowable.set("foo")
        action {
            // no action
        }
        test {
            assertEquals("foo", it, "Conflation inside the flowable should preserve the set value")
        }

    }

    @Test
    fun testExceptionBeingThrown() = runBlockingWithFlow {

        action {
            flowable.set("Test")
        }
        try {
            test {
                debug("cause an exception")
                throw Exception("some error!")
            }
            debug("wait for the exception..")
            action {}
            fail("No throwable was thrown, even though we caused an exception")
        } catch (t: Throwable) {
            assertEquals("some error!", t.message)
            debug("We got the throwable ($t) we expected")
        }
    }


}