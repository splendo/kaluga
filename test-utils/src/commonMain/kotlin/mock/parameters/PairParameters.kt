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

fun <T0, T1, R> ((T0, T1) -> R).asMock() = MethodMock<PairParameters.Matchers<T0, T1>, PairParameters.MatchersOrCaptor<T0, T1>, PairParameters.Values<T0, T1>, PairParameters<T0, T1>, R>(PairParameters())

fun <T0, T1, R> ((T0, T1) -> R).mock(defaultAnswer: Answer<PairParameters.Values<T0, T1>, R>) = asMock().also {
    it.on(ParameterMatcher.any<T0>(), ParameterMatcher.any<T1>()).doAnswer(defaultAnswer)
}
fun <T0, T1, R> ((T0, T1) -> R).mock(defaultValue: R) = asMock().also {
    it.on(ParameterMatcher.any<T0>(), ParameterMatcher.any<T1>()).doReturn(defaultValue)
}

fun <T0, T1> ((T0, T1) -> Boolean).mock() = mock(false)
fun <T0, T1> ((T0, T1) -> BooleanArray).mock() = mock(booleanArrayOf())
fun <T0, T1> ((T0, T1) -> Byte).mock() = mock(0x0)
fun <T0, T1> ((T0, T1) -> ByteArray).mock() = mock(byteArrayOf())
fun <T0, T1> ((T0, T1) -> Char).mock() = mock(0.toChar())
fun <T0, T1> ((T0, T1) -> CharArray).mock() = mock(charArrayOf())
fun <T0, T1> ((T0, T1) -> CharRange).mock() = mock(CharRange.Companion.EMPTY)
fun <T0, T1> ((T0, T1) -> Double).mock() = mock(0.0)
fun <T0, T1> ((T0, T1) -> DoubleArray).mock() = mock(doubleArrayOf())
fun <T0, T1> ((T0, T1) -> Float).mock() = mock(0.0f)
fun <T0, T1> ((T0, T1) -> FloatArray).mock() = mock(floatArrayOf())
fun <T0, T1> ((T0, T1) -> Int).mock() = mock( 0)
fun <T0, T1> ((T0, T1) -> IntArray).mock() = mock(intArrayOf())
fun <T0, T1> ((T0, T1) -> IntRange).mock() = mock(IntRange.EMPTY)
fun <T0, T1> ((T0, T1) -> Long).mock() = mock( 0L)
fun <T0, T1> ((T0, T1) -> LongArray).mock() = mock(longArrayOf())
fun <T0, T1> ((T0, T1) -> LongRange).mock() = mock(LongRange.EMPTY)
fun <T0, T1> ((T0, T1) -> Number).mock() = mock( 0)
fun <T0, T1> ((T0, T1) -> Short).mock() = mock(0.toShort())
fun <T0, T1> ((T0, T1) -> ShortArray).mock() = mock( shortArrayOf())
fun <T0, T1> ((T0, T1) -> String).mock() = mock("")
fun <T0, T1> ((T0, T1) -> UByte).mock() = mock(0x0U)
fun <T0, T1> ((T0, T1) -> UByteArray).mock() = mock(ubyteArrayOf())
fun <T0, T1> ((T0, T1) -> UInt).mock() = mock(0U)
fun <T0, T1> ((T0, T1) -> UIntArray).mock() = mock(uintArrayOf())
fun <T0, T1> ((T0, T1) -> UIntRange).mock() = mock(UIntRange.EMPTY)
fun <T0, T1> ((T0, T1) -> ULong).mock() = mock( 0UL)
fun <T0, T1> ((T0, T1) -> ULongArray).mock() = mock(ulongArrayOf())
fun <T0, T1> ((T0, T1) -> ULongRange).mock() = mock(ULongRange.EMPTY)
fun <T0, T1> ((T0, T1) -> UShort).mock() = mock( 0.toUShort())
fun <T0, T1> ((T0, T1) -> UShortArray).mock() = mock(ushortArrayOf())
inline fun <T0, T1, reified R> ((T0, T1) -> Array<R>).mock() = mock(emptyArray())
fun <T0, T1, R> ((T0, T1) -> List<R>).mock() = mock(emptyList())
fun <T0, T1, R> ((T0, T1) -> Set<R>).mock() = mock(emptySet())
fun <T0, T1, K, V> ((T0, T1) -> Map<K, V>).mock() = mock(emptyMap())
fun <T0, T1, R : Any> ((T0, T1) -> R?).mock() = mock(null)

