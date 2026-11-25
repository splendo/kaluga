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
import com.splendo.kaluga.bluetooth.test.MockCharacteristicWrapper
import com.splendo.kaluga.bluetooth.test.createDeviceWrapper
import com.splendo.kaluga.bluetooth.test.createServiceWrapper
import com.splendo.kaluga.bluetooth.test.device.MockAdvertisementData
import com.splendo.kaluga.bluetooth.test.device.MockDeviceConnectionManager
import com.splendo.kaluga.logging.ContextualLogger
import com.splendo.kaluga.logging.RestrictedLogLevel
import com.splendo.kaluga.logging.RestrictedLogger
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.coroutines.yield
import kotlin.test.Test
import kotlin.test.assertContentEquals
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
        val service = connection.connect()
        val characteristic = service.characteristics.first()
        val read = async { characteristic.read() }
        assertIs<DeviceAction.Read.Characteristic>(connection.manager.disconnectWhilePerformingAction())
        assertEquals(GattResponse.DeviceUnavailable, read.await())
        connection.awaitDisconnected()
        service.invalidated.await()
        assertTrue(characteristic.invalidated.isCompleted)
        assertTrue(characteristic.descriptors.first().invalidated.isCompleted)
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
    fun actionCompletedWhileDisconnectingAnswersItsResponse() = test { connection ->
        val characteristic = connection.connect().characteristics.first()
        connection.manager.disconnectMock.on().doExecute { }
        val write = async { characteristic.write(byteArrayOf(1)) }
        connection.manager.awaitCurrentAction()
        val disconnect = async { connection.device.disconnect() }
        connection.device.state.filterIsInstance<ConnectableDeviceState.Disconnecting>().first()
        connection.manager.handleCurrentAction()
        assertEquals(GattResponse.WriteSuccess.Acknowledged, write.await())
        connection.manager.handleDisconnect()
        disconnect.await()
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
    fun enableSucceedingAsTheConnectionEndsLeavesTheCharacteristicNotNotifying() = testBlockingAndCancelScope {
        val quiet = ContextualLogger(RestrictedLogger(RestrictedLogLevel.None), "Race")
        repeat(RACE_ITERATIONS) {
            val invalidated = CompletableDeferred<Unit>()
            val service = RemoteService(createServiceWrapper(BluetoothFlowTest.defaultService()), emptyList(), {}, quiet, invalidated)
            val characteristic = service.characteristics.first()
            val enable = assertNotNull(characteristic.startEnableNotification())
            val start = StartTogether(parties = 2)
            withContext(Dispatchers.Default) {
                launch {
                    start.await()
                    enable.complete(GattResponse.WriteSuccess.Acknowledged)
                }
                launch {
                    start.await()
                    invalidated.complete(Unit)
                }
            }
            assertFalse(characteristic.isNotifying.value, "isNotifying after iteration $it")
        }
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
    fun subscriptionEndsAtDisconnect() = test { connection ->
        val characteristic = connection.connect().characteristics.first()
        val subscribe = async { characteristic.subscribe {} }
        connection.manager.handleCurrentAction()
        val subscription = assertIs<RemoteCharacteristic.SubscriptionResult.DidSubscribe>(subscribe.await()).subscription
        // Already notifying, so the flow subscribes without suspending and is registered before the value is pushed.
        val values = async(start = CoroutineStart.UNDISPATCHED) { characteristic.value().toList() }
        connection.manager.notify(characteristic.uuid, byteArrayOf(1))

        connection.manager.handleDisconnect()
        assertContentEquals(byteArrayOf(1), values.await().single())
        assertFalse(characteristic.isNotifying.value)
        assertEquals(GattResponse.DeviceUnavailable, subscription.unsubscribe())
        assertEquals(GattResponse.DeviceUnavailable, characteristic.enableNotification())
        assertEquals(GattResponse.DeviceUnavailable, characteristic.disableNotification())
        assertEquals(RemoteCharacteristic.SubscriptionResult.FailedToSubscribe(GattResponse.DeviceUnavailable), characteristic.subscribe {})
        assertTrue(characteristic.value().toList().isEmpty())
        // Only the enable reached the device: nothing is disabled on a connection that is gone.
        connection.manager.performActionMock.verify(times = 1)
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
    fun objectsOfAnEndedConnectionStayUnavailableAfterReconnecting() = test(ConnectionSettings.ReconnectionSettings.Always) { connection ->
        val oldService = connection.connect()
        val old = oldService.characteristics.first()
        val oldDescriptor = old.descriptors.first()
        val received = mutableListOf<ByteArray>()
        val subscribe = async { old.subscribe { received += it } }
        connection.manager.handleCurrentAction()
        val oldSubscription = assertIs<RemoteCharacteristic.SubscriptionResult.DidSubscribe>(subscribe.await()).subscription

        connection.manager.handleDisconnect()
        val newService = connection.discover()
        connection.manager.connectMock.verify(times = 2)
        assertTrue(oldService.invalidated.isCompleted)
        assertTrue(old.invalidated.isCompleted)
        assertTrue(oldDescriptor.invalidated.isCompleted)
        assertFalse(newService.invalidated.isCompleted)

        assertEquals(GattResponse.DeviceUnavailable, old.read())
        assertEquals(GattResponse.DeviceUnavailable, old.write(byteArrayOf(1)))
        assertEquals(GattResponse.DeviceUnavailable, old.write(byteArrayOf(1), WriteType.WithResponse))
        assertEquals(GattResponse.DeviceUnavailable, oldDescriptor.read())
        assertEquals(GattResponse.DeviceUnavailable, oldDescriptor.write(byteArrayOf(1)))
        assertEquals(RemoteCharacteristic.SubscriptionResult.FailedToSubscribe(GattResponse.DeviceUnavailable), old.subscribe {})
        assertEquals(GattResponse.DeviceUnavailable, oldSubscription.unsubscribe())
        assertFalse(old.isNotifying.value)
        // The old subscription did not survive into the new connection.
        connection.manager.notify(old.uuid, byteArrayOf(9))
        assertTrue(received.isEmpty())

        val new = newService.characteristics.first()
        (new.wrapper as MockCharacteristicWrapper).updateValue(byteArrayOf(2))
        val read = async { new.read() }
        connection.manager.handleCurrentAction()
        assertContentEquals(byteArrayOf(2), assertIs<GattResponse.ReadSuccess>(read.await()).value)
        // The enable of the old connection and the read on the new one; nothing on the stale objects reached the device.
        connection.manager.performActionMock.verify(times = 2)
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
        val service = connection.connect()
        val characteristic = service.characteristics.first()
        val read = async { characteristic.read() }
        connection.manager.awaitCurrentAction()
        connection.manager.reset()
        assertEquals(GattResponse.DeviceUnavailable, read.await())
        assertTrue(service.invalidated.isCompleted)
        connection.awaitDisconnected()
        assertEquals(GattResponse.DeviceUnavailable, characteristic.read())
    }

    private class StartTogether(private val parties: Int) {
        private val arrived = atomic(0)

        suspend fun await() {
            arrived.incrementAndGet()
            while (arrived.value < parties) yield()
        }
    }

    private companion object {
        const val RACE_ITERATIONS = 20_000
    }
}
