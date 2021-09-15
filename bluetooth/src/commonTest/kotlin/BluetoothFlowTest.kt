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

import co.touchlab.stately.freeze
import com.splendo.kaluga.base.utils.EmptyCompletableDeferred
import com.splendo.kaluga.base.utils.complete
import com.splendo.kaluga.bluetooth.BluetoothFlowTest.Setup.BLUETOOTH
import com.splendo.kaluga.bluetooth.BluetoothFlowTest.Setup.CHARACTERISTIC
import com.splendo.kaluga.bluetooth.BluetoothFlowTest.Setup.DEVICE
import com.splendo.kaluga.bluetooth.BluetoothFlowTest.Setup.SERVICE
import com.splendo.kaluga.bluetooth.device.BaseAdvertisementData
import com.splendo.kaluga.bluetooth.device.BaseDeviceConnectionManager
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.DeviceInfoImpl
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.bluetooth.device.DeviceStateFlowRepo
import com.splendo.kaluga.bluetooth.device.DeviceWrapper
import com.splendo.kaluga.bluetooth.scanner.BaseScanner
import com.splendo.kaluga.bluetooth.scanner.ScanningState
import com.splendo.kaluga.bluetooth.scanner.ScanningStateFlowRepo
import com.splendo.kaluga.permissions.PermissionState
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.permissions.bluetooth.BluetoothPermission
import com.splendo.kaluga.test.FlowTestBlock
import com.splendo.kaluga.test.MockPermissionManager
import com.splendo.kaluga.test.SimpleFlowTest
import com.splendo.kaluga.test.mock.bluetooth.createDeviceWrapper
import com.splendo.kaluga.test.mock.bluetooth.createServiceWrapper
import com.splendo.kaluga.test.mock.bluetooth.device.MockAdvertisementData
import com.splendo.kaluga.test.mock.bluetooth.device.MockDeviceConnectionManager
import com.splendo.kaluga.test.mock.bluetooth.scanner.MockBaseScanner
import com.splendo.kaluga.test.mock.permissions.MockPermissionsBuilder
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

abstract class BluetoothFlowTest<T> : SimpleFlowTest<T>() {

    companion object {
    }

    var rssi = -100
    var advertisementData = MockAdvertisementData(name = "Name")
    var autoRequestPermission: Boolean = true
    var autoEnableBluetooth: Boolean = true
    var isEnabled: Boolean = true
    var permissionState: PermissionState<BluetoothPermission> = PermissionState.Allowed()

    private val deferredScanningStateFlowRepo: CompletableDeferred<ScanningStateFlowRepo> = CompletableDeferred()
    val scanningStateRepo: ScanningStateFlowRepo
        get() = deferredScanningStateFlowRepo.getCompleted()

    protected lateinit var device: Device
    protected lateinit var deviceWrapper: DeviceWrapper
    internal lateinit var connectionManager: MockDeviceConnectionManager
    protected lateinit var serviceWrapper: ServiceWrapper
    protected lateinit var service: Service
    protected lateinit var characteristic: Characteristic
    protected lateinit var descriptor: Descriptor

    protected lateinit var permissionsBuilder: MockPermissionsBuilder
    protected lateinit var permissions: Permissions
    protected val permissionManager: MockPermissionManager<BluetoothPermission>
        get() {
            return permissionsBuilder.bluetoothPMManager!!
        }

    val deferredBaseScanner = CompletableDeferred<MockBaseScanner>()
    protected suspend fun mockBaseScanner() = deferredBaseScanner.await()

    protected lateinit var bluetooth: Bluetooth

    protected fun setupPermissions() {
        permissionsBuilder = MockPermissionsBuilder().apply {
            registerAllPermissionsBuilders()
        }
        permissions = Permissions(permissionsBuilder, this.coroutineContext)
        permissions[BluetoothPermission]
        permissions.getManager(BluetoothPermission).grantPermission()
    }

