/*
 Copyright (c) 2020. Splendo Consulting B.V. The Netherlands

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

import com.splendo.kaluga.flow.BaseFlowable
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel

/**
 * A [BaseFlowable] that represents a Hot flow. This flowable will contain data even if not observed.
 *
 * @param T the type of the value to flow on
 * @param initialValue the initial value of the flow
 * @param channelFactory Factory for generating a [BroadcastChannel] on which the data is flown
 */
class HotFlowable<T>(initialValue: T, channelFactory: () -> BroadcastChannel<T> = {ConflatedBroadcastChannel()}) : BaseFlowable<T>(channelFactory) {

    init {
        setBlocking(initialValue)
    }

}