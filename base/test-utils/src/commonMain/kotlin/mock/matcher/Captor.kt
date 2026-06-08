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

import com.splendo.kaluga.base.collections.concurrentMutableListOf

/**
 * Captures a parameter to it can be extracted from [com.splendo.kaluga.test.base.mock.verify]
 */
sealed interface Captor<T> : ParameterMatcherOrCaptor<T> {
    /**
     * The list of all parameters that were captured by this captor
     */
    val captured: List<T>

    /**
     * The last parameter captured by this captor or null if no parameter was captured
     */
    val lastCaptured: T?

    /**
     * Captures a given value
     * @param value the value to capture.
     */
    fun capture(value: T)
}

/**
 * A [Captor] for capturing non-nullable parameters
 */
class AnyCaptor<T : Any> : Captor<T> {
    private val _captured = concurrentMutableListOf<T>()
    override val captured: List<T> get() = _captured.toList()
    override val lastCaptured: T? get() = _captured.lastOrNull()

    override fun capture(value: T) {
        _captured.add(value)
    }

    override fun asMatcher(): ParameterMatcher<T> = ParameterMatcher.any()
}

/**
 * A [Captor] for capturing nullable parameters
 */
class AnyOrNullCaptor<T : Any> : Captor<T?> {
    private val _captured = concurrentMutableListOf<T?>()
    override val captured: List<T?> get() = _captured.toList()
    override val lastCaptured: T? get() = _captured.lastOrNull()

    override fun capture(value: T?) {
        _captured.add(value)
    }

    override fun asMatcher(): ParameterMatcher<T?> = ParameterMatcher.any()
}
