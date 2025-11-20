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
import com.splendo.kaluga.logging.ContextualLogger
import com.splendo.kaluga.logging.info
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

interface Attribute {
    /**
     * The [UUID] of the attribute
     */
    val uuid: UUID
}

operator fun <T : Attribute> List<T>.get(uuid: UUID) = first { it.uuid.uuidString == uuid.uuidString }
fun <T : Attribute> List<T>.getOrNull(uuid: UUID) = find { it.uuid.uuidString == uuid.uuidString }
operator fun <T : Attribute> Flow<List<T>>.get(uuid: UUID): Flow<T> = this.map { attributes ->
    attributes[uuid]
}.distinctUntilChanged()
fun <T : Attribute> Flow<List<T>>.getOrNull(uuid: UUID): Flow<T?> = this.map { attributes ->
    attributes.getOrNull(uuid)
}.distinctUntilChanged()

/**
 * A bluetooth attribute conforming to the Attribute Protocol in Bluetooth Low Energy
 * @param ReadAction the [DeviceAction.Read] associated with the attribute
 * @param WriteAction the [DeviceAction.Write] associated with the attribute
 * @param emitNewAction method to call when a new [DeviceConnectionManager.Event.AddAction] event should take place
 * @param logger the [ContextualLogger] to use for logging.
 */
abstract class RemoteAttribute<ReadAction : DeviceAction.Read, WriteAction : DeviceAction.Write>(
    private val emitNewAction: (DeviceConnectionManager.Event.AddAction) -> Unit,
    private val logger: ContextualLogger,
) : Attribute {

    /**
     * Creates and emits a [ReadAction]
     * @return the [ReadAction] created
     */
    fun readValue(): ReadAction {
        val action = createReadAction()
        if (!action.response.isCompleted) {
            addAction(action)
        }
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
        if (!action.response.isCompleted) {
            addAction(action)
        }
        return action
    }

    internal abstract fun createWriteAction(newValue: ByteArray): WriteAction

    protected fun addAction(action: DeviceAction<*>) {
        logger.info { "Add action $action" }
        emitNewAction(DeviceConnectionManager.Event.AddAction(action))
    }
}
