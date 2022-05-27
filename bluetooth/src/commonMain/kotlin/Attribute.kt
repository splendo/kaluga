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

import com.splendo.kaluga.bluetooth.device.BaseDeviceConnectionManager
import com.splendo.kaluga.bluetooth.device.DeviceAction
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow

abstract class Attribute<R : DeviceAction.Read, W : DeviceAction.Write>(initialValue: ByteArray? = null, protected val newActionFlow: MutableSharedFlow<in BaseDeviceConnectionManager.Event.AddAction>) : Flow<ByteArray?> {
    abstract val uuid: UUID

    override suspend fun collect(collector: FlowCollector<ByteArray?>) =
        sharedFlow.collect(collector)

    // TODO make configurable
    private val sharedFlow = MutableSharedFlow<ByteArray?>(0, 256, BufferOverflow.DROP_OLDEST).also { it.tryEmit(initialValue) }

    suspend fun readValue(): DeviceAction {
        val action = createReadAction()
        addAction(action)
        return action
    }

    internal abstract fun createReadAction(): R

    suspend fun writeValue(newValue: ByteArray?): DeviceAction {
        val action = createWriteAction(newValue)
        addAction(action)
        return action
    }

    internal abstract fun createWriteAction(newValue: ByteArray?): W

    open suspend fun updateValue() {
        val nextValue = getUpdatedValue()
        sharedFlow.emit(nextValue)
    }

    internal abstract fun getUpdatedValue(): ByteArray?

    protected suspend fun addAction(action: DeviceAction) {
        newActionFlow.emit(BaseDeviceConnectionManager.Event.AddAction(action))
    }
}
