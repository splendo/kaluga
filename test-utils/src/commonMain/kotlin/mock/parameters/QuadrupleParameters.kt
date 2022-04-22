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

class QuadrupleParameters<T0, T1, T2, T3> : ParametersSpec<QuadrupleParameters.Matchers<T0, T1, T2, T3>, QuadrupleParameters.MatchersOrCaptor<T0, T1, T2, T3>, QuadrupleParameters.Values<T0, T1, T2, T3>> {
    data class Matchers<T0, T1, T2, T3>(
        val first: ParameterMatcher<T0>,
        val second: ParameterMatcher<T1>,
        val third: ParameterMatcher<T2>,
        val fourth: ParameterMatcher<T3>
        ) :
        ParametersSpec.Matchers {
        override fun asList() = listOf(first, second, third, fourth)
    }
    data class MatchersOrCaptor<T0, T1, T2, T3>(
        val first: ParameterMatcherOrCaptor<T0>,
        val second: ParameterMatcherOrCaptor<T1>,
        val third: ParameterMatcherOrCaptor<T2>,
        val fourth: ParameterMatcherOrCaptor<T3>) :
        ParametersSpec.MatchersOrCaptor<Matchers<T0, T1, T2, T3>> {
        override fun asMatchers(): Matchers<T0, T1, T2, T3> = Matchers(
            first.asMatcher(),
            second.asMatcher(),
            third.asMatcher(),
            fourth.asMatcher()
        )
    }
    data class Values<T0, T1, T2, T3>(val first: T0, val second: T1, val third: T2, val fourth: T3) : ParametersSpec.Values

    override fun Matchers<T0, T1, T2, T3>.matches(values: Values<T0, T1, T2, T3>): Boolean = first.matches(values.first) &&
        second.matches(values.second) &&
        third.matches(values.third) &&
        fourth.matches(values.fourth)


    override fun MatchersOrCaptor<T0, T1, T2, T3>.capture(values: Values<T0, T1, T2, T3>) {
        (first as? Captor<T0>)?.capture(values.first)
        (second as? Captor<T1>)?.capture(values.second)
        (third as? Captor<T2>)?.capture(values.third)
        (fourth as? Captor<T3>)?.capture(values.fourth)
    }
}

internal fun <T0, T1, T2, T3, R> ((T0, T1, T2, T3) -> R).asMock() = MethodMock<QuadrupleParameters.Matchers<T0, T1, T2, T3>, QuadrupleParameters.MatchersOrCaptor<T0, T1, T2, T3>, QuadrupleParameters.Values<T0, T1, T2, T3>, QuadrupleParameters<T0, T1, T2, T3>, R>(QuadrupleParameters())

fun <T0, T1, T2, T3, R> ((T0, T1, T2, T3) -> R).mock(defaultAnswer: Answer<QuadrupleParameters.Values<T0, T1, T2, T3>, R>) = asMock().also {
    it.on(ParameterMatcher.any<T0>(), ParameterMatcher.any<T1>(), ParameterMatcher.any<T2>(), ParameterMatcher.any<T3>()).doAnswer(defaultAnswer)
}
fun <T0, T1, T2, T3, R> ((T0, T1, T2, T3) -> R).mock(defaultValue: R) = asMock().also {
    it.on(ParameterMatcher.any<T0>(), ParameterMatcher.any<T1>(), ParameterMatcher.any<T2>(), ParameterMatcher.any<T3>()).doReturn(defaultValue)
}

