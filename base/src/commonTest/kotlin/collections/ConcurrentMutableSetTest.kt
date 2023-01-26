/*
 Copyright 2023 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.base.collections

import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.base.singleThreadDispatcher
import com.splendo.kaluga.test.base.BaseTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ConcurrentMutableSetTest : BaseTest() {

    @Test
    fun testConcurrentMutableSet() = runBlocking {
        val set = concurrentMutableSetOf(0, 1, 2)
        withContext(Dispatchers.Main.immediate) {
            set.add(3)
            set.addAll(listOf(4, 5, 5))
        }
        assertEquals(6, set.size)
        val otherSet = concurrentMutableSetOf(0, 1, 2, 3, 4, 5)
        assertEquals(otherSet, set)
        assertTrue(set.contains(0))
        assertFalse(set.contains(6))
        assertTrue(set.containsAll(listOf(1, 2)))
        assertFalse(set.containsAll(listOf(0, 6)))

        set.remove(0)
        set.removeAll(listOf(4, 5))
        assertEquals(setOf(1, 2, 3), set)

        set.retainAll(listOf(2, 3))
        assertEquals(setOf(2, 3), set)

        set.clear()
        assertTrue(set.isEmpty())
    }

    @Test
    fun testConcurrency() = runBlocking {
        val dispatchers = List(10) { singleThreadDispatcher("Thread $it") }
        val set = concurrentMutableSetOf<Int>()
        val jobs = dispatchers.mapIndexed { index, dispatcher ->
            launch(coroutineContext + dispatcher) {
                repeat(1000) {
                    set.add(index * 1000 + it)
                }
            }
        }
        jobs.joinAll()
        assertEquals(10000, set.size)
        dispatchers.forEach { it.close() }
    }
}
