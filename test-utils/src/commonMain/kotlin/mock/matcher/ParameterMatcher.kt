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

package com.splendo.kaluga.test.mock.matcher

sealed class ParameterMatcher<in T> : ParameterMatcherOrCaptor<T>, Comparable<ParameterMatcher<*>> {
    /** @return true if the matches the matcher condition. */
    abstract fun matches(value: T): Boolean
    override fun asMatcher(): ParameterMatcher<T> = this

    companion object {
        fun <T> eq(value: T): ParameterMatcher<T> = EqualsMatcher(value)
        fun <T> notEq(value: T): ParameterMatcher<T> = NotEqualsMatcher(value)
        fun <T> any(): AnyMatcher<T> = AnyMatcher()
        fun <T : Any> notNull(): ParameterMatcher<T?> = AnyNotNullMatcher()
        fun <T : Any> AnyMatcher<T>.orNull() = AnyMatcher<T?>()
        fun anyBoolean() = any<Boolean>()
        fun anyBooleanArray() = any<BooleanArray>()
        fun anyByte() = any<Byte>()
        fun anyByteArray() = any<ByteArray>()
        fun anyChar() = any<Char>()
        fun anyCharArray() = any<CharArray>()
        fun anyCharRange() = any<CharRange>()
        fun anyDouble() = any<Double>()
        fun anyDoubleArray() = any<DoubleArray>()
        fun anyFloat() = any<Float>()
        fun anyFloatArray() = any<FloatArray>()
        fun anyInt() = any<Int>()
        fun anyIntArray() = any<IntArray>()
        fun anyIntRange() = any<IntRange>()
        fun anyLong() = any<Long>()
        fun anyLongArray() = any<LongArray>()
        fun anyLongRange() = any<LongRange>()
        fun anyNumber() = any<Number>()
        fun anyShort() = any<Short>()
        fun anyShortArray() = any<ShortArray>()
        fun anyString() = any<String>()
        fun anyUByte() = any<UByte>()
        fun anyUByteArray() = any<UByteArray>()
        fun anyUInt() = any<UInt>()
        fun anyUIntArray() = any<UIntArray>()
        fun anyUIntRange() = any<UIntRange>()
        fun anyULong() = any<ULong>()
        fun anyULongArray() = any<ULongArray>()
        fun anyULongRange() = any<ULongRange>()
        fun anyUnit() = any<Unit>()
        fun anyUShort() = any<UShort>()
        fun anyUShortArray() = any<UShortArray>()
        fun <T> anyList() = any<List<T>>()
        fun <K, V> anyMap() = any<Map<K, V>>()
        fun <F, S> anyPair() = any<Pair<F, S>>()
        fun <T> anySet() = any<Set<T>>()
        fun <F, S, T> anyTriple() = any<Triple<F,S,T>>()
        fun anyObject() = any<Any>()
        fun <T> arg(condition: (T) -> Boolean): ParameterMatcher<T> = ConditionMatcher(condition)

        private val priorityOrder = setOf(EqualsMatcher::class, ConditionMatcher::class, NotEqualsMatcher::class, AnyNotNullMatcher::class, AnyMatcher::class)
    }

    override fun compareTo(other: ParameterMatcher<*>): Int = priorityOrder.indexOf(this::class).compareTo(priorityOrder.indexOf(other::class))

    private data class EqualsMatcher<in T>(private val value: T) : ParameterMatcher<T>() {
        override fun matches(value: T): Boolean = this.value == value
    }

    private data class NotEqualsMatcher<in T>(private val value: T) : ParameterMatcher<T>() {
        override fun matches(value: T): Boolean = this.value != value
    }

    private data class ConditionMatcher<in T>(private val condition: (T) -> Boolean) : ParameterMatcher<T>() {
        override fun matches(value: T): Boolean = condition(value)
    }

    class AnyNotNullMatcher<in T : Any> : ParameterMatcher<T?>() {
        override fun matches(value: T?): Boolean = value != null
        override fun equals(other: Any?): Boolean {
            return (other as? AnyNotNullMatcher<*>) != null
        }

        override fun hashCode(): Int {
            return this::class.hashCode()
        }
    }

    class AnyMatcher<in T> : ParameterMatcher<T>() {
        override fun matches(value: T): Boolean = true
        override fun equals(other: Any?): Boolean {
            return (other as? AnyMatcher<*>) != null
        }

        override fun hashCode(): Int {
            return this::class.hashCode()
        }
    }
}
