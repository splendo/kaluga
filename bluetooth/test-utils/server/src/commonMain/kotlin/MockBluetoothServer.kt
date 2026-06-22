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

package com.splendo.kaluga.bluetooth.test.server

import com.splendo.kaluga.base.collections.concurrentMutableMapOf
import com.splendo.kaluga.base.test.mock.call
import com.splendo.kaluga.base.test.mock.on
import com.splendo.kaluga.base.test.mock.parameters.mock
import com.splendo.kaluga.bluetooth.GattResponse
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.server.AdvertiseData
import com.splendo.kaluga.bluetooth.server.BluetoothServer
import com.splendo.kaluga.bluetooth.server.ConnectedDevice
import com.splendo.kaluga.bluetooth.server.LocalCharacteristic
import com.splendo.kaluga.bluetooth.server.LocalDescriptor
import com.splendo.kaluga.bluetooth.server.LocalService
import com.splendo.kaluga.bluetooth.server.ServerStatus
import com.splendo.kaluga.bluetooth.server.buildCapturingLocalService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * A behavioral mock implementation of [BluetoothServer].
 *
 * The [status], [isAdvertising] and [services] flows are backed by [MutableStateFlow]s the test can observe,
 * and the methods are also mocked using the Kaluga mocking library so calls can be verified.
 *
 * Unlike a purely call-recording mock, [add] actually builds the requested [LocalService] graph (via
 * [buildCapturingLocalService]), stores it in [servicesState] and captures the per-characteristic read/write actions
 * into [reads]/[writes]. This lets a test simulate a connected central reading, writing and subscribing through
 * [triggerRead], [triggerWrite] and [subscribe] without a live Bluetooth stack or a real [BluetoothServer].
 * @param initialStatus the initial [ServerStatus]
 * @param setupMocks if `true` the mocks are configured with sensible default behaviour
 */
class MockBluetoothServer(initialStatus: ServerStatus = ServerStatus.AVAILABLE, setupMocks: Boolean = true) : BluetoothServer {

    /**
     * The [MutableStateFlow] backing [status]
     */
    val statusState = MutableStateFlow(initialStatus)
    override val status: StateFlow<ServerStatus> = statusState

    /**
     * The [MutableStateFlow] backing [isAdvertising]
     */
    val isAdvertisingState = MutableStateFlow(false)
    override val isAdvertising: StateFlow<Boolean> = isAdvertisingState

    /**
     * The [MutableStateFlow] backing [services]
     */
    val servicesState = MutableStateFlow<List<LocalService>>(emptyList())
    override val services: StateFlow<List<LocalService>> = servicesState

    /**
     * The read actions captured per-characteristic [UUID] while building services.
     */
    val reads = concurrentMutableMapOf<UUID, suspend LocalCharacteristic.(ConnectedDevice, Int) -> GattResponse.ReadResponse>()

    /**
     * The write actions captured per-characteristic [UUID] while building services.
     */
    val writes = concurrentMutableMapOf<UUID, suspend LocalCharacteristic.(ConnectedDevice, ByteArray, Int) -> GattResponse.WriteResponse>()

    /**
     * The read actions captured per-descriptor [UUID] while building services.
     */
    val descriptorReads = concurrentMutableMapOf<UUID, suspend LocalDescriptor.(ConnectedDevice, Int) -> GattResponse.ReadResponse>()

    /**
     * The write actions captured per-descriptor [UUID] while building services.
     */
    val descriptorWrites = concurrentMutableMapOf<UUID, suspend LocalDescriptor.(ConnectedDevice, ByteArray, Int) -> GattResponse.WriteResponse>()

    /**
     * The [UUID]s of the characteristics that can be subscribed to.
     */
    val subscribableCharacteristics = mutableSetOf<UUID>()

    /**
     * Mock for [advertise]
     */
    val advertiseMock = ::advertise.mock()

    /**
     * Mock for [stopAdvertising]
     */
    val stopAdvertisingMock = ::stopAdvertising.mock()

    /**
     * Mock for [add]
     */
    val addMock = ::add.mock()

    /**
     * Mock for [remove]
     */
    val removeMock = ::remove.mock()

    /**
     * Mock for [removeAllServices]
     */
    val removeAllServicesMock = ::removeAllServices.mock()

    /**
     * Mock for [close]
     */
    val closeMock = ::close.mock()

    init {
        if (setupMocks) {
            advertiseMock.on().doExecuteSuspended {
                isAdvertisingState.value = true
                true
            }
            stopAdvertisingMock.on().doExecute {
                isAdvertisingState.value = false
            }
            addMock.on().doExecuteSuspended { (uuid, service) ->
                buildService(uuid, service)
            }
            removeMock.on().doExecuteSuspended { (service) ->
                servicesState.value = servicesState.value - service
                true
            }
            removeAllServicesMock.on().doExecuteSuspended {
                servicesState.value = emptyList()
                true
            }
        }
    }

