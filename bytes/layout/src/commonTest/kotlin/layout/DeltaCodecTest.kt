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

package com.splendo.kaluga.bytes.layout

import com.splendo.kaluga.bytes.annotations.Length
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DeltaCodecTest {

    private val short = Length.`16_BIT`
    private val byte = Length.`8_BIT`

    @Test
    fun countsAFirstItemAndWholeSteps() {
        assertEquals(0, DeltaCodec.count(0, short, byte))
        assertEquals(1, DeltaCodec.count(2, short, byte))
        assertEquals(3, DeltaCodec.count(4, short, byte))
        assertEquals(3, DeltaCodec.count(6, short, short))
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> { DeltaCodec.count(1, short, byte) }
        assertFailsWith<ByteArrayEndedBeforeSerializationCompleted> { DeltaCodec.count(5, short, short) }
        assertEquals(0, DeltaCodec.size(0, short, byte))
        assertEquals(4, DeltaCodec.size(3, short, byte))
    }

    @Test
    fun addsStepsModuloTheSizeOfAnItem() {
        assertEquals(18L, DeltaCodec.next(16, 2, short, signed = true))
        assertEquals(-32768L, DeltaCodec.next(32767, 1, short, signed = true))
        assertEquals(32767L, DeltaCodec.next(-32768, -1, short, signed = true))
        assertEquals(0L, DeltaCodec.next(65535, 1, short, signed = false))
        assertEquals(65535L, DeltaCodec.next(0, -1, short, signed = false))
        assertEquals(Long.MIN_VALUE, DeltaCodec.next(Long.MAX_VALUE, 1, Length.`64_BIT`, signed = true))
    }

    @Test
    fun encodesTheStepToEachItemAndRefusesWhatDoesNotFit() {
        assertEquals(16L, DeltaCodec.encode(0, 0, 16, short, signed = true, byte))
        assertEquals(-3L, DeltaCodec.encode(2, 18, 15, short, signed = true, byte))
        assertEquals(1L, DeltaCodec.encode(1, 32767, -32768, short, signed = true, byte))
        assertEquals(1L, DeltaCodec.encode(1, 65535, 0, short, signed = false, byte))
        assertEquals(-128L, DeltaCodec.encode(1, 128, 0, short, signed = true, byte))
        assertFailsWith<IllegalArgumentException> { DeltaCodec.encode(1, 0, 128, short, signed = true, byte) }
        assertFailsWith<IllegalArgumentException> { DeltaCodec.encode(0, 0, 32768, short, signed = true, byte) }
        assertFailsWith<IllegalArgumentException> { DeltaCodec.encode(1, 0, -1, short, signed = false, byte) }
    }
}