    protected fun setupBluetooth(autoRequestPermission: Boolean, autoEnableBluetooth: Boolean, isEnabled: Boolean, permissionState: PermissionState<BluetoothPermission>) {
        val deferredBaseScanner = this.deferredBaseScanner
        val deferredScanningStateFlowRepo = this.deferredScanningStateFlowRepo
        val scannerBuilder = object : BaseScanner.Builder {
            override fun create(
                permissions: Permissions,
                connectionSettings: ConnectionSettings,
                autoRequestPermission: Boolean,
                autoEnableBluetooth: Boolean,
                scanningStateRepo: ScanningStateFlowRepo,
            ): BaseScanner {
                deferredScanningStateFlowRepo.complete(scanningStateRepo)
                val scanner = MockBaseScanner(
                    permissions,
                    connectionSettings,
                    autoRequestPermission,
                    autoEnableBluetooth,
                    scanningStateRepo
                )
                scanner.isEnabled.value = isEnabled
                deferredBaseScanner.complete(scanner)
                return scanner
            }
        }

        permissions.freeze()
        scannerBuilder.freeze()
        bluetooth = Bluetooth(permissions, ConnectionSettings(), autoRequestPermission, autoEnableBluetooth, scannerBuilder, MainScope())
        bluetooth.freeze()
        permissionManager.currentState = permissionState
    }

    fun testWithBluetoothFlow(
        autoRequestPermission: Boolean = this.autoRequestPermission,
        autoEnableBluetooth: Boolean = this.autoEnableBluetooth,
        permissionState: PermissionState<BluetoothPermission> = this.permissionState,
        isEnabled: Boolean = this.isEnabled,
        advertisementData: MockAdvertisementData = this.advertisementData,
        rssi: Int = this.rssi,
        block: FlowTestBlock<T, Flow<T>>
    ) {
        this.autoRequestPermission = autoRequestPermission
        this.autoEnableBluetooth = autoEnableBluetooth
        this.permissionState = permissionState
        this.isEnabled = isEnabled
        this.advertisementData = advertisementData
        this.rssi = rssi
        testWithFlow(block)
    }

    protected enum class Setup {
        BLUETOOTH,
        DEVICE,
        SERVICE,
        CHARACTERISTIC,
        DESCRIPTOR
    }
    protected fun setup(
        setup: Setup,
    ) {
        setupPermissions()
        setupBluetooth(autoRequestPermission, autoEnableBluetooth, isEnabled, permissionState)
        if (setup == BLUETOOTH) return

        deviceWrapper = createDeviceWrapper()
        device = createDevice(deviceWrapper, rssi = rssi, advertisementData = advertisementData)
        connectionManager = device.peekState().connectionManager as MockDeviceConnectionManager
        if (setup == DEVICE) return

        serviceWrapper = createServiceWrapper(connectionManager.stateRepo)
        service = Service(serviceWrapper, connectionManager.stateRepo)

        if (setup == SERVICE) return
        characteristic = service.characteristics.first()
        if (setup == CHARACTERISTIC) return
        descriptor = characteristic.descriptors.first()
    }

    protected suspend fun scanDevice(
        device: Device = this.device,
        deviceWrapper: DeviceWrapper = this.deviceWrapper,
        rssi: Int = this.rssi,
        advertisementData: BaseAdvertisementData = this.advertisementData,
        scanCompleted: EmptyCompletableDeferred? = null
    ) {

        val bluetooth = this.bluetooth

        launch {

            bluetooth.scanningStateRepo.filter {
                it is ScanningState.Initialized.Enabled.Scanning
            }.take(1).collect {
                bluetooth.scanningStateRepo.takeAndChangeState(ScanningState.Initialized.Enabled.Scanning::class) { state ->
                    state.discoverDevice(
                        deviceWrapper.identifier,
                        rssi,
                        advertisementData
                    ) { device }
                }
                scanCompleted?.complete()
            }
        }
    }

    internal suspend fun connectDevice(device: Device = this.device) {
        connectionManager.reset()
        val bluetooth = bluetooth
        val connectingJob = async {
            bluetooth.devices()[device.identifier].connect()
        }
        connectionManager.connectCompleted.get().await()
        connectionManager.handleConnect()
        connectingJob.await()
    }

    internal suspend fun disconnectDevice(device: Device) {
        connectionManager.reset()
        val bluetooth = bluetooth
        val disconnectingJob = async {
            bluetooth.devices()[device.identifier].disconnect()
        }
        connectionManager.disconnectCompleted.get().await()
        connectionManager.handleDisconnect()
        disconnectingJob.await()
    }

    protected suspend fun discoverService(service: Service = this.service, device: Device = this.device) {
        device.filter { it is DeviceState.Connected.Discovering }.first()
        connectionManager.handleScanCompleted(listOf(service))
    }

    protected fun createDevice(
        deviceWrapper: DeviceWrapper,
        rssi: Int = this.rssi,
        advertisementData: BaseAdvertisementData = this.advertisementData
    ): Device {
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
