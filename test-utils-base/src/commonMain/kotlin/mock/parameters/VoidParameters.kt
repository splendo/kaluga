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

package com.splendo.kaluga.test.mock.parameters

import com.splendo.kaluga.test.mock.MethodMock
import com.splendo.kaluga.test.mock.SuspendMethodMock
import com.splendo.kaluga.test.mock.answer.Answer
import com.splendo.kaluga.test.mock.answer.SuspendedAnswer
import com.splendo.kaluga.test.mock.matcher.ParameterMatcher
import com.splendo.kaluga.test.mock.on
import kotlin.js.JsName
import kotlin.jvm.JvmName

object VoidParameters : ParametersSpec<VoidParameters.Matchers, VoidParameters.MatchersOrCaptor, VoidParameters.Values> {
    object Matchers : ParametersSpec.Matchers {
        override fun asList(): List<ParameterMatcher<*>> = emptyList()
    }
    object MatchersOrCaptor : ParametersSpec.MatchersOrCaptor<Matchers> {
        override fun asMatchers(): Matchers = Matchers
    }
    object Values : ParametersSpec.Values

    override fun Matchers.matches(values: Values): Boolean = true
    override fun MatchersOrCaptor.capture(values: Values) {}
}

internal fun <R> (() -> R).asMock() = MethodMock<VoidParameters.Matchers, VoidParameters.MatchersOrCaptor, VoidParameters.Values, VoidParameters, R>(VoidParameters)

fun <R> (() -> R).mock(
    defaultAnswer: Answer<VoidParameters.Values, R>
) = asMock().also {
    it.on().doAnswer(defaultAnswer)
}
fun <R> (() -> R).mock(defaultValue: R) = asMock().also {
    it.on().doReturn(defaultValue)
}

@JvmName("mockBoolean")
fun (() -> Boolean).mock() = mock(false)
@JvmName("mockBooleanArray")
fun (() -> BooleanArray).mock() = mock(booleanArrayOf())
@JvmName("mockByte")
fun (() -> Byte).mock() = mock(0x0)
@JvmName("mockByteArray")
fun (() -> ByteArray).mock() = mock(byteArrayOf())
@JvmName("mockChar")
fun (() -> Char).mock() = mock(0.toChar())
@JvmName("mockCharArray")
fun (() -> CharArray).mock() = mock(charArrayOf())
@JvmName("mockCharRange")
fun (() -> CharRange).mock() = mock(CharRange.Companion.EMPTY)
@JvmName("mockDouble")
fun (() -> Double).mock() = mock(0.0)
@JvmName("mockDoubleArray")
fun (() -> DoubleArray).mock() = mock(doubleArrayOf())
@JvmName("mockFloat")
fun (() -> Float).mock() = mock(0.0f)
@JvmName("mockFloatArray")
fun (() -> FloatArray).mock() = mock(floatArrayOf())
@JvmName("mockInt")
fun (() -> Int).mock() = mock(0)
@JvmName("mockIntArray")
fun (() -> IntArray).mock() = mock(intArrayOf())
@JvmName("mockIntRange")
fun (() -> IntRange).mock() = mock(IntRange.EMPTY)
@JvmName("mockLong")
fun (() -> Long).mock() = mock(0L)
@JvmName("mockLongArray")
fun (() -> LongArray).mock() = mock(longArrayOf())
@JvmName("mockLongRange")
fun (() -> LongRange).mock() = mock(LongRange.EMPTY)
@JvmName("mockNumber")
fun (() -> Number).mock() = mock(0)
@JvmName("mockShort")
fun (() -> Short).mock() = mock(0.toShort())
@JvmName("mockShortArray")
fun (() -> ShortArray).mock() = mock(shortArrayOf())
@JvmName("mockString")
fun (() -> String).mock() = mock("")
@JvmName("mockUByte")
fun (() -> UByte).mock() = mock(0x0U)
@JvmName("mockUByteArray")
fun (() -> UByteArray).mock() = mock(ubyteArrayOf())
@JvmName("mockUInt")
fun (() -> UInt).mock() = mock(0U)
@JvmName("mockUIntArray")
fun (() -> UIntArray).mock() = mock(uintArrayOf())
@JvmName("mockUIntRange")
fun (() -> UIntRange).mock() = mock(UIntRange.EMPTY)
@JvmName("mockULong")
fun (() -> ULong).mock() = mock(0UL)
@JvmName("mockULongArray")
fun (() -> ULongArray).mock() = mock(ulongArrayOf())
@JvmName("mockULongRange")
fun (() -> ULongRange).mock() = mock(ULongRange.EMPTY)
@JvmName("mockUShort")
fun (() -> UShort).mock() = mock(0.toUShort())
@JvmName("mockUShortArray")
fun (() -> UShortArray).mock() = mock(ushortArrayOf())
@JvmName("mockUnit")
fun (() -> Unit).mock() = mock(Unit)
@JvmName("mockArray")
inline fun <reified R> (() -> Array<R>).mock() = mock(emptyArray())
@JvmName("mockList")
fun <R> (() -> List<R>).mock() = mock(emptyList())
@JvmName("mockSet")
fun <R> (() -> Set<R>).mock() = mock(emptySet())
@JvmName("mockMap")
fun <K, V> (() -> Map<K, V>).mock() = mock(emptyMap())
@JvmName("mockNullable")
@JsName("mockVoidNullable")
fun <R : Any> (() -> R?).mock() = mock(null)
@JvmName("mockNonNullable")
@JsName("mockVoidNonNullable")
fun <R : Any> (() -> R).mock() = asMock()

internal fun <R> (suspend () -> R).asSuspendedMock() = SuspendMethodMock<VoidParameters.Matchers, VoidParameters.MatchersOrCaptor, VoidParameters.Values, VoidParameters, R>(VoidParameters)

fun <R> (suspend () -> R).mock(
    defaultAnswer: SuspendedAnswer<VoidParameters.Values, R>
) = asSuspendedMock().also {
    it.on().doAnswer(defaultAnswer)
}

fun <R> (suspend () -> R).mock(defaultValue: R) = asSuspendedMock().also {
    it.on().doReturn(defaultValue)
}

@JvmName("mockBoolean")
fun (suspend () -> Boolean).mock() = mock(false)
@JvmName("mockBooleanArray")
fun (suspend () -> BooleanArray).mock() = mock(booleanArrayOf())
@JvmName("mockByte")
fun (suspend () -> Byte).mock() = mock(0x0)
@JvmName("mockByteArray")
fun (suspend () -> ByteArray).mock() = mock(byteArrayOf())
@JvmName("mockChar")
fun (suspend () -> Char).mock() = mock(0.toChar())
@JvmName("mockCharArray")
fun (suspend () -> CharArray).mock() = mock(charArrayOf())
@JvmName("mockCharRange")
fun (suspend () -> CharRange).mock() = mock(CharRange.Companion.EMPTY)
@JvmName("mockDouble")
fun (suspend () -> Double).mock() = mock(0.0)
@JvmName("mockDoubleArray")
fun (suspend () -> DoubleArray).mock() = mock(doubleArrayOf())
@JvmName("mockFloat")
fun (suspend () -> Float).mock() = mock(0.0f)
@JvmName("mockFloatArray")
fun (suspend () -> FloatArray).mock() = mock(floatArrayOf())
@JvmName("mockInt")
fun (suspend () -> Int).mock() = mock(0)
@JvmName("mockIntArray")
fun (suspend () -> IntArray).mock() = mock(intArrayOf())
@JvmName("mockIntRange")
fun (suspend () -> IntRange).mock() = mock(IntRange.EMPTY)
@JvmName("mockLong")
fun (suspend () -> Long).mock() = mock(0L)
@JvmName("mockLongArray")
fun (suspend () -> LongArray).mock() = mock(longArrayOf())
@JvmName("mockLongRange")
fun (suspend () -> LongRange).mock() = mock(LongRange.EMPTY)
@JvmName("mockNumber")
fun (suspend () -> Number).mock() = mock(0)
@JvmName("mockShort")
fun (suspend () -> Short).mock() = mock(0.toShort())
@JvmName("mockShortArray")
fun (suspend () -> ShortArray).mock() = mock(shortArrayOf())
@JvmName("mockString")
fun (suspend () -> String).mock() = mock("")
@JvmName("mockUByte")
fun (suspend () -> UByte).mock() = mock(0x0U)
@JvmName("mockUByteArray")
fun (suspend () -> UByteArray).mock() = mock(ubyteArrayOf())
@JvmName("mockUInt")
fun (suspend () -> UInt).mock() = mock(0U)
@JvmName("mockUIntArray")
fun (suspend () -> UIntArray).mock() = mock(uintArrayOf())
@JvmName("mockUIntRange")
fun (suspend () -> UIntRange).mock() = mock(UIntRange.EMPTY)
@JvmName("mockULong")
fun (suspend () -> ULong).mock() = mock(0UL)
@JvmName("mockULongArray")
fun (suspend () -> ULongArray).mock() = mock(ulongArrayOf())
@JvmName("mockULongRange")
fun (suspend () -> ULongRange).mock() = mock(ULongRange.EMPTY)
@JvmName("mockUShort")
fun (suspend () -> UShort).mock() = mock(0.toUShort())
@JvmName("mockUShortArray")
fun (suspend () -> UShortArray).mock() = mock(ushortArrayOf())
@JvmName("mockUnit")
fun (suspend () -> Unit).mock() = mock(Unit)
@JvmName("mockArray")
inline fun <reified R> (suspend () -> Array<R>).mock() = mock(emptyArray())
@JvmName("mockList")
fun <R> (suspend () -> List<R>).mock() = mock(emptyList())
@JvmName("mockSet")
fun <R> (suspend () -> Set<R>).mock() = mock(emptySet())
@JvmName("mockMap")
fun <K, V> (suspend () -> Map<K, V>).mock() = mock(emptyMap())
@JvmName("mockNullable")
@JsName("mockVoidNullableSuspended")
fun <R : Any> (suspend () -> R?).mock() = mock(null)
@JvmName("mockNonNullable")
@JsName("mockVoidNonNullableSuspended")
fun <R : Any> (suspend () -> R).mock() = asSuspendedMock()
