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

fun <T0, R> ((T0) -> R).asMock() = MethodMock<SingleParameters.Matchers<T0>, SingleParameters.MatchersOrCaptor<T0>, SingleParameters.Values<T0>, SingleParameters<T0>, R>(SingleParameters())

fun <T0, R> ((T0) -> R).mock(defaultAnswer: Answer<SingleParameters.Values<T0>, R>) = asMock().also {
    it.on(ParameterMatcher.any<T0>()).doAnswer(defaultAnswer)
}
fun <T0, R> ((T0) -> R).mock(defaultValue: R) = asMock().also {
    it.on(ParameterMatcher.any<T0>()).doReturn(defaultValue)
}

fun <T0> ((T0) -> Boolean).mock() = mock(false)
fun <T0> ((T0) -> BooleanArray).mock() = mock(booleanArrayOf())
fun <T0> ((T0) -> Byte).mock() = mock(0x0)
fun <T0> ((T0) -> ByteArray).mock() = mock(byteArrayOf())
fun <T0> ((T0) -> Char).mock() = mock(0.toChar())
fun <T0> ((T0) -> CharArray).mock() = mock(charArrayOf())
fun <T0> ((T0) -> CharRange).mock() = mock(CharRange.Companion.EMPTY)
fun <T0> ((T0) -> Double).mock() = mock(0.0)
fun <T0> ((T0) -> DoubleArray).mock() = mock(doubleArrayOf())
fun <T0> ((T0) -> Float).mock() = mock(0.0f)
fun <T0> ((T0) -> FloatArray).mock() = mock(floatArrayOf())
fun <T0> ((T0) -> Int).mock() = mock( 0)
fun <T0> ((T0) -> IntArray).mock() = mock(intArrayOf())
fun <T0> ((T0) -> IntRange).mock() = mock(IntRange.EMPTY)
fun <T0> ((T0) -> Long).mock() = mock( 0L)
fun <T0> ((T0) -> LongArray).mock() = mock(longArrayOf())
fun <T0> ((T0) -> LongRange).mock() = mock(LongRange.EMPTY)
fun <T0> ((T0) -> Number).mock() = mock( 0)
fun <T0> ((T0) -> Short).mock() = mock(0.toShort())
fun <T0> ((T0) -> ShortArray).mock() = mock( shortArrayOf())
fun <T0> ((T0) -> String).mock() = mock("")
fun <T0> ((T0) -> UByte).mock() = mock(0x0U)
fun <T0> ((T0) -> UByteArray).mock() = mock(ubyteArrayOf())
fun <T0> ((T0) -> UInt).mock() = mock(0U)
fun <T0> ((T0) -> UIntArray).mock() = mock(uintArrayOf())
fun <T0> ((T0) -> UIntRange).mock() = mock(UIntRange.EMPTY)
fun <T0> ((T0) -> ULong).mock() = mock( 0UL)
fun <T0> ((T0) -> ULongArray).mock() = mock(ulongArrayOf())
fun <T0> ((T0) -> ULongRange).mock() = mock(ULongRange.EMPTY)
fun <T0> ((T0) -> UShort).mock() = mock( 0.toUShort())
fun <T0> ((T0) -> UShortArray).mock() = mock(ushortArrayOf())
inline fun <T0, reified R> ((T0) -> Array<R>).mock() = mock(emptyArray())
fun <T0, R> ((T0) -> List<R>).mock() = mock(emptyList())
fun <T0, R> ((T0) -> Set<R>).mock() = mock(emptySet())
fun <T0, K, V> ((T0) -> Map<K, V>).mock() = mock(emptyMap())
fun <T0, R : Any> ((T0) -> R?).mock() = mock(null)

fun <T0, R> (suspend (T0) -> R).asMock() = SuspendMethodMock<SingleParameters.Matchers<T0>, SingleParameters.MatchersOrCaptor<T0>, SingleParameters.Values<T0>, SingleParameters<T0>, R>(SingleParameters())

fun <T0, R> (suspend (T0) -> R).mock(defaultAnswer: SuspendedAnswer<SingleParameters.Values<T0>, R>) = asMock().also {
    it.on(ParameterMatcher.any<T0>()).doAnswer(defaultAnswer)
}
fun <T0, R> (suspend (T0) -> R).mock(defaultValue: R) = asMock().also {
    it.on(ParameterMatcher.any<T0>()).doReturn(defaultValue)
}

fun <T0> (suspend (T0) -> Boolean).mock() = mock(false)
fun <T0> (suspend (T0) -> BooleanArray).mock() = mock(booleanArrayOf())
fun <T0> (suspend (T0) -> Byte).mock() = mock(0x0)
fun <T0> (suspend (T0) -> ByteArray).mock() = mock(byteArrayOf())
fun <T0> (suspend (T0) -> Char).mock() = mock(0.toChar())
fun <T0> (suspend (T0) -> CharArray).mock() = mock(charArrayOf())
fun <T0> (suspend (T0) -> CharRange).mock() = mock(CharRange.Companion.EMPTY)
fun <T0> (suspend (T0) -> Double).mock() = mock(0.0)
fun <T0> (suspend (T0) -> DoubleArray).mock() = mock(doubleArrayOf())
fun <T0> (suspend (T0) -> Float).mock() = mock(0.0f)
fun <T0> (suspend (T0) -> FloatArray).mock() = mock(floatArrayOf())
fun <T0> (suspend (T0) -> Int).mock() = mock(0)
fun <T0> (suspend (T0) -> IntArray).mock() = mock(intArrayOf())
fun <T0> (suspend (T0) -> IntRange).mock() = mock(IntRange.EMPTY)
fun <T0> (suspend (T0) -> Long).mock() = mock(0L)
fun <T0> (suspend (T0) -> LongArray).mock() = mock(longArrayOf())
fun <T0> (suspend (T0) -> LongRange).mock() = mock(LongRange.EMPTY)
fun <T0> (suspend (T0) -> Number).mock() = mock(0)
fun <T0> (suspend (T0) -> Short).mock() = mock(0.toShort())
fun <T0> (suspend (T0) -> ShortArray).mock() = mock(shortArrayOf())
fun <T0> (suspend (T0) -> String).mock() = mock("")
fun <T0> (suspend (T0) -> UByte).mock() = mock(0x0U)
fun <T0> (suspend (T0) -> UByteArray).mock() = mock(ubyteArrayOf())
fun <T0> (suspend (T0) -> UInt).mock() = mock(0U)
fun <T0> (suspend (T0) -> UIntArray).mock() = mock(uintArrayOf())
fun <T0> (suspend (T0) -> UIntRange).mock() = mock(UIntRange.EMPTY)
fun <T0> (suspend (T0) -> ULong).mock() = mock(0UL)
fun <T0> (suspend (T0) -> ULongArray).mock() = mock(ulongArrayOf())
fun <T0> (suspend (T0) -> ULongRange).mock() = mock(ULongRange.EMPTY)
fun <T0> (suspend (T0) -> UShort).mock() = mock(0.toUShort())
fun <T0> (suspend (T0) -> UShortArray).mock() = mock(ushortArrayOf())
inline fun <T0, reified R> (suspend (T0) -> Array<R>).mock() = mock(emptyArray())
fun <T0, R> (suspend (T0) -> List<R>).mock() = mock(emptyList())
fun <T0, R> (suspend (T0) -> Set<R>).mock() = mock(emptySet())
fun <T0, K, V> (suspend (T0) -> Map<K, V>).mock() = mock(emptyMap())
fun <T0, R : Any> (suspend (T0) -> R?).mock() = mock( null)
