/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

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

import com.splendo.kaluga.base.test.BaseTest
import com.splendo.kaluga.base.test.testBlockingAndCancelScope
import com.splendo.kaluga.bluetooth.device.BaseDeviceConnectionManager
import com.splendo.kaluga.bluetooth.device.PortableDeviceWrapper
import com.splendo.kaluga.bluetooth.device.ConnectableDeviceImpl
import com.splendo.kaluga.bluetooth.device.ConnectableDeviceState
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.DeviceAction
import com.splendo.kaluga.bluetooth.device.DeviceConnectionManager
import com.splendo.kaluga.bluetooth.device.DeviceInfoImpl
import com.splendo.kaluga.bluetooth.device.DeviceWrapper
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.bluetooth.device.PairingResult
import com.splendo.kaluga.bluetooth.extensions.characteristic
import com.splendo.kaluga.bluetooth.extensions.descriptor
import com.splendo.kaluga.bluetooth.test.device.MockAdvertisementData
import com.splendo.kaluga.bluetooth.test.randomIdentifier
import com.splendo.kaluga.bluetooth.test.randomUUID
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * Drives the client over wrappers written entirely in common code, the way a transport that is not the platform
 * Bluetooth stack (an emulator, an in-process fake) would.
 */
class PortableWrapperTransportTest : BaseTest() {

    private class ServiceWrapper(override val uuid: UUID, characteristics: List<(RemoteServiceWrapper) -> RemoteCharacteristicWrapper>) : PortableRemoteServiceWrapper() {
        override val type = Service.Type.PRIMARY
        override val includedServices: List<RemoteServiceWrapper> = emptyList()
        override val characteristics: List<RemoteCharacteristicWrapper> = characteristics.map { it(this) }
    }

    private class CharacteristicWrapper(
        override val uuid: UUID,
        override val service: RemoteServiceWrapper,
        override val properties: Set<CharacteristicProperty>,
        descriptorUuids: List<UUID>,
    ) : PortableRemoteCharacteristicWrapper() {
        override val descriptors: List<RemoteDescriptorWrapper> = descriptorUuids.map { DescriptorWrapper(it, this) }
    }

    private class DescriptorWrapper(override val uuid: UUID, override val characteristic: RemoteCharacteristicWrapper) : PortableRemoteDescriptorWrapper()

    private class InProcessDeviceWrapper(override val name: String?, override val identifier: Identifier) : PortableDeviceWrapper()

    /**
     * An in-process transport: actions complete against a value store, the test pushes notifications.
     */
    private class InProcessConnectionManager(
        deviceWrapper: DeviceWrapper,
        settings: ConnectionSettings,
        coroutineScope: CoroutineScope,
        private val serviceWrapper: RemoteServiceWrapper,
    ) : BaseDeviceConnectionManager(deviceWrapper, settings, coroutineScope) {

        val values = mutableMapOf<String, ByteArray>()
        private var state = DeviceConnectionManager.State.DISCONNECTED

        override fun getCurrentState() = state

        override fun connect() {
            state = DeviceConnectionManager.State.CONNECTED
            handleConnect()
        }

        override fun intentionalDisconnect() {
            state = DeviceConnectionManager.State.DISCONNECTED
            handleDisconnect()
        }

        override suspend fun discoverServices() = handleDiscoverCompleted(listOf(createService(serviceWrapper)))

        override suspend fun requestReadRssi() = handleNewRssi(-50)

        override suspend fun requestStartPairing() = PairingResult.SUCCESS

        override suspend fun requestStartUnpairing() = PairingResult.SUCCESS

        override suspend fun didStartPerformingAction(action: DeviceAction<*>) {
            currentAction = action
            when (action) {
                is DeviceAction.Read.Characteristic -> handleCharacteristicReadOrNotified(action.characteristic.uuid, read(action.characteristic.uuid))

                is DeviceAction.Read.Descriptor -> handleDescriptorRead(action.descriptor.uuid, read(action.descriptor.uuid))

                is DeviceAction.Write.Characteristic -> {
                    values[action.characteristic.uuid.uuidString] = action.newValue
                    handleCharacteristicWritten(action.characteristic.uuid, GattResponse.WriteSuccess.Acknowledged)
                }

                is DeviceAction.Write.Descriptor -> {
                    values[action.descriptor.uuid.uuidString] = action.newValue
                    handleDescriptorWritten(action.descriptor.uuid, GattResponse.WriteSuccess.Acknowledged)
                }

                is DeviceAction.Notification -> action.handleNotificationStateChanged(GattResponse.WriteSuccess.Acknowledged)

                is DeviceAction.RequestMtu -> handleNewMtu(GattResponse.MTUSuccess(action.mtu))
            }
        }

        fun notify(uuid: UUID, value: ByteArray) = handleCharacteristicNotified(uuid, GattResponse.ReadSuccess(value))

        private fun read(uuid: UUID): GattResponse.ReadResponse = values[uuid.uuidString]?.let { GattResponse.ReadSuccess(it) } ?: GattResponse.ReadNotPermitted
    }