fun <T0, T1, R> (suspend (T0, T1) -> R).asMock() = SuspendMethodMock<PairParameters.Matchers<T0, T1>, PairParameters.MatchersOrCaptor<T0, T1>, PairParameters.Values<T0, T1>, PairParameters<T0, T1>, R>(PairParameters())

fun <T0, T1, R> (suspend (T0, T1) -> R).mock(defaultAnswer: SuspendedAnswer<PairParameters.Values<T0, T1>, R>) = asMock().also {
    it.on(ParameterMatcher.any<T0>(), ParameterMatcher.any<T1>()).doAnswer(defaultAnswer)
}
fun <T0, T1, R> (suspend (T0, T1) -> R).mock(defaultValue: R) = asMock().also {
    it.on(ParameterMatcher.any<T0>(), ParameterMatcher.any<T1>()).doReturn(defaultValue)
}

fun <T0, T1> (suspend (T0, T1) -> Boolean).mock() = mock(false)
fun <T0, T1> (suspend (T0, T1) -> BooleanArray).mock() = mock(booleanArrayOf())
fun <T0, T1> (suspend (T0, T1) -> Byte).mock() = mock(0x0)
fun <T0, T1> (suspend (T0, T1) -> ByteArray).mock() = mock(byteArrayOf())
fun <T0, T1> (suspend (T0, T1) -> Char).mock() = mock(0.toChar())
fun <T0, T1> (suspend (T0, T1) -> CharArray).mock() = mock(charArrayOf())
fun <T0, T1> (suspend (T0, T1) -> CharRange).mock() = mock(CharRange.Companion.EMPTY)
fun <T0, T1> (suspend (T0, T1) -> Double).mock() = mock(0.0)
fun <T0, T1> (suspend (T0, T1) -> DoubleArray).mock() = mock(doubleArrayOf())
fun <T0, T1> (suspend (T0, T1) -> Float).mock() = mock(0.0f)
fun <T0, T1> (suspend (T0, T1) -> FloatArray).mock() = mock(floatArrayOf())
fun <T0, T1> (suspend (T0, T1) -> Int).mock() = mock(0)
fun <T0, T1> (suspend (T0, T1) -> IntArray).mock() = mock(intArrayOf())
fun <T0, T1> (suspend (T0, T1) -> IntRange).mock() = mock(IntRange.EMPTY)
fun <T0, T1> (suspend (T0, T1) -> Long).mock() = mock(0L)
fun <T0, T1> (suspend (T0, T1) -> LongArray).mock() = mock(longArrayOf())
fun <T0, T1> (suspend (T0, T1) -> LongRange).mock() = mock(LongRange.EMPTY)
fun <T0, T1> (suspend (T0, T1) -> Number).mock() = mock(0)
fun <T0, T1> (suspend (T0, T1) -> Short).mock() = mock(0.toShort())
fun <T0, T1> (suspend (T0, T1) -> ShortArray).mock() = mock(shortArrayOf())
fun <T0, T1> (suspend (T0, T1) -> String).mock() = mock("")
fun <T0, T1> (suspend (T0, T1) -> UByte).mock() = mock(0x0U)
fun <T0, T1> (suspend (T0, T1) -> UByteArray).mock() = mock(ubyteArrayOf())
fun <T0, T1> (suspend (T0, T1) -> UInt).mock() = mock(0U)
fun <T0, T1> (suspend (T0, T1) -> UIntArray).mock() = mock(uintArrayOf())
fun <T0, T1> (suspend (T0, T1) -> UIntRange).mock() = mock(UIntRange.EMPTY)
fun <T0, T1> (suspend (T0, T1) -> ULong).mock() = mock(0UL)
fun <T0, T1> (suspend (T0, T1) -> ULongArray).mock() = mock(ulongArrayOf())
fun <T0, T1> (suspend (T0, T1) -> ULongRange).mock() = mock(ULongRange.EMPTY)
fun <T0, T1> (suspend (T0, T1) -> UShort).mock() = mock(0.toUShort())
fun <T0, T1> (suspend (T0, T1) -> UShortArray).mock() = mock(ushortArrayOf())
inline fun <T0, T1, reified R> (suspend (T0, T1) -> Array<R>).mock() = mock(emptyArray())
fun <T0, T1, R> (suspend (T0, T1) -> List<R>).mock() = mock(emptyList())
fun <T0, T1, R> (suspend (T0, T1) -> Set<R>).mock() = mock(emptySet())
fun <T0, T1, K, V> (suspend (T0, T1) -> Map<K, V>).mock() = mock(emptyMap())
fun <T0, T1, R : Any> (suspend (T0, T1) -> R?).mock() = mock( null)

