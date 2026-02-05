/*
 Copyright 2024 Splendo Consulting B.V. The Netherlands

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

import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.test.base.BaseTest
import kotlinx.coroutines.Dispatchers
import kotlin.test.Test
import kotlin.test.assertEquals

class ConcurrentModificationTest : BaseTest() {

    @Test
    fun testConcurrentModification() = runBlocking(Dispatchers.Main) {
        val subject = subjectOf(0)
        var observer3: Disposable? = null
        var observer2: Disposable? = null
        var dispose = false

        val observer1 = subject.observe {
            if (dispose) {
                observer2!!.dispose()
            }
        }
        observer2 = subject.observe {
            if (dispose) {
                observer1.dispose()
                observer3!!.dispose()
            }
        }

        observer3 = subject.observe {
            // noop
        }

        dispose = true
        // due to the weird order of observers disposing each other while they are iterated this will crash if we remove the `toTypedArray()` before `forEach()` in `Observation.kt::setValueUnconfined`
        subject.post(1)

        assertEquals(1, subject.current)
    }
}
