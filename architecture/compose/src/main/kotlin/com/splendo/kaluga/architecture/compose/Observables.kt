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
package com.splendo.kaluga.architecture.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import com.splendo.kaluga.architecture.observable.WithMutableState
import com.splendo.kaluga.architecture.observable.WithState

/**
 * Creates a [State] whose value always matches a [WithState] using [WithState.stateFlow]
 */
@Suppress("NOTHING_TO_INLINE")
@Composable
inline fun <R> WithState<R>.state(): State<R> = this.stateFlow.collectAsState()

/**
 * Creates a [MutableState] that is synchronized with a [WithMutableState].
 */
// @Suppress("NOTHING_TO_INLINE") // inlining currently breaks compilation
@Composable
fun <R> WithMutableState<R>.mutableState(): MutableState<R> {
    val readState = state()

    return remember(this) {
        object : MutableState<R> {
            override var value: R
                get() = readState.value
                set(value) {
                    stateFlow.value = value
                }

            override fun component1(): R = value
            override fun component2(): (R) -> Unit = {
                value = it
            }
        }
    }
}
