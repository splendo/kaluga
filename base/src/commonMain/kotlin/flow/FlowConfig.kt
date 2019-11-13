package com.splendo.kaluga.flow
/*

Copyright 2019 Splendo Consulting B.V. The Netherlands

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

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.conflate

typealias FlowModifier = (Flow<*>) -> Unit

sealed class FlowConfig(val config:FlowModifier) {

    object None:FlowConfig({})
    object Conflate:FlowConfig({it.conflate()})
    object Infinite:FlowConfig({it.buffer(Channel.UNLIMITED)})
    class Custom(options:FlowModifier):FlowConfig(options)

    fun <T, F:Flow<T>> apply(flow:F):F {
        config(flow)
        return flow
    }
}




