/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.test.mock.bluetooth.device

import com.splendo.kaluga.base.utils.EmptyCompletableDeferred
import com.splendo.kaluga.base.utils.complete
import com.splendo.kaluga.base.utils.toHexString
import com.splendo.kaluga.bluetooth.asBytes
import com.splendo.kaluga.bluetooth.device.BaseDeviceConnectionManager
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.DeviceAction
import com.splendo.kaluga.bluetooth.device.DeviceStateFlowRepo
import com.splendo.kaluga.bluetooth.device.DeviceWrapper
import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.test.mock.bluetooth.MockCharacteristicWrapper
import com.splendo.kaluga.test.mock.bluetooth.MockDescriptorWrapper
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MockDeviceConnectionManager(
    connectionSettings: ConnectionSettings,
    deviceWrapper: DeviceWrapper,
    stateRepo: DeviceStateFlowRepo
) : BaseDeviceConnectionManager(connectionSettings, deviceWrapper, stateRepo) {

    var connectCompleted = EmptyCompletableDeferred()
    var discoverServicesCompleted = EmptyCompletableDeferred()
    var disconnectCompleted = EmptyCompletableDeferred()
    var readRssiCompleted = EmptyCompletableDeferred()
    var performActionCompleted = CompletableDeferred<DeviceAction>()
    private val _handledAction = MutableSharedFlow<DeviceAction>(replay = 1)
    val handledAction = _handledAction.asSharedFlow()

    fun reset() {
        connectCompleted = EmptyCompletableDeferred()
        discoverServicesCompleted = EmptyCompletableDeferred()
        disconnectCompleted = EmptyCompletableDeferred()
        readRssiCompleted = EmptyCompletableDeferred()
        performActionCompleted = CompletableDeferred()
    }

    override suspend fun connect() {
        connectCompleted.complete()
    }

    override suspend fun discoverServices() {
        discoverServicesCompleted.complete()
    }

    override suspend fun disconnect() {
        disconnectCompleted.complete()
    }

    override suspend fun readRssi() {
        readRssiCompleted.complete()
    }

    override suspend fun performAction(action: DeviceAction) {
        currentAction = action
        debug("Mock Action: $currentAction")
        when(action) {
            is DeviceAction.Read.Characteristic -> launch {
                handleUpdatedCharacteristic(action.characteristic.uuid) {
                    debug("Mock Read: ${action.characteristic.uuid} value ${action.characteristic.wrapper.value?.asBytes?.toHexString()}")
                }
                _handledAction.emit(action)
            }
            is DeviceAction.Read.Descriptor -> launch {
                handleUpdatedDescriptor(action.descriptor.uuid)
                _handledAction.emit(action)
            }
            is DeviceAction.Write.Characteristic -> launch {
                (action.characteristic.wrapper as MockCharacteristicWrapper).updateMockValue(action.newValue)
                handleUpdatedCharacteristic(action.characteristic.uuid) {
                    debug("Mock Write: ${action.characteristic.uuid} value ${action.characteristic.wrapper.value?.asBytes?.toHexString()}")
                }
                _handledAction.emit(action)
            }
            is DeviceAction.Write.Descriptor -> launch {
                (action.descriptor.wrapper as MockDescriptorWrapper).updateMockValue(action.newValue)
                handleUpdatedDescriptor(action.descriptor.uuid)
                _handledAction.emit(action)
            }
            is DeviceAction.Notification -> TODO()
        }

        performActionCompleted.complete(action)
    }
}
