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
import com.splendo.kaluga.bluetooth.device.DeviceAction
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
 * @param bufferCapacity The capacity of the buffer for state transitions
 * @param coroutineScope The [CoroutineScope] of the [BaseDeviceConnectionManager]
 * @param setupMocks If `true` this will automatically configure the mocks to handle connecting
 */
class MockDeviceConnectionManager(
    initialWillActionSucceed: Boolean = true,
    deviceWrapper: DeviceWrapper,
    bufferCapacity: Int,
    coroutineScope: CoroutineScope,
    setupMocks: Boolean = true
) : BaseDeviceConnectionManager(deviceWrapper, bufferCapacity, coroutineScope) {

    /**
     * Mock implementation of [BaseDeviceConnectionManager.Builder]
     * @param initialWillActionSucceed Sets the initial status of whether actions will succeed for each created [MockDeviceConnectionManager]
     * @param setupMocks If `true` this will automatically configure the [createMock] to create a [MockDeviceConnectionManager]
     */
    class Builder(initialWillActionSucceed: Boolean = true, setupMocks: Boolean = true) : BaseDeviceConnectionManager.Builder {

        /**
         * List of created [MockDeviceConnectionManager]
         */
        val createdDeviceConnectionManager = sharedMutableListOf<MockDeviceConnectionManager>()

        /**
         * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [create]
         */
        val createMock = ::create.mock()

        init {
            if (setupMocks) {
                createMock.on().doExecute { (deviceWrapper, bufferCapacity, coroutineScope) ->
                    MockDeviceConnectionManager(initialWillActionSucceed, deviceWrapper, bufferCapacity, coroutineScope, setupMocks).also {
                        createdDeviceConnectionManager.add(it)
                    }
                }
            }
        }

        override fun create(
            deviceWrapper: DeviceWrapper,
            bufferCapacity: Int,
            coroutineScope: CoroutineScope
        ): BaseDeviceConnectionManager {
            return createMock.call(deviceWrapper, bufferCapacity, coroutineScope)
        }
    }

    private val _willActionSucceed = AtomicBoolean(initialWillActionSucceed)

    /**
     * Configure whether a [DeviceAction] will succeed
     */
    var willActionSucceed: Boolean
        get() = _willActionSucceed.value
        set(value) { _willActionSucceed.value = value }

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
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [performAction]
     */
    val performActionMock = ::performAction.mock()

    /**
     * [com.splendo.kaluga.test.base.mock.BaseMethodMock] for [handleCurrentActionCompleted]
     */
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

    override suspend fun handleCurrentActionCompleted(succeeded: Boolean) {
        handleCurrentActionCompletedWithAction(succeeded, currentAction)
        return super.handleCurrentActionCompleted(succeeded)
    }

    private fun handleCurrentActionCompletedWithAction(succeeded: Boolean, deviceAction: DeviceAction?): Unit = handleCurrentActionCompletedMock.call(succeeded, deviceAction)
}