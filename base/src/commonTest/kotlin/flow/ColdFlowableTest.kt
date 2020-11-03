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

import com.splendo.kaluga.base.flow.ColdFlowable
import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.base.utils.EmptyCompletableDeferred
import com.splendo.kaluga.base.utils.complete
import com.splendo.kaluga.test.BaseTest
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ColdFlowableTest : BaseTest() {

    lateinit var flowable: ColdFlowable<Int>
    lateinit var initialized: EmptyCompletableDeferred
    lateinit var deinitialized: CompletableDeferred<Int>
    lateinit var broadcastChannel: BroadcastChannel<Int>

    @BeforeTest
    fun setUp() {
        super.beforeTest()
        broadcastChannel = ConflatedBroadcastChannel()
        initialized = EmptyCompletableDeferred()
        deinitialized = CompletableDeferred()
        flowable = ColdFlowable(
            {
                initialized.complete()
                0
            },
            {
                value ->
                deinitialized.complete(value)
            }
        ) {
            if (broadcastChannel.isClosedForSend)
                broadcastChannel = ConflatedBroadcastChannel()
            broadcastChannel
        }
    }

    @Test
    fun testFlowing() = runBlocking {
        assertFalse { initialized.isCompleted }
        val initialBroadcastValue = CompletableDeferred<Int>()
        val broadcastValue = CompletableDeferred<Int>()

        val channelJob = launch {
            broadcastChannel.asFlow().take(2).collect { value ->
                if (initialBroadcastValue.isCompleted)
                    broadcastValue.complete(value)
                else
                    initialBroadcastValue.complete(value)
            }
        }
        assertFalse { initialBroadcastValue.isCompleted }
        flowable.set(1) // this will be ignored since there is no flow
        assertFalse { initialBroadcastValue.isCompleted }
        val initialFlowableValue = CompletableDeferred<Int>()
        val flowableValue = CompletableDeferred<Int>()
        val flowableJob = launch {
            flowable.flow().take(2).collect { value ->
                if (initialFlowableValue.isCompleted)
                    flowableValue.complete(value)
                else
                    initialFlowableValue.complete(value)
            }
        }

        initialized.await()
        assertEquals(0, initialBroadcastValue.await())
        assertEquals(0, initialFlowableValue.await())
        assertFalse { broadcastValue.isCompleted }
        assertFalse { flowableValue.isCompleted }

        launch {
            flowable.set(2)
        }

        assertEquals(2, broadcastValue.await())
        assertEquals(2, flowableValue.await())

        channelJob.cancel()
        flowableJob.cancel()

        assertEquals(2, deinitialized.await())
    }

    @Test
    fun testMultipleFlowing() = runBlocking {
        val values1 = CompletableDeferred<List<Int>>()
        val values2 = CompletableDeferred<List<Int>>()
        val scope = MainScope()
        val flow = flowable.flow()
        val job1 = scope.launch {
            val values = emptyList<Int>().toMutableList()
            flow.take(3).collect { value ->
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
            flowable.flow().take(3).collect { value ->
                values.add(value)
                if (values.size == 3) {
                    values2.complete(values)
                }
            }
        }
        scope.launch {
            flowable.set(2)
        }

        assertEquals(3, values1.await().size)

        assertEquals(0, values1.await()[0])

        job1.cancel()

        scope.launch {
            assertFalse { deinitialized.isCompleted }
            flowable.set(3)
        }

        assertEquals(3, values2.await().size)
        assertEquals(1, values2.await()[0])

        job2.cancel()

        assertEquals(3, deinitialized.await())
    }

    @Test
    fun testStoppingFlow() = runBlocking {
        val scope = MainScope()
        val flow = flowable.flow()
        val job = scope.launch {
            flow.collect {}
        }
        initialized.await()
        flowable.set(1)
        assertFalse(broadcastChannel.isClosedForSend)
        job.cancel()
        deinitialized.await()
        assertTrue(broadcastChannel.isClosedForSend)
    }
}
