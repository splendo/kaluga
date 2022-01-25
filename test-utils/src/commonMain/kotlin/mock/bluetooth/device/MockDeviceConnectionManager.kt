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

import co.touchlab.stately.collections.sharedMutableMapOf
import co.touchlab.stately.concurrency.AtomicBoolean
import co.touchlab.stately.concurrency.AtomicReference
import com.splendo.kaluga.base.utils.EmptyCompletableDeferred
import com.splendo.kaluga.base.utils.complete
import com.splendo.kaluga.base.utils.toHexString
import com.splendo.kaluga.bluetooth.asBytes
import com.splendo.kaluga.bluetooth.device.BaseDeviceConnectionManager
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.DeviceAction
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.bluetooth.device.DeviceStateFlowRepo
import com.splendo.kaluga.bluetooth.device.DeviceWrapper
import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.test.mock.bluetooth.MockCharacteristicWrapper
import com.splendo.kaluga.test.mock.bluetooth.MockDescriptorWrapper
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

class MockDeviceConnectionManager(
    connectionSettings: ConnectionSettings,
    deviceWrapper: DeviceWrapper,
    stateRepo: DeviceStateFlowRepo
) : BaseDeviceConnectionManager(connectionSettings, deviceWrapper, stateRepo) {

    val connectCompleted = AtomicReference(EmptyCompletableDeferred())
    val discoverServicesCompleted = AtomicReference(EmptyCompletableDeferred())
    val disconnectCompleted = AtomicReference(EmptyCompletableDeferred())
    val readRssiCompleted = AtomicReference(EmptyCompletableDeferred())
    val performActionCompleted = AtomicReference(CompletableDeferred<DeviceAction>())
    val performActionStarted = AtomicReference(CompletableDeferred<DeviceAction>())
    private val _handledAction = MutableSharedFlow<DeviceAction>(replay = 16, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val handledAction = _handledAction.asSharedFlow()
    var willActionSucceed = AtomicBoolean(true)
    val unpairCompleted = AtomicReference(EmptyCompletableDeferred())

    fun reset() {
        connectCompleted.set(EmptyCompletableDeferred())
        discoverServicesCompleted.set(EmptyCompletableDeferred())
        disconnectCompleted.set(EmptyCompletableDeferred())
        readRssiCompleted.set(EmptyCompletableDeferred())
        performActionCompleted.set(CompletableDeferred())
        performActionStarted.set(CompletableDeferred())
        _handledAction.resetReplayCache()
        unpairCompleted.set(EmptyCompletableDeferred())
    }

    override suspend fun connect() {
        connectCompleted.get().complete()
    }

    override suspend fun discoverServices() {
        discoverServicesCompleted.get().complete()
    }

    override suspend fun disconnect() {
        disconnectCompleted.get().complete()
    }

    override suspend fun readRssi() {
        readRssiCompleted.get().complete()
    }

    var waitAfterHandlingAction: MutableMap<KClass<out DeviceAction>, EmptyCompletableDeferred> = sharedMutableMapOf()

    override suspend fun performAction(action: DeviceAction) {
        currentAction = action
        debug("Mock Action: $currentAction")
        performActionStarted.get().complete(action)

        when (action) {
            is DeviceAction.Read.Characteristic -> launch {
                handleUpdatedCharacteristic(action.characteristic.uuid, willActionSucceed.value) {
                    debug("Mock Read: ${action.characteristic.uuid} value ${action.characteristic.wrapper.value?.asBytes?.toHexString()}")
                }
                _handledAction.emit(action)
            }
            is DeviceAction.Read.Descriptor -> launch {
                handleUpdatedDescriptor(action.descriptor.uuid, willActionSucceed.value)
                _handledAction.emit(action)
            }
            is DeviceAction.Write.Characteristic -> launch {
                (action.characteristic.wrapper as MockCharacteristicWrapper).updateMockValue(action.newValue)
                handleUpdatedCharacteristic(action.characteristic.uuid, willActionSucceed.value) {
                    debug("Mock Write: ${action.characteristic.uuid} value ${action.characteristic.wrapper.value?.asBytes?.toHexString()}")
                }
                debug("Will emit write action")
                _handledAction.emit(action)
                debug("Did emit write action")
            }
            is DeviceAction.Write.Descriptor -> launch {
                (action.descriptor.wrapper as MockDescriptorWrapper).updateMockValue(action.newValue)
                handleUpdatedDescriptor(action.descriptor.uuid, willActionSucceed.value)
                _handledAction.emit(action)
            }
            is DeviceAction.Notification -> launch {
                handleCurrentActionCompleted(willActionSucceed.value)
                _handledAction.emit(action)
            }
        }

        performActionCompleted.get().complete(action)
    }

    override suspend fun handleCurrentActionCompleted(succeeded: Boolean): DeviceState {

        currentAction?.let { currentAction ->
            val wait = waitAfterHandlingAction[currentAction::class]
            wait?.await()
        }
        return super.handleCurrentActionCompleted(succeeded)
    }

    override fun unpair() = unpairCompleted.get().complete()
}
