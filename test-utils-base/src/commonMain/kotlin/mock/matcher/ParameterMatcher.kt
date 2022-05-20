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

package com.splendo.kaluga.test.base.mock.matcher

import kotlin.reflect.KClass

sealed class ParameterMatcher<in T> : ParameterMatcherOrCaptor<T>, Comparable<ParameterMatcher<*>> {
    /** @return true if the matches the matcher condition. */
    abstract fun matches(value: T): Boolean
    override fun asMatcher(): ParameterMatcher<T> = this

    companion object {
        fun <T> eq(value: T): ParameterMatcher<T> = EqualsMatcher(value)
        fun <T> notEq(value: T): ParameterMatcher<T> = NotEqualsMatcher(value)
        fun <T> onOf(values: List<T>): ParameterMatcher<T> = OneOfMatcher(values)
        fun <T> matching(condition: (T) -> Boolean): ParameterMatcher<T> = ConditionMatcher(condition)
        fun <T : Any, S : T> isInstance(subclass: KClass<S>): ParameterMatcher<T> = SubClassMatcher(subclass)
        fun <T : Any> notNull(): ParameterMatcher<T?> = AnyNotNullMatcher()
        fun <T> any(): ParameterMatcher<T> = AnyMatcher()

        private val priorityOrder = setOf(EqualsMatcher::class, OneOfMatcher::class, ConditionMatcher::class, NotEqualsMatcher::class, SubClassMatcher::class, AnyNotNullMatcher::class, AnyMatcher::class)
    }

    override fun compareTo(other: ParameterMatcher<*>): Int = priorityOrder.indexOf(this::class).compareTo(priorityOrder.indexOf(other::class))

    private data class EqualsMatcher<in T>(private val value: T) : ParameterMatcher<T>() {
        override fun matches(value: T): Boolean = this.value == value
    }

    private data class OneOfMatcher<in T>(private val values: List<T>) : ParameterMatcher<T>() {
        override fun matches(value: T): Boolean = values.contains(value)
    }

    private data class NotEqualsMatcher<in T>(private val value: T) : ParameterMatcher<T>() {
        override fun matches(value: T): Boolean = this.value != value
    }

    private data class ConditionMatcher<in T>(private val condition: (T) -> Boolean) : ParameterMatcher<T>() {
        override fun matches(value: T): Boolean = condition(value)
    }

    private data class SubClassMatcher<T : Any, S : T>(private val subclass: KClass<S>) : ParameterMatcher<T>() {
        override fun matches(value: T): Boolean = subclass.isInstance(value)
    }

    private class AnyNotNullMatcher<in T : Any> : ParameterMatcher<T?>() {
        override fun matches(value: T?): Boolean = value != null
        override fun equals(other: Any?): Boolean {
            return (other as? AnyNotNullMatcher<*>) != null
        }

        override fun hashCode(): Int {
            return this::class.hashCode()
        }
    }

    private class AnyMatcher<in T> : ParameterMatcher<T>() {
        override fun matches(value: T): Boolean = true
        override fun equals(other: Any?): Boolean {
            return (other as? AnyMatcher<*>) != null
        }

        override fun hashCode(): Int {
            return this::class.hashCode()
        }
    }
}
