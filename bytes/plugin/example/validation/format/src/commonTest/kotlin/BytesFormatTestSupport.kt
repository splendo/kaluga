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

import com.splendo.kaluga.bytes.annotations.Postfix
import com.splendo.kaluga.bytes.annotations.Prefix
import com.splendo.kaluga.bytes.serialization.BytesFormat
import com.splendo.kaluga.bytes.toHexString
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Wraps a value with a distinct [Prefix]/[Postfix] so [BytesFormatTestBase.validateEncoding] can confirm a fixture
 * still encodes correctly nested inside a containing structure, not just at the root.
 */
@Serializable
@Prefix([0x42, 0x23])
@Postfix([0x22])
data class Nested<T>(val nested: T)

/**
 * A behaviour a [ByteLayoutBackend] either supports or doesn't. Tests that depend on one assert it explicitly
 * (see `BytesFormatKotlinxOnlyTest` and `BytesFormatDescriptorErrorTest` in bytes-serialization) instead of a backend silently skipping it.
 */
enum class Capability {
    /** Open, [SerializersModule]-registered polymorphic dispatch over an externally-extensible hierarchy. */
    OPEN_POLYMORPHISM,

    /**
     * Invalid layouts (ambiguous discriminators, a checksum on a flag-packed enum, ...) are rejected with a runtime
     * exception the first time the type is encoded or decoded. A KSP-generated backend instead rejects these at
     * compile time, so it will never implement this capability.
     */
    RUNTIME_LAYOUT_VALIDATION,
}

/** The knobs a [ByteLayoutBackendFactory] applies when constructing a [ByteLayoutBackend]. */
data class BackendOptions(val serializersModule: SerializersModule = EmptySerializersModule(), val validateChecksum: Boolean = true)

/**
 * Encodes and decodes a [KSerializer]-described layout. [BytesFormatBackend] (kotlinx [BytesFormat]) runs the generated
 * DATA classes and [ViewBackend] the generated zero-copy views, so the same tests run against the same expectations.
 */
interface ByteLayoutBackend {
    val capabilities: Set<Capability>
    fun <T> encode(serializer: KSerializer<T>, value: T): ByteArray
    fun <T> decode(serializer: KSerializer<T>, bytes: ByteArray): T
}

interface ByteLayoutBackendFactory {
    fun create(options: BackendOptions = BackendOptions()): ByteLayoutBackend
}

/** The [ByteLayoutBackend] backed by the kotlinx-serialization-driven [BytesFormat]. */
class BytesFormatBackend(options: BackendOptions = BackendOptions()) : ByteLayoutBackend {

    private val format = BytesFormat {
        serializersModule = options.serializersModule
        validateChecksum = options.validateChecksum
    }

    override val capabilities: Set<Capability> = setOf(Capability.OPEN_POLYMORPHISM, Capability.RUNTIME_LAYOUT_VALIDATION)

    override fun <T> encode(serializer: KSerializer<T>, value: T): ByteArray = format.encodeToByteArray(serializer, value)

    override fun <T> decode(serializer: KSerializer<T>, bytes: ByteArray): T = format.decodeFromByteArray(serializer, bytes)

    companion object Factory : ByteLayoutBackendFactory {
        override fun create(options: BackendOptions): ByteLayoutBackend = BytesFormatBackend(options)
    }
}

/**
 * Shared assertions for the BytesFormat test suite, routed through a [ByteLayoutBackend] so a second backend can
 * run the same tests against the same expectations. Each feature group has an abstract suite (e.g. [NumbersSuite])
 * with the tests both backends support, run by a DATA class with [BytesFormatBackend.Factory] (e.g.
 * [BytesFormatNumbersTest], which adds the tests only DATA supports so far) and a VIEW class with
 * [ViewBackend.Factory] (e.g. [ViewNumbersTest]). Supporting more in the views moves tests into the suite. `BytesFormatKotlinxOnlyTest` and `BytesFormatDescriptorErrorTest` are deliberately *not*
 * part of this reusable hierarchy - they assert the capability they need instead.
 */
abstract class BytesFormatTestBase(private val backendFactory: ByteLayoutBackendFactory) {

    protected fun createBackend(options: BackendOptions = BackendOptions()): ByteLayoutBackend = backendFactory.create(options)

    // Like validateEncoding but without the LSB Nested<T> wrapper, since a MOST_SIGNIFICANT_FIRST structure
    // cannot legally be nested inside a LEAST_SIGNIFICANT_FIRST one (InvalidByteOrderException).
    protected fun <T> validateRoundTrip(value: T, serializer: KSerializer<T>, expectedValue: ByteArray, backend: ByteLayoutBackend = createBackend()) {
        val bytes = backend.encode(serializer, value)
        assertTrue(bytes.contentEquals(expectedValue), "Expected ${expectedValue.toHexString(separator = " ")} but got ${bytes.toHexString(separator = " ")}")
        assertEquals(value, backend.decode(serializer, bytes))
    }

    // For a layout whose exact wire bytes are incidental (a checksum, a long payload): the encoding must decode back.
    protected fun <T> validateRoundTrip(value: T, serializer: KSerializer<T>, backend: ByteLayoutBackend = createBackend()): ByteArray {
        val bytes = backend.encode(serializer, value)
        assertEquals(value, backend.decode(serializer, bytes), "encoded=${bytes.toHexString(separator = " ")}")
        return bytes
    }

    @OptIn(InternalSerializationApi::class)
    protected inline fun <reified T : Any> validateEncoding(value: T, expectedValue: ByteArray) = validateEncoding(value, T::class.serializer(), expectedValue)

    protected fun <T> validateEncoding(value: T, serializer: KSerializer<T>, expectedValue: ByteArray, backend: ByteLayoutBackend = createBackend()) {
        val bytes = backend.encode(serializer, value)
        assertTrue(bytes.contentEquals(expectedValue), "Expected ${expectedValue.toHexString(separator = " ")} but got ${bytes.toHexString(separator = " ")}")

        assertEquals(value, backend.decode(serializer, bytes))

        val nestedSerializer = Nested.serializer(serializer)
        val nested = Nested(value)
        val nestedBytes = backend.encode(nestedSerializer, nested)
        assertTrue(nestedBytes.contentEquals(byteArrayOf(0x42, 0x23) + expectedValue + 0x22))

        assertEquals(nested, backend.decode(nestedSerializer, nestedBytes))
    }

    // Kotlin/JS has no true 32-bit Float and does not canonicalize Float literals, so e.g. `1234.56f`
    // keeps full double precision and would not equal the value decoded back from its 32-bit encoding.
    // Round-tripping through the raw bits yields the genuine 32-bit value on every platform (a no-op on
    // jvm/native/wasm), so the encode-decode round-trip assertions hold on js too.
    protected fun Float.as32Bit(): Float = Float.fromBits(toRawBits())
}
