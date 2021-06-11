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

import co.touchlab.stately.ensureNeverFrozen
import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.test.assertFrozen
import com.splendo.kaluga.test.assertNotFrozen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.test.Test
import kotlin.test.assertEquals

class ThreadingTest {

    class Value

    @Test
    fun testThreadingMainOnly() = runBlocking(Dispatchers.Main) {
        val s = subjectOf<Value?>(null)
        assertNotFrozen(s)
        val value = Value()
        value.ensureNeverFrozen() // provide early failure for debugging test
        assertNotFrozen(value)
        s.set(value)
        assertEquals(value, s.currentOrNull)
        // still nothing should be frozen
        assertNotFrozen(value)
        assertNotFrozen(s)

        assertEquals(value, s.stateFlow.value)
        assertNotFrozen(value) // using StateFlow from the same thread should not freeze the value
        assertNotFrozen(s) // the subject itself is also not frozen
    }

    @Test
    fun testThreadingDefaultThenMain() = runBlocking(Dispatchers.Default) {
        val s = subjectOf<Value?>(null)
        withContext(Dispatchers.Main) {
            assertFrozen(s) // due to the context switch the subject itself is frozen
            val value = Value()
            assertNotFrozen(value) // of course not
            s.set(value)
            assertFrozen(value) // due to the context switch the var inside subject is frozen
        }
    }

    @Test
    fun testThreadingMainThenDefault() = runBlocking(Dispatchers.Main) {
        val s = subjectOf<Value?>(null)
        val value = Value()
        s.stateFlow.value = value
        assertNotFrozen(value)
        withContext(Dispatchers.Default) {
            assertFrozen(s) // due to the context switch the subject itself is frozen
            assertFrozen(value)
        }
    }

    @Test
    fun testThreadingMainThenDefaultObservers() = runBlocking(Dispatchers.Main) {
        val s = subjectOf<Value?>(null)
        val value = Value()
        val observer:(Value?) -> Unit = { }
        val disposable = s.observe(observer)
        assertNotFrozen(value)
        assertNotFrozen(observer)

        withContext(Dispatchers.Default) {
            // due to the context switch the subject itself is frozen
            assertFrozen(s)
            assertFrozen(value)
            val observer2:(Value?) -> Unit = { }
            val disposable2 = s.observe(observer2)
            assertFrozen(observer2)

            disposable2.dispose()
        }


        assertNotFrozen(observer)
        disposable.dispose()
    }

}
