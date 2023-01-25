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

import com.splendo.kaluga.base.collections.concurrentMutableListOf
import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.test.base.BaseTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ConcurrentMutableListTest : BaseTest() {

    @Test
    fun testConcurrentMutableList() = runBlocking {
        val list = concurrentMutableListOf(0, 1, 2)
        withContext(Dispatchers.Main.immediate) {
            assertEquals(0, list[0])
            list.add(3)
            list.addAll(listOf(4, 5))
        }
        assertEquals(6, list.size)
        val otherList = concurrentMutableListOf(0, 1, 2, 3, 4, 5)
        assertEquals(otherList, list)

        list.add(0, -1)
        list.addAll(0, listOf(0, -2))
        list[0] = -3
        assertEquals(listOf(-3, -2, -1, 0, 1, 2, 3, 4, 5), list)
        assertEquals(listOf(-3, -2, -1), list.subList(0, 3))
        assertTrue(list.contains(5))
        assertTrue(list.containsAll(listOf(3, 4, 5)))
        assertFalse(list.contains(6))
        assertFalse(list.containsAll(listOf(3, 6)))
        assertEquals(3, list.indexOf(0))

        list.remove(0)
        list.removeAll(listOf(-3, -2, -1))
        list.removeAt(1)
        assertEquals(listOf(1, 3, 4, 5), list)

        list.retainAll(listOf(4, 5))
        assertEquals(listOf(4, 5), list)

        list.clear()
        assertTrue(list.isEmpty())
    }
}
