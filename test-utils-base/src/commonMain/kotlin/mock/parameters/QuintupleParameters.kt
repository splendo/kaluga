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
import com.splendo.kaluga.test.base.mock.matcher.Captor
import com.splendo.kaluga.test.base.mock.matcher.ParameterMatcher
import com.splendo.kaluga.test.base.mock.matcher.ParameterMatcherOrCaptor
import com.splendo.kaluga.test.base.mock.on
import com.splendo.kaluga.test.base.mock.quintupleParametersMock
import com.splendo.kaluga.test.base.mock.suspendQuintupleParametersMock
import kotlin.js.JsName
import kotlin.jvm.JvmName

/**
 * The [ParametersSpec] for a five parameters
 */
class QuintupleParameters<T0, T1, T2, T3, T4> : ParametersSpec<
    QuintupleParameters.Matchers<T0, T1, T2, T3, T4>,
    QuintupleParameters.MatchersOrCaptor<T0, T1, T2, T3, T4>,
    QuintupleParameters.Values<T0, T1, T2, T3, T4>,
    > {

    /**
     * The [ParametersSpec.Matchers] for five parameters
     * @param first the first parameters [ParameterMatcher]
     * @param second the second parameters [ParameterMatcher]
     * @param third the third parameters [ParameterMatcher]
     * @param fourth the fourth parameters [ParameterMatcher]
     * @param fifth the fifth parameters [ParameterMatcher]
     */
    data class Matchers<T0, T1, T2, T3, T4>(
        val first: ParameterMatcher<T0>,
        val second: ParameterMatcher<T1>,
        val third: ParameterMatcher<T2>,
        val fourth: ParameterMatcher<T3>,
        val fifth: ParameterMatcher<T4>,
    ) : ParametersSpec.Matchers {
        override fun asList() = listOf(first, second, third, fourth, fifth)
    }

    /**
     * The [ParametersSpec.MatchersOrCaptor] for five parameters
     * @param first the first parameters [ParameterMatcherOrCaptor]
     * @param second the second parameters [ParameterMatcherOrCaptor]
     * @param third the third parameters [ParameterMatcherOrCaptor]
     * @param fourth the fourth parameters [ParameterMatcherOrCaptor]
     * @param fifth the fifth parameters [ParameterMatcherOrCaptor]
     */
    data class MatchersOrCaptor<T0, T1, T2, T3, T4>(
        val first: ParameterMatcherOrCaptor<T0>,
        val second: ParameterMatcherOrCaptor<T1>,
        val third: ParameterMatcherOrCaptor<T2>,
        val fourth: ParameterMatcherOrCaptor<T3>,
        val fifth: ParameterMatcherOrCaptor<T4>,
    ) : ParametersSpec.MatchersOrCaptor<Matchers<T0, T1, T2, T3, T4>> {
        override fun asMatchers(): Matchers<T0, T1, T2, T3, T4> = Matchers(
            first.asMatcher(),
            second.asMatcher(),
            third.asMatcher(),
            fourth.asMatcher(),
            fifth.asMatcher(),
        )
    }

    /**
     * The [ParametersSpec.Values] for five parameters
     * @property first the first parameter
     * @property second the second parameter
     * @property third the third parameter
     * @property fourth the fourth parameter
     * @property fifth the fifth parameter
     */
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

internal fun <T0, T1, T2, T3, T4, R> ((T0, T1, T2, T3, T4) -> R).asMock() = quintupleParametersMock<T0, T1, T2, T3, T4, R>()

fun <T0, T1, T2, T3, T4, R> ((T0, T1, T2, T3, T4) -> R).mockWithDefaultAnswer(defaultAnswer: Answer<QuintupleParameters.Values<T0, T1, T2, T3, T4>, R>) = asMock().also {
    it.on(ParameterMatcher.any<T0>(), ParameterMatcher.any<T1>(), ParameterMatcher.any<T2>(), ParameterMatcher.any<T3>(), ParameterMatcher.any<T4>()).doAnswer(defaultAnswer)
}
fun <T0, T1, T2, T3, T4, R> ((T0, T1, T2, T3, T4) -> R).mockWithDefaultValue(defaultValue: R) = asMock().also {
    it.on(ParameterMatcher.any<T0>(), ParameterMatcher.any<T1>(), ParameterMatcher.any<T2>(), ParameterMatcher.any<T3>(), ParameterMatcher.any<T4>()).doReturn(defaultValue)
}

@JvmName("mockBoolean")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> Boolean).mock() = mockWithDefaultValue(false)

@JvmName("mockBooleanArray")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> BooleanArray).mock() = mockWithDefaultValue(booleanArrayOf())

@JvmName("mockByte")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> Byte).mock() = mockWithDefaultValue(0x0)

@JvmName("mockByteArray")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> ByteArray).mock() = mockWithDefaultValue(byteArrayOf())

@JvmName("mockChar")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> Char).mock() = mockWithDefaultValue(0.toChar())

@JvmName("mockCharArray")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> CharArray).mock() = mockWithDefaultValue(charArrayOf())