@JvmName("mockBoolean")
fun <T0, T1, T2, T3> ((T0, T1, T2, T3) -> Boolean).mock() = mock(false)
@JvmName("mockBooleanArray")
fun <T0, T1, T2, T3> ((T0, T1, T2, T3) -> BooleanArray).mock() = mock(booleanArrayOf())
@JvmName("mockByte")
fun <T0, T1, T2, T3> ((T0, T1, T2, T3) -> Byte).mock() = mock(0x0)
@JvmName("mockByteArray")
fun <T0, T1, T2, T3> ((T0, T1, T2, T3) -> ByteArray).mock() = mock(byteArrayOf())
@JvmName("mockChar")
fun <T0, T1, T2, T3> ((T0, T1, T2, T3) -> Char).mock() = mock(0.toChar())
@JvmName("mockCharArray")
fun <T0, T1, T2, T3> ((T0, T1, T2, T3) -> CharArray).mock() = mock(charArrayOf())
@JvmName("mockCharRange")
fun <T0, T1, T2, T3> ((T0, T1, T2, T3) -> CharRange).mock() = mock(CharRange.Companion.EMPTY)
@JvmName("mockDouble")
fun <T0, T1, T2, T3> ((T0, T1, T2, T3) -> Double).mock() = mock(0.0)
@JvmName("mockDoubleArray")
fun <T0, T1, T2, T3> ((T0, T1, T2, T3) -> DoubleArray).mock() = mock(doubleArrayOf())
@JvmName("mockFloat")
fun <T0, T1, T2, T3> ((T0, T1, T2, T3) -> Float).mock() = mock(0.0f)
@JvmName("mockFloatArray")
fun <T0, T1, T2, T3> ((T0, T1, T2, T3) -> FloatArray).mock() = mock(floatArrayOf())
@JvmName("mockInt")
fun <T0, T1, T2, T3> ((T0, T1, T2, T3) -> Int).mock() = mock( 0)
@JvmName("mockIntArray")
fun <T0, T1, T2, T3> ((T0, T1, T2, T3) -> IntArray).mock() = mock(intArrayOf())
@JvmName("mockIntRange")
fun <T0, T1, T2, T3> ((T0, T1, T2, T3) -> IntRange).mock() = mock(IntRange.EMPTY)
@JvmName("mockLong")
fun <T0, T1, T2, T3> ((T0, T1, T2, T3) -> Long).mock() = mock( 0L)
@JvmName("mockLongArray")
fun <T0, T1, T2, T3> ((T0, T1, T2, T3) -> LongArray).mock() = mock(longArrayOf())
@JvmName("mockLongRange")
fun <T0, T1, T2, T3> ((T0, T1, T2, T3) -> LongRange).mock() = mock(LongRange.EMPTY)
@JvmName("mockNumber")
fun <T0, T1, T2, T3> ((T0, T1, T2, T3) -> Number).mock() = mock( 0)
@JvmName("mockShort")
fun <T0, T1, T2, T3> ((T0, T1, T2, T3) -> Short).mock() = mock(0.toShort())
@JvmName("mockShortArray")
fun <T0, T1, T2, T3> ((T0, T1, T2, T3) -> ShortArray).mock() = mock( shortArrayOf())
@JvmName("mockString")
fun <T0, T1, T2, T3> ((T0, T1, T2, T3) -> String).mock() = mock("")
@JvmName("mockUByte")
fun <T0, T1, T2, T3> ((T0, T1, T2, T3) -> UByte).mock() = mock(0x0U)
@JvmName("mockUByteArray")
fun <T0, T1, T2, T3> ((T0, T1, T2, T3) -> UByteArray).mock() = mock(ubyteArrayOf())
@JvmName("mockUInt")
fun <T0, T1, T2, T3> ((T0, T1, T2, T3) -> UInt).mock() = mock(0U)
@JvmName("mockUIntArray")
fun <T0, T1, T2, T3> ((T0, T1, T2, T3) -> UIntArray).mock() = mock(uintArrayOf())
@JvmName("mockUIntRange")
fun <T0, T1, T2, T3> ((T0, T1, T2, T3) -> UIntRange).mock() = mock(UIntRange.EMPTY)
@JvmName("mockULong")
fun <T0, T1, T2, T3> ((T0, T1, T2, T3) -> ULong).mock() = mock( 0UL)
@JvmName("mockULongArray")
fun <T0, T1, T2, T3> ((T0, T1, T2, T3) -> ULongArray).mock() = mock(ulongArrayOf())
@JvmName("mockULongRange")
fun <T0, T1, T2, T3> ((T0, T1, T2, T3) -> ULongRange).mock() = mock(ULongRange.EMPTY)
@JvmName("mockUShort")
fun <T0, T1, T2, T3> ((T0, T1, T2, T3) -> UShort).mock() = mock( 0.toUShort())
@JvmName("mockUShortArray")
fun <T0, T1, T2, T3> ((T0, T1, T2, T3) -> UShortArray).mock() = mock(ushortArrayOf())
@JvmName("mockUnit")
fun <T0, T1, T2, T3> ((T0, T1, T2, T3) -> Unit).mock() = mock(Unit)
@JvmName("mockArray")
inline fun <T0, T1, T2, T3, reified R> ((T0, T1, T2, T3) -> Array<R>).mock() = mock(emptyArray())
@JvmName("mockList")
fun <T0, T1, T2, T3, R> ((T0, T1, T2, T3) -> List<R>).mock() = mock(emptyList())
@JvmName("mockSet")
fun <T0, T1, T2, T3, R> ((T0, T1, T2, T3) -> Set<R>).mock() = mock(emptySet())
@JvmName("mockMap")
fun <T0, T1, T2, T3, K, V> ((T0, T1, T2, T3) -> Map<K, V>).mock() = mock(emptyMap())
@JvmName("mockNullable")
@JsName("mockQuadrupleNullable")
fun <T0, T1, T2, T3, R : Any> ((T0, T1, T2, T3) -> R?).mock() = mock(null)
@JvmName("mockNonNullable")
@JsName("mockQuadrupleNonNullable")
fun <T0, T1, T2, T3, R : Any> ((T0, T1, T2, T3) -> R).mock() = asMock()

