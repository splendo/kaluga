/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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

import com.splendo.kaluga.base.collections.concurrentMutableListOf
import com.splendo.kaluga.base.utils.toHexString
import com.splendo.kaluga.bluetooth.Characteristic
import com.splendo.kaluga.bluetooth.Descriptor
import com.splendo.kaluga.bluetooth.MTU
import com.splendo.kaluga.bluetooth.RSSI
import com.splendo.kaluga.bluetooth.Service
import com.splendo.kaluga.bluetooth.ServiceWrapper
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.asBytes
import com.splendo.kaluga.bluetooth.device.BaseDeviceConnectionManager
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.DeviceAction
import com.splendo.kaluga.bluetooth.device.DeviceConnectionManager
import com.splendo.kaluga.bluetooth.device.DeviceWrapper
import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.test.base.mock.call
import com.splendo.kaluga.test.base.mock.on
import com.splendo.kaluga.test.base.mock.parameters.mock
import com.splendo.kaluga.test.bluetooth.MockCharacteristicWrapper
import com.splendo.kaluga.test.bluetooth.MockDescriptorWrapper
import kotlinx.coroutines.CoroutineScope

/**
 * Mock implementation of [BaseDeviceConnectionManager]
 * @param initialWillActionSucceed Sets the initial status of whether actions will succeed
 * @param deviceWrapper The [DeviceWrapper] to connect to
 * @param connectionSettings The [ConnectionSettings] to apply for connecting
 * @param coroutineScope The [CoroutineScope] of the [BaseDeviceConnectionManager]
 * @param setupMocks If `true` this will automatically configure the mocks to handle connecting
 */
class MockDeviceConnectionManager(
    initialWillActionSucceed: Boolean = true,
    deviceWrapper: DeviceWrapper,
    connectionSettings: ConnectionSettings,
    coroutineScope: CoroutineScope,
    setupMocks: Boolean = true,
) : BaseDeviceConnectionManager(deviceWrapper, connectionSettings, coroutineScope) {

    /**
     * Mock implementation of [DeviceConnectionManager.Builder]
     * @param initialWillActionSucceed Sets the initial status of whether actions will succeed for each created [MockDeviceConnectionManager]
     * @param setupMocks If `true` this will automatically configure the [createMock] to create a [MockDeviceConnectionManager]
     */
    class Builder(initialWillActionSucceed: Boolean = true, setupMocks: Boolean = true) : DeviceConnectionManager.Builder {

        /**
         * List of created [MockDeviceConnectionManager]
         */
        val createdDeviceConnectionManager = concurrentMutableListOf<MockDeviceConnectionManager>()

        /**
         * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [create]
         */
        val createMock = ::create.mock()

        init {
            if (setupMocks) {
                createMock.on().doExecute { (deviceWrapper, settings, coroutineScope) ->
                    MockDeviceConnectionManager(initialWillActionSucceed, deviceWrapper, settings, coroutineScope, setupMocks).also {
                        createdDeviceConnectionManager.add(it)
                    }
                }
            }
        }

        override fun create(deviceWrapper: DeviceWrapper, settings: ConnectionSettings, coroutineScope: CoroutineScope): BaseDeviceConnectionManager {
            return createMock.call(deviceWrapper, settings, coroutineScope)
        }
    }

    /**
     * Configure whether a [DeviceAction] will succeed
     */
    var willActionSucceed: Boolean = initialWillActionSucceed

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [getCurrentState]
     */
    val getCurrentStateMock = ::getCurrentState.mock()

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [connect]
     */
    val connectMock = ::connect.mock()

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [discoverServices]
     */
    val discoverServicesMock = ::discoverServices.mock()

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [disconnect]
     */
    val disconnectMock = ::disconnect.mock()

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [readRssi]
     */
    val readRssiMock = ::readRssi.mock()

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [requestMtu]
     */
    val requestMtuMock = ::requestMtu.mock()

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [pair]
     */
    val pairMock = ::pair.mock()

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [unpair]
     */
    val unpairMock = ::unpair.mock()

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [performAction]
     */
    val performActionMock = ::performAction.mock()

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [handleCurrentActionCompleted]
     */
    val handleCurrentActionCompletedMock = ::handleCurrentActionCompletedWithAction.mock()

    init {
        if (setupMocks) {
            getCurrentStateMock.on().doReturn(DeviceConnectionManager.State.DISCONNECTED)
            requestMtuMock.on().doExecuteSuspended { (mtu) ->
                handleNewMtu(mtu)
                true
            }

            performActionMock.on().doExecuteSuspended { (action) ->
                currentAction = action
            }
        }
    }

    override fun getCurrentState(): DeviceConnectionManager.State = getCurrentStateMock.call()

    public override fun handleNewRssi(rssi: RSSI) {
        super.handleNewRssi(rssi)
    }

    override fun connect(): Unit = connectMock.call()

    override suspend fun discoverServices(): Unit = discoverServicesMock.call()

    override fun disconnect(): Unit = disconnectMock.call()

    override suspend fun readRssi(): Unit = readRssiMock.call()

    override suspend fun requestMtu(mtu: MTU): Boolean = requestMtuMock.call(mtu)

    override suspend fun requestStartPairing(): Unit = pairMock.call()

    override suspend fun requestStartUnpairing(): Unit = unpairMock.call()

    override suspend fun didStartPerformingAction(action: DeviceAction): Unit = performActionMock.call(action)

    suspend fun handleCurrentAction() {
        currentAction?.let { action ->
            when (action) {
                is DeviceAction.Read.Characteristic -> handleUpdatedCharacteristic(
                    action.characteristic.uuid,
                    willActionSucceed,
                ) {
                    debug("Mock Read: ${action.characteristic.uuid} value ${action.characteristic.wrapper.value?.asBytes?.toHexString()}")
                }
                is DeviceAction.Read.Descriptor -> handleUpdatedDescriptor(
                    action.descriptor.uuid,
                    willActionSucceed,
                )
                is DeviceAction.Write.Characteristic -> {
                    (action.characteristic.wrapper as MockCharacteristicWrapper).updateMockValue(
                        action.newValue,
                    )
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
    }

    public override fun handleDiscoverCompleted(services: List<Service>) {
        super.handleDiscoverCompleted(services)
    }

    public override fun createService(wrapper: ServiceWrapper): Service {
        return super.createService(wrapper)
    }

    override fun handleCurrentActionCompleted(succeeded: Boolean) {
        handleCurrentActionCompletedWithAction(succeeded, currentAction)
        return super.handleCurrentActionCompleted(succeeded)
    }

    public override fun handleUpdatedCharacteristic(uuid: UUID, succeeded: Boolean, onUpdate: ((Characteristic) -> Unit)?) {
        super.handleUpdatedCharacteristic(uuid, succeeded, onUpdate)
    }

    public override fun handleUpdatedDescriptor(uuid: UUID, succeeded: Boolean, onUpdate: ((Descriptor) -> Unit)?) {
        super.handleUpdatedDescriptor(uuid, succeeded, onUpdate)
    }

    private fun handleCurrentActionCompletedWithAction(succeeded: Boolean, deviceAction: DeviceAction?): Unit = handleCurrentActionCompletedMock.call(succeeded, deviceAction)
}
