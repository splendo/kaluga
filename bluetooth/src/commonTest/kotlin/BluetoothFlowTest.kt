/*
 Copyright (c) 2020. Splendo Consulting B.V. The Netherlands

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

import co.touchlab.stately.ensureNeverFrozen
import co.touchlab.stately.freeze
import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.bluetooth.device.BaseAdvertisementData
import com.splendo.kaluga.bluetooth.device.BaseDeviceConnectionManager
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.DeviceInfoImpl
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.bluetooth.device.DeviceStateFlowRepo
import com.splendo.kaluga.test.mock.bluetooth.device.MockAdvertisementData
import com.splendo.kaluga.bluetooth.scanner.BaseScanner
import com.splendo.kaluga.bluetooth.scanner.MockBaseScanner
import com.splendo.kaluga.bluetooth.scanner.ScanningState
import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.PermissionState
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.test.FlowTest
import com.splendo.kaluga.test.MockPermissionManager
import com.splendo.kaluga.test.mock.permissions.MockPermissionsBuilder
import com.splendo.kaluga.base.utils.EmptyCompletableDeferred
import com.splendo.kaluga.base.utils.complete
import com.splendo.kaluga.bluetooth.BluetoothFlowTest.Setup.BLUETOOTH
import com.splendo.kaluga.bluetooth.BluetoothFlowTest.Setup.CHARACTERISTIC
import com.splendo.kaluga.bluetooth.BluetoothFlowTest.Setup.DESCRIPTOR
import com.splendo.kaluga.bluetooth.BluetoothFlowTest.Setup.DEVICE
import com.splendo.kaluga.bluetooth.BluetoothFlowTest.Setup.SERVICE
import com.splendo.kaluga.bluetooth.device.DeviceWrapper
import com.splendo.kaluga.bluetooth.scanner.ScanningStateFlowRepo
import com.splendo.kaluga.test.SimpleFlowTest
import com.splendo.kaluga.test.mock.bluetooth.createDeviceWrapper
import com.splendo.kaluga.test.mock.bluetooth.createServiceWrapper
import com.splendo.kaluga.test.mock.bluetooth.device.MockDeviceConnectionManager
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


abstract class BluetoothFlowTest<T> : SimpleFlowTest<T>() {

    companion object {
        const val initialRssi = -100
        val initialAdvertisementData = MockAdvertisementData(name="Name")
    }

    protected lateinit var device: Device
    protected lateinit var deviceWrapper: DeviceWrapper
    protected lateinit var advertisementData: MockAdvertisementData
    internal lateinit var connectionManager: MockDeviceConnectionManager
    protected lateinit var serviceWrapper: ServiceWrapper
    protected lateinit var service: Service
    protected lateinit var characteristic: Characteristic
    protected lateinit var descriptor: Descriptor

    protected lateinit var permissionsBuilder: MockPermissionsBuilder
    protected lateinit var permissions: Permissions
    protected val permissionManager: MockPermissionManager<Permission.Bluetooth>
        get() {
            return permissionsBuilder.bluetoothPMManager
        }
    
    val deferredBaseScanner = CompletableDeferred<MockBaseScanner>()
    protected suspend fun mockBaseScanner() = deferredBaseScanner.await()
    
    protected lateinit var bluetooth: Bluetooth

    protected fun setupPermissions() {
        permissionsBuilder = MockPermissionsBuilder()
        permissions = Permissions(permissionsBuilder, this.coroutineContext)
        permissions[Permission.Bluetooth]
        permissions.getManager(Permission.Bluetooth).grantPermission()
    }

    protected suspend fun setupBluetooth(autoRequestPermission: Boolean = true, autoEnableBluetooth: Boolean = true, isEnabled: Boolean = true, permissionState: PermissionState<Permission.Bluetooth> = PermissionState.Allowed()) {
        val deferredBaseScanner = this.deferredBaseScanner
        val scannerBuilder = object : BaseScanner.Builder {
            override fun create(
                permissions: Permissions,
                connectionSettings: ConnectionSettings,
                autoRequestPermission: Boolean,
                autoEnableBluetooth: Boolean,
                scanningStateRepo: ScanningStateFlowRepo,
            ): BaseScanner {
                val scanner = MockBaseScanner(permissions, connectionSettings, autoRequestPermission, autoEnableBluetooth, scanningStateRepo)
                scanner.isEnabled = isEnabled
                deferredBaseScanner.complete(scanner)
                return scanner
            }
        }

        permissions.freeze()
        scannerBuilder.freeze()
        bluetooth = Bluetooth(permissions, ConnectionSettings(), autoRequestPermission, autoEnableBluetooth, scannerBuilder, MainScope())
        bluetooth.freeze()
        // mockBaseScanner = deferredBaseScanner.await()
        permissionManager.currentState = permissionState
    }


    protected enum class Setup {
        BLUETOOTH,
        DEVICE,
        SERVICE,
        CHARACTERISTIC,
        DESCRIPTOR
    }
    protected suspend fun setup(
        setup: Setup,
        mockAdvertisementData: MockAdvertisementData = initialAdvertisementData,
        rssi: Int = initialRssi
    ) {


        setupPermissions()
        setupBluetooth()
        if (setup == BLUETOOTH) return

        deviceWrapper = createDeviceWrapper()
        device = createDevice(deviceWrapper, rssi = rssi, advertisementData = mockAdvertisementData)
        advertisementData = mockAdvertisementData
        connectionManager = device.peekState().connectionManager as MockDeviceConnectionManager
        if (setup == DEVICE) return


        serviceWrapper = createServiceWrapper(connectionManager.stateRepo)
        service = Service(serviceWrapper, connectionManager.stateRepo)

        if (setup == SERVICE) return
        characteristic = service.characteristics.first()
        if (setup == CHARACTERISTIC) return
        descriptor = characteristic.descriptors.first()

    }

    protected suspend fun scanDevice(device: Device, deviceWrapper: DeviceWrapper, rssi: Int = initialRssi, advertisementData: BaseAdvertisementData = initialAdvertisementData, scanCompleted: EmptyCompletableDeferred? = null) {
        bluetooth.scanningStateRepo.filter {
            it is ScanningState.Initialized.Enabled.Scanning
        }.first()
        bluetooth.scanningStateRepo.takeAndChangeState { state ->
            when (state) {
                is ScanningState.Initialized.Enabled.Scanning -> {
                    state.discoverDevice(deviceWrapper.identifier, rssi, advertisementData) { device }
                }
                else -> {
                    state.remain()
                }
            }
        }
        scanCompleted?.complete()
    }

    protected suspend fun awaitDevice(flowTest: FlowTest<Device?, Flow<Device?>>, foundDevice: CompletableDeferred<Device>) {
        // val deviceNotFound = EmptyCompletableDeferred()
        // deviceNotFound.invokeOnCompletion {
        //     awaitDevice(flowTest, foundDevice)
        // }
        flowTest.test {
            if (it != null) {
                foundDevice.complete(it)
            } else {
                awaitDevice(flowTest, foundDevice)
                // deviceNotFound.complete()
            }
        }
    }

    internal suspend fun connectDevice(device: Device, connectionManager: MockDeviceConnectionManager, coroutineScope: CoroutineScope) {
        connectionManager.reset()
        val connectingJob = coroutineScope.async {
            bluetooth.devices()[device.identifier].connect()
        }
        connectionManager.connectCompleted.await()
        connectionManager.handleConnect()
        connectingJob.await()
    }

    internal suspend fun disconnectDevice(device: Device, connectionManager: MockDeviceConnectionManager, coroutineScope: CoroutineScope) {
        connectionManager.reset()
        val disconnectingJob = coroutineScope.async {
            bluetooth.devices()[device.identifier].disconnect()
        }
        connectionManager.disconnectCompleted.await()
        connectionManager.handleDisconnect()
        disconnectingJob.await()
    }

    protected suspend fun discoverService(service: Service, device: Device) {
        device.filter { it is DeviceState.Connected.Discovering }.first()
        connectionManager.handleScanCompleted(listOf(service))
    }

    protected suspend fun awaitService(flowTest: SimpleFlowTest<Service?>, foundService: CompletableDeferred<Service>) {
        // val serviceNotFound = EmptyCompletableDeferred()
        // serviceNotFound.invokeOnCompletion {
        //
        // }
        flowTest.test {
            if (it != null) {
                foundService.complete(it)
            } else {
                awaitService(flowTest, foundService)
                // serviceNotFound.complete()
            }
        }
    }

    protected suspend fun awaitCharacteristic(flowTest: SimpleFlowTest<Characteristic?>, foundCharacteristic: CompletableDeferred<Characteristic>) {
        // val characteristicNotFound = EmptyCompletableDeferred()
        // characteristicNotFound.invokeOnCompletion {
        //
        // }
        flowTest.test {
            if (it != null) {
                foundCharacteristic.complete(it)
            } else {
                awaitCharacteristic(flowTest, foundCharacteristic)
                // characteristicNotFound.complete()
            }
        }
    }

    protected suspend fun awaitDescriptor(flowTest: SimpleFlowTest<Descriptor?>, foundDescriptor: CompletableDeferred<Descriptor>) {
        // val descriptorNotFound = EmptyCompletableDeferred()
        // descriptorNotFound.invokeOnCompletion {
        //
        // }
        flowTest.test {
            if (it != null) {
                foundDescriptor.complete(it)
            } else {
                awaitDescriptor(flowTest, foundDescriptor)
                // descriptorNotFound.complete()
            }
        }
    }

    protected suspend fun awaitByte(flowTest: FlowTest<ByteArray?, Flow<ByteArray?>>, foundByte: CompletableDeferred<ByteArray>) {
        // val byteNotFound = EmptyCompletableDeferred()
        // byteNotFound.invokeOnCompletion {
        //
        // }
        flowTest.test {
            if (it != null) {
                foundByte.complete(it)
            } else {
                awaitByte(flowTest, foundByte)
                // byteNotFound.complete()
            }
        }
    }

    protected fun createDevice(deviceWrapper: DeviceWrapper, rssi: Int = initialRssi, advertisementData: BaseAdvertisementData = initialAdvertisementData): Device {
        return Device(
            ConnectionSettings(),
            DeviceInfoImpl(deviceWrapper, rssi, advertisementData),
            object : BaseDeviceConnectionManager.Builder {

                override fun create(
                    connectionSettings: ConnectionSettings,
                    deviceWrapper: DeviceWrapper,
                    stateRepo: DeviceStateFlowRepo,
                ): BaseDeviceConnectionManager {
                    return MockDeviceConnectionManager(
                        connectionSettings,
                        deviceWrapper,
                        stateRepo
                    )
                }
            },
            coroutineContext
        )
    }
}