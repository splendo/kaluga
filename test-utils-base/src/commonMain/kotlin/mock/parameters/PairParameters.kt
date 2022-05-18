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

class PairParameters<T0, T1> : ParametersSpec<PairParameters.Matchers<T0, T1>, PairParameters.MatchersOrCaptor<T0, T1>, PairParameters.Values<T0, T1>> {
    data class Matchers<T0, T1>(val first: ParameterMatcher<T0>, val second: ParameterMatcher<T1>) :
        ParametersSpec.Matchers {
        override fun asList() = listOf(first, second)
    }
    data class MatchersOrCaptor<T0, T1>(val first: ParameterMatcherOrCaptor<T0>, val second: ParameterMatcherOrCaptor<T1>) :
        ParametersSpec.MatchersOrCaptor<Matchers<T0, T1>> {
        override fun asMatchers(): Matchers<T0, T1> = Matchers(first.asMatcher(), second.asMatcher())
    }
    data class Values<T0, T1>(val first: T0, val second: T1) : ParametersSpec.Values

    override fun Matchers<T0, T1>.matches(values: Values<T0, T1>): Boolean = first.matches(values.first) && second.matches(values.second)
    override fun MatchersOrCaptor<T0, T1>.capture(values: Values<T0, T1>) {
        (first as? Captor<T0>)?.capture(values.first)
        (second as? Captor<T1>)?.capture(values.second)
    }
}

internal fun <T0, T1, R> ((T0, T1) -> R).asMock() = MethodMock<PairParameters.Matchers<T0, T1>, PairParameters.MatchersOrCaptor<T0, T1>, PairParameters.Values<T0, T1>, PairParameters<T0, T1>, R>(PairParameters())

fun <T0, T1, R> ((T0, T1) -> R).mock(defaultAnswer: Answer<PairParameters.Values<T0, T1>, R>) = asMock().also {
    it.on(ParameterMatcher.any<T0>(), ParameterMatcher.any<T1>()).doAnswer(defaultAnswer)
}
fun <T0, T1, R> ((T0, T1) -> R).mock(defaultValue: R) = asMock().also {
    it.on(ParameterMatcher.any<T0>(), ParameterMatcher.any<T1>()).doReturn(defaultValue)
}

