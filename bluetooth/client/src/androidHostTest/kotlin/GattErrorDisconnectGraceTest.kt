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

import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothProfile
import android.content.ContextWrapper
import com.splendo.kaluga.base.test.mock.on
import com.splendo.kaluga.base.test.mock.verify
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.DefaultDeviceConnectionManager
import com.splendo.kaluga.bluetooth.device.DeviceAction
import com.splendo.kaluga.bluetooth.device.DeviceConnectionManager
import com.splendo.kaluga.bluetooth.device.DeviceWrapper
import com.splendo.kaluga.bluetooth.test.AndroidMockDeviceWrapper
import com.splendo.kaluga.bluetooth.test.MockBluetoothGattWrapper
import com.splendo.kaluga.bluetooth.test.MockCharacteristic
import com.splendo.kaluga.bluetooth.test.MockCharacteristicWrapperImpl
import com.splendo.kaluga.bluetooth.test.MockServiceWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource

/**
 * Drives [DefaultDeviceConnectionManager] through the callbacks Android calls, in the order the stack was seen to use
 * when a link fails: the request in flight fails with `GATT_ERROR` (0x85) while the device still reads as connected, and
 * the disconnect follows a few milliseconds later.
 */
class GattErrorDisconnectGraceTest {

    private companion object {
        const val GATT_ERROR = 0x85
        const val CONNECTION_TERMINATED_BY_LOCAL_HOST = 0x16
    }

    private class AndroidService(private val uuid: UUID) : BluetoothGattService(uuid, SERVICE_TYPE_PRIMARY) {
        override fun getUuid(): UUID = uuid
    }

    private class AndroidCharacteristic(private val uuid: UUID, private val service: BluetoothGattService) : BluetoothGattCharacteristic(uuid, 0, 0) {
        override fun getUuid(): UUID = uuid
        override fun getService(): BluetoothGattService = service

        @Deprecated("Deprecated in Java")
        override fun getValue(): ByteArray = byteArrayOf()
    }

    private class AndroidDescriptor(private val uuid: UUID, private val characteristic: BluetoothGattCharacteristic) : BluetoothGattDescriptor(uuid, 0) {
        override fun getUuid(): UUID = uuid
        override fun getCharacteristic(): BluetoothGattCharacteristic = characteristic
    }

    private class Connection(val manager: DefaultDeviceConnectionManager, val gatt: MockBluetoothGattWrapper, val callback: BluetoothGattCallback, val scope: CoroutineScope) {
        val events = Channel<DeviceConnectionManager.Event>(Channel.UNLIMITED)
        val characteristic = MockCharacteristic(
            MockCharacteristicWrapperImpl(
                properties = setOf(CharacteristicProperty.Read, CharacteristicProperty.Notify),
                service = MockServiceWrapper(),
                descriptorUUIDs = listOf(Descriptor.CLIENT_CHARACTERISTIC_CONFIGURATION_DESCRIPTOR),
            ),
        ) {}
        private val androidCharacteristic = AndroidCharacteristic(characteristic.uuid, AndroidService(characteristic.wrapper.service.uuid))

        suspend fun nextEvent(): DeviceConnectionManager.Event = withTimeout(5.seconds) { events.receive() }

        suspend fun <R : GattResponse> answer(action: DeviceAction<R>): R = withTimeout(1.seconds) { action.response.await() }

        @Suppress("DEPRECATION")
        fun failRead(status: Int) = callback.onCharacteristicRead(null, androidCharacteristic, status)

        fun failCccdWrite(status: Int) =
            callback.onDescriptorWrite(null, AndroidDescriptor(Descriptor.CLIENT_CHARACTERISTIC_CONFIGURATION_DESCRIPTOR, androidCharacteristic), status)

        fun failMtu(mtu: Int, status: Int) = callback.onMtuChanged(null, mtu, status)

        fun dropLink(status: Int = CONNECTION_TERMINATED_BY_LOCAL_HOST) = callback.onConnectionStateChange(null, status, BluetoothProfile.STATE_DISCONNECTED)
    }

    private fun connection(grace: Duration, connectionState: () -> Int = { BluetoothProfile.STATE_CONNECTED }, block: suspend Connection.() -> Unit) = runBlocking {
        val deviceWrapper = AndroidMockDeviceWrapper("device", "device", DeviceWrapper.BondState.NONE, setupMocks = false)
        val gatt = MockBluetoothGattWrapper()
        lateinit var callback: BluetoothGattCallback
        deviceWrapper.connectGattMock.on().doExecute { (_, _, gattCallback) ->
            callback = gattCallback
            gatt
        }
        val scope = CoroutineScope(Dispatchers.Default + Job())
        val manager = DefaultDeviceConnectionManager(ContextWrapper(null), deviceWrapper, ConnectionSettings(), scope, grace) { connectionState() }
        manager.connect()
        val connection = Connection(manager, gatt, callback, scope)
        scope.launch { manager.events.collect { connection.events.send(it) } }
        callback.onConnectionStateChange(null, 0, BluetoothProfile.STATE_CONNECTED)
        assertIs<DeviceConnectionManager.Event.Connected>(connection.nextEvent())
        try {
            connection.block()
        } finally {
            scope.cancel()
        }
    }

