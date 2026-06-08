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

package com.splendo.kaluga.bluetooth.server

import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.bluetooth.CharacteristicProperty
import com.splendo.kaluga.bluetooth.GattResponse
import com.splendo.kaluga.bluetooth.Service
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.serialization.BluetoothFormat
import com.splendo.kaluga.base.test.BaseTest
import com.splendo.kaluga.base.test.mock.call
import com.splendo.kaluga.base.test.mock.on
import com.splendo.kaluga.base.test.mock.suspendTripleParametersMock
import com.splendo.kaluga.base.test.mock.verify
import com.splendo.kaluga.bluetooth.test.randomUUID
import com.splendo.kaluga.bluetooth.test.server.MockConnectedDevice
import com.splendo.kaluga.bluetooth.test.server.MockLocalServiceWrapperBuilder
import kotlinx.serialization.serializer
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * Tests the common [LocalService] / [LocalCharacteristic] / [LocalDescriptor] DSL. Builds attribute
 * graphs with mock wrappers, so it runs on every platform without a live Bluetooth stack.
 */
class LocalServerAttributesTest : BaseTest() {

    /**
     * Builds [LocalServiceDSL.Primary] instances with recording register actions so the tests can
     * inspect what was registered and drive the registered read/write callbacks directly.
     */
    private class Fixture {
        val reads = mutableMapOf<UUID, suspend LocalCharacteristic.(ConnectedDevice, Int) -> GattResponse.ReadResponse>()
        val writes = mutableMapOf<UUID, suspend LocalCharacteristic.(ConnectedDevice, ByteArray, Int) -> GattResponse.WriteResponse>()
        val subscriptionRegistrations = mutableListOf<LocalCharacteristic.Notifiable>()
        val notified = mutableListOf<Pair<ConnectedDevice, ByteArray>>()

        val notifyMock = suspendTripleParametersMock<LocalCharacteristic.Notifiable, ConnectedDevice, ByteArray, Boolean>()

        init {
            notifyMock.on().doExecuteSuspended { params ->
                notified.add(params.second to params.third)
                true
            }
        }

        fun primary(uuid: UUID = randomUUID()): LocalServiceDSL.Primary = LocalServiceDSL.Primary(
            uuid,
            { characteristic, device, value -> notifyMock.call(characteristic, device, value) },
            { characteristic, onRead -> reads[characteristic.uuid] = onRead },
            { characteristic, onWrite -> writes[characteristic.uuid] = onWrite },
            { subscriptionRegistrations.add(this) },
            { descriptorUuid -> LocalDescriptorDSL(descriptorUuid, { _, _ -> }, { _, _ -> }) },
            MockLocalServiceWrapperBuilder(),
        )
    }

    @Test
    fun testReadableCharacteristic() {
        val fixture = Fixture()
        val charUuid = randomUUID()
        val service = fixture.primary().apply {
            characteristic(charUuid) {
                readableAlwaysSuccess { _, _ -> byteArrayOf(1, 2, 3) }
            }
        }.build()

        val characteristic = service.characteristics.single()
        assertEquals(charUuid, characteristic.uuid)
        assertTrue(characteristic.properties.contains(CharacteristicProperty.Read))
        assertTrue(characteristic.permissions.contains(LocalCharacteristic.Permission.READABLE))

        val onRead = fixture.reads.getValue(charUuid)
        val response = runBlocking { onRead(characteristic, MockConnectedDevice(), 0) }
        assertIs<GattResponse.ReadSuccess>(response)
        assertContentEquals(byteArrayOf(1, 2, 3), response.value)
    }

    @Test
    fun testEncryptedReadableUsesEncryptedPermission() {
        val fixture = Fixture()
        val charUuid = randomUUID()
        val service = fixture.primary().apply {
            characteristic(charUuid) {
                readableAlwaysSuccess(encrypted = true) { _, _ -> byteArrayOf() }
            }
        }.build()

        assertTrue(service.characteristics.single().permissions.contains(LocalCharacteristic.Permission.READ_ENCRYPTION_REQUIRED))
    }

    @Test
    fun testWritableCharacteristic() {
        val fixture = Fixture()
        val charUuid = randomUUID()
        val written = mutableListOf<ByteArray>()
        val service = fixture.primary().apply {
            characteristic(charUuid) {
                writableAlwaysSuccess { _, value, _ -> written.add(value) }
            }
        }.build()

        val characteristic = service.characteristics.single()
        assertTrue(characteristic.properties.contains(CharacteristicProperty.Write))
        assertTrue(characteristic.permissions.contains(LocalCharacteristic.Permission.WRITABLE))

        val onWrite = fixture.writes.getValue(charUuid)
        val response = runBlocking { onWrite(characteristic, MockConnectedDevice(), byteArrayOf(9), 0) }
        assertEquals(GattResponse.WriteSuccess, response)
        assertContentEquals(byteArrayOf(9), written.single())
    }

