/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.formatted

/**
 * Collection of modifiers
 *
 * Class can be used if multiple modification should be applied to value in [Formatted] class
 *
 * @property list list of modifiers
 */
class ModifiersScope<T>(private val list: List<Modifier<T>>) : Modifier<T> {
    constructor(vararg list: Modifier<T>) : this(list.toList())

    /**
     * Applies modifiers in the same order as they are in the [list]
     */
    override fun apply(value: T): T {
        var mutableValue = value
        list.forEach {
            mutableValue = it.apply(mutableValue)
        }
        return mutableValue
    }
}
