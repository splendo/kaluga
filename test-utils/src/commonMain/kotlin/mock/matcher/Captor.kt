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

import com.splendo.kaluga.test.mock.matcher.ParameterMatcher.Companion.orNull

sealed interface Captor<T> : ParameterMatcherOrCaptor<T> {
    val captured: List<T>
    val lastCaptured: T?
    fun capture(value: T)
}

class AnyCaptor<T : Any> : Captor<T> {
    private val _captured = mutableListOf<T>()
    override val captured: List<T> get() = _captured.toList()
    override val lastCaptured: T? get() = _captured.lastOrNull()

    override fun capture(value: T) {
        _captured.add(value)
    }

    override fun asMatcher(): ParameterMatcher<T> = ParameterMatcher.any<T>()
}

class AnyOrNullCaptor<T : Any> : Captor<T?> {
    private val _captured = mutableListOf<T?>()
    override val captured: List<T?> get() = _captured.toList()
    override val lastCaptured: T? get() = _captured.lastOrNull()

    override fun capture(value: T?) {
        _captured.add(value)
    }

    override fun asMatcher(): ParameterMatcher<T?> = ParameterMatcher.any<T>().orNull()
}
