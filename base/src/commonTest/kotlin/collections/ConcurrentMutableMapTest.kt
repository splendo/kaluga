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

package collections

import com.splendo.kaluga.base.collections.concurrentMutableMapOf
import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.test.base.BaseTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ConcurrentMutableMapTest : BaseTest() {

    @Test
    fun testConcurrentMutableMap() = runBlocking {
        val map = concurrentMutableMapOf(0 to "Zero", 1 to "One", 2 to "Two")
        withContext(Dispatchers.Main.immediate) {
            assertEquals("Zero", map[0])
            map[3] = "Three"
            map.putAll(mapOf(4 to "Four", 5 to "Five"))
        }
        assertEquals(6, map.size)
        val otherMap = concurrentMutableMapOf(0 to "Zero", 1 to "One", 2 to "Two", 3 to "Three", 4 to "Four", 5 to "Five")
        assertEquals(otherMap, map)
        assertEquals(otherMap.toMap().entries, map.entries)
        assertEquals(setOf(0, 1, 2, 3, 4, 5), map.keys)
        assertEquals(listOf("Zero", "One", "Two", "Three", "Four", "Five"), map.values.toList())
        assertTrue(map.containsKey(0))
        assertFalse(map.containsKey(6))
        assertTrue(map.containsValue("One"))
        assertFalse(map.containsValue("Six"))

        map[0] = "Minus One"
        map.remove(1)
        assertEquals(mapOf(0 to "Minus One", 2 to "Two", 3 to "Three", 4 to "Four", 5 to "Five"), map)

        map.clear()
        assertTrue(map.isEmpty())
    }
}
