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
import com.splendo.kaluga.base.test.mock.on
import com.splendo.kaluga.base.test.mock.verify
import com.splendo.kaluga.base.test.testBlockingAndCancelScope
import com.splendo.kaluga.bluetooth.device.ConnectableDeviceImpl
import com.splendo.kaluga.bluetooth.device.ConnectableDeviceState
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.DeviceAction
import com.splendo.kaluga.bluetooth.device.DeviceInfoImpl
import com.splendo.kaluga.bluetooth.test.createDeviceWrapper
import com.splendo.kaluga.bluetooth.test.createServiceWrapper
import com.splendo.kaluga.bluetooth.test.device.MockAdvertisementData
import com.splendo.kaluga.bluetooth.test.device.MockDeviceConnectionManager
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

/**
 * What a disconnect does to the actions pending on a connection and to the objects discovered on it.
 */
class BluetoothDisconnectTest : BaseTest() {

    private class Connection(scope: CoroutineScope, reconnectionSettings: ConnectionSettings.ReconnectionSettings) {
        val serviceWrapper = createServiceWrapper(BluetoothFlowTest.defaultService())
        private val deviceWrapper = createDeviceWrapper()
        private val settings = ConnectionSettings(reconnectionSettings = reconnectionSettings)
        private val managerBuilder = MockDeviceConnectionManager.Builder()
        val device = ConnectableDeviceImpl(
            deviceWrapper.identifier,
            DeviceInfoImpl(deviceWrapper, rssi = -50, MockAdvertisementData(serviceUUIDs = listOf(serviceWrapper.uuid))),
            settings,
            { managerBuilder.create(deviceWrapper, settings, scope) },
            scope,
        )
        lateinit var manager: MockDeviceConnectionManager
            private set

        suspend fun connect(): RemoteService {
            while (managerBuilder.createdDeviceConnectionManager.isEmpty()) delay(10)
            manager = managerBuilder.createdDeviceConnectionManager.first()
            manager.connectMock.on().doExecute { manager.handleConnect() }
            manager.disconnectMock.on().doExecute { manager.handleDisconnect() }
            assertTrue(device.connect())
            return discover()
        }

        suspend fun discover(): RemoteService {
            val service = manager.create(serviceWrapper)
            device.state.filterIsInstance<ConnectableDeviceState.Connected.NoServices>().first().startDiscovering()
            device.state.filterIsInstance<ConnectableDeviceState.Connected.Discovering>().first()
            manager.discover(listOf(service))
            device.state.filterIsInstance<ConnectableDeviceState.Connected.Idle>().first()
            return service
        }

        suspend fun awaitDisconnected() = device.state.filterIsInstance<ConnectableDeviceState.Disconnected>().first()
    }

    private fun test(
        reconnectionSettings: ConnectionSettings.ReconnectionSettings = ConnectionSettings.ReconnectionSettings.Never,
        block: suspend CoroutineScope.(Connection) -> Unit,
    ) = testBlockingAndCancelScope {
        val connection = Connection(this, reconnectionSettings)
        withTimeout(10.seconds) { block(connection) }
    }

    @Test
    fun readPendingAtDisconnectAnswersDeviceUnavailable() = test { connection ->
        val characteristic = connection.connect().characteristics.first()
        val read = async { characteristic.read() }
        assertIs<DeviceAction.Read.Characteristic>(connection.manager.disconnectWhilePerformingAction())
        assertEquals(GattResponse.DeviceUnavailable, read.await())
        connection.awaitDisconnected()
    }

    @Test
    fun queuedActionsAnswerDeviceUnavailableAtDisconnect() = test { connection ->
        val characteristic = connection.connect().characteristics.first()
        val descriptor = characteristic.descriptors.first()
        val write = async { characteristic.write(byteArrayOf(1)) }
        assertIs<DeviceAction.Write.Characteristic>(connection.manager.awaitCurrentAction())
        val read = async { characteristic.read() }
        val descriptorWrite = async { descriptor.write(byteArrayOf(2)) }
        val mtu = async { connection.device.requestMtu(100).response.await() }
        connection.device.state.filterIsInstance<ConnectableDeviceState.Connected.HandlingAction>().first { it.nextActions.size == 3 }
        connection.manager.handleDisconnect()
        assertEquals(GattResponse.DeviceUnavailable, write.await())
        assertEquals(GattResponse.DeviceUnavailable, read.await())
        assertEquals(GattResponse.DeviceUnavailable, descriptorWrite.await())
        assertEquals(GattResponse.MTUDeviceUnavailable(100), mtu.await())
        connection.awaitDisconnected()
    }

    @Test
    fun queuedActionsAnswerDeviceUnavailableAtARequestedDisconnect() = test { connection ->
        val characteristic = connection.connect().characteristics.first()
        val write = async { characteristic.write(byteArrayOf(1)) }
        assertIs<DeviceAction.Write.Characteristic>(connection.manager.awaitCurrentAction())
        val read = async { characteristic.read() }
        val mtu = async { connection.device.requestMtu(100).response.await() }
        connection.device.state.filterIsInstance<ConnectableDeviceState.Connected.HandlingAction>().first { it.nextActions.size == 2 }
        connection.device.disconnect()
        assertEquals(GattResponse.DeviceUnavailable, write.await())
        assertEquals(GattResponse.DeviceUnavailable, read.await())
        assertEquals(GattResponse.MTUDeviceUnavailable(100), mtu.await())
        connection.manager.performActionMock.verify(times = 1)
    }