@JvmName("mockCharRange")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> CharRange).mock() = mockWithDefaultValue(CharRange.EMPTY)

@JvmName("mockDouble")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> Double).mock() = mockWithDefaultValue(0.0)

@JvmName("mockDoubleArray")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> DoubleArray).mock() = mockWithDefaultValue(doubleArrayOf())

@JvmName("mockFloat")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> Float).mock() = mockWithDefaultValue(0.0f)

@JvmName("mockFloatArray")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> FloatArray).mock() = mockWithDefaultValue(floatArrayOf())

@JvmName("mockInt")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> Int).mock() = mockWithDefaultValue(0)

@JvmName("mockIntArray")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> IntArray).mock() = mockWithDefaultValue(intArrayOf())

@JvmName("mockIntRange")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> IntRange).mock() = mockWithDefaultValue(IntRange.EMPTY)

@JvmName("mockLong")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> Long).mock() = mockWithDefaultValue(0L)

@JvmName("mockLongArray")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> LongArray).mock() = mockWithDefaultValue(longArrayOf())

@JvmName("mockLongRange")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> LongRange).mock() = mockWithDefaultValue(LongRange.EMPTY)

@JvmName("mockNumber")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> Number).mock() = mockWithDefaultValue(0)

@JvmName("mockShort")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> Short).mock() = mockWithDefaultValue(0.toShort())

@JvmName("mockShortArray")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> ShortArray).mock() = mockWithDefaultValue(shortArrayOf())

@JvmName("mockString")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> String).mock() = mockWithDefaultValue("")

@JvmName("mockUByte")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> UByte).mock() = mockWithDefaultValue(0x0U)

@JvmName("mockUByteArray")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> UByteArray).mock() = mockWithDefaultValue(ubyteArrayOf())

@JvmName("mockUInt")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> UInt).mock() = mockWithDefaultValue(0U)

@JvmName("mockUIntArray")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> UIntArray).mock() = mockWithDefaultValue(uintArrayOf())

@JvmName("mockUIntRange")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> UIntRange).mock() = mockWithDefaultValue(UIntRange.EMPTY)

@JvmName("mockULong")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> ULong).mock() = mockWithDefaultValue(0UL)

@JvmName("mockULongArray")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> ULongArray).mock() = mockWithDefaultValue(ulongArrayOf())

@JvmName("mockULongRange")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> ULongRange).mock() = mockWithDefaultValue(ULongRange.EMPTY)

@JvmName("mockUShort")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> UShort).mock() = mockWithDefaultValue(0.toUShort())

@JvmName("mockUShortArray")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> UShortArray).mock() = mockWithDefaultValue(ushortArrayOf())

@JvmName("mockUnit")
fun <T0, T1, T2, T3, T4> ((T0, T1, T2, T3, T4) -> Unit).mock() = mockWithDefaultValue(Unit)

@JvmName("mockArray")
inline fun <T0, T1, T2, T3, T4, reified R> ((T0, T1, T2, T3, T4) -> Array<R>).mock() = mockWithDefaultValue(emptyArray())

@JvmName("mockList")
fun <T0, T1, T2, T3, T4, R> ((T0, T1, T2, T3, T4) -> List<R>).mock() = mockWithDefaultValue(emptyList())

@JvmName("mockSet")
fun <T0, T1, T2, T3, T4, R> ((T0, T1, T2, T3, T4) -> Set<R>).mock() = mockWithDefaultValue(emptySet())

@JvmName("mockMap")
fun <T0, T1, T2, T3, T4, K, V> ((T0, T1, T2, T3, T4) -> Map<K, V>).mock() = mockWithDefaultValue(emptyMap())

@JvmName("mockNullable")
@JsName("mockQuintupleNullable")
fun <T0, T1, T2, T3, T4, R : Any> ((T0, T1, T2, T3, T4) -> R?).mock() = mockWithDefaultValue(null)

@JvmName("mockNonNullable")
@JsName("mockQuintupleNonNullable")
fun <T0, T1, T2, T3, T4, R : Any> ((T0, T1, T2, T3, T4) -> R).mock() = asMock()

internal fun <T0, T1, T2, T3, T4, R> (suspend (T0, T1, T2, T3, T4) -> R).asSuspendedMock() = suspendQuintupleParametersMock<T0, T1, T2, T3, T4, R>()

fun <T0, T1, T2, T3, T4, R> (suspend (T0, T1, T2, T3, T4) -> R).mockWithDefaultAnswer(defaultAnswer: SuspendedAnswer<QuintupleParameters.Values<T0, T1, T2, T3, T4>, R>) =
    asSuspendedMock().also {
        it.on(ParameterMatcher.any<T0>(), ParameterMatcher.any<T1>(), ParameterMatcher.any<T2>(), ParameterMatcher.any<T3>(), ParameterMatcher.any<T4>()).doAnswer(defaultAnswer)
    }
