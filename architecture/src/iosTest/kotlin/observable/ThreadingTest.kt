/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.architecture.observable

import co.touchlab.stately.isFrozen
import com.splendo.kaluga.base.runBlocking
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ThreadingTest {

    class Value

    @Test
    fun testThreadingMainOnly() = runBlocking(Dispatchers.Main) {
        val s = subjectOf<Value?>(null)
        assertFalse(s.isFrozen)
        val value = Value()
        assertFalse(value.isFrozen)
        s.set(value)
        assertEquals(value, s.currentOrNull)
        // still nothing should be frozen
        assertFalse(value.isFrozen)
        assertFalse(s.isFrozen)

        s.stateFlow
        assertTrue(value.isFrozen) // initializing the stateFlow will freeze the value
        assertFalse(s.isFrozen) // the subject itself however, not
    }

    @Test
    fun testThreadingDefaultThenMain() = runBlocking(Dispatchers.Default) {
        val s = subjectOf<Value?>(null)
        withContext(Dispatchers.Main) {
            assertTrue(s.isFrozen) // due to the context switch the subject itself is frozen
            val value = Value()
            assertFalse(value.isFrozen) // of course not
            s.set(value)
            assertTrue(value.isFrozen) // due to the context switch the var inside subject is frozen
        }
    }
}
