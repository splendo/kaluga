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

import com.splendo.kaluga.bluetooth.advertisement
import com.splendo.kaluga.bluetooth.characteristics
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.DeviceState
import com.splendo.kaluga.bluetooth.get
import com.splendo.kaluga.bluetooth.services
import com.splendo.kaluga.bluetooth.state
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
import kotlinx.coroutines.launch

interface BluetoothUARTService {
    val received: Flow<ByteArray?>
    suspend fun send(data: ByteArray): Boolean
}

fun Flow<Device?>.hasUART() = advertisement()
    .mapLatest {
        it.serviceUUIDs.contains(uuidFrom(BluetoothUART.UART_SERVICE_UUID))
    }

fun Flow<Device?>.uartService() = services()[uuidFrom(BluetoothUART.UART_SERVICE_UUID)]
fun Flow<Device?>.rxCharacteristic() = uartService().characteristics()[uuidFrom(BluetoothUART.UART_RX_UUID)]
fun Flow<Device?>.txCharacteristic() = uartService().characteristics()[uuidFrom(BluetoothUART.UART_TX_UUID)]

class BluetoothUART(
    private val device: Flow<Device>
) : BluetoothUARTService {

    companion object {
        const val UART_SERVICE_UUID = "6E400001-B5A3-F393-E0A9-E50E24DCCA9E"
        const val UART_RX_UUID = "6E400002-B5A3-F393-E0A9-E50E24DCCA9E"
        const val UART_TX_UUID = "6E400003-B5A3-F393-E0A9-E50E24DCCA9E"

        private const val TAG = "BluetoothUART"
    }

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Default + job)

    init {
        coroutineScope.launch {
            device.state().collectLatest {
                when (it) {
                    is DeviceState.Connected.Idle -> if(!setUpNotifications()) {
                        debug(TAG, "Can't setup notifications!")
                    }
                    else -> {}
                }
            }
        }
    }

    override val received = device
        .rxCharacteristic()
        .filterNotNull()
        .flatMapLatest { it }

    override suspend fun send(data: ByteArray): Boolean {
        val tx = device.txCharacteristic().firstOrNull() ?: return false
        return tx.writeValue(data).completedSuccessfully.await()
    }

    private suspend fun setUpNotifications(): Boolean {
        val rx = device.rxCharacteristic().firstOrNull() ?: return false
        val tx = device.txCharacteristic().firstOrNull() ?: return false

        val rxEnabled = rx.enableNotification()?.completedSuccessfully?.await() ?: true
        val txEnabled = tx.enableNotification()?.completedSuccessfully?.await() ?: true

        return rxEnabled && txEnabled
    }
}
