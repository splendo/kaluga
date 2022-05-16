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

import com.splendo.kaluga.bluetooth.device.BaseAdvertisementData
import com.splendo.kaluga.bluetooth.device.ConnectionSettings
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.DeviceInfoImpl
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.bluetooth.device.DeviceWrapper
import com.splendo.kaluga.bluetooth.scanner.ScanningState
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.permissions.bluetooth.BluetoothPermission
import com.splendo.kaluga.test.BaseFlowTest
import com.splendo.kaluga.test.bluetooth.ServiceWrapperBuilder
import com.splendo.kaluga.test.bluetooth.characteristic
import com.splendo.kaluga.test.bluetooth.createDeviceWrapper
import com.splendo.kaluga.test.bluetooth.createServiceWrapper
import com.splendo.kaluga.test.bluetooth.descriptor
import com.splendo.kaluga.test.bluetooth.device.MockAdvertisementData
import com.splendo.kaluga.test.bluetooth.device.MockDeviceConnectionManager
import com.splendo.kaluga.test.bluetooth.scanner.MockScanner
import com.splendo.kaluga.test.mock.on
import com.splendo.kaluga.test.permissions.MockPermissionManager
import com.splendo.kaluga.test.permissions.MockPermissionsBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

abstract class BluetoothFlowTest<CONF : BluetoothFlowTest.Configuration, C : BluetoothFlowTest.Context<CONF>, T> : BaseFlowTest<CONF, C, T, Flow<T>>() {

    companion object {
        fun defaultService(): ServiceWrapperBuilder.() -> Unit = {
            uuid = randomUUID()
            characteristics {
                characteristic {
                    uuid = randomUUID()
                    properties = 0
                    descriptors {
                        descriptor(uuid = randomUUID())
                    }
                }
            }
        }
    }

    sealed class Configuration {
        abstract val autoRequestPermission: Boolean
        abstract val autoEnableBluetooth: Boolean
        abstract val isEnabled: Boolean
        abstract val initialPermissionState: MockPermissionManager.MockPermissionState
        abstract val serviceWrapperBuilder: ServiceWrapperBuilder.() -> Unit

        data class Bluetooth(
            override val autoRequestPermission: Boolean = true,
            override val autoEnableBluetooth: Boolean = true,
            override val isEnabled: Boolean = true,
            override val initialPermissionState: MockPermissionManager.MockPermissionState = MockPermissionManager.MockPermissionState.ALLOWED,
            override val serviceWrapperBuilder: ServiceWrapperBuilder.() -> Unit = defaultService()
        ) : Configuration()

        sealed class Device : Configuration() {
            abstract val connectionSettings: ConnectionSettings
            abstract val willActionsSucceed: Boolean
            abstract val rssi: Int
            abstract val advertisementData: BaseAdvertisementData
        }

        interface Service
        interface Characteristic : Service
        interface Descriptor : Characteristic
        data class DeviceWithoutService(
            override val connectionSettings: ConnectionSettings = ConnectionSettings(),
            override val willActionsSucceed: Boolean = true,
            override val autoRequestPermission: Boolean = true,
            override val autoEnableBluetooth: Boolean = true,
            override val isEnabled: Boolean = true,
            override val initialPermissionState: MockPermissionManager.MockPermissionState = MockPermissionManager.MockPermissionState.ALLOWED,
            override val rssi: Int = -100,
            override val advertisementData: BaseAdvertisementData = MockAdvertisementData(name = "Name"),
            override val serviceWrapperBuilder: ServiceWrapperBuilder.() -> Unit = defaultService()
        ) : Device()

        data class DeviceWithService(
            override val connectionSettings: ConnectionSettings = ConnectionSettings(),
            override val willActionsSucceed: Boolean = true,
            override val autoRequestPermission: Boolean = true,
            override val autoEnableBluetooth: Boolean = true,
            override val isEnabled: Boolean = true,
            override val initialPermissionState: MockPermissionManager.MockPermissionState = MockPermissionManager.MockPermissionState.ALLOWED,
            override val rssi: Int = -100,
            override val advertisementData: BaseAdvertisementData = MockAdvertisementData(name = "Name"),
            override val serviceWrapperBuilder: ServiceWrapperBuilder.() -> Unit = defaultService()
        ) : Device(), Service

