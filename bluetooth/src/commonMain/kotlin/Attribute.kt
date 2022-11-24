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

package com.splendo.kaluga.bluetooth

import com.splendo.kaluga.bluetooth.device.DeviceAction
import com.splendo.kaluga.bluetooth.device.DeviceConnectionManager
import com.splendo.kaluga.logging.Logger
import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.logging.info
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow

abstract class Attribute<R : DeviceAction.Read, W : DeviceAction.Write>(
    initialValue: ByteArray? = null,
    private val emitNewAction: (DeviceConnectionManager.Event.AddAction) -> Unit,
    private val parentLogTag: String,
    private val logger: Logger
) : Flow<ByteArray?> {

    protected val logTag: String get() = "$parentLogTag-${uuid.uuidString}"

    abstract val uuid: UUID

    override suspend fun collect(collector: FlowCollector<ByteArray?>) =
        sharedFlow.collect(collector)

    // TODO make configurable
    private val sharedFlow = MutableSharedFlow<ByteArray?>(0, 256, BufferOverflow.DROP_OLDEST).also { it.tryEmit(initialValue) }

    fun readValue(): DeviceAction {
        val action = createReadAction()
        addAction(action)
        return action
    }

    internal abstract fun createReadAction(): R

    fun writeValue(newValue: ByteArray): DeviceAction {
        val action = createWriteAction(newValue)
        addAction(action)
        return action
    }

    internal abstract fun createWriteAction(newValue: ByteArray): W

    open fun updateValue() {
        val nextValue = getUpdatedValue()
        logger.debug(logTag) { "Updated value to $nextValue" }
        sharedFlow.tryEmit(nextValue)
    }

    internal abstract fun getUpdatedValue(): ByteArray?

    protected fun addAction(action: DeviceAction) {
        logger.info(logTag) { "Add action $action" }
        emitNewAction(DeviceConnectionManager.Event.AddAction(action))
    }
}
