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
import kotlin.jvm.JvmName

class QuintupleParameters<T0, T1, T2, T3, T4> : ParametersSpec<QuintupleParameters.Matchers<T0, T1, T2, T3, T4>, QuintupleParameters.MatchersOrCaptor<T0, T1, T2, T3, T4>, QuintupleParameters.Values<T0, T1, T2, T3, T4>> {
    data class Matchers<T0, T1, T2, T3, T4>(
        val first: ParameterMatcher<T0>,
        val second: ParameterMatcher<T1>,
        val third: ParameterMatcher<T2>,
        val fourth: ParameterMatcher<T3>,
        val fifth: ParameterMatcher<T4>
        ) :
        ParametersSpec.Matchers {
        override fun asList() = listOf(first, second, third, fourth, fifth)
    }
    data class MatchersOrCaptor<T0, T1, T2, T3, T4>(
        val first: ParameterMatcherOrCaptor<T0>,
        val second: ParameterMatcherOrCaptor<T1>,
        val third: ParameterMatcherOrCaptor<T2>,
        val fourth: ParameterMatcherOrCaptor<T3>,
        val fifth: ParameterMatcherOrCaptor<T4>) :
        ParametersSpec.MatchersOrCaptor<Matchers<T0, T1, T2, T3, T4>> {
        override fun asMatchers(): Matchers<T0, T1, T2, T3, T4> = Matchers(
            first.asMatcher(),
            second.asMatcher(),
            third.asMatcher(),
            fourth.asMatcher(),
            fifth.asMatcher()
        )
    }
    data class Values<T0, T1, T2, T3, T4>(val first: T0, val second: T1, val third: T2, val fourth: T3, val fifth: T4) : ParametersSpec.Values

    override fun Matchers<T0, T1, T2, T3, T4>.matches(values: Values<T0, T1, T2, T3, T4>): Boolean = first.matches(values.first) &&
        second.matches(values.second) &&
        third.matches(values.third) &&
        fourth.matches(values.fourth) &&
        fifth.matches(values.fifth)


    override fun MatchersOrCaptor<T0, T1, T2, T3, T4>.capture(values: Values<T0, T1, T2, T3, T4>) {
        (first as? Captor<T0>)?.capture(values.first)
        (second as? Captor<T1>)?.capture(values.second)
        (third as? Captor<T2>)?.capture(values.third)
        (fourth as? Captor<T3>)?.capture(values.fourth)
        (fifth as? Captor<T4>)?.capture(values.fifth)
    }
}

fun <T0, T1, T2, T3, T4, R> ((T0, T1, T2, T3, T4) -> R).asMock() = MethodMock<QuintupleParameters.Matchers<T0, T1, T2, T3, T4>, QuintupleParameters.MatchersOrCaptor<T0, T1, T2, T3, T4>, QuintupleParameters.Values<T0, T1, T2, T3, T4>, QuintupleParameters<T0, T1, T2, T3, T4>, R>(QuintupleParameters())

fun <T0, T1, T2, T3, T4, R> ((T0, T1, T2, T3, T4) -> R).mock(defaultAnswer: Answer<QuintupleParameters.Values<T0, T1, T2, T3, T4>, R>) = asMock().also {
    it.on(ParameterMatcher.any<T0>(), ParameterMatcher.any<T1>(), ParameterMatcher.any<T2>(), ParameterMatcher.any<T3>(), ParameterMatcher.any<T4>()).doAnswer(defaultAnswer)
}
fun <T0, T1, T2, T3, T4, R> ((T0, T1, T2, T3, T4) -> R).mock(defaultValue: R) = asMock().also {
    it.on(ParameterMatcher.any<T0>(), ParameterMatcher.any<T1>(), ParameterMatcher.any<T2>(), ParameterMatcher.any<T3>(), ParameterMatcher.any<T4>()).doReturn(defaultValue)
}

