package com.splendo.kaluga.example.ios.bluetooth

import com.splendo.kaluga.base.MainQueueDispatcher
import com.splendo.kaluga.bluetooth.*
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.DeviceState
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.collect

class KNBluetoothFramework {


    val mainScope = MainScope()
    val bluetooth = BluetoothBuilder().create()

    fun isScanning(onResult: (Boolean) -> Unit) {
        mainScope.launch(MainQueueDispatcher) {
            onResult(bluetooth.isScanning())
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

}