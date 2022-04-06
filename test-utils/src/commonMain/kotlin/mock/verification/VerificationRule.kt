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

package com.splendo.kaluga.test.mock.verification

sealed interface VerificationRule {
    fun matches(times: Int): Boolean

    companion object {
        fun never() = Never
        fun times(times: Int) = Exactly(times)
        fun between(range: IntRange) = Range(range)
        fun atLeast(times: Int) = Range(times..Int.MAX_VALUE)
        fun atMost(times: Int) = Range(0..times)
    }

    object Never : VerificationRule {
        override fun matches(times: Int) = times == 0
    }

    data class Exactly(val times: Int) : VerificationRule {
        override fun matches(times: Int): Boolean = times == this.times
    }

    data class Range(val range: IntRange) : VerificationRule {
        override fun matches(times: Int): Boolean = times in range
    }
}
