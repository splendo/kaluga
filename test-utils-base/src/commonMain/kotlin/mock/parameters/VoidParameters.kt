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

package com.splendo.kaluga.test.base.mock.parameters

import com.splendo.kaluga.test.base.mock.answer.Answer
import com.splendo.kaluga.test.base.mock.answer.SuspendedAnswer
import com.splendo.kaluga.test.base.mock.matcher.ParameterMatcher
import com.splendo.kaluga.test.base.mock.on
import com.splendo.kaluga.test.base.mock.suspendVoidParametersMock
import com.splendo.kaluga.test.base.mock.voidParametersMock
import kotlin.js.JsName
import kotlin.jvm.JvmName

/**
 * The [ParametersSpec] without parameters
 */
object VoidParameters : ParametersSpec<VoidParameters.Matchers, VoidParameters.MatchersOrCaptor, VoidParameters.Values> {

    /**
     * The [ParametersSpec.Matchers] without parameters
     */
    object Matchers : ParametersSpec.Matchers {
        override fun asList(): List<ParameterMatcher<*>> = emptyList()
    }

    /**
     * The [ParametersSpec.MatchersOrCaptor] without parameters
     */
    object MatchersOrCaptor : ParametersSpec.MatchersOrCaptor<Matchers> {
        override fun asMatchers(): Matchers = Matchers
    }

    /**
     * The [ParametersSpec.Values] without parameters
     */
    object Values : ParametersSpec.Values

    override fun Matchers.matches(values: Values): Boolean = true
    override fun MatchersOrCaptor.capture(values: Values) {}
}

internal fun <R> (() -> R).asMock() = voidParametersMock<R>()

fun <R> (() -> R).mockWithDefaultAnswer(
    defaultAnswer: Answer<VoidParameters.Values, R>,
) = asMock().also {
    it.on().doAnswer(defaultAnswer)
}
fun <R> (() -> R).mockWithDefaultValue(defaultValue: R) = asMock().also {
    it.on().doReturn(defaultValue)
}

@JvmName("mockBoolean")
fun (() -> Boolean).mock() = mockWithDefaultValue(false)

@JvmName("mockBooleanArray")
fun (() -> BooleanArray).mock() = mockWithDefaultValue(booleanArrayOf())

@JvmName("mockByte")
fun (() -> Byte).mock() = mockWithDefaultValue(0x0)

@JvmName("mockByteArray")
fun (() -> ByteArray).mock() = mockWithDefaultValue(byteArrayOf())

@JvmName("mockChar")
fun (() -> Char).mock() = mockWithDefaultValue(0.toChar())

@JvmName("mockCharArray")
fun (() -> CharArray).mock() = mockWithDefaultValue(charArrayOf())

@JvmName("mockCharRange")
fun (() -> CharRange).mock() = mockWithDefaultValue(CharRange.EMPTY)

@JvmName("mockDouble")
fun (() -> Double).mock() = mockWithDefaultValue(0.0)

@JvmName("mockDoubleArray")
fun (() -> DoubleArray).mock() = mockWithDefaultValue(doubleArrayOf())

@JvmName("mockFloat")
fun (() -> Float).mock() = mockWithDefaultValue(0.0f)

@JvmName("mockFloatArray")
fun (() -> FloatArray).mock() = mockWithDefaultValue(floatArrayOf())

@JvmName("mockInt")
fun (() -> Int).mock() = mockWithDefaultValue(0)

@JvmName("mockIntArray")
fun (() -> IntArray).mock() = mockWithDefaultValue(intArrayOf())

@JvmName("mockIntRange")
fun (() -> IntRange).mock() = mockWithDefaultValue(IntRange.EMPTY)

@JvmName("mockLong")
fun (() -> Long).mock() = mockWithDefaultValue(0L)

@JvmName("mockLongArray")
fun (() -> LongArray).mock() = mockWithDefaultValue(longArrayOf())

@JvmName("mockLongRange")
fun (() -> LongRange).mock() = mockWithDefaultValue(LongRange.EMPTY)

