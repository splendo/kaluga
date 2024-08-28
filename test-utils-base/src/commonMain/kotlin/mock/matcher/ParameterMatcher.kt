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

/**
 * Class for validating whether a parameter matches given constraints
 */
sealed class ParameterMatcher<in T> :
    ParameterMatcherOrCaptor<T>,
    Comparable<ParameterMatcher<*>> {
    /**
     * Checks if the parameter matches
     * @param value the parameter to match
     * @return `true` if matches
     */
    abstract fun matches(value: T): Boolean
    override fun asMatcher(): ParameterMatcher<T> = this

    companion object {
        /**
         * Creates a [ParameterMatcher] that matches only a given value.
         * This is the strongest [ParameterMatcher]
         * @param value The value that the parameter must match
         */
        fun <T> eq(value: T): ParameterMatcher<T> = EqualsMatcher(value)

        /**
         * Creates a [ParameterMatcher] that matches anything but a given value
         * This is stronger than [isInstance] but weaker than [matches].
         * @param value The value that the parameter must not match
         */
        fun <T> notEq(value: T): ParameterMatcher<T> = NotEqualsMatcher(value)

        /**
         * Creates a [ParameterMatcher] that matches any value in a given list
         * This is stronger than [matches] but weaker than [eq].
         * @param values The list of values that the parameter is allowed to match.
         */
        fun <T> oneOf(values: List<T>): ParameterMatcher<T> = OneOfMatcher(values)

        /**
         * Creates a [ParameterMatcher] that matches based on a given condition.
         * This is stronger than [notEq] but weaker than [oneOf].
         * @param condition The condition on which to match
         */
        fun <T> matching(condition: (T) -> Boolean): ParameterMatcher<T> = ConditionMatcher(condition)

        /**
         * Creates a [ParameterMatcher] that matches any value that is an instance of a given class
         * This is stronger than [notNull] but weaker than [notEq].
         * @param subclass The [KClass] to check the isntance for
         */
        fun <T : Any, S : T> isInstance(subclass: KClass<S>): ParameterMatcher<T> = SubClassMatcher(subclass)

        /**
         * Creates a [ParameterMatcher] that matches only non-null values
         * This is stronger than [any] but weaker than [isInstance].
         */
        fun <T : Any> notNull(): ParameterMatcher<T?> = AnyNotNullMatcher()

        /**
         * Creates a [ParameterMatcher] that matches anything
         * This is the weakest [ParameterMatcher].
         */
        fun <T> any(): ParameterMatcher<T> = AnyMatcher()

        private val priorityOrder = setOf(
            EqualsMatcher::class,
            OneOfMatcher::class,
            ConditionMatcher::class,
            NotEqualsMatcher::class,
            SubClassMatcher::class,
            AnyNotNullMatcher::class,
            AnyMatcher::class,
        )
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
        override fun equals(other: Any?): Boolean = (other as? AnyNotNullMatcher<*>) != null

        override fun hashCode(): Int = this::class.hashCode()
    }

    private class AnyMatcher<in T> : ParameterMatcher<T>() {
        override fun matches(value: T): Boolean = true
        override fun equals(other: Any?): Boolean = (other as? AnyMatcher<*>) != null

        override fun hashCode(): Int = this::class.hashCode()
    }
}
