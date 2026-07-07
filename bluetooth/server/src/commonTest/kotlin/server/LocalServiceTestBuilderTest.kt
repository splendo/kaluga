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
import com.splendo.kaluga.base.test.BaseTest
import com.splendo.kaluga.bluetooth.GattResponse
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.test.randomUUID
import com.splendo.kaluga.bluetooth.test.server.MockConnectedDevice
import com.splendo.kaluga.bluetooth.test.server.MockLocalServiceWrapperBuilder
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * Tests that [buildCapturingLocalService] captures every characteristic and descriptor read/write action
 * and every subscribable characteristic into the returned [CapturedLocalService].
 */
class LocalServiceTestBuilderTest : BaseTest() {

    @Test
    fun testCapturesAllActions() {
        val serviceUuid = randomUUID()
        val readableUuid = randomUUID()
        val writableUuid = randomUUID()
        val notifiableUuid = randomUUID()
        val descriptorCharacteristicUuid = randomUUID()
        val descriptorUuid = randomUUID()

        val written = mutableListOf<ByteArray>()
        val descriptorWritten = mutableListOf<ByteArray>()

        val captured = buildCapturingLocalService(
            serviceUuid,
            MockLocalServiceWrapperBuilder(),
        ) {
            characteristic(readableUuid) {
                readableAlwaysSuccess { _, _ -> byteArrayOf(1, 2, 3) }
            }
            characteristic(writableUuid) {
                writableAlwaysSuccess { _, value, _ -> written.add(value) }
            }
            characteristic(notifiableUuid) {
                notifiable(onSubscribe = { }, onUnsubscribe = { })
            }
            characteristic(descriptorCharacteristicUuid) {
                readableAlwaysSuccess { _, _ -> byteArrayOf() }
                descriptor(descriptorUuid) {
                    readableAlwaysSuccess { _, _ -> byteArrayOf(4, 5, 6) }
                    writableAlwaysSuccess { _, value, _ -> descriptorWritten.add(value) }
                }
            }
        }

        // Characteristic reads are captured and produce the expected response.
        assertTrue(captured.characteristicReads.containsKey(readableUuid))
        val readResponse = runBlocking {
            captured.characteristicReads.getValue(readableUuid)(findCharacteristic(captured.service, readableUuid), MockConnectedDevice(), 0)
        }
        assertContentEquals(byteArrayOf(1, 2, 3), assertIs<GattResponse.ReadSuccess>(readResponse).value)

        // Characteristic writes are captured and produce the expected response.
        assertTrue(captured.characteristicWrites.containsKey(writableUuid))
        val writeResponse = runBlocking {
            captured.characteristicWrites.getValue(writableUuid)(findCharacteristic(captured.service, writableUuid), MockConnectedDevice(), byteArrayOf(9), 0)
        }
        assertEquals(GattResponse.WriteSuccess.Acknowledged, writeResponse)
        assertContentEquals(byteArrayOf(9), written.single())

        // Descriptor reads are captured and produce the expected response.
        assertTrue(captured.descriptorReads.containsKey(descriptorUuid))
        val descriptorReadResponse = runBlocking {
            captured.descriptorReads.getValue(descriptorUuid)(findDescriptor(captured.service, descriptorUuid), MockConnectedDevice(), 0)
        }
        assertContentEquals(byteArrayOf(4, 5, 6), assertIs<GattResponse.ReadSuccess>(descriptorReadResponse).value)

        // Descriptor writes are captured and produce the expected response.
        assertTrue(captured.descriptorWrites.containsKey(descriptorUuid))
        val descriptorWriteResponse = runBlocking {
            captured.descriptorWrites.getValue(descriptorUuid)(findDescriptor(captured.service, descriptorUuid), MockConnectedDevice(), byteArrayOf(7), 0)
        }
        assertEquals(GattResponse.WriteSuccess.Acknowledged, descriptorWriteResponse)
        assertContentEquals(byteArrayOf(7), descriptorWritten.single())

        // The notifiable characteristic is captured as subscribable.
        assertTrue(captured.subscribableCharacteristics.contains(notifiableUuid))
    }

    private fun findCharacteristic(service: LocalService, uuid: UUID): LocalCharacteristic = service.characteristics.first { it.uuid == uuid }

    private fun findDescriptor(service: LocalService, uuid: UUID): LocalDescriptor =
        service.characteristics.firstNotNullOf { characteristic -> characteristic.descriptors.find { it.uuid == uuid } }
}