@JvmName("mockBoolean")
fun <T0, T1> ((T0, T1) -> Boolean).mock() = mock(false)
@JvmName("mockBooleanArray")
fun <T0, T1> ((T0, T1) -> BooleanArray).mock() = mock(booleanArrayOf())
@JvmName("mockByte")
fun <T0, T1> ((T0, T1) -> Byte).mock() = mock(0x0)
@JvmName("mockByteArray")
fun <T0, T1> ((T0, T1) -> ByteArray).mock() = mock(byteArrayOf())
@JvmName("mockChar")
fun <T0, T1> ((T0, T1) -> Char).mock() = mock(0.toChar())
@JvmName("mockCharArray")
fun <T0, T1> ((T0, T1) -> CharArray).mock() = mock(charArrayOf())
@JvmName("mockCharRange")
fun <T0, T1> ((T0, T1) -> CharRange).mock() = mock(CharRange.Companion.EMPTY)
@JvmName("mockDouble")
fun <T0, T1> ((T0, T1) -> Double).mock() = mock(0.0)
@JvmName("mockDoubleArray")
fun <T0, T1> ((T0, T1) -> DoubleArray).mock() = mock(doubleArrayOf())
@JvmName("mockFloat")
fun <T0, T1> ((T0, T1) -> Float).mock() = mock(0.0f)
@JvmName("mockFloatArray")
fun <T0, T1> ((T0, T1) -> FloatArray).mock() = mock(floatArrayOf())
@JvmName("mockInt")
fun <T0, T1> ((T0, T1) -> Int).mock() = mock(0)
@JvmName("mockIntArray")
fun <T0, T1> ((T0, T1) -> IntArray).mock() = mock(intArrayOf())
@JvmName("mockIntRange")
fun <T0, T1> ((T0, T1) -> IntRange).mock() = mock(IntRange.EMPTY)
@JvmName("mockLong")
fun <T0, T1> ((T0, T1) -> Long).mock() = mock(0L)
@JvmName("mockLongArray")
fun <T0, T1> ((T0, T1) -> LongArray).mock() = mock(longArrayOf())
@JvmName("mockLongRange")
fun <T0, T1> ((T0, T1) -> LongRange).mock() = mock(LongRange.EMPTY)
@JvmName("mockNumber")
fun <T0, T1> ((T0, T1) -> Number).mock() = mock(0)
@JvmName("mockShort")
fun <T0, T1> ((T0, T1) -> Short).mock() = mock(0.toShort())
@JvmName("mockShortArray")
fun <T0, T1> ((T0, T1) -> ShortArray).mock() = mock(shortArrayOf())
@JvmName("mockString")
fun <T0, T1> ((T0, T1) -> String).mock() = mock("")
@JvmName("mockUByte")
fun <T0, T1> ((T0, T1) -> UByte).mock() = mock(0x0U)
@JvmName("mockUByteArray")
fun <T0, T1> ((T0, T1) -> UByteArray).mock() = mock(ubyteArrayOf())
@JvmName("mockUInt")
fun <T0, T1> ((T0, T1) -> UInt).mock() = mock(0U)
@JvmName("mockUIntArray")
fun <T0, T1> ((T0, T1) -> UIntArray).mock() = mock(uintArrayOf())
@JvmName("mockUIntRange")
fun <T0, T1> ((T0, T1) -> UIntRange).mock() = mock(UIntRange.EMPTY)
@JvmName("mockULong")
fun <T0, T1> ((T0, T1) -> ULong).mock() = mock(0UL)
@JvmName("mockULongArray")
fun <T0, T1> ((T0, T1) -> ULongArray).mock() = mock(ulongArrayOf())
@JvmName("mockULongRange")
fun <T0, T1> ((T0, T1) -> ULongRange).mock() = mock(ULongRange.EMPTY)
@JvmName("mockUShort")
fun <T0, T1> ((T0, T1) -> UShort).mock() = mock(0.toUShort())
@JvmName("mockUShortArray")
fun <T0, T1> ((T0, T1) -> UShortArray).mock() = mock(ushortArrayOf())
@JvmName("mockUnit")
fun <T0, T1> ((T0, T1) -> Unit).mock() = mock(Unit)
@JvmName("mockArray")
inline fun <T0, T1, reified R> ((T0, T1) -> Array<R>).mock() = mock(emptyArray())
@JvmName("mockList")
fun <T0, T1, R> ((T0, T1) -> List<R>).mock() = mock(emptyList())
@JvmName("mockSet")
fun <T0, T1, R> ((T0, T1) -> Set<R>).mock() = mock(emptySet())
@JvmName("mockMap")
fun <T0, T1, K, V> ((T0, T1) -> Map<K, V>).mock() = mock(emptyMap())
@JvmName("mockNullable")
@JsName("mockPairNullable")
fun <T0, T1, R : Any> ((T0, T1) -> R?).mock() = mock(null)
@JvmName("mockNonNullable")
@JsName("mockPairNonNullable")
fun <T0, T1, R : Any> ((T0, T1) -> R).mock() = asMock()

internal fun <T0, T1, R> (suspend (T0, T1) -> R).asSuspendedMock() = SuspendMethodMock<PairParameters.Matchers<T0, T1>, PairParameters.MatchersOrCaptor<T0, T1>, PairParameters.Values<T0, T1>, PairParameters<T0, T1>, R>(PairParameters())

fun <T0, T1, R> (suspend (T0, T1) -> R).mock(defaultAnswer: SuspendedAnswer<PairParameters.Values<T0, T1>, R>) = asSuspendedMock()
    .also {
        it.on(ParameterMatcher.any<T0>(), ParameterMatcher.any<T1>()).doAnswer(defaultAnswer)
    }
fun <T0, T1, R> (suspend (T0, T1) -> R).mock(defaultValue: R) = asSuspendedMock().also {
    it.on(ParameterMatcher.any<T0>(), ParameterMatcher.any<T1>()).doReturn(defaultValue)
}

