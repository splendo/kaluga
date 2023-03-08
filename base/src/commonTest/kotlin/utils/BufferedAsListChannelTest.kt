/*
 * Copyright 2023 Splendo Consulting B.V. The Netherlands
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.splendo.kaluga.base.utils

import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.base.singleThreadDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class BufferedAsListChannelTest {

    @Test
    fun testEqualChannel() = testBufferedAsListChannel(10.milliseconds, 10.milliseconds, 98..100)

    @Test
    fun testSlowToProduceChannel() = testBufferedAsListChannel(10.milliseconds, 1.milliseconds, 99..100)

    @Test
    fun testSlowToConsumeChannel() = testBufferedAsListChannel(1.milliseconds, 10.milliseconds, 10..13)

    private fun testBufferedAsListChannel(producingDuration: Duration, consumingDuration: Duration, expectedNumberOfGroupsIn: IntRange) = runBlocking {
        val groupingChannel = BufferedAsListChannel<Int>(coroutineContext)
        val producingDispatcher = singleThreadDispatcher("Produce")
        CoroutineScope(coroutineContext + producingDispatcher).launch {
            for (i in 1..100) {
                groupingChannel.trySend(i)
                delay(producingDuration)
            }
            groupingChannel.close()
        }

        val consumerDispatcher = singleThreadDispatcher("Consume")
        val list = withContext(CoroutineScope(coroutineContext + consumerDispatcher).coroutineContext) {
            val result = mutableListOf<List<Int>>()
            groupingChannel.receiveAsFlow().collect {
                result.add(it)
                delay(consumingDuration)
            }
            result.toList()
        }

        assertContains(expectedNumberOfGroupsIn, list.size)
        assertEquals((1..100).toList(), list.flatten())
    }
}
