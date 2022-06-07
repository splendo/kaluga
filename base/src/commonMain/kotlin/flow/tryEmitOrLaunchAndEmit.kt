/*
 * Copyright 2022 Splendo Consulting B.V. The Netherlands
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.splendo.kaluga.base.flow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

/**
 * Calls [MutableSharedFlow.tryEmit]. If emitting the value fails, this method will launch [coroutineScope] and emit on it (suspended) instead.
 */
fun <T> MutableSharedFlow<T>.tryEmitOrLaunchAndEmit(value: T, coroutineScope: CoroutineScope): Boolean = if (!tryEmit(value)) {
    coroutineScope.launch {
        emit(value)
    }
    false
} else {
    true
}

