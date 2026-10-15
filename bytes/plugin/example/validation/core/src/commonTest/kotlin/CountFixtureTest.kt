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

package com.splendo.kaluga.bytes.example

import com.splendo.kaluga.bytes.serialization.BytesFormat
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class CountFixtureTest {

    private class FixtureImpl(override val count: Int) : CountFixture

    @Test
    fun encodesAndDecodesTheDataClassThroughBytesFormat() {
        val data = CountFixtureData.from(FixtureImpl(0x01020304))
        val bytes = BytesFormat.encodeToByteArray(CountFixtureData.serializer(), data)
        assertContentEquals(byteArrayOf(0x04, 0x03, 0x02, 0x01), bytes)
        assertEquals(data, BytesFormat.decodeFromByteArray(CountFixtureData.serializer(), bytes))
        assertEquals(0x01020304, CountFixtureView.wrap(bytes).count)
    }

    @Test
    fun generatesAView() {
        val view = FixtureImpl(0x01020304).toCountFixtureView()
        assertContentEquals(byteArrayOf(0x04, 0x03, 0x02, 0x01), view.bytes)
        assertEquals(CountFixtureData(0x01020304), view.toData())
    }
}