@JvmName("mockNumber")
fun (() -> Number).mock() = mockWithDefaultValue(0)

@JvmName("mockShort")
fun (() -> Short).mock() = mockWithDefaultValue(0.toShort())

@JvmName("mockShortArray")
fun (() -> ShortArray).mock() = mockWithDefaultValue(shortArrayOf())

@JvmName("mockString")
fun (() -> String).mock() = mockWithDefaultValue("")

@JvmName("mockUByte")
fun (() -> UByte).mock() = mockWithDefaultValue(0x0U)

@JvmName("mockUByteArray")
fun (() -> UByteArray).mock() = mockWithDefaultValue(ubyteArrayOf())

@JvmName("mockUInt")
fun (() -> UInt).mock() = mockWithDefaultValue(0U)

@JvmName("mockUIntArray")
fun (() -> UIntArray).mock() = mockWithDefaultValue(uintArrayOf())

@JvmName("mockUIntRange")
fun (() -> UIntRange).mock() = mockWithDefaultValue(UIntRange.EMPTY)

@JvmName("mockULong")
fun (() -> ULong).mock() = mockWithDefaultValue(0UL)

@JvmName("mockULongArray")
fun (() -> ULongArray).mock() = mockWithDefaultValue(ulongArrayOf())

@JvmName("mockULongRange")
fun (() -> ULongRange).mock() = mockWithDefaultValue(ULongRange.EMPTY)

@JvmName("mockUShort")
fun (() -> UShort).mock() = mockWithDefaultValue(0.toUShort())

@JvmName("mockUShortArray")
fun (() -> UShortArray).mock() = mockWithDefaultValue(ushortArrayOf())

@JvmName("mockUnit")
fun (() -> Unit).mock() = mockWithDefaultValue(Unit)

@JvmName("mockArray")
inline fun <reified R> (() -> Array<R>).mock() = mockWithDefaultValue(emptyArray())

@JvmName("mockList")
fun <R> (() -> List<R>).mock() = mockWithDefaultValue(emptyList())

@JvmName("mockSet")
fun <R> (() -> Set<R>).mock() = mockWithDefaultValue(emptySet())

@JvmName("mockMap")
fun <K, V> (() -> Map<K, V>).mock() = mockWithDefaultValue(emptyMap())

@JvmName("mockNullable")
@JsName("mockVoidNullable")
fun <R : Any> (() -> R?).mock() = mockWithDefaultValue(null)

@JvmName("mockNonNullable")
@JsName("mockVoidNonNullable")
fun <R : Any> (() -> R).mock() = asMock()

internal fun <R> (suspend () -> R).asSuspendedMock() = suspendVoidParametersMock<R>()

fun <R> (suspend () -> R).mockWithDefaultAnswer(
    defaultAnswer: SuspendedAnswer<VoidParameters.Values, R>,
) = asSuspendedMock().also {
    it.on().doAnswer(defaultAnswer)
}

fun <R> (suspend () -> R).mockWithDefaultValue(defaultValue: R) = asSuspendedMock().also {
    it.on().doReturn(defaultValue)
}

@JvmName("mockBoolean")
fun (suspend () -> Boolean).mock() = mockWithDefaultValue(false)

@JvmName("mockBooleanArray")
fun (suspend () -> BooleanArray).mock() = mockWithDefaultValue(booleanArrayOf())

@JvmName("mockByte")
fun (suspend () -> Byte).mock() = mockWithDefaultValue(0x0)

@JvmName("mockByteArray")
fun (suspend () -> ByteArray).mock() = mockWithDefaultValue(byteArrayOf())

@JvmName("mockChar")
fun (suspend () -> Char).mock() = mockWithDefaultValue(0.toChar())

@JvmName("mockCharArray")
fun (suspend () -> CharArray).mock() = mockWithDefaultValue(charArrayOf())

@JvmName("mockCharRange")
fun (suspend () -> CharRange).mock() = mockWithDefaultValue(CharRange.EMPTY)