    @Test
    fun readWriteNotifyThroughCommonWrappers() = testBlockingAndCancelScope {
        val serviceUuid = randomUUID()
        val characteristicUuid = randomUUID()
        val descriptorUuid = randomUUID()
        val serviceWrapper = ServiceWrapper(
            serviceUuid,
            listOf { service ->
                CharacteristicWrapper(
                    characteristicUuid,
                    service,
                    setOf(CharacteristicProperty.Read, CharacteristicProperty.Write, CharacteristicProperty.Notify),
                    listOf(descriptorUuid),
                )
            },
        )
        val deviceWrapper = InProcessDeviceWrapper("In-process device", randomIdentifier())
        val settings = ConnectionSettings(reconnectionSettings = ConnectionSettings.ReconnectionSettings.Never)
        val createdManager = CompletableDeferred<InProcessConnectionManager>()
        val device = ConnectableDeviceImpl(
            deviceWrapper.identifier,
            DeviceInfoImpl(deviceWrapper, rssi = -50, MockAdvertisementData(serviceUUIDs = listOf(serviceUuid))),
            settings,
            { InProcessConnectionManager(deviceWrapper, it, this, serviceWrapper).also(createdManager::complete) },
            this,
        )

        assertTrue(device.connect())
        val manager = createdManager.await()

        val characteristic = device.characteristic(serviceUuid.uuidString, characteristicUuid.uuidString)
        assertEquals(serviceUuid, characteristic.service.uuid)
        assertEquals(descriptorUuid, characteristic.descriptors.single().uuid)

        manager.values[characteristicUuid.uuidString] = byteArrayOf(1, 2)
        assertContentEquals(byteArrayOf(1, 2), assertIs<GattResponse.ReadSuccess>(characteristic.read()).value)

        assertIs<GattResponse.WriteSuccess>(characteristic.write(byteArrayOf(3)))
        assertContentEquals(byteArrayOf(3), manager.values[characteristicUuid.uuidString])

        val notified = CompletableDeferred<ByteArray>()
        val subscription = assertIs<RemoteCharacteristic.SubscriptionResult.DidSubscribe>(characteristic.subscribe { notified.complete(it) })
        assertTrue(characteristic.isNotifying.value)
        manager.notify(characteristicUuid, byteArrayOf(9))
        assertContentEquals(byteArrayOf(9), notified.await())
        assertIs<GattResponse.WriteSuccess>(subscription.subscription.unsubscribe())
        assertFalse(characteristic.isNotifying.value)

        val descriptor = device.descriptor(serviceUuid.uuidString, characteristicUuid.uuidString, descriptorUuid.uuidString)
        assertIs<GattResponse.WriteSuccess>(descriptor.write(byteArrayOf(4)))
        assertContentEquals(byteArrayOf(4), assertIs<GattResponse.ReadSuccess>(descriptor.read()).value)

        device.disconnect()
        device.state.filterIsInstance<ConnectableDeviceState.Disconnected>().first()
    }
}
