package com.splendo.kaluga.example.ios.bluetooth

import com.splendo.kaluga.base.MainQueueDispatcher
import com.splendo.kaluga.bluetooth.*
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.bluetooth.device.Identifier
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.collect
import com.splendo.kaluga.base.utils.toHexString

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

    fun deviceState(forDevice: Device, onChange: (DeviceState) -> Unit): Job {
        return mainScope.launch(MainQueueDispatcher) {
            forDevice.flow()?.collect { deviceState ->
                onChange(deviceState)
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
                "$result/n$nextString"
        }
    }

}