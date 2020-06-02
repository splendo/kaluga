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

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

/**
 * An object that be turned into a multi channel [Flow]
 *
 * @param T the type of the values emitted by the flowable.
 */
interface Flowable<T> {

    /**
     * Generates a [Flow] for this Flowable
     *
     * @param flowConfig the [FlowConfig] to apply to this Flow
     * @return A [Flow] of values set to the Flowable
     */
    @ExperimentalCoroutinesApi
    fun flow(flowConfig: FlowConfig = FlowConfig.Conflate): Flow<T>

    /**
     * Sets the value of the Flowable. All active [Flow]s should be notified
     *
     * @param value the value to set
     */
    suspend fun set(value: T)

    /**
     * Applies [set] in a blocking coroutine
     */
    fun setBlocking(value: T)

    /**
     * Closes the flowable from being observed. Closes all active [Flow]s
     */
    fun close()
}