package com.splendo.kaluga.example.ios.bluetooth

import com.splendo.kaluga.base.MainQueueDispatcher
import com.splendo.kaluga.base.utils.toHexString
import com.splendo.kaluga.bluetooth.*
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.bluetooth.device.DeviceInfoImpl
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first

class KNBluetoothFramework {

    val mainScope = MainScope()
    val bluetooth = BluetoothBuilder().create()

    fun isScanning(onResult: (Boolean) -> Unit) {
        mainScope.launch(MainQueueDispatcher) {
            bluetooth.isScanning().collect { isScanning ->
                onResult(isScanning)
            }
        }
    }

    fun startScanning(onDone: () -> Unit ) {
        mainScope.launch(MainQueueDispatcher) {
            bluetooth.startScanning()
            onDone()
        }
    }

    fun stopScanning(onDone: () -> Unit ) {
        mainScope.launch(MainQueueDispatcher) {
            bluetooth.stopScanning()
            onDone()
        }
    }

    fun devices(onChange: (List<Device>) -> Unit) {
        mainScope.launch(MainQueueDispatcher) {
            bluetooth.devices().collect{ devices ->
                onChange(devices)
            }
        }
    }

    fun deviceState(forIdentifier: Identifier, onChange: (DeviceState) -> Unit): Job {
        return mainScope.launch(MainQueueDispatcher) {
            bluetooth.devices()[forIdentifier].state().collect { deviceState ->
                onChange(deviceState)
            }
        }
    }

    fun info(forIdentifier: Identifier, onChange: (DeviceInfoImpl) -> Unit): Job {
        return mainScope.launch(MainQueueDispatcher) {
            bluetooth.devices()[forIdentifier].info().collect { info ->
                onChange(info)
            }
        }
    }

    fun rssi(forIdentifier: Identifier, onChange: (Int) -> Unit): Job {
        return mainScope.launch(MainQueueDispatcher) {
            bluetooth.devices()[forIdentifier].rssi().collect { rssi ->
                onChange(rssi)
            }
        }
    }

    fun distance(forIdentifier: Identifier, onChange: (Double) -> Unit): Job {
        return mainScope.launch(MainQueueDispatcher) {
            bluetooth.devices()[forIdentifier].distance().collect { distance ->
                onChange(distance)
            }
        }
    }

    fun services(forIdentifier: Identifier, onChange: (List<Service>) -> Unit): Job {
        return mainScope.launch(MainQueueDispatcher) {
            bluetooth.devices()[forIdentifier].services().collect { services ->
                onChange(services)
            }
        }
    }

    fun serviceIdentifier(forService: Service): String {
        return forService.uuid.UUIDString
    }

    fun characteristics(forDeviceIdentifier: Identifier, andService: Service, onChange: (List<Characteristic>) -> Unit): Job {
        return mainScope.launch(MainQueueDispatcher) {
            bluetooth.devices()[forDeviceIdentifier].services()[andService.uuid].characteristics().collect { characteristics ->
                onChange(characteristics)
            }
        }
    }

    fun characteristicIdentifier(forCharacteristic: Characteristic): String {
        return forCharacteristic.uuid.UUIDString
    }

    fun characteristicValue(forDeviceIdentifier: Identifier, service: Service, andCharacteristic: Characteristic, onChange: (String?) -> Unit): Job {
        return mainScope.launch(MainQueueDispatcher) {
            val flow = bluetooth.devices()[forDeviceIdentifier].services()[service.uuid].characteristics()[andCharacteristic.uuid]
            flow.first()?.readValue()
            flow.value().collect { value ->
                onChange(value?.toHexString())
            }
        }
    }

    fun descriptors(forDeviceIdentifier: Identifier, service: Service, andCharacteristic: Characteristic, onChange: (List<Descriptor>) -> Unit): Job {
        return mainScope.launch(MainQueueDispatcher) {
            bluetooth.devices()[forDeviceIdentifier].services()[service.uuid].characteristics()[andCharacteristic.uuid].descriptors().collect { descriptors ->
                onChange(descriptors)
            }
        }
    }

    fun descriptorIdentifier(forDescriptor: Descriptor): String {
        return forDescriptor.uuid.UUIDString
    }

    fun desciptorValue(forDeviceIdentifier: Identifier, service: Service, characteristic: Characteristic, andDescriptor: Descriptor, onChange: (String?) -> Unit): Job {
        return mainScope.launch(MainQueueDispatcher) {
            val flow = bluetooth.devices()[forDeviceIdentifier].services()[service.uuid].characteristics()[characteristic.uuid].descriptors()[andDescriptor.uuid]
            flow.first()?.readValue()
            flow.value().collect { value ->
                onChange(value?.toHexString())
            }
        }
    }

    fun isDisconnectedOrDisconnecting(deviceState: DeviceState): Boolean {
        return when (deviceState) {
            is DeviceState.Disconnected, is DeviceState.Disconnecting -> true
            else -> false
        }
    }

    fun isConnected(deviceState: DeviceState): Boolean {
        return deviceState is DeviceState.Connected
    }

    fun connectionStateKey(deviceState: DeviceState): String {
        return when(deviceState) {
            is DeviceState.Disconnecting -> "bluetooth_disconneting"
            is DeviceState.Disconnected -> "bluetooth_disconnected"
            is DeviceState.Connected -> "bluetooth_connected"
            is DeviceState.Connecting -> "bluetooth_connecting"
            is DeviceState.Reconnecting -> "bluetooth_reconnecting"
        }
    }

    fun connect(identifier: Identifier) {
        mainScope.launch(MainQueueDispatcher) {
            bluetooth.devices()[identifier].connect()
        }
    }

    fun disconnect(identifier: Identifier) {
        mainScope.launch(MainQueueDispatcher) {
            bluetooth.devices()[identifier].disconnect()
        }
    }

    fun hexString(data: ByteArray?): String? {
        return data?.toHexString()
    }

    fun serviceUUIDString(deviceState: DeviceState): String {
        val uuids = deviceState.advertisementData.serviceUUIDs
        return uuids.fold("") { result, next ->
            if (result.isEmpty())
                next.toString()
            else
                "$result, $next"
        }
    }

    fun serviceDataString(deviceState: DeviceState): String {
        val data = deviceState.advertisementData.serviceData
        return data.entries.fold("") { result, next ->
            val nextString = "${next.key}: ${next.value?.toHexString() ?: ""}"
            if (result.isEmpty())
                nextString
            else
                "$result\n$nextString"
        }
    }

}