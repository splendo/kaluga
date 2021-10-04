/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.bluetooth.uart

import com.splendo.kaluga.bluetooth.advertisement
import com.splendo.kaluga.bluetooth.characteristics
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.bluetooth.get
import com.splendo.kaluga.bluetooth.services
import com.splendo.kaluga.bluetooth.uuidFrom
import com.splendo.kaluga.logging.debug
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch

interface BluetoothUARTInterface {
    /** Represents data (as [Flow] of [ByteArray]?) received from TX Characteristic */
    val received: Flow<ByteArray?>
    /** Writes data (as [ByteArray]) to RX Characteristic */
    suspend fun send(data: ByteArray): Boolean
}

/** Returns [Flow] of [Boolean] set to `true` if [Device] has UART Service */
fun Flow<Device?>.hasUART() = advertisement()
    .mapLatest {
        it.serviceUUIDs.contains(uuidFrom(BluetoothUART.UART_SERVICE_UUID))
    }

/** Returns [Flow] of UART Service */
fun Flow<Device?>.uartService() = services()[uuidFrom(BluetoothUART.UART_SERVICE_UUID)]
/** Returns [Flow] of UART Service */
fun Flow<DeviceState>.uartService() = transformLatest {
    emit(
        when (it) {
            is DeviceState.Connected.Idle -> it.services.firstOrNull { service ->
                service.uuid == uuidFrom(BluetoothUART.UART_SERVICE_UUID)
            }
            else -> null
        }
    )
}
/** Returns [Flow] of UART RX Characteristic */
fun Flow<Device?>.rxCharacteristic() = uartService()
    .characteristics()[uuidFrom(BluetoothUART.UART_RX_UUID)]
/** Returns [Flow] of UART RX Characteristic */
fun Flow<DeviceState>.rxCharacteristic() = uartService()
    .characteristics()[uuidFrom(BluetoothUART.UART_RX_UUID)]
/** Returns [Flow] of UART TX Characteristic */
fun Flow<Device?>.txCharacteristic() = uartService()
    .characteristics()[uuidFrom(BluetoothUART.UART_TX_UUID)]
/** Returns [Flow] of UART TX Characteristic */
fun Flow<DeviceState>.txCharacteristic() = uartService()
    .characteristics()[uuidFrom(BluetoothUART.UART_TX_UUID)]

class BluetoothUART(
    private val deviceStateFlow: Flow<DeviceState>
) : BluetoothUARTInterface {

    companion object {
        /** UART Service UUID */
        const val UART_SERVICE_UUID = "6E400001-B5A3-F393-E0A9-E50E24DCCA9E"
        /** UART RX UUID (Write and Write Without Response) */
        const val UART_RX_UUID = "6E400002-B5A3-F393-E0A9-E50E24DCCA9E"
        /** UART TX UUID (Notify) */
        const val UART_TX_UUID = "6E400003-B5A3-F393-E0A9-E50E24DCCA9E"

        private const val TAG = "BluetoothUART"
    }

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Default + job)

    init {
        coroutineScope.launch {
            deviceStateFlow.collectLatest {
                when (it) {
                    is DeviceState.Connected.Idle -> if (!setupTxNotifications()) {
                        debug(TAG, "Can't setup Tx notifications!")
                    }
                    else -> {}
                }
            }
        }
    }

    override val received = deviceStateFlow
        .txCharacteristic()
        .filterNotNull()
        .flatMapLatest { it }

    override suspend fun send(data: ByteArray) = writeRxCharacteristic(data)

    private suspend fun writeRxCharacteristic(data: ByteArray): Boolean {
        val rx = deviceStateFlow.rxCharacteristic().firstOrNull() ?: return false
        return rx.writeValue(data).completedSuccessfully.await()
    }

    private suspend fun setupTxNotifications(): Boolean {
        val tx = deviceStateFlow.txCharacteristic().firstOrNull() ?: return false
        return tx.enableNotification()?.completedSuccessfully?.await() ?: true
    }
}
