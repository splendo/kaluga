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

/**
 * A bluetooth attribute conforming to the Attribute Protocol in Bluetooth Low Energy
 * @param ReadAction the [DeviceAction.Read] associated with the attribute
 * @param WriteAction the [DeviceAction.Write] associated with the attribute
 * @param initialValue the initial [ByteArray] value of the attribute
 * @param emitNewAction method to call when a new [DeviceConnectionManager.Event.AddAction] event should take place
 * @param parentLogTag the log tag used to modify the log tag of this attribute
 * @param logger the [Logger] to use for logging.
 */
abstract class Attribute<ReadAction : DeviceAction.Read, WriteAction : DeviceAction.Write>(
    initialValue: ByteArray? = null,
    private val emitNewAction: (DeviceConnectionManager.Event.AddAction) -> Unit,
    private val parentLogTag: String,
    private val logger: Logger,
) : Flow<ByteArray?> {

    protected val logTag: String get() = "$parentLogTag-${uuid.uuidString}"

    /**
     * The [UUID] of the attribute
     */
    abstract val uuid: UUID

    override suspend fun collect(collector: FlowCollector<ByteArray?>) = sharedFlow.collect(collector)

    // TODO make configurable
    private val sharedFlow = MutableSharedFlow<ByteArray?>(0, 1024, BufferOverflow.SUSPEND).also { it.tryEmit(initialValue) }

    /**
     * Creates and emits a [ReadAction]
     * @return the [ReadAction] created
     */
    fun readValue(): ReadAction {
        val action = createReadAction()
        addAction(action)
        return action
    }

    internal abstract fun createReadAction(): ReadAction

    /**
     * Creates and emits a [WriteAction] to write a given [ByteArray]
     * @param newValue the [ByteArray] to write to the attribute
     * @return the [WriteAction] created
     */
    fun writeValue(newValue: ByteArray): WriteAction {
        val action = createWriteAction(newValue)
        addAction(action)
        return action
    }

    internal abstract fun createWriteAction(newValue: ByteArray): WriteAction

    /**
     * Notifies the attribute that a new value may be available
     */
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