        data class DeviceWithCharacteristic(
            override val connectionSettings: ConnectionSettings = ConnectionSettings(),
            override val willActionsSucceed: Boolean = true,
            override val autoRequestPermission: Boolean = true,
            override val autoEnableBluetooth: Boolean = true,
            override val isEnabled: Boolean = true,
            override val initialPermissionState: MockPermissionManager.MockPermissionState = MockPermissionManager.MockPermissionState.ALLOWED,
            override val rssi: Int = -100,
            override val advertisementData: BaseAdvertisementData = MockAdvertisementData(name = "Name"),
            override val serviceWrapperBuilder: ServiceWrapperBuilder.() -> Unit = defaultService()
        ) : Device(), Characteristic

        data class DeviceWithDescriptor(
            override val connectionSettings: ConnectionSettings = ConnectionSettings(),
            override val willActionsSucceed: Boolean = true,
            override val autoRequestPermission: Boolean = true,
            override val autoEnableBluetooth: Boolean = true,
            override val isEnabled: Boolean = true,
            override val initialPermissionState: MockPermissionManager.MockPermissionState = MockPermissionManager.MockPermissionState.ALLOWED,
            override val rssi: Int = -100,
            override val advertisementData: BaseAdvertisementData = MockAdvertisementData(name = "Name"),
            override val serviceWrapperBuilder: ServiceWrapperBuilder.() -> Unit = defaultService()
        ) : Device(), Descriptor
    }

    abstract class Context<C : Configuration>(
        val configuration: C,
        val coroutineScope: CoroutineScope
    ) : TestContext {

        val deviceFilter = setOf(randomUUID())

        val permissionsBuilder: MockPermissionsBuilder = MockPermissionsBuilder(
            initialPermissionState = configuration.initialPermissionState
        ).apply {
            registerAllPermissionsBuilders()
        }
        val permissions = Permissions(
            permissionsBuilder,
            coroutineContext = coroutineScope.coroutineContext
        ).apply {
            // Make sure permissionState has been created as it may break the tests otherwise
            get(BluetoothPermission)
        }

        val permissionManager get() = permissionsBuilder.createdBluetoothPermissionManagers.first()

        private val scannerBuilder = MockScanner.Builder(configuration.isEnabled)
        val scanner get() = scannerBuilder.createdScanners.first()

        val bluetooth = Bluetooth(permissions, ConnectionSettings(), configuration.autoRequestPermission, configuration.autoEnableBluetooth, scannerBuilder, coroutineScope)
        val scanningStateRepo = bluetooth.scanningStateRepo

        val serviceWrapper = createServiceWrapper(configuration.serviceWrapperBuilder)

        fun createDevice(
            connectionSettings: ConnectionSettings,
            deviceWrapper: DeviceWrapper,
            rssi: Int,
            advertisementData: BaseAdvertisementData,
            deviceConnectionManagerBuilder: MockDeviceConnectionManager.Builder = MockDeviceConnectionManager.Builder()
        ): Device {
            return Device(
                connectionSettings,
                DeviceInfoImpl(deviceWrapper, rssi, advertisementData),
                deviceConnectionManagerBuilder,
                coroutineScope.coroutineContext
            )
        }

        fun scanDevice(
            device: Device,
            deviceWrapper: DeviceWrapper,
            rssi: Int,
            advertisementData: BaseAdvertisementData
        ) {
            coroutineScope.launch {
                bluetooth.scanningStateRepo.filter {
                    it is ScanningState.Enabled.Scanning
                }.first()
                bluetooth.scanningStateRepo.takeAndChangeState(ScanningState.Enabled.Scanning::class) { state ->
                    state.discoverDevice(
                        deviceWrapper.identifier,
                        rssi,
                        advertisementData
                    ) { device }
                }
            }
        }

        suspend fun connectDevice(device: Device, connectionManager: MockDeviceConnectionManager) {
            connectionManager.connectMock.on().doExecuteSuspended {
                connectionManager.handleConnect()
            }
            bluetooth.devices()[device.identifier].connect()
        }

        suspend fun disconnectDevice(device: Device, connectionManager: MockDeviceConnectionManager) {
            connectionManager.disconnectMock.on().doExecuteSuspended {
                connectionManager.handleDisconnect()
            }
            bluetooth.devices()[device.identifier].disconnect()
        }

        suspend fun discoverService(service: Service, device: Device, connectionManager: MockDeviceConnectionManager) {
            device.filter { it is DeviceState.Connected.Discovering }.first()
            connectionManager.handleScanCompleted(listOf(service))
        }
    }