@JvmName("mockDouble")
fun (suspend () -> Double).mock() = mockWithDefaultValue(0.0)

@JvmName("mockDoubleArray")
fun (suspend () -> DoubleArray).mock() = mockWithDefaultValue(doubleArrayOf())

@JvmName("mockFloat")
fun (suspend () -> Float).mock() = mockWithDefaultValue(0.0f)

@JvmName("mockFloatArray")
fun (suspend () -> FloatArray).mock() = mockWithDefaultValue(floatArrayOf())

@JvmName("mockInt")
fun (suspend () -> Int).mock() = mockWithDefaultValue(0)

@JvmName("mockIntArray")
fun (suspend () -> IntArray).mock() = mockWithDefaultValue(intArrayOf())

@JvmName("mockIntRange")
fun (suspend () -> IntRange).mock() = mockWithDefaultValue(IntRange.EMPTY)

@JvmName("mockLong")
fun (suspend () -> Long).mock() = mockWithDefaultValue(0L)

@JvmName("mockLongArray")
fun (suspend () -> LongArray).mock() = mockWithDefaultValue(longArrayOf())

@JvmName("mockLongRange")
fun (suspend () -> LongRange).mock() = mockWithDefaultValue(LongRange.EMPTY)

@JvmName("mockNumber")
fun (suspend () -> Number).mock() = mockWithDefaultValue(0)

@JvmName("mockShort")
fun (suspend () -> Short).mock() = mockWithDefaultValue(0.toShort())

@JvmName("mockShortArray")
fun (suspend () -> ShortArray).mock() = mockWithDefaultValue(shortArrayOf())

@JvmName("mockString")
fun (suspend () -> String).mock() = mockWithDefaultValue("")

@JvmName("mockUByte")
fun (suspend () -> UByte).mock() = mockWithDefaultValue(0x0U)

@JvmName("mockUByteArray")
fun (suspend () -> UByteArray).mock() = mockWithDefaultValue(ubyteArrayOf())

@JvmName("mockUInt")
fun (suspend () -> UInt).mock() = mockWithDefaultValue(0U)

@JvmName("mockUIntArray")
fun (suspend () -> UIntArray).mock() = mockWithDefaultValue(uintArrayOf())

@JvmName("mockUIntRange")
fun (suspend () -> UIntRange).mock() = mockWithDefaultValue(UIntRange.EMPTY)

@JvmName("mockULong")
fun (suspend () -> ULong).mock() = mockWithDefaultValue(0UL)

@JvmName("mockULongArray")
fun (suspend () -> ULongArray).mock() = mockWithDefaultValue(ulongArrayOf())

@JvmName("mockULongRange")
fun (suspend () -> ULongRange).mock() = mockWithDefaultValue(ULongRange.EMPTY)

@JvmName("mockUShort")
fun (suspend () -> UShort).mock() = mockWithDefaultValue(0.toUShort())

@JvmName("mockUShortArray")
fun (suspend () -> UShortArray).mock() = mockWithDefaultValue(ushortArrayOf())

@JvmName("mockUnit")
fun (suspend () -> Unit).mock() = mockWithDefaultValue(Unit)

@JvmName("mockArray")
inline fun <reified R> (suspend () -> Array<R>).mock() = mockWithDefaultValue(emptyArray())

@JvmName("mockList")
fun <R> (suspend () -> List<R>).mock() = mockWithDefaultValue(emptyList())

@JvmName("mockSet")
fun <R> (suspend () -> Set<R>).mock() = mockWithDefaultValue(emptySet())

@JvmName("mockMap")
fun <K, V> (suspend () -> Map<K, V>).mock() = mockWithDefaultValue(emptyMap())

@JvmName("mockNullable")
@JsName("mockVoidNullableSuspended")
fun <R : Any> (suspend () -> R?).mock() = mockWithDefaultValue(null)

@JvmName("mockNonNullable")
@JsName("mockVoidNonNullableSuspended")
fun <R : Any> (suspend () -> R).mock() = asSuspendedMock()