internal fun <T0, T1, T2, T3, R> (suspend (T0, T1, T2, T3) -> R).asSuspendedMock() = SuspendMethodMock<QuadrupleParameters.Matchers<T0, T1, T2, T3>, QuadrupleParameters.MatchersOrCaptor<T0, T1, T2, T3>, QuadrupleParameters.Values<T0, T1, T2, T3>, QuadrupleParameters<T0, T1, T2, T3>, R>(QuadrupleParameters())

fun <T0, T1, T2, T3, R> (suspend (T0, T1, T2, T3) -> R).mock(defaultAnswer: SuspendedAnswer<QuadrupleParameters.Values<T0, T1, T2, T3>, R>) = asSuspendedMock()
    .also {
    it.on(ParameterMatcher.any<T0>(), ParameterMatcher.any<T1>(), ParameterMatcher.any<T2>(), ParameterMatcher.any<T3>()).doAnswer(defaultAnswer)
}
fun <T0, T1, T2, T3, R> (suspend (T0, T1, T2, T3) -> R).mock(defaultValue: R) = asSuspendedMock()
    .also {
    it.on(ParameterMatcher.any<T0>(), ParameterMatcher.any<T1>(), ParameterMatcher.any<T2>(), ParameterMatcher.any<T3>()).doReturn(defaultValue)
}

