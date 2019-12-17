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

import com.splendo.kaluga.base.runBlocking
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

open class BaseFlowable<T>(private val channelFactory: () -> BroadcastChannel<T> = {ConflatedBroadcastChannel()}) : Flowable<T> {

    private var channel = lazy {  channelFactory() }
    private var flowing: Boolean = false

    final override fun flow(flowConfig: FlowConfig): Flow<T> {
        val flow = channel.value.asFlow()

        flow.onStart {
            flowing = true
            initialize()
        }
        flow.onCompletion {complete()}

        return flowConfig.apply(flow)
    }
    protected open suspend fun initialize() {}
    //protected open suspend fun complete() {}

    suspend fun set(value: T) {
        if (flowing)
            channel.value.send(value)
    }

    fun setBlocking(value:T) {
        // if a conflated broadcast channel is used it always accepts input non-blocking (provided the channel is not closed)
        runBlocking {
            set(value)
        }
    }
}