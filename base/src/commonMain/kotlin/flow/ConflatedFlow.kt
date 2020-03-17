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

package com.splendo.kaluga.base.flow

import com.splendo.kaluga.base.MainQueueDispatcher
import com.splendo.kaluga.logging.debug
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

// Example from:
// https://github.com/JetBrains/kotlinconf-app/blob/master/common/src/mobileMain/kotlin/org/jetbrains/kotlinconf/FlowUtils.kt

fun <T> ConflatedBroadcastChannel<T>.wrap(): ConflatedFlow<T> = ConflatedFlow(asFlow())

fun <T> Flow<T>.wrap(): ConflatedFlow<T> = ConflatedFlow(this)

class ConflatedFlow<T>(private val origin: Flow<T>) : Flow<T> by origin {

    init {
        debug("ConflatedFlow has been initialized")
    }

    fun watch(block: (T) -> Unit) {
        val conflatedFlowJob = Job()
        debug("watch() has been called")
        onEach {
            debug("onEach() has been called")
            block(it)
        }.launchIn(CoroutineScope(MainQueueDispatcher + conflatedFlowJob))
    }
}
