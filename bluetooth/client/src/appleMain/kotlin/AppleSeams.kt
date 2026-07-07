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
import platform.CoreBluetooth.CBPeripheral
import platform.Foundation.NSData
import platform.Foundation.NSError
import platform.Foundation.NSString

// CoreBluetooth state/property/write-type enums, NSError.code and NSString encodings are backed by
// NSInteger/NSUInteger, which are 64-bit on most Apple targets but 32-bit on watchosArm64 (arm64_32).
// Referencing them from shared appleMain fails compileAppleMainKotlinMetadata, so every touch of them is
// isolated behind these seams, with per-pointer-width implementations in apple64BitMain and watchosArm64Main.

/**
 * Whether this [CBCentralManager] is currently powered on.
 */
internal expect fun CBCentralManager.isPoweredOn(): Boolean

/**
 * The [DeviceConnectionManager.State] corresponding to this [CBPeripheral]'s current connection state.
 */
internal expect fun CBPeripheral.currentConnectionState(): DeviceConnectionManager.State

/**
 * This [NSError]'s code as an [Int].
 */
internal expect val NSError.gattCode: Int

/**
 * This [CBCharacteristic]'s property bitmask as an [Int].
 */
internal expect fun CBCharacteristic.propertyBits(): Int

/**
 * Writes [value] to [characteristic] on this [CBPeripheral], with or without a response.
 */
internal expect fun CBPeripheral.writeCharacteristicValue(value: NSData, characteristic: CBCharacteristic, withResponse: Boolean)

/**
 * This [NSString] encoded as UTF-8 [NSData].
 */
internal expect fun NSString.dataUsingUtf8(): NSData?
