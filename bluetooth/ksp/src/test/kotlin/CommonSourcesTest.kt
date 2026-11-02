/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.bluetooth.ksp

import com.splendo.kaluga.bluetooth.ksp.helpers.commonSourceDirectories
import com.splendo.kaluga.bluetooth.ksp.helpers.isWithin
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CommonSourcesTest {

    @Test
    fun `matches only paths inside a common source directory`() {
        assertTrue("/project/src/commonMain/kotlin/Spec.kt".isWithin("/project/src/commonMain/kotlin"))
        assertTrue("/project/src/commonMain/kotlin/Spec.kt".isWithin("/project/src/commonMain/kotlin/"))
        assertFalse("/project/src/commonMain/kotlinx/Spec.kt".isWithin("/project/src/commonMain/kotlin"))
        assertFalse("/project/src/jvmMain/kotlin/Spec.kt".isWithin("/project/src/commonMain/kotlin"))
    }

    @Test
    fun `reads no directories from a missing or empty option`() {
        assertEquals(emptyList(), commonSourceDirectories(null))
        assertEquals(emptyList(), commonSourceDirectories(""))
        assertEquals(listOf("/a", "/b"), commonSourceDirectories("/a::/b"))
    }
}