@JvmName("mockBoolean")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> Boolean).mock() = mock(false)
@JvmName("mockBooleanArray")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> BooleanArray).mock() = mock(booleanArrayOf())
@JvmName("mockByte")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> Byte).mock() = mock(0x0)
@JvmName("mockByteArray")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> ByteArray).mock() = mock(byteArrayOf())
@JvmName("mockChar")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> Char).mock() = mock(0.toChar())
@JvmName("mockCharArray")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> CharArray).mock() = mock(charArrayOf())
@JvmName("mockCharRange")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> CharRange).mock() = mock(CharRange.Companion.EMPTY)
@JvmName("mockDouble")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> Double).mock() = mock(0.0)
@JvmName("mockDoubleArray")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> DoubleArray).mock() = mock(doubleArrayOf())
@JvmName("mockFloat")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> Float).mock() = mock(0.0f)
@JvmName("mockFloatArray")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> FloatArray).mock() = mock(floatArrayOf())
@JvmName("mockInt")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> Int).mock() = mock( 0)
@JvmName("mockIntArray")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> IntArray).mock() = mock(intArrayOf())
@JvmName("mockIntRange")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> IntRange).mock() = mock(IntRange.EMPTY)
@JvmName("mockLong")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> Long).mock() = mock( 0L)
@JvmName("mockLongArray")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> LongArray).mock() = mock(longArrayOf())
@JvmName("mockLongRange")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> LongRange).mock() = mock(LongRange.EMPTY)
@JvmName("mockNumber")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> Number).mock() = mock( 0)
@JvmName("mockShort")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> Short).mock() = mock(0.toShort())
@JvmName("mockShortArray")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> ShortArray).mock() = mock( shortArrayOf())
@JvmName("mockString")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> String).mock() = mock("")
@JvmName("mockUByte")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> UByte).mock() = mock(0x0U)
@JvmName("mockUByteArray")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> UByteArray).mock() = mock(ubyteArrayOf())
@JvmName("mockUInt")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> UInt).mock() = mock(0U)
@JvmName("mockUIntArray")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> UIntArray).mock() = mock(uintArrayOf())
@JvmName("mockUIntRange")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> UIntRange).mock() = mock(UIntRange.EMPTY)
@JvmName("mockULong")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> ULong).mock() = mock( 0UL)
@JvmName("mockULongArray")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> ULongArray).mock() = mock(ulongArrayOf())
@JvmName("mockULongRange")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> ULongRange).mock() = mock(ULongRange.EMPTY)
@JvmName("mockUShort")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> UShort).mock() = mock( 0.toUShort())
@JvmName("mockUShortArray")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> UShortArray).mock() = mock(ushortArrayOf())
@JvmName("mockUnit")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> Unit).mock() = mock(Unit)
@JvmName("mockArray")
inline fun <T0, T1, T2, T3, T4, reified R> ((T0, T1, T2, T3, T4) -> Array<R>).mock() = mock(emptyArray())
@JvmName("mockList")
fun <T0, T1, T2, T3, T4, R> ((T0, T1, T2, T3, T4) -> List<R>).mock() = mock(emptyList())
@JvmName("mockSet")
fun <T0, T1, T2, T3, T4, R> ((T0, T1, T2, T3, T4) -> Set<R>).mock() = mock(emptySet())
@JvmName("mockMap")
fun <T0, T1, T2, T3, T4, K, V> ((T0, T1, T2, T3, T4) -> Map<K, V>).mock() = mock(emptyMap())
@JvmName("mockNullable")
fun <T0, T1, T2, T3, T4, R : Any> ((T0, T1, T2, T3, T4) -> R?).mock() = mock(null)

fun <T0, T1, T2, T3, T4, R> (suspend (T0, T1, T2, T3, T4) -> R).asMock() = SuspendMethodMock<QuintupleParameters.Matchers<T0, T1, T2, T3, T4>, QuintupleParameters.MatchersOrCaptor<T0, T1, T2, T3, T4>, QuintupleParameters.Values<T0, T1, T2, T3, T4>, QuintupleParameters<T0, T1, T2, T3, T4>, R>(QuintupleParameters())

