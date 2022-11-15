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

import com.splendo.kaluga.test.base.mock.matcher.ParameterMatcher

/**
 * Specifies the parameters to be received by a [com.splendo.kaluga.test.base.mock.BaseMethodMock]
 */
interface ParametersSpec<M : ParametersSpec.Matchers, C : ParametersSpec.MatchersOrCaptor<M>, V : ParametersSpec.Values> {
    /**
     * The spec describing the [ParameterMatcher] used for a [ParametersSpec]
     */
    interface Matchers {
        /**
         * Returns a list of [ParameterMatcher] for this spec
         */
        fun asList(): List<ParameterMatcher<*>>
    }

    /**
     * The spec describing the [com.splendo.kaluga.test.base.mock.matcher.ParameterMatcherOrCaptor] used for a [ParametersSpec]
     */
    interface MatchersOrCaptor<M : Matchers> {
        /**
         * Converts into a [Matchers]
         */
        fun asMatchers(): M
    }

    /**
     * The spec describing the values used for a [ParametersSpec]
     */
    interface Values

    /**
     * Checks if [V] matches for [M]
     * @param values The [V] to match
     * @return `true` if the values match
     */
    fun M.matches(values: V): Boolean

    /**
     * Captures [V] for [C]
     * @param values the [V] to capture
     */
    fun C.capture(values: V)
}