    @Test
    fun readAnswersDeviceUnavailableWhenTheLinkDropsWithinTheGrace() = connection(5.seconds) {
        val read = DeviceAction.Read.Characteristic(characteristic)
        manager.performAction(read)
        failRead(GATT_ERROR)
        dropLink()

        // Answered without a completion event, so the disconnect answers the actions queued behind it.
        assertIs<DeviceConnectionManager.Event.Disconnected>(nextEvent())
        assertEquals(GattResponse.DeviceUnavailable(GattResponse.ApplicationError(GATT_ERROR)), answer(read))
    }

    @Test
    fun notificationAnswersDeviceUnavailableWhenTheLinkDropsWithinTheGrace() = connection(5.seconds) {
        val enable = DeviceAction.Notification.Enable(characteristic)
        manager.performAction(enable)
        gatt.writeDescriptorMock.verify()
        failCccdWrite(GATT_ERROR)
        dropLink()

        assertIs<DeviceConnectionManager.Event.Disconnected>(nextEvent())
        assertEquals(GattResponse.DeviceUnavailable(GattResponse.ApplicationError(GATT_ERROR)), answer(enable))
    }

    @Test
    fun mtuRequestAnswersMtuDeviceUnavailableWhenTheLinkDropsWithinTheGrace() = connection(5.seconds) {
        val request = DeviceAction.RequestMtu(185)
        manager.performAction(request)
        failMtu(23, GATT_ERROR)
        dropLink()

        assertIs<DeviceConnectionManager.Event.Disconnected>(nextEvent())
        assertEquals(GattResponse.MTUDeviceUnavailable(23, GattResponse.ApplicationError(GATT_ERROR)), answer(request))
    }

    @Test
    fun readAnswersThePlainErrorAfterTheGraceWithoutDisconnect() = connection(300.milliseconds) {
        val read = DeviceAction.Read.Characteristic(characteristic)
        manager.performAction(read)
        val failed = TimeSource.Monotonic.markNow()
        failRead(GATT_ERROR)

        val completed = assertIs<DeviceConnectionManager.Event.CompletedAction<*>>(nextEvent())
        val elapsed = failed.elapsedNow()
        assertTrue(elapsed >= 300.milliseconds, "answered after $elapsed")
        assertEquals(read, completed.action)
        assertEquals(GattResponse.ApplicationError(GATT_ERROR), completed.response)

        // A disconnect after the grace finds nothing held.
        dropLink()
        assertIs<DeviceConnectionManager.Event.Disconnected>(nextEvent())
    }

    @Test
    fun readAnswersThePlainErrorAtOnceWithoutGrace() = connection(Duration.ZERO) {
        val read = DeviceAction.Read.Characteristic(characteristic)
        manager.performAction(read)
        failRead(GATT_ERROR)
        dropLink()

        val completed = assertIs<DeviceConnectionManager.Event.CompletedAction<*>>(nextEvent())
        assertEquals(read, completed.action)
        assertEquals(GattResponse.ApplicationError(GATT_ERROR), completed.response)
        assertIs<DeviceConnectionManager.Event.Disconnected>(nextEvent())
    }

    @Test
    fun readAnswersDeviceUnavailableAtOnceWhenTheLinkIsAlreadyDown() = connection(5.seconds, { BluetoothProfile.STATE_DISCONNECTED }) {
        val read = DeviceAction.Read.Characteristic(characteristic)
        manager.performAction(read)
        failRead(GATT_ERROR)

        val completed = assertIs<DeviceConnectionManager.Event.CompletedAction<*>>(withTimeout(1.seconds) { events.receive() })
        assertEquals(read, completed.action)
        assertEquals(GattResponse.DeviceUnavailable(GattResponse.ApplicationError(GATT_ERROR)), completed.response)
    }

    @Test
    fun otherStatusAnswersAtOnce() = connection(5.seconds) {
        val read = DeviceAction.Read.Characteristic(characteristic)
        manager.performAction(read)
        failRead(GattResponse.ReadNotPermitted.statusCode)

        val completed = assertIs<DeviceConnectionManager.Event.CompletedAction<*>>(withTimeout(1.seconds) { events.receive() })
        assertEquals(read, completed.action)
        assertEquals(GattResponse.ReadNotPermitted, completed.response)
    }

    @Test
    fun requestedDisconnectWithinTheGraceAnswersThePlainError() = connection(5.seconds) {
        val read = DeviceAction.Read.Characteristic(characteristic)
        manager.performAction(read)
        failRead(GATT_ERROR)
        manager.disconnect()

        assertEquals(GattResponse.ApplicationError(GATT_ERROR), answer(read))
        gatt.disconnectMock.verify()
        dropLink(status = 0)
        assertIs<DeviceConnectionManager.Event.Disconnected>(nextEvent())
        assertNull(withTimeoutOrNull(200.milliseconds) { events.receive() }, "the held read was answered twice")
    }
}
