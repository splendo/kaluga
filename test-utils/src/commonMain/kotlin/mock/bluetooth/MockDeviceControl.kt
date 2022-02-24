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

package com.splendo.kaluga.test.mock.bluetooth

import com.splendo.kaluga.bluetooth.Service
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.connect
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.DeviceInfo
import com.splendo.kaluga.bluetooth.device.DeviceStateFlowRepo
import com.splendo.kaluga.bluetooth.disconnect
import com.splendo.kaluga.bluetooth.services
import com.splendo.kaluga.test.mock.bluetooth.device.MockDeviceConnectionManager
import com.splendo.kaluga.test.mock.bluetooth.device.MockDeviceInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first

class MockDeviceControl private constructor(
    private val deviceInfo: DeviceInfo
) {
    private val mockDeviceFactory = MockDeviceFactory(Dispatchers.Main)
    private val connectionManager: MockDeviceConnectionManager? get() = mockDeviceFactory.connectionManager

    private val devicesFlow = MutableStateFlow<Device?>(value = null)
    val mock: Flow<Device?> = devicesFlow.asStateFlow()

    class Builder {
        var deviceInfo: DeviceInfo? = null
        fun deviceInfo(build: MockDeviceInfo.Builder.() -> Unit) {
            val builder = MockDeviceInfo.Builder()
            build(builder)
            deviceInfo = builder.build()
        }
    }

    companion object {
        fun build(
            build: Builder.() -> Unit = {}
        ): MockDeviceControl {
            val builder = Builder()
            build(builder)
            return MockDeviceControl(
                deviceInfo = builder.deviceInfo ?: MockDeviceInfo.build { }
            )
        }
    }

    suspend fun reset() {
        connectionManager?.reset()
        servicesAdvertisingData.clear()
        discover()
        connect()
        disconnect()
        connectionManager?.reset()
    }

    suspend fun discover() {
        val device = mockDeviceFactory.build(deviceInfo)
        devicesFlow.emit(device)
    }

    suspend fun connect() {
        devicesFlow.first()?.stateFlow?.first()

        connectionManager?.run {
            reset()
            val connectingJob = async {
                devicesFlow.connect()
            }
            connectCompleted.get().await()
            handleConnect()
            connectingJob.await()
        } ?: throw Error("The Connection Manager was not created")
    }

    suspend fun disconnect() {
        devicesFlow.first()?.stateFlow?.first()

        connectionManager?.run {
            reset()
            val disconnectingJob = async {
                devicesFlow.disconnect()
            }
            disconnectCompleted.get().await()
            handleDisconnect()
            disconnectingJob.await()
        } ?: throw Error("The Connection Manager was not created")
    }

    private val servicesAdvertisingData = mutableListOf<MockServiceAdvertisingData>()
    suspend fun simulate(serviceAdvertisingData: MockServiceAdvertisingData) {
        discover()
        connect()

        servicesAdvertisingData.add(serviceAdvertisingData)
        updateDiscoveredService(servicesAdvertisingData)
    }

    private suspend fun updateDiscoveredService(advertisingData: List<MockServiceAdvertisingData>) {
        devicesFlow.services().first()
        connectionManager?.run {
            val services = advertisingData.map { createServiceMock(it, stateRepo) }
            handleScanCompleted(services)
        }
    }

    private fun createServiceMock(advertisementData: MockServiceAdvertisingData, deviceStateFlowRepo: DeviceStateFlowRepo): Service {
        val serviceWrapper = createServiceWrapper(
            uuid = advertisementData.uuid,
            stateRepo = deviceStateFlowRepo
        )
        return Service(serviceWrapper, deviceStateFlowRepo)
    }
}

data class MockServiceAdvertisingData(
    val uuid: UUID
)