    private fun buildService(uuid: UUID, service: LocalService.DSL.Primary.() -> Unit): LocalService {
        val captured = buildCapturingLocalService(
            uuid,
            MockLocalServiceWrapperBuilder(),
            service = service,
        )
        reads.putAll(captured.characteristicReads)
        writes.putAll(captured.characteristicWrites)
        descriptorReads.putAll(captured.descriptorReads)
        descriptorWrites.putAll(captured.descriptorWrites)
        subscribableCharacteristics.addAll(captured.subscribableCharacteristics)
        servicesState.value = servicesState.value + captured.service
        return captured.service
    }

    private fun findCharacteristic(characteristic: UUID): LocalCharacteristic = servicesState.value
        .firstNotNullOfOrNull { service -> service.characteristics.find { it.uuid == characteristic } }
        ?: throw IllegalArgumentException("No characteristic $characteristic in the server")

    private fun findDescriptor(descriptor: UUID): LocalDescriptor = servicesState.value
        .firstNotNullOfOrNull { service ->
            service.characteristics.firstNotNullOfOrNull { characteristic ->
                characteristic.descriptors.find { it.uuid == descriptor }
            }
        }
        ?: throw IllegalArgumentException("No descriptor $descriptor in the server")

    override suspend fun advertise(data: AdvertiseData.Builder.() -> Unit): Boolean = advertiseMock.call(data)
    override fun stopAdvertising() {
        stopAdvertisingMock.call()
    }
    override suspend fun add(uuid: UUID, service: LocalService.DSL.Primary.() -> Unit): LocalService? = addMock.call(uuid, service)
    override suspend fun remove(service: LocalService): Boolean = removeMock.call(service)
    override suspend fun removeAllServices(): Boolean = removeAllServicesMock.call()
    override fun close() {
        closeMock.call()
    }

    /**
     * Simulates a connected central reading [characteristic] by invoking the captured read action.
     * @return the [GattResponse.ReadResponse] produced by the server-side delegate.
     */
    suspend fun triggerRead(characteristic: UUID, device: ConnectedDevice = MockConnectedDevice(), offset: Int = 0): GattResponse.ReadResponse {
        val target = findCharacteristic(characteristic)
        val onRead = reads[characteristic] ?: throw IllegalArgumentException("Characteristic $characteristic is not readable")
        return onRead(target, device, offset)
    }

    /**
     * Simulates a connected central writing [value] to [characteristic] by invoking the captured write action.
     * @return the [GattResponse.WriteResponse] produced by the server-side delegate.
     */
    suspend fun triggerWrite(characteristic: UUID, value: ByteArray, device: ConnectedDevice = MockConnectedDevice(), offset: Int = 0): GattResponse.WriteResponse {
        val target = findCharacteristic(characteristic)
        val onWrite = writes[characteristic] ?: throw IllegalArgumentException("Characteristic $characteristic is not writable")
        return onWrite(target, device, value, offset)
    }

    /**
     * Simulates a connected central reading [descriptor] by invoking the captured descriptor read action.
     * @return the [GattResponse.ReadResponse] produced by the server-side delegate.
     */
    suspend fun triggerDescriptorRead(descriptor: UUID, device: ConnectedDevice = MockConnectedDevice(), offset: Int = 0): GattResponse.ReadResponse {
        val target = findDescriptor(descriptor)
        val onRead = descriptorReads[descriptor] ?: throw IllegalArgumentException("Descriptor $descriptor is not readable")
        return onRead(target, device, offset)
    }

    /**
     * Simulates a connected central writing [value] to [descriptor] by invoking the captured descriptor write action.
     * @return the [GattResponse.WriteResponse] produced by the server-side delegate.
     */
    suspend fun triggerDescriptorWrite(descriptor: UUID, value: ByteArray, device: ConnectedDevice = MockConnectedDevice(), offset: Int = 0): GattResponse.WriteResponse {
        val target = findDescriptor(descriptor)
        val onWrite = descriptorWrites[descriptor] ?: throw IllegalArgumentException("Descriptor $descriptor is not writable")
        return onWrite(target, device, value, offset)
    }

    /**
     * Simulates a connected central subscribing to [characteristic]. The characteristic must be a [LocalCharacteristic.Notifiable].
     */
    fun subscribe(characteristic: UUID, device: ConnectedDevice = MockConnectedDevice()) {
        val notifiable = findCharacteristic(characteristic) as? LocalCharacteristic.Notifiable
            ?: throw IllegalArgumentException("Characteristic $characteristic is not notifiable")
        notifiable.subscribe(device)
    }
}