    @Test
    fun lateCompletionOfAnActionAnsweredAtDisconnectClearsTheCurrentAction() = test { connection ->
        val characteristic = connection.connect().characteristics.first()
        // Queued ahead of the Disconnected event, so the read starts after the manager ended the connection and only
        // the event answers it.
        val read = characteristic.startRead()
        connection.manager.handleDisconnect()
        assertEquals(GattResponse.DeviceUnavailable, read.response.await())
        connection.awaitDisconnected()
        assertIs<DeviceAction.Read.Characteristic>(connection.manager.awaitCurrentAction())
        connection.manager.handleCurrentAction()
        assertNull(withTimeoutOrNull(100.milliseconds) { connection.manager.awaitCurrentAction() })
    }

    @Test
    fun actionAddedOutsideADiscoveryAnswersDeviceUnavailable() = test { connection ->
        connection.connect()
        connection.device.disconnect()
        connection.awaitDisconnected()
        assertEquals(GattResponse.MTUDeviceUnavailable(100), connection.manager.requestMtu(100).response.await())

        assertTrue(connection.device.connect())
        val beforeDiscovery = connection.manager.create(connection.serviceWrapper)
        assertEquals(GattResponse.DeviceUnavailable, beforeDiscovery.characteristics.first().read())
        connection.manager.performActionMock.verify(times = 0)
    }

    @Test
    fun disconnectWhileDisconnectedRunsThePlatformCleanup() = test { connection ->
        connection.connect()
        connection.device.disconnect()
        connection.awaitDisconnected()
        val cleanedUp = CompletableDeferred<Unit>()
        connection.manager.handleDisconnect { cleanedUp.complete(Unit) }
        cleanedUp.await()
    }

    @Test
    fun notificationRequestsPendingAtDisconnectAnswerDeviceUnavailable() = test { connection ->
        val characteristic = connection.connect().characteristics.first()
        val subscribe = async { characteristic.subscribe {} }
        assertIs<DeviceAction.Notification.Enable>(connection.manager.awaitCurrentAction())
        val disable = assertNotNull(characteristic.startDisableNotification())
        connection.manager.handleDisconnect()
        assertEquals(RemoteCharacteristic.SubscriptionResult.FailedToSubscribe(GattResponse.DeviceUnavailable), subscribe.await())
        assertEquals(GattResponse.DeviceUnavailable, disable.response.await())
        assertFalse(characteristic.isNotifying.value)
    }

    @Test
    fun notificationRequestReplacedByItsOppositeAnswersSuperseded() = test { connection ->
        val characteristic = connection.connect().characteristics.first()
        val enable = async { characteristic.enableNotification() }
        assertIs<DeviceAction.Notification.Enable>(connection.manager.awaitCurrentAction())
        val disable = assertNotNull(characteristic.startDisableNotification())
        assertNotNull(characteristic.startEnableNotification())
        assertEquals(GattResponse.Superseded, disable.response.await())
        connection.manager.handleCurrentAction()
        assertEquals(GattResponse.WriteSuccess.Acknowledged, enable.await())
        assertTrue(characteristic.isNotifying.value)
        connection.manager.performActionMock.verify(times = 1)
    }

    @Test
    fun lateCompletionOfAnActionOfAnEndedConnectionIsIgnored() = test(ConnectionSettings.ReconnectionSettings.Always) { connection ->
        val old = connection.connect().characteristics.first()
        val oldSubscribe = async { old.subscribe {} }
        val oldEnable = assertIs<DeviceAction.Notification.Enable>(connection.manager.disconnectWhilePerformingAction())
        assertEquals(RemoteCharacteristic.SubscriptionResult.FailedToSubscribe(GattResponse.DeviceUnavailable), oldSubscribe.await())

        val new = connection.discover().characteristics.first()
        val received = mutableListOf<ByteArray>()
        val newSubscribe = async { new.subscribe { received += it } }
        assertIs<DeviceAction.Notification.Enable>(connection.manager.awaitCurrentAction())
        // The platform answers the old enable while the new one is pending: the new one must still complete.
        connection.manager.completeNotificationAction(oldEnable, GattResponse.WriteSuccess.Acknowledged)
        connection.manager.handleCurrentAction()
        assertIs<RemoteCharacteristic.SubscriptionResult.DidSubscribe>(newSubscribe.await())
        // And once more after it completed: the new characteristic keeps receiving.
        connection.manager.completeNotificationAction(oldEnable, GattResponse.WriteSuccess.Acknowledged)
        connection.manager.notify(new.uuid, byteArrayOf(1))
        assertEquals(1, received.size)
        assertFalse(old.isNotifying.value)
        assertTrue(new.isNotifying.value)
    }

    @Test
    fun resetEndsTheConnectionLikeADisconnect() = test { connection ->
        val characteristic = connection.connect().characteristics.first()
        val read = async { characteristic.read() }
        connection.manager.awaitCurrentAction()
        connection.manager.reset()
        assertEquals(GattResponse.DeviceUnavailable, read.await())
        connection.awaitDisconnected()
    }
}
