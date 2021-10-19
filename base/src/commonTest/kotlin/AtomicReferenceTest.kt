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

import com.splendo.kaluga.base.AtomicReferenceDelegate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AtomicReferenceTest {

    @Test
    fun testAtomicReferenceDelegate() {

        data class Class(val property: Int)

        var reference: Class? by AtomicReferenceDelegate(Class(0))
        assertEquals(0, reference?.property)
        reference = Class(1)
        assertEquals(1, reference?.property)
        reference = null
        assertNull(reference)
    }
}