@JvmName("mockBoolean")
fun <T0, T1> (suspend (T0, T1) -> Boolean).mock() = mock(false)
@JvmName("mockBooleanArray")
fun <T0, T1> (suspend (T0, T1) -> BooleanArray).mock() = mock(booleanArrayOf())
@JvmName("mockByte")
fun <T0, T1> (suspend (T0, T1) -> Byte).mock() = mock(0x0)
@JvmName("mockByteArray")
fun <T0, T1> (suspend (T0, T1) -> ByteArray).mock() = mock(byteArrayOf())
@JvmName("mockChar")
fun <T0, T1> (suspend (T0, T1) -> Char).mock() = mock(0.toChar())
@JvmName("mockCharArray")
fun <T0, T1> (suspend (T0, T1) -> CharArray).mock() = mock(charArrayOf())
@JvmName("mockCharRange")
fun <T0, T1> (suspend (T0, T1) -> CharRange).mock() = mock(CharRange.Companion.EMPTY)
@JvmName("mockDouble")
fun <T0, T1> (suspend (T0, T1) -> Double).mock() = mock(0.0)
@JvmName("mockDoubleArray")
fun <T0, T1> (suspend (T0, T1) -> DoubleArray).mock() = mock(doubleArrayOf())
@JvmName("mockFloat")
fun <T0, T1> (suspend (T0, T1) -> Float).mock() = mock(0.0f)
@JvmName("mockFloatArray")
fun <T0, T1> (suspend (T0, T1) -> FloatArray).mock() = mock(floatArrayOf())
@JvmName("mockInt")
fun <T0, T1> (suspend (T0, T1) -> Int).mock() = mock(0)
@JvmName("mockIntArray")
fun <T0, T1> (suspend (T0, T1) -> IntArray).mock() = mock(intArrayOf())
@JvmName("mockIntRange")
fun <T0, T1> (suspend (T0, T1) -> IntRange).mock() = mock(IntRange.EMPTY)
@JvmName("mockLong")
fun <T0, T1> (suspend (T0, T1) -> Long).mock() = mock(0L)
@JvmName("mockLongArray")
fun <T0, T1> (suspend (T0, T1) -> LongArray).mock() = mock(longArrayOf())
@JvmName("mockLongRange")
fun <T0, T1> (suspend (T0, T1) -> LongRange).mock() = mock(LongRange.EMPTY)
@JvmName("mockNumber")
fun <T0, T1> (suspend (T0, T1) -> Number).mock() = mock(0)
@JvmName("mockShort")
fun <T0, T1> (suspend (T0, T1) -> Short).mock() = mock(0.toShort())
@JvmName("mockShortArray")
fun <T0, T1> (suspend (T0, T1) -> ShortArray).mock() = mock(shortArrayOf())
@JvmName("mockString")
fun <T0, T1> (suspend (T0, T1) -> String).mock() = mock("")
@JvmName("mockUByte")
fun <T0, T1> (suspend (T0, T1) -> UByte).mock() = mock(0x0U)
@JvmName("mockUByteArray")
fun <T0, T1> (suspend (T0, T1) -> UByteArray).mock() = mock(ubyteArrayOf())
@JvmName("mockUInt")
fun <T0, T1> (suspend (T0, T1) -> UInt).mock() = mock(0U)
@JvmName("mockUIntArray")
fun <T0, T1> (suspend (T0, T1) -> UIntArray).mock() = mock(uintArrayOf())
@JvmName("mockUIntRange")
fun <T0, T1> (suspend (T0, T1) -> UIntRange).mock() = mock(UIntRange.EMPTY)
@JvmName("mockULong")
fun <T0, T1> (suspend (T0, T1) -> ULong).mock() = mock(0UL)
@JvmName("mockULongArray")
fun <T0, T1> (suspend (T0, T1) -> ULongArray).mock() = mock(ulongArrayOf())
@JvmName("mockULongRange")
fun <T0, T1> (suspend (T0, T1) -> ULongRange).mock() = mock(ULongRange.EMPTY)
@JvmName("mockUShort")
fun <T0, T1> (suspend (T0, T1) -> UShort).mock() = mock(0.toUShort())
@JvmName("mockUShortArray")
fun <T0, T1> (suspend (T0, T1) -> UShortArray).mock() = mock(ushortArrayOf())
@JvmName("mockUnit")
fun <T0, T1> (suspend (T0, T1) -> Unit).mock() = mock(Unit)
@JvmName("mockArray")
inline fun <T0, T1, reified R> (suspend (T0, T1) -> Array<R>).mock() = mock(emptyArray())
@JvmName("mockList")
fun <T0, T1, R> (suspend (T0, T1) -> List<R>).mock() = mock(emptyList())
@JvmName("mockSet")
fun <T0, T1, R> (suspend (T0, T1) -> Set<R>).mock() = mock(emptySet())
@JvmName("mockMap")
fun <T0, T1, K, V> (suspend (T0, T1) -> Map<K, V>).mock() = mock(emptyMap())
@JvmName("mockNullable")
@JsName("mockPairNullableSuspended")
fun <T0, T1, R : Any> (suspend (T0, T1) -> R?).mock() = mock(null)
@JvmName("mockNonNullable")
@JsName("mockPairNonNullableSuspended")
fun <T0, T1, R : Any> (suspend (T0, T1) -> R).mock() = asSuspendedMock()
