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

package com.splendo.kaluga.bytes.example.format

import com.splendo.kaluga.bytes.layout.InvalidPostfix
import com.splendo.kaluga.bytes.layout.InvalidPrefix
import com.splendo.kaluga.bytes.toHexString
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlin.test.assertEquals
import kotlin.test.fail

/**
 * The [ByteLayoutBackend] backed by the generated zero-copy views, looked up in [ValidationFormatCommonMainBytesViews] by the serial name
 * of the DATA class a test encodes.
 *
 * Encoding goes through `XView.encode`, decoding through `XView.wrap(bytes).toData()`: `wrap` checks the bytes as
 * `BytesFormat` does, and `toData` reads every property through the view's getters. [Nested] is not a layout: its frame is added and checked here, and its content decoded through
 * `XView.wrapRange`, so the sub-range path is exercised as well. Every decode that `BytesFormat` also accepts must
 * produce the value `BytesFormat` decodes.
 */
class ViewBackend(private val options: BackendOptions = BackendOptions()) : ByteLayoutBackend {

    private val data = BytesFormatBackend(options)

    override val capabilities: Set<Capability> = emptySet()

    override fun <T> encode(serializer: KSerializer<T>, value: T): ByteArray {
        val nested = nestedContent(serializer.descriptor)
        return if (nested != null) {
            NESTED_PREFIX + view(nested).encode((value as Nested<*>).nested!!) + NESTED_POSTFIX
        } else {
            view(serializer.descriptor).encode(value!!)
        }
    }

    override fun <T> decode(serializer: KSerializer<T>, bytes: ByteArray): T {
        val nested = nestedContent(serializer.descriptor)
        val decoded: Any = if (nested != null) {
            if (bytes.size < NESTED_PREFIX.size + NESTED_POSTFIX.size || !bytes.copyOfRange(0, NESTED_PREFIX.size).contentEquals(NESTED_PREFIX)) {
                throw InvalidPrefix("Expected Prefix ${NESTED_PREFIX.toHexString()} in ${bytes.toHexString()}")
            }
            if (!bytes.copyOfRange(bytes.size - NESTED_POSTFIX.size, bytes.size).contentEquals(NESTED_POSTFIX)) {
                throw InvalidPostfix("Expected Postfix ${NESTED_POSTFIX.toHexString()} in ${bytes.toHexString()}")
            }
            val entry = view(nested)
            Nested(entry.toData(entry.wrapRange(bytes, NESTED_PREFIX.size, bytes.size - NESTED_POSTFIX.size, options.validateChecksum)))
        } else {
            val entry = view(serializer.descriptor)
            entry.toData(entry.view(bytes, options.validateChecksum))
        }
        runCatching { data.decode(serializer, bytes) }.getOrNull()?.let { expected ->
            assertEquals(expected, decoded, "The view decodes ${bytes.toHexString(separator = " ")} differently from BytesFormat")
        }
        @Suppress("UNCHECKED_CAST")
        return decoded as T
    }

    private fun nestedContent(descriptor: SerialDescriptor): SerialDescriptor? = descriptor.takeIf { it.serialName == NESTED_SERIAL_NAME }?.getElementDescriptor(0)

    private fun view(descriptor: SerialDescriptor): ValidationFormatCommonMainBytesViews.Entry =
        ValidationFormatCommonMainBytesViews.views.firstOrNull { it.serialName == descriptor.serialName }
            ?: fail("No view is generated for ${descriptor.serialName}")

    companion object Factory : ByteLayoutBackendFactory {
        private val NESTED_SERIAL_NAME = Nested.serializer(Unit.serializer()).descriptor.serialName
        private val NESTED_PREFIX = byteArrayOf(0x42, 0x23)
        private val NESTED_POSTFIX = byteArrayOf(0x22)

        override fun create(options: BackendOptions): ByteLayoutBackend = ViewBackend(options)
    }
}
