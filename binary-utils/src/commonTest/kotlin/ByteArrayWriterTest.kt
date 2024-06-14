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

import co.touchlab.stately.freeze
import com.splendo.kaluga.base.utils.bytesOf
import kotlin.test.Test
import kotlin.test.assertContentEquals

class ByteArrayWriterTest {

    @Test
    fun testDynamicBufferRemoveFirst() {
        val writer = ByteArrayWriter.Dynamic()
        val data = ByteArray(48) { it.toByte() }
        writer.append(data)
        assertContentEquals(data, writer.toByteArray())
        // Drop first 44 bytes
        val removed = writer.removeFirst(44)
        val expected = data.take(44).toByteArray()
        // Check removed
        assertContentEquals(expected, removed)
        writer.append(bytesOf(48, 49, 50))
        val rest = ByteArray(7) { (44 + it).toByte() }
        // Check rest
        assertContentEquals(rest, writer.toByteArray())
    }

    @Test
    fun testDynamicBufferFrozen() {
        val writer = ByteArrayWriter.Dynamic()
        writer.freeze()
        // Write more then initial buffer size (32)
        writer.append(ByteArray(33) { 1 })
    }
}
