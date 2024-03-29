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
package com.splendo.kaluga.scientific.unit

import com.splendo.kaluga.scientific.convert
import com.splendo.kaluga.scientific.invoke
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class ScientificUnitTest {

    @Serializable
    data class UnitContainer(val unit: AbstractScientificUnit<*>)

    @Test
    fun testUnits() {
        assertFalse(Units.isEmpty())
    }

    @Test
    fun testInvalidConverters() {
        assertEquals(Double.NaN(Inch), Double.NaN(Meter).convert(Inch))
        assertEquals(Double.POSITIVE_INFINITY(Inch), Double.POSITIVE_INFINITY(Meter).convert(Inch))
        assertEquals(Double.NEGATIVE_INFINITY(Inch), Double.NEGATIVE_INFINITY(Meter).convert(Inch))
    }

    @Test
    fun testSerialization() {
        Units.forEach { unit ->
            val container = UnitContainer(unit)
            val jsonString = Json.encodeToString(UnitContainer.serializer(), container)
            val decoded = Json.decodeFromString(UnitContainer.serializer(), jsonString)
            assertEquals(container, decoded)
        }
    }
}
