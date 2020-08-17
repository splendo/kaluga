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

import com.splendo.kaluga.base.flow.HotFlowable
import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.test.BaseTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

class HotFlowableTest : BaseTest() {

    lateinit var flowable: HotFlowable<Int>
    lateinit var broadcastChannel: BroadcastChannel<Int>

    @BeforeTest
    fun setUp() {
        super.beforeTest()
        broadcastChannel = ConflatedBroadcastChannel()
        flowable = HotFlowable(0) { broadcastChannel }
    }

    @Test
    fun testFlowing() = runBlocking {
        assertEquals(0, broadcastChannel.asFlow().first())
        flowable.set(1)
        assertEquals(1, broadcastChannel.asFlow().first())
        assertEquals(1, flowable.flow().first())
    }

    @Test
    fun testMultipleFlowing() = runBlocking {
        val values1 = CompletableDeferred<List<Int>>()
        val values2 = CompletableDeferred<List<Int>>()
        val scope = MainScope()
        val job1 = scope.launch {
            val values = emptyList<Int>().toMutableList()
            flowable.flow().take(3).collect { value ->
                values.add(value)
                if (values.size == 3) {
                    values1.complete(values)
                }
            }
        }
        scope.launch {
            flowable.set(1)
        }
        val job2 = scope.launch {
            val values = emptyList<Int>().toMutableList()
            flowable.flow().take(2).collect { value ->
                values.add(value)
                if (values.size == 2) {
                    values2.complete(values)
                }
            }
        }
        scope.launch {
            flowable.set(2)
        }

        assertEquals(3, values1.await().size)
        assertEquals(2, values2.await().size)

        assertEquals(0, values1.await()[0])
        assertEquals(1, values2.await()[0])

        job1.cancel()
        job2.cancel()
    }
}
