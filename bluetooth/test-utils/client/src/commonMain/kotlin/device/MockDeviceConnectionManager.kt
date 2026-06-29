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

package com.splendo.kaluga.bluetooth.test.device

import com.splendo.kaluga.base.collections.concurrentMutableListOf
import com.splendo.kaluga.base.test.mock.call
import com.splendo.kaluga.base.test.mock.on
import com.splendo.kaluga.base.test.mock.parameters.mock
import com.splendo.kaluga.base.test.mock.singleParametersMock
import com.splendo.kaluga.base.bytes.toHexString
import com.splendo.kaluga.bluetooth.GattResponse
import com.splendo.kaluga.bluetooth.RSSI
import com.splendo.kaluga.bluetooth.RemoteService
import com.splendo.kaluga.bluetooth.RemoteServiceWrapper
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.device.BaseDeviceConnectionManager
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.DeviceAction
import com.splendo.kaluga.bluetooth.device.DeviceConnectionManager
import com.splendo.kaluga.bluetooth.device.DeviceWrapper
import com.splendo.kaluga.bluetooth.device.PairingResult
import com.splendo.kaluga.bluetooth.test.MockCharacteristicWrapper
import com.splendo.kaluga.bluetooth.test.MockDescriptorWrapper
import com.splendo.kaluga.logging.debug
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay

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
         * [com.splendo.kaluga.base.test.mock.BaseMethodMock] for [create]
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

        override fun create(deviceWrapper: DeviceWrapper, settings: ConnectionSettings, coroutineScope: CoroutineScope): BaseDeviceConnectionManager =
            createMock.call(deviceWrapper, settings, coroutineScope)
    }

    data class ActionCompleted<R : GattResponse>(val action: DeviceAction<R>, val response: R)

    /**
     * Configure whether a [DeviceAction] will succeed
     */
    var willActionSucceed: Boolean = initialWillActionSucceed

    /**
     * [com.splendo.kaluga.base.test.mock.BaseMethodMock] for [getCurrentState]
     */
    val getCurrentStateMock = ::getCurrentState.mock()

    /**
     * [com.splendo.kaluga.base.test.mock.BaseMethodMock] for [connect]
     */
    val connectMock = ::connect.mock()

    /**
     * [com.splendo.kaluga.base.test.mock.BaseMethodMock] for [discoverServices]
     */
    val discoverServicesMock = ::discoverServices.mock()

    /**
     * [com.splendo.kaluga.base.test.mock.BaseMethodMock] for [disconnect]
     */
    val disconnectMock = ::disconnect.mock()

    /**
     * [com.splendo.kaluga.base.test.mock.BaseMethodMock] for [readRssi]
     */
    val readRssiMock = ::readRssi.mock()

    /**
     * [com.splendo.kaluga.base.test.mock.BaseMethodMock] for [pair]
     */
    val pairMock = ::pair.mock()

    /**
     * [com.splendo.kaluga.base.test.mock.BaseMethodMock] for [unpair]
     */
    val unpairMock = ::unpair.mock()

    /**
     * [com.splendo.kaluga.base.test.mock.BaseMethodMock] for [performAction]
     */
    val performActionMock = ::performAction.mock()

    /**
     * [com.splendo.kaluga.base.test.mock.BaseMethodMock] for [handleActionCompleted]
     */
    val handleCurrentActionCompletedMock = singleParametersMock<ActionCompleted<*>, Unit>().apply {
        on().doReturn(Unit)
    }

    init {
        if (setupMocks) {
            getCurrentStateMock.on().doReturn(DeviceConnectionManager.State.DISCONNECTED)
            pairMock.on().doReturn(PairingResult.SUCCESS)
            unpairMock.on().doReturn(PairingResult.SUCCESS)

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

    override suspend fun requestReadRssi(): Unit = readRssiMock.call()

    override suspend fun requestStartPairing(): PairingResult = pairMock.call()

    override suspend fun requestStartUnpairing(): PairingResult = unpairMock.call()

    override suspend fun didStartPerformingAction(action: DeviceAction<*>): Unit = performActionMock.call(action)

    suspend fun handleCurrentAction() {
        lateinit var action: DeviceAction<*>
        do {
            val success = currentAction?.let {
                action = it
                true
            } ?: run {
                delay(100)
                false
            }
        } while (!success)
        debug("Handle $action")
        when (action) {
            is DeviceAction.Read.Characteristic -> {
                val value = (action.characteristic.wrapper as MockCharacteristicWrapper).value
                debug("Mock Read: ${action.characteristic.uuid} value ${value?.toHexString()}")
                handleCharacteristicReadOrNotified(
                    action.characteristic.uuid,
                    if (willActionSucceed && value != null) GattResponse.ReadSuccess(value) else GattResponse.ReadNotPermitted,
                )
            }

            is DeviceAction.Read.Descriptor -> {
                val value = (action.descriptor.wrapper as MockDescriptorWrapper).value
                debug("Mock Read: ${action.descriptor.uuid} value ${value?.toHexString()}")
                handleDescriptorRead(
                    action.descriptor.uuid,
                    if (willActionSucceed && value != null) GattResponse.ReadSuccess(value) else GattResponse.ReadNotPermitted,
                )
            }

            is DeviceAction.Write.Characteristic -> {
                (action.characteristic.wrapper as MockCharacteristicWrapper).updateValue(
                    action.newValue,
                )
                handleCharacteristicWritten(action.characteristic.uuid, if (willActionSucceed) GattResponse.WriteSuccess.Acknowledged else GattResponse.WriteNotPermitted)
                debug("Mock Write: ${action.characteristic.uuid} value ${action.newValue.toHexString()}")
            }

            is DeviceAction.Write.Descriptor -> {
                (action.descriptor.wrapper as MockDescriptorWrapper).updateValue(
                    action.newValue,
                )
                handleDescriptorWritten(action.descriptor.uuid, if (willActionSucceed) GattResponse.WriteSuccess.Acknowledged else GattResponse.WriteNotPermitted)
                debug("Mock Write: ${action.descriptor.uuid} value ${action.newValue.toHexString()}")
            }

            is DeviceAction.Notification -> action.handleNotificationStateChanged(if (willActionSucceed) GattResponse.WriteSuccess.Acknowledged else GattResponse.WriteNotPermitted)

            is DeviceAction.RequestMtu -> handleNewMtu(if (willActionSucceed) GattResponse.MTUSuccess(action.mtu) else GattResponse.MTUNotPermitted(action.mtu))
        }
    }

    fun create(wrapper: RemoteServiceWrapper): RemoteService = createService(wrapper)

    fun discover(services: List<RemoteService>) = handleDiscoverCompleted(services)

    fun notify(characteristic: UUID, value: ByteArray) = handleCharacteristicReadOrNotified(characteristic, GattResponse.ReadSuccess(value))

    override fun <R : GattResponse> handleActionCompleted(response: R, deviceAction: DeviceAction<R>): Unit =
        handleCurrentActionCompletedMock.call(ActionCompleted(deviceAction, response))
}