fun <T0, T1, T2, T3, T4, R> (suspend (T0, T1, T2, T3, T4) -> R).mockWithDefaultValue(defaultValue: R) = asSuspendedMock().also {
    it.on(ParameterMatcher.any<T0>(), ParameterMatcher.any<T1>(), ParameterMatcher.any<T2>(), ParameterMatcher.any<T3>(), ParameterMatcher.any<T4>()).doReturn(defaultValue)
}

@JvmName("mockBoolean")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> Boolean).mock() = mockWithDefaultValue(false)

@JvmName("mockBooleanArray")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> BooleanArray).mock() = mockWithDefaultValue(booleanArrayOf())

@JvmName("mockByte")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> Byte).mock() = mockWithDefaultValue(0x0)

@JvmName("mockByteArray")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> ByteArray).mock() = mockWithDefaultValue(byteArrayOf())

@JvmName("mockChar")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> Char).mock() = mockWithDefaultValue(0.toChar())

@JvmName("mockCharArray")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> CharArray).mock() = mockWithDefaultValue(charArrayOf())

@JvmName("mockCharRange")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> CharRange).mock() = mockWithDefaultValue(CharRange.EMPTY)

@JvmName("mockDouble")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> Double).mock() = mockWithDefaultValue(0.0)

@JvmName("mockDoubleArray")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> DoubleArray).mock() = mockWithDefaultValue(doubleArrayOf())

@JvmName("mockFloat")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> Float).mock() = mockWithDefaultValue(0.0f)

@JvmName("mockFloatArray")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> FloatArray).mock() = mockWithDefaultValue(floatArrayOf())

@JvmName("mockInt")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> Int).mock() = mockWithDefaultValue(0)

@JvmName("mockIntArray")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> IntArray).mock() = mockWithDefaultValue(intArrayOf())

@JvmName("mockIntRange")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> IntRange).mock() = mockWithDefaultValue(IntRange.EMPTY)

@JvmName("mockLong")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> Long).mock() = mockWithDefaultValue(0L)

@JvmName("mockLongArray")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> LongArray).mock() = mockWithDefaultValue(longArrayOf())

@JvmName("mockLongRange")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> LongRange).mock() = mockWithDefaultValue(LongRange.EMPTY)

@JvmName("mockNumber")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> Number).mock() = mockWithDefaultValue(0)

@JvmName("mockShort")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> Short).mock() = mockWithDefaultValue(0.toShort())

@JvmName("mockShortArray")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> ShortArray).mock() = mockWithDefaultValue(shortArrayOf())

@JvmName("mockString")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> String).mock() = mockWithDefaultValue("")

@JvmName("mockUByte")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> UByte).mock() = mockWithDefaultValue(0x0U)

@JvmName("mockUByteArray")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> UByteArray).mock() = mockWithDefaultValue(ubyteArrayOf())

@JvmName("mockUInt")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> UInt).mock() = mockWithDefaultValue(0U)

@JvmName("mockUIntArray")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> UIntArray).mock() = mockWithDefaultValue(uintArrayOf())

@JvmName("mockUIntRange")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> UIntRange).mock() = mockWithDefaultValue(UIntRange.EMPTY)

@JvmName("mockULong")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> ULong).mock() = mockWithDefaultValue(0UL)

@JvmName("mockULongArray")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> ULongArray).mock() = mockWithDefaultValue(ulongArrayOf())

@JvmName("mockULongRange")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> ULongRange).mock() = mockWithDefaultValue(ULongRange.EMPTY)

@JvmName("mockUShort")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> UShort).mock() = mockWithDefaultValue(0.toUShort())

@JvmName("mockUShortArray")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> UShortArray).mock() = mockWithDefaultValue(ushortArrayOf())

@JvmName("mockUnit")
fun <T0, T1, T2, T3, T4> (suspend (T0, T1, T2, T3, T4) -> Unit).mock() = mockWithDefaultValue(Unit)

@JvmName("mockArray")
inline fun <T0, T1, T2, T3, T4, reified R> (suspend (T0, T1, T2, T3, T4) -> Array<R>).mock() = mockWithDefaultValue(emptyArray())

@JvmName("mockList")
fun <T0, T1, T2, T3, T4, R> (suspend (T0, T1, T2, T3, T4) -> List<R>).mock() = mockWithDefaultValue(emptyList())

@JvmName("mockSet")
fun <T0, T1, T2, T3, T4, R> (suspend (T0, T1, T2, T3, T4) -> Set<R>).mock() = mockWithDefaultValue(emptySet())

@JvmName("mockMap")
fun <T0, T1, T2, T3, T4, K, V> (suspend (T0, T1, T2, T3, T4) -> Map<K, V>).mock() = mockWithDefaultValue(emptyMap())

@JvmName("mockNullable")
@JsName("mockQuintupleNullableSuspended")
fun <T0, T1, T2, T3, T4, R : Any> (suspend (T0, T1, T2, T3, T4) -> R?).mock() = mockWithDefaultValue(null)

@JvmName("mockNonNullable")
@JsName("mockQuintupleNonNullableSuspended")
fun <T0, T1, T2, T3, T4, R : Any> (suspend (T0, T1, T2, T3, T4) -> R).mock() = asSuspendedMock()