    @Test
    fun testWritableDeserializesValue() {
        val fixture = Fixture()
        val charUuid = randomUUID()
        val decoded = mutableListOf<Int>()
        val service = fixture.primary().apply {
            characteristic(charUuid) {
                writable<Int> { _, value ->
                    decoded.add(value)
                    GattResponse.WriteSuccess
                }
            }
        }.build()

        val onWrite = fixture.writes.getValue(charUuid)
        val bytes = BluetoothFormat.encodeToByteArray(serializer<Int>(), 42)
        val response = runBlocking { onWrite(service.characteristics.single(), MockConnectedDevice(), bytes, 0) }
        assertEquals(GattResponse.WriteSuccess, response)
        assertEquals(listOf(42), decoded)
    }

    @Test
    fun testReadingTwiceThrows() {
        val fixture = Fixture()
        assertFailsWith<IllegalArgumentException> {
            fixture.primary().apply {
                characteristic(randomUUID()) {
                    readable { _, _ -> GattResponse.ReadSuccess(byteArrayOf()) }
                    readable { _, _ -> GattResponse.ReadSuccess(byteArrayOf()) }
                }
            }
        }
    }

    @Test
    fun testWritingTwiceThrows() {
        val fixture = Fixture()
        assertFailsWith<IllegalArgumentException> {
            fixture.primary().apply {
                characteristic(randomUUID()) {
                    writableAlwaysSuccess { _, _, _ -> }
                    writableAlwaysSuccess { _, _, _ -> }
                }
            }
        }
    }

    @Test
    fun testWritableWithEmptyPropertiesThrows() {
        val fixture = Fixture()
        assertFailsWith<IllegalArgumentException> {
            fixture.primary().apply {
                characteristic(randomUUID()) {
                    writable(properties = emptySet()) { _, _, _ -> GattResponse.WriteSuccess }
                }
            }
        }
    }

    @Test
    fun testDuplicateDescriptorThrows() {
        val fixture = Fixture()
        val descriptorUuid = randomUUID()
        assertFailsWith<IllegalArgumentException> {
            fixture.primary().apply {
                characteristic(randomUUID()) {
                    descriptor(descriptorUuid) { }
                    descriptor(descriptorUuid) { }
                }
            }
        }
    }

    @Test
    fun testNotifiableSubscribeNotifyUnsubscribe() {
        val fixture = Fixture()
        val charUuid = randomUUID()
        val service = fixture.primary().apply {
            characteristic(charUuid) {
                notifiable(onSubscribe = { }, onUnsubscribe = { })
            }
        }.build()

        val characteristic = assertIs<LocalCharacteristic.Notifiable>(service.characteristics.single())
        assertEquals(1, fixture.subscriptionRegistrations.size)
        assertTrue(characteristic.properties.contains(CharacteristicProperty.Notify))

        val device = MockConnectedDevice()
        characteristic.subscribe(device)
        assertEquals(listOf(device), characteristic.subscribedDevices.value)

        val didNotify = runBlocking { characteristic.notify(device, byteArrayOf(7)) }
        assertTrue(didNotify)
        fixture.notifyMock.verify()
        assertContentEquals(byteArrayOf(7), fixture.notified.single().second)

        characteristic.unsubscribe(device)
        assertTrue(characteristic.subscribedDevices.value.isEmpty())
    }

    @Test
    fun testNotifyingUnsubscribedDeviceFails() {
        val fixture = Fixture()
        val charUuid = randomUUID()
        val service = fixture.primary().apply {
            characteristic(charUuid) {
                notifiable(onSubscribe = { }, onUnsubscribe = { })
            }
        }.build()

        val characteristic = assertIs<LocalCharacteristic.Notifiable>(service.characteristics.single())
        val didNotify = runBlocking { characteristic.notify(MockConnectedDevice(), byteArrayOf(7)) }
        assertFalse(didNotify)
        assertTrue(fixture.notified.isEmpty())
    }

    @Test
    fun testServiceStructureWithIncludedService() {
        val fixture = Fixture()
        val serviceUuid = randomUUID()
        val service = fixture.primary(serviceUuid).apply {
            includedService(randomUUID()) {
                characteristic(randomUUID()) {
                    readableAlwaysSuccess { _, _ -> byteArrayOf() }
                }
            }
            characteristic(randomUUID()) {
                readableAlwaysSuccess { _, _ -> byteArrayOf() }
            }
        }.build()

        assertEquals(serviceUuid, service.uuid)
        assertEquals(Service.Type.PRIMARY, service.type)
        assertEquals(1, service.characteristics.size)
        assertEquals(1, service.includedServices.size)
        assertEquals(Service.Type.SECONDARY, service.includedServices.single().type)
    }
}
