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

package com.splendo.kaluga.test.base.mock.answer

import com.splendo.kaluga.test.base.mock.parameters.ParametersSpec

/**
 * Receives [ParametersSpec.Values] to return a result
 */
sealed interface BaseAnswer<V : ParametersSpec.Values, R>

/**
 * A [BaseAnswer] for non-suspending methods
 */
interface Answer<V : ParametersSpec.Values, R> : BaseAnswer<V, R> {

    /**
     * Answers with a result for a set of arguments
     * @param arguments The [V] arguments received
     * @return The [R] result based on [arguments]
     */
    fun call(arguments: V): R
}

/**
 * A [BaseAnswer] for suspending methods
 */
interface SuspendedAnswer<V : ParametersSpec.Values, R> : BaseAnswer<V, R> {

    /**
     * Answers suspended with a result for a set of arguments
     * @param arguments The [V] arguments received
     * @return The [R] result based on [arguments]
     */
    suspend fun call(arguments: V): R
}