fun <T0, T1, T2, T3, T4, R> (suspend (T0, T1, T2, T3, T4) -> R).mock(defaultAnswer: SuspendedAnswer<QuintupleParameters.Values<T0, T1, T2, T3, T4>, R>) = asMock().also {
    it.on(ParameterMatcher.any<T0>(), ParameterMatcher.any<T1>(), ParameterMatcher.any<T2>(), ParameterMatcher.any<T3>(), ParameterMatcher.any<T4>()).doAnswer(defaultAnswer)
}
fun <T0, T1, T2, T3, T4, R> (suspend (T0, T1, T2, T3, T4) -> R).mock(defaultValue: R) = asMock().also {
    it.on(ParameterMatcher.any<T0>(), ParameterMatcher.any<T1>(), ParameterMatcher.any<T2>(), ParameterMatcher.any<T3>(), ParameterMatcher.any<T4>()).doReturn(defaultValue)
}

@JvmName("mockBoolean")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> Boolean).mock() = mock(false)
@JvmName("mockBooleanArray")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> BooleanArray).mock() = mock(booleanArrayOf())
@JvmName("mockByte")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> Byte).mock() = mock(0x0)
@JvmName("mockByteArray")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> ByteArray).mock() = mock(byteArrayOf())
@JvmName("mockChar")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> Char).mock() = mock(0.toChar())
@JvmName("mockCharArray")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> CharArray).mock() = mock(charArrayOf())
@JvmName("mockCharRange")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> CharRange).mock() = mock(CharRange.Companion.EMPTY)
@JvmName("mockDouble")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> Double).mock() = mock(0.0)
@JvmName("mockDoubleArray")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> DoubleArray).mock() = mock(doubleArrayOf())
@JvmName("mockFloat")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> Float).mock() = mock(0.0f)
@JvmName("mockFloatArray")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> FloatArray).mock() = mock(floatArrayOf())
@JvmName("mockInt")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> Int).mock() = mock(0)
@JvmName("mockIntArray")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> IntArray).mock() = mock(intArrayOf())
@JvmName("mockIntRange")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> IntRange).mock() = mock(IntRange.EMPTY)
@JvmName("mockLong")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> Long).mock() = mock(0L)
@JvmName("mockLongArray")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> LongArray).mock() = mock(longArrayOf())
@JvmName("mockLongRange")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> LongRange).mock() = mock(LongRange.EMPTY)
@JvmName("mockNumber")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> Number).mock() = mock(0)
@JvmName("mockShort")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> Short).mock() = mock(0.toShort())
@JvmName("mockShortArray")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> ShortArray).mock() = mock(shortArrayOf())
@JvmName("mockString")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> String).mock() = mock("")
@JvmName("mockUByte")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> UByte).mock() = mock(0x0U)
@JvmName("mockUByteArray")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> UByteArray).mock() = mock(ubyteArrayOf())
@JvmName("mockUInt")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> UInt).mock() = mock(0U)
@JvmName("mockUIntArray")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> UIntArray).mock() = mock(uintArrayOf())
@JvmName("mockUIntRange")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> UIntRange).mock() = mock(UIntRange.EMPTY)
@JvmName("mockULong")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> ULong).mock() = mock(0UL)
@JvmName("mockULongArray")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> ULongArray).mock() = mock(ulongArrayOf())
@JvmName("mockULongRange")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> ULongRange).mock() = mock(ULongRange.EMPTY)
@JvmName("mockUShort")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> UShort).mock() = mock(0.toUShort())
@JvmName("mockUShortArray")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> UShortArray).mock() = mock(ushortArrayOf())
@JvmName("mockUnit")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> Unit).mock() = mock(Unit)
@JvmName("mockArray")
inline fun <T0, T1, T2, T3, T4, reified R> (suspend (T0, T1, T2, T3, T4) -> Array<R>).mock() = mock(emptyArray())
@JvmName("mockList")
fun <T0, T1, T2, T3, T4, R> (suspend (T0, T1, T2, T3, T4) -> List<R>).mock() = mock(emptyList())
@JvmName("mockSet")
fun <T0, T1, T2, T3, T4, R> (suspend (T0, T1, T2, T3, T4) -> Set<R>).mock() = mock(emptySet())
@JvmName("mockMap")
fun <T0, T1, T2, T3, T4, K, V> (suspend (T0, T1, T2, T3, T4) -> Map<K, V>).mock() = mock(emptyMap())
@JvmName("mockNullable")
fun <T0, T1, T2, T3, T4, R : Any> (suspend (T0, T1, T2, T3, T4) -> R?).mock() = mock( null)