    class BluetoothContext(configuration: Configuration.Bluetooth, coroutineScope: CoroutineScope) : Context<Configuration.Bluetooth>(configuration, coroutineScope)
    sealed class BaseDeviceContext<C: Configuration.Device>(configuration: C, coroutineScope: CoroutineScope) : Context<C>(configuration, coroutineScope) {
        val deviceWrapper = createDeviceWrapper()
        val deviceConnectionManagerBuilder: MockDeviceConnectionManager.Builder = MockDeviceConnectionManager.Builder()
        val device = createDevice()
        val connectionManager get() = deviceConnectionManagerBuilder.createdDeviceConnectionManager.first()

        fun createDevice(
            deviceWrapper: DeviceWrapper = this.deviceWrapper,
            deviceConnectionManagerBuilder: MockDeviceConnectionManager.Builder = this.deviceConnectionManagerBuilder
        ) = createDevice(configuration.connectionSettings, deviceWrapper, configuration.rssi, configuration.advertisementData, deviceConnectionManagerBuilder)

        fun scanDevice(
            rssi: Int = configuration.rssi,
            advertisementData: BaseAdvertisementData = configuration.advertisementData
        ) = super.scanDevice(device, deviceWrapper, rssi, advertisementData)
        suspend fun connectDevice() = connectDevice(device, connectionManager)

    }
    class DeviceContext(configuration: Configuration.DeviceWithoutService, coroutineScope: CoroutineScope) : BaseDeviceContext<Configuration.DeviceWithoutService>(configuration, coroutineScope)
    sealed class BaseServiceContext<C>(configuration: C, coroutineScope: CoroutineScope) : BaseDeviceContext<C>(configuration, coroutineScope) where C : Configuration.Device, C : Configuration.Service {
        val service by lazy { Service(serviceWrapper, connectionManager.stateRepo) }
        suspend fun discoverService() =
            discoverService(service, device, connectionManager)
    }
    class ServiceContext(configuration: Configuration.DeviceWithService, coroutineScope: CoroutineScope) : BaseServiceContext<Configuration.DeviceWithService>(configuration, coroutineScope)
    sealed class BaseCharacteristicContext<C>(configuration: C, coroutineScope: CoroutineScope) : BaseServiceContext<C>(configuration, coroutineScope) where C : Configuration.Device, C : Configuration.Characteristic {
        val characteristic by lazy { service.characteristics.first() }
    }
    class CharacteristicContext(configuration: Configuration.DeviceWithCharacteristic, coroutineScope: CoroutineScope) : BaseCharacteristicContext<Configuration.DeviceWithCharacteristic>(configuration, coroutineScope)
    sealed class BaseDescriptorContext<C>(configuration: C, coroutineScope: CoroutineScope) : BaseCharacteristicContext<C>(configuration, coroutineScope) where C : Configuration.Device, C : Configuration.Descriptor {
        val descriptor by lazy { characteristic.descriptors.first() }
    }
    class DescriptorContext(configuration: Configuration.DeviceWithDescriptor, coroutineScope: CoroutineScope) : BaseDescriptorContext<Configuration.DeviceWithDescriptor>(configuration, coroutineScope)
}