@JvmName("mockBoolean")
fun <T0, T1, T2, T3> (suspend (T0, T1, T2, T3) -> Boolean).mock() = mock(false)
@JvmName("mockBooleanArray")
fun <T0, T1, T2, T3> (suspend (T0, T1, T2, T3) -> BooleanArray).mock() = mock(booleanArrayOf())
@JvmName("mockByte")
fun <T0, T1, T2, T3> (suspend (T0, T1, T2, T3) -> Byte).mock() = mock(0x0)
@JvmName("mockByteArray")
fun <T0, T1, T2, T3> (suspend (T0, T1, T2, T3) -> ByteArray).mock() = mock(byteArrayOf())
@JvmName("mockChar")
fun <T0, T1, T2, T3> (suspend (T0, T1, T2, T3) -> Char).mock() = mock(0.toChar())
@JvmName("mockCharArray")
fun <T0, T1, T2, T3> (suspend (T0, T1, T2, T3) -> CharArray).mock() = mock(charArrayOf())
@JvmName("mockCharRange")
fun <T0, T1, T2, T3> (suspend (T0, T1, T2, T3) -> CharRange).mock() = mock(CharRange.Companion.EMPTY)
@JvmName("mockDouble")
fun <T0, T1, T2, T3> (suspend (T0, T1, T2, T3) -> Double).mock() = mock(0.0)
@JvmName("mockDoubleArray")
fun <T0, T1, T2, T3> (suspend (T0, T1, T2, T3) -> DoubleArray).mock() = mock(doubleArrayOf())
@JvmName("mockFloat")
fun <T0, T1, T2, T3> (suspend (T0, T1, T2, T3) -> Float).mock() = mock(0.0f)
@JvmName("mockFloatArray")
fun <T0, T1, T2, T3> (suspend (T0, T1, T2, T3) -> FloatArray).mock() = mock(floatArrayOf())
@JvmName("mockInt")
fun <T0, T1, T2, T3> (suspend (T0, T1, T2, T3) -> Int).mock() = mock(0)
@JvmName("mockIntArray")
fun <T0, T1, T2, T3> (suspend (T0, T1, T2, T3) -> IntArray).mock() = mock(intArrayOf())
@JvmName("mockIntRange")
fun <T0, T1, T2, T3> (suspend (T0, T1, T2, T3) -> IntRange).mock() = mock(IntRange.EMPTY)
@JvmName("mockLong")
fun <T0, T1, T2, T3> (suspend (T0, T1, T2, T3) -> Long).mock() = mock(0L)
@JvmName("mockLongArray")
fun <T0, T1, T2, T3> (suspend (T0, T1, T2, T3) -> LongArray).mock() = mock(longArrayOf())
@JvmName("mockLongRange")
fun <T0, T1, T2, T3> (suspend (T0, T1, T2, T3) -> LongRange).mock() = mock(LongRange.EMPTY)
@JvmName("mockNumber")
fun <T0, T1, T2, T3> (suspend (T0, T1, T2, T3) -> Number).mock() = mock(0)
@JvmName("mockShort")
fun <T0, T1, T2, T3> (suspend (T0, T1, T2, T3) -> Short).mock() = mock(0.toShort())
@JvmName("mockShortArray")
fun <T0, T1, T2, T3> (suspend (T0, T1, T2, T3) -> ShortArray).mock() = mock(shortArrayOf())
@JvmName("mockString")
fun <T0, T1, T2, T3> (suspend (T0, T1, T2, T3) -> String).mock() = mock("")
@JvmName("mockUByte")
fun <T0, T1, T2, T3> (suspend (T0, T1, T2, T3) -> UByte).mock() = mock(0x0U)
@JvmName("mockUByteArray")
fun <T0, T1, T2, T3> (suspend (T0, T1, T2, T3) -> UByteArray).mock() = mock(ubyteArrayOf())
@JvmName("mockUInt")
fun <T0, T1, T2, T3> (suspend (T0, T1, T2, T3) -> UInt).mock() = mock(0U)
@JvmName("mockUIntArray")
fun <T0, T1, T2, T3> (suspend (T0, T1, T2, T3) -> UIntArray).mock() = mock(uintArrayOf())
@JvmName("mockUIntRange")
fun <T0, T1, T2, T3> (suspend (T0, T1, T2, T3) -> UIntRange).mock() = mock(UIntRange.EMPTY)
@JvmName("mockULong")
fun <T0, T1, T2, T3> (suspend (T0, T1, T2, T3) -> ULong).mock() = mock(0UL)
@JvmName("mockULongArray")
fun <T0, T1, T2, T3> (suspend (T0, T1, T2, T3) -> ULongArray).mock() = mock(ulongArrayOf())
@JvmName("mockULongRange")
fun <T0, T1, T2, T3> (suspend (T0, T1, T2, T3) -> ULongRange).mock() = mock(ULongRange.EMPTY)
@JvmName("mockUShort")
fun <T0, T1, T2, T3> (suspend (T0, T1, T2, T3) -> UShort).mock() = mock(0.toUShort())
@JvmName("mockUShortArray")
fun <T0, T1, T2, T3> (suspend (T0, T1, T2, T3) -> UShortArray).mock() = mock(ushortArrayOf())
@JvmName("mockUnit")
fun <T0, T1, T2, T3> (suspend (T0, T1, T2, T3) -> Unit).mock() = mock(Unit)
@JvmName("mockArray")
inline fun <T0, T1, T2, T3, reified R> (suspend (T0, T1, T2, T3) -> Array<R>).mock() = mock(emptyArray())
@JvmName("mockList")
fun <T0, T1, T2, T3, R> (suspend (T0, T1, T2, T3) -> List<R>).mock() = mock(emptyList())
@JvmName("mockSet")
fun <T0, T1, T2, T3, R> (suspend (T0, T1, T2, T3) -> Set<R>).mock() = mock(emptySet())
@JvmName("mockMap")
fun <T0, T1, T2, T3, K, V> (suspend (T0, T1, T2, T3) -> Map<K, V>).mock() = mock(emptyMap())
@JvmName("mockNullable")
@JsName("mockQuadrupleNullableSuspended")
fun <T0, T1, T2, T3, R : Any> (suspend (T0, T1, T2, T3) -> R?).mock() = mock(null)
@JvmName("mockNonNullable")
@JsName("mockQuadrupleNonNullableSuspended")
fun <T0, T1, T2, T3, R : Any> (suspend (T0, T1, T2, T3) -> R).mock() = asSuspendedMock()
