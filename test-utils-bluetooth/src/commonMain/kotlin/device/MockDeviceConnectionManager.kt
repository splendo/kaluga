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

package com.splendo.kaluga.test.bluetooth.device

import co.touchlab.stately.collections.sharedMutableListOf
import co.touchlab.stately.concurrency.AtomicBoolean
import com.splendo.kaluga.base.utils.toHexString
import com.splendo.kaluga.bluetooth.asBytes
import com.splendo.kaluga.bluetooth.device.BaseDeviceConnectionManager
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.DeviceAction
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.bluetooth.device.DeviceStateFlowRepo
import com.splendo.kaluga.bluetooth.device.DeviceWrapper
import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.test.bluetooth.MockCharacteristicWrapper
import com.splendo.kaluga.test.bluetooth.MockDescriptorWrapper
import com.splendo.kaluga.test.mock.call
import com.splendo.kaluga.test.mock.on
import com.splendo.kaluga.test.mock.parameters.mock
import kotlinx.coroutines.launch

class MockDeviceConnectionManager(
    initialWillActionSucceed: Boolean = true,
    connectionSettings: ConnectionSettings,
    deviceWrapper: DeviceWrapper,
    stateRepo: DeviceStateFlowRepo,
    setupMocks: Boolean = true
) : BaseDeviceConnectionManager(connectionSettings, deviceWrapper, stateRepo) {

    class Builder(initialWillActionSucceed: Boolean = true, setupMocks: Boolean = true) : BaseDeviceConnectionManager.Builder {

        val createdDeviceConnectionManager = sharedMutableListOf<MockDeviceConnectionManager>()
        val createMock = ::create.mock()

        init {
            if (setupMocks) {
                createMock.on().doExecute { (connectionSettings, deviceWrapper, stateRepo) ->
                    MockDeviceConnectionManager(initialWillActionSucceed, connectionSettings, deviceWrapper, stateRepo, setupMocks).also {
                        createdDeviceConnectionManager.add(it)
                    }
                }
            }
        }

        override fun create(
            connectionSettings: ConnectionSettings,
            deviceWrapper: DeviceWrapper,
            stateRepo: DeviceStateFlowRepo
        ): BaseDeviceConnectionManager = createMock.call(connectionSettings, deviceWrapper, stateRepo)
    }

    private val _willActionSucceed = AtomicBoolean(initialWillActionSucceed)
    var willActionSucceed: Boolean
        get() = _willActionSucceed.value
        set(value) { _willActionSucceed.value = value }

    val connectMock = ::connect.mock()
    val discoverServicesMock = ::discoverServices.mock()
    val disconnectMock = ::disconnect.mock()
    val readRssiMock = ::readRssi.mock()
    val requestMtuMock = ::requestMtu.mock()
    val performActionMock = ::performAction.mock()
    val handleCurrentActionCompletedMock = ::handleCurrentActionCompletedWithAction.mock()

    init {
        if (setupMocks) {
            requestMtuMock.on().doExecuteSuspended { (mtu) ->
                handleNewMtu(mtu)
                true
            }

            performActionMock.on().doExecuteSuspended { (action) ->
                currentAction = action
            }
        }
    }

    override suspend fun connect(): Unit = connectMock.call()

    override suspend fun discoverServices(): Unit = discoverServicesMock.call()

    override suspend fun disconnect(): Unit = disconnectMock.call()

    override suspend fun readRssi(): Unit = readRssiMock.call()

    override suspend fun requestMtu(mtu: Int): Boolean = requestMtuMock.call(mtu)

    override suspend fun performAction(action: DeviceAction): Unit = performActionMock.call(action)

    suspend fun handleCurrentAction() {
        when (val action = currentAction) {
            is DeviceAction.Read.Characteristic -> handleUpdatedCharacteristic(action.characteristic.uuid, willActionSucceed) {
                debug("Mock Read: ${action.characteristic.uuid} value ${action.characteristic.wrapper.value?.asBytes?.toHexString()}")
            }
            is DeviceAction.Read.Descriptor -> handleUpdatedDescriptor(action.descriptor.uuid, willActionSucceed)
            is DeviceAction.Write.Characteristic -> {
                (action.characteristic.wrapper as MockCharacteristicWrapper).updateMockValue(action.newValue)
                handleUpdatedCharacteristic(action.characteristic.uuid, willActionSucceed) {
                    debug("Mock Write: ${action.characteristic.uuid} value ${action.characteristic.wrapper.value?.asBytes?.toHexString()}")
                }
            }
            is DeviceAction.Write.Descriptor -> {
                (action.descriptor.wrapper as MockDescriptorWrapper).updateMockValue(action.newValue)
                handleUpdatedDescriptor(action.descriptor.uuid, willActionSucceed)
            }
            is DeviceAction.Notification -> handleCurrentActionCompleted(willActionSucceed)
        }
    }

    override suspend fun handleCurrentActionCompleted(succeeded: Boolean): DeviceState {
        handleCurrentActionCompletedWithAction(succeeded, currentAction)
        return super.handleCurrentActionCompleted(succeeded)
    }

    private fun handleCurrentActionCompletedWithAction(succeeded: Boolean, deviceAction: DeviceAction?): Unit = handleCurrentActionCompletedMock.call(succeeded, deviceAction)
}
