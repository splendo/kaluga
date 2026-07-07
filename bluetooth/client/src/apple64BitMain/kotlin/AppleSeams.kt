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

package com.splendo.kaluga.bluetooth

import com.splendo.kaluga.bluetooth.device.DeviceConnectionManager
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCharacteristic
import platform.CoreBluetooth.CBCharacteristicWriteWithResponse
import platform.CoreBluetooth.CBCharacteristicWriteWithoutResponse
import platform.CoreBluetooth.CBManagerStatePoweredOn
import platform.CoreBluetooth.CBPeripheral
import platform.CoreBluetooth.CBPeripheralStateConnected
import platform.CoreBluetooth.CBPeripheralStateConnecting
import platform.CoreBluetooth.CBPeripheralStateDisconnected
import platform.CoreBluetooth.CBPeripheralStateDisconnecting
import platform.Foundation.NSData
import platform.Foundation.NSError
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.dataUsingEncoding

internal actual fun CBCentralManager.isPoweredOn(): Boolean = state == CBManagerStatePoweredOn

internal actual fun CBPeripheral.currentConnectionState(): DeviceConnectionManager.State = when (state) {
    CBPeripheralStateConnected -> DeviceConnectionManager.State.CONNECTED
    CBPeripheralStateConnecting -> DeviceConnectionManager.State.CONNECTING
    CBPeripheralStateDisconnected -> DeviceConnectionManager.State.DISCONNECTED
    CBPeripheralStateDisconnecting -> DeviceConnectionManager.State.DISCONNECTING
    else -> DeviceConnectionManager.State.DISCONNECTED
}

internal actual val NSError.gattCode: Int get() = code.toInt()

internal actual fun CBCharacteristic.propertyBits(): Int = properties.toInt()

internal actual fun CBPeripheral.writeCharacteristicValue(value: NSData, characteristic: CBCharacteristic, withResponse: Boolean) {
    val type = if (withResponse) CBCharacteristicWriteWithResponse else CBCharacteristicWriteWithoutResponse
    writeValue(value, characteristic, type)
}

internal actual fun NSString.dataUsingUtf8(): NSData? = dataUsingEncoding(NSUTF8StringEncoding)
