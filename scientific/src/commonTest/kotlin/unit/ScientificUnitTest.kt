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

import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.scientific.convert
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.scientificSerializationModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class ScientificUnitTest {

    companion object {
        val json = Json {
            serializersModule = scientificSerializationModule
        }

        // Targets typical core counts (4–10) without over-splitting and paying coroutine overhead.
        private const val PARALLEL_CHUNKS = 8
    }

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
    fun testSerialization() = runBlocking {
        // Round-trip every unit once through the module-aware Json instance. The work is
        // CPU-bound (per-unit polymorphic dispatch in kotlinx-serialization on K/N), so we
        // chunk across Dispatchers.Default workers to use all cores. ListSerializer within
        // each chunk amortises per-call setup. Per-unit assertions so a regression names the
        // specific unit rather than dumping the entire list diff. The original test ran two
        // encoders (default + module) per unit and compared their output — that comparison is
        // a property of the serializer config, not of individual data, so it now lives in
        // `testDefaultAndModuleJsonAgree` and runs once.
        val listSerializer = ListSerializer(UnitContainer.serializer())
        val containers = Units.map { UnitContainer(it) }
        val chunkSize = ((containers.size + PARALLEL_CHUNKS - 1) / PARALLEL_CHUNKS).coerceAtLeast(1)
        val chunks = containers.chunked(chunkSize)

        val encodedChunks = chunks.map { chunk ->
            async(Dispatchers.Default) { json.encodeToString(listSerializer, chunk) }
        }.awaitAll()

        val decoded = encodedChunks.map { encoded ->
            async(Dispatchers.Default) { json.decodeFromString(listSerializer, encoded) }
        }.awaitAll().flatten()

        containers.forEachIndexed { i, expected ->
            assertEquals(expected, decoded[i], "round-trip failed for ${expected.unit}")
        }
    }

    @Test
    fun testDefaultAndModuleJsonAgree() {
        // The default Json (which relies on sealed polymorphism inferred at compile time) must
        // produce the same wire format as the explicit serializersModule. Spot-checked on one
        // representative unit — agreement is a property of the serializer config, not of the
        // data, so we don't need to repeat this for every unit.
        val container = UnitContainer(Meter)
        val defaultEncoded = Json.encodeToString(UnitContainer.serializer(), container)
        val moduleEncoded = json.encodeToString(UnitContainer.serializer(), container)
        assertEquals(defaultEncoded, moduleEncoded)

        val decoded = Json.decodeFromString(UnitContainer.serializer(), defaultEncoded)
        assertEquals(container, decoded)
    }
}
