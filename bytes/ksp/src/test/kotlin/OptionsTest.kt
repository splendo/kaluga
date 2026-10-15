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

package com.splendo.kaluga.bytes.ksp

import com.splendo.kaluga.bytes.annotations.Generate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class OptionsTest {

    @Test
    fun `defaults to DATA and VIEW when the generate option is absent`() {
        assertEquals(setOf(Generate.DATA, Generate.VIEW), Options.parseGenerate(null))
    }

    @Test
    fun `rejects a generate option that names nothing to generate`() {
        assertFailsWith<IllegalArgumentException> { Options.parseGenerate("") }
        assertFailsWith<IllegalArgumentException> { Options.parseGenerate(" , ") }
        assertFailsWith<IllegalArgumentException> { Options.parseGenerate("DATA,SOMETHING") }
    }

    @Test
    fun `leaves only sources outside the common source directories to a per-target pass`() {
        val options = Options(setOf(Generate.DATA), commonSources = listOf("/project/src/commonMain/kotlin"))
        assertEquals(false, options.isOutside("/project/src/commonMain/kotlin/Layouts.kt"))
        assertEquals(true, options.isOutside("/project/src/commonMain/kotlinx/Layouts.kt"))
        assertEquals(true, options.isOutside("/project/src/jvmMain/kotlin/Layouts.kt"))
    }

    @Test
    fun `parses a single generate mode`() {
        assertEquals(setOf(Generate.VIEW), Options.parseGenerate("VIEW"))
    }

    @Test
    fun `parses several comma separated generate modes`() {
        assertEquals(setOf(Generate.DATA, Generate.VIEW), Options.parseGenerate("DATA,VIEW"))
    }
}
