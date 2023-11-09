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

package com.splendo.kaluga.test.base.mock.verification

/**
 * Rule for checking how often a [com.splendo.kaluga.test.base.mock.BaseMethodMock] can be called
 */
sealed interface VerificationRule {
    /**
     * Checks if the number of times a mock was called matches the verification rule
     * @param times the number of times the mock was called.
     */
    fun matches(times: Int): Boolean

    companion object {
        /**
         * Creates a [VerificationRule] that doesnt allow for any calls
         */
        fun never() = Never

        /**
         * Creates a [VerificationRule] that allows for a fixed number of calls
         * @param times the number of calls allowed
         */
        fun times(times: Int) = Exactly(times)

        /**
         * Creates a [VerificationRule] that allows for a number of calls within a given range
         * @param range the range between which the number of calls is allowed
         */
        fun between(range: IntRange) = Range(range)

        /**
         * Creates a [VerificationRule] that allows for at least a number of calls
         * @param times the minimum number of calls required
         */
        fun atLeast(times: Int) = Range(times..Int.MAX_VALUE)

        /**
         * Creates a [VerificationRule] that allows for at most a number of calls
         * @param times the maximum number of calls required
         */
        fun atMost(times: Int) = Range(0..times)
    }

    data object Never : VerificationRule {
        override fun matches(times: Int) = times == 0
    }

    data class Exactly(val times: Int) : VerificationRule {
        override fun matches(times: Int): Boolean = times == this.times
    }

    data class Range(val range: IntRange) : VerificationRule {
        override fun matches(times: Int): Boolean = times in range
    }
}
