/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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

package utils

import com.splendo.kaluga.base.KalugaThread
import com.splendo.kaluga.base.MAX_PRIORITY
import com.splendo.kaluga.base.MIN_PRIORITY
import kotlin.test.Test
import kotlin.test.assertFails
import kotlin.test.assertTrue

class KalugaThreadTest {
    @Test
    fun getNameSetName() {
        KalugaThread.currentThread.name
        KalugaThread.currentThread.name = "test-thread"
    }

    @Test
    fun getPrioritySetPriority() {
        assertTrue { KalugaThread.currentThread.priority in KalugaThread.MIN_PRIORITY..KalugaThread.MAX_PRIORITY }
        assertFails {
            KalugaThread.currentThread.priority = KalugaThread.MIN_PRIORITY - 1
        }
        assertFails {
            KalugaThread.currentThread.priority = KalugaThread.MIN_PRIORITY + 1
        }
    }
}