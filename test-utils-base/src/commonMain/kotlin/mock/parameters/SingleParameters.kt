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
import com.splendo.kaluga.test.mock.matcher.Captor
import com.splendo.kaluga.test.mock.matcher.ParameterMatcher
import com.splendo.kaluga.test.mock.matcher.ParameterMatcherOrCaptor
import com.splendo.kaluga.test.mock.on
import kotlin.js.JsName
import kotlin.jvm.JvmName

class SingleParameters<T0> : ParametersSpec<SingleParameters.Matchers<T0>, SingleParameters.MatchersOrCaptor<T0>, SingleParameters.Values<T0>> {
    data class Matchers<T0>(val value: ParameterMatcher<T0>) :
        ParametersSpec.Matchers {
        override fun asList() = listOf(value)
    }
    data class MatchersOrCaptor<T0>(val value: ParameterMatcherOrCaptor<T0>) :
        ParametersSpec.MatchersOrCaptor<Matchers<T0>> {
        override fun asMatchers(): Matchers<T0> = Matchers(value.asMatcher())
    }
    data class Values<T0>(val value: T0) : ParametersSpec.Values

    override fun Matchers<T0>.matches(values: Values<T0>): Boolean = value.matches(values.value)
    override fun MatchersOrCaptor<T0>.capture(values: Values<T0>) {
        (value as? Captor<T0>)?.capture(values.value)
    }
}

internal fun <T0, R> ((T0) -> R).asMock() = MethodMock<SingleParameters.Matchers<T0>, SingleParameters.MatchersOrCaptor<T0>, SingleParameters.Values<T0>, SingleParameters<T0>, R>(SingleParameters())

fun <T0, R> ((T0) -> R).mock(defaultAnswer: Answer<SingleParameters.Values<T0>, R>) = asMock().also {
    it.on(ParameterMatcher.any<T0>()).doAnswer(defaultAnswer)
}
fun <T0, R> ((T0) -> R).mock(defaultValue: R) = asMock().also {
    it.on(ParameterMatcher.any<T0>()).doReturn(defaultValue)
}

@JvmName("mockBoolean")
fun <T0> ((T0) -> Boolean).mock() = mock(false)
@JvmName("mockBooleanArray")
fun <T0> ((T0) -> BooleanArray).mock() = mock(booleanArrayOf())
@JvmName("mockByte")
fun <T0> ((T0) -> Byte).mock() = mock(0x0)
@JvmName("mockByteArray")
fun <T0> ((T0) -> ByteArray).mock() = mock(byteArrayOf())
@JvmName("mockChar")
fun <T0> ((T0) -> Char).mock() = mock(0.toChar())
@JvmName("mockCharArray")
fun <T0> ((T0) -> CharArray).mock() = mock(charArrayOf())
@JvmName("mockCharRange")
fun <T0> ((T0) -> CharRange).mock() = mock(CharRange.Companion.EMPTY)
@JvmName("mockDouble")
fun <T0> ((T0) -> Double).mock() = mock(0.0)
@JvmName("mockDoubleArray")
fun <T0> ((T0) -> DoubleArray).mock() = mock(doubleArrayOf())
@JvmName("mockFloat")
fun <T0> ((T0) -> Float).mock() = mock(0.0f)
@JvmName("mockFloatArray")
fun <T0> ((T0) -> FloatArray).mock() = mock(floatArrayOf())
@JvmName("mockInt")
fun <T0> ((T0) -> Int).mock() = mock( 0)
@JvmName("mockIntArray")
fun <T0> ((T0) -> IntArray).mock() = mock(intArrayOf())
@JvmName("mockIntRange")
fun <T0> ((T0) -> IntRange).mock() = mock(IntRange.EMPTY)
@JvmName("mockLong")
fun <T0> ((T0) -> Long).mock() = mock( 0L)
@JvmName("mockLongArray")
fun <T0> ((T0) -> LongArray).mock() = mock(longArrayOf())
@JvmName("mockLongRange")
fun <T0> ((T0) -> LongRange).mock() = mock(LongRange.EMPTY)
@JvmName("mockNumber")
fun <T0> ((T0) -> Number).mock() = mock( 0)
@JvmName("mockShort")
fun <T0> ((T0) -> Short).mock() = mock(0.toShort())
@JvmName("mockShortArray")
fun <T0> ((T0) -> ShortArray).mock() = mock( shortArrayOf())
@JvmName("mockString")
fun <T0> ((T0) -> String).mock() = mock("")
@JvmName("mockUByte")
fun <T0> ((T0) -> UByte).mock() = mock(0x0U)
@JvmName("mockUByteArray")
fun <T0> ((T0) -> UByteArray).mock() = mock(ubyteArrayOf())
@JvmName("mockUInt")
fun <T0> ((T0) -> UInt).mock() = mock(0U)
@JvmName("mockUIntArray")
fun <T0> ((T0) -> UIntArray).mock() = mock(uintArrayOf())
@JvmName("mockUIntRange")
fun <T0> ((T0) -> UIntRange).mock() = mock(UIntRange.EMPTY)
@JvmName("mockULong")
fun <T0> ((T0) -> ULong).mock() = mock( 0UL)
@JvmName("mockULongArray")
fun <T0> ((T0) -> ULongArray).mock() = mock(ulongArrayOf())
@JvmName("mockULongRange")
fun <T0> ((T0) -> ULongRange).mock() = mock(ULongRange.EMPTY)
@JvmName("mockUShort")
fun <T0> ((T0) -> UShort).mock() = mock( 0.toUShort())
@JvmName("mockUShortArray")
fun <T0> ((T0) -> UShortArray).mock() = mock(ushortArrayOf())
@JvmName("mockUnit")
fun <T0> ((T0) -> Unit).mock() = mock(Unit)
@JvmName("mockArray")
inline fun <T0, reified R> ((T0) -> Array<R>).mock() = mock(emptyArray())
@JvmName("mockList")
fun <T0, R> ((T0) -> List<R>).mock() = mock(emptyList())
@JvmName("mockSet")
fun <T0, R> ((T0) -> Set<R>).mock() = mock(emptySet())
@JvmName("mockMap")
fun <T0, K, V> ((T0) -> Map<K, V>).mock() = mock(emptyMap())
@JvmName("mockNullable")
@JsName("mockSingleNullable")
fun <T0, R : Any> ((T0) -> R?).mock() = mock(null)
@JvmName("mockNonNullable")
@JsName("mockSingleNonNullable")
fun <T0, R : Any> ((T0) -> R).mock() = asMock()

internal fun <T0, R> (suspend (T0) -> R).asSuspendedMock() = SuspendMethodMock<SingleParameters.Matchers<T0>, SingleParameters.MatchersOrCaptor<T0>, SingleParameters.Values<T0>, SingleParameters<T0>, R>(SingleParameters())

fun <T0, R> (suspend (T0) -> R).mock(defaultAnswer: SuspendedAnswer<SingleParameters.Values<T0>, R>) = asSuspendedMock()
    .also {
    it.on(ParameterMatcher.any<T0>()).doAnswer(defaultAnswer)
}
fun <T0, R> (suspend (T0) -> R).mock(defaultValue: R) = asSuspendedMock().also {
    it.on(ParameterMatcher.any<T0>()).doReturn(defaultValue)
}

@JvmName("mockBoolean")
fun <T0> (suspend (T0) -> Boolean).mock() = mock(false)
@JvmName("mockBooleanArray")
fun <T0> (suspend (T0) -> BooleanArray).mock() = mock(booleanArrayOf())
@JvmName("mockByte")
fun <T0> (suspend (T0) -> Byte).mock() = mock(0x0)
@JvmName("mockByteArray")
fun <T0> (suspend (T0) -> ByteArray).mock() = mock(byteArrayOf())
@JvmName("mockChar")
fun <T0> (suspend (T0) -> Char).mock() = mock(0.toChar())
@JvmName("mockCharArray")
fun <T0> (suspend (T0) -> CharArray).mock() = mock(charArrayOf())
@JvmName("mockCharRange")
fun <T0> (suspend (T0) -> CharRange).mock() = mock(CharRange.Companion.EMPTY)
@JvmName("mockDouble")
fun <T0> (suspend (T0) -> Double).mock() = mock(0.0)
@JvmName("mockDoubleArray")
fun <T0> (suspend (T0) -> DoubleArray).mock() = mock(doubleArrayOf())
@JvmName("mockFloat")
fun <T0> (suspend (T0) -> Float).mock() = mock(0.0f)
@JvmName("mockFloatArray")
fun <T0> (suspend (T0) -> FloatArray).mock() = mock(floatArrayOf())
@JvmName("mockInt")
fun <T0> (suspend (T0) -> Int).mock() = mock(0)
@JvmName("mockIntArray")
fun <T0> (suspend (T0) -> IntArray).mock() = mock(intArrayOf())
@JvmName("mockIntRange")
fun <T0> (suspend (T0) -> IntRange).mock() = mock(IntRange.EMPTY)
@JvmName("mockLong")
fun <T0> (suspend (T0) -> Long).mock() = mock(0L)
@JvmName("mockLongArray")
fun <T0> (suspend (T0) -> LongArray).mock() = mock(longArrayOf())
@JvmName("mockLongRange")
fun <T0> (suspend (T0) -> LongRange).mock() = mock(LongRange.EMPTY)
@JvmName("mockNumber")
fun <T0> (suspend (T0) -> Number).mock() = mock(0)
@JvmName("mockShort")
fun <T0> (suspend (T0) -> Short).mock() = mock(0.toShort())
@JvmName("mockShortArray")
fun <T0> (suspend (T0) -> ShortArray).mock() = mock(shortArrayOf())
@JvmName("mockString")
fun <T0> (suspend (T0) -> String).mock() = mock("")
@JvmName("mockUByte")
fun <T0> (suspend (T0) -> UByte).mock() = mock(0x0U)
@JvmName("mockUByteArray")
fun <T0> (suspend (T0) -> UByteArray).mock() = mock(ubyteArrayOf())
@JvmName("mockUInt")
fun <T0> (suspend (T0) -> UInt).mock() = mock(0U)
@JvmName("mockUIntArray")
fun <T0> (suspend (T0) -> UIntArray).mock() = mock(uintArrayOf())
@JvmName("mockUIntRange")
fun <T0> (suspend (T0) -> UIntRange).mock() = mock(UIntRange.EMPTY)
@JvmName("mockULong")
fun <T0> (suspend (T0) -> ULong).mock() = mock(0UL)
@JvmName("mockULongArray")
fun <T0> (suspend (T0) -> ULongArray).mock() = mock(ulongArrayOf())
@JvmName("mockULongRange")
fun <T0> (suspend (T0) -> ULongRange).mock() = mock(ULongRange.EMPTY)
@JvmName("mockUShort")
fun <T0> (suspend (T0) -> UShort).mock() = mock(0.toUShort())
@JvmName("mockUShortArray")
fun <T0> (suspend (T0) -> UShortArray).mock() = mock(ushortArrayOf())
@JvmName("mockUnit")
fun <T0> (suspend (T0) -> Unit).mock() = mock(Unit)
@JvmName("mockArray")
inline fun <T0, reified R> (suspend (T0) -> Array<R>).mock() = mock(emptyArray())
@JvmName("mockList")
fun <T0, R> (suspend (T0) -> List<R>).mock() = mock(emptyList())
@JvmName("mockSet")
fun <T0, R> (suspend (T0) -> Set<R>).mock() = mock(emptySet())
@JvmName("mockMap")
fun <T0, K, V> (suspend (T0) -> Map<K, V>).mock() = mock(emptyMap())
@JvmName("mockNullable")
@JsName("mockSingleNullableSuspended")
fun <T0, R : Any> (suspend (T0) -> R?).mock() = mock(null)
@JvmName("mockNonNullable")
@JsName("mockSingleNonNullableSuspended")
fun <T0, R : Any> (suspend (T0) -> R).mock() = asSuspendedMock()
