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

import platform.CoreBluetooth.CBCentral
import platform.CoreBluetooth.CBCharacteristicWriteWithResponse
import platform.CoreBluetooth.CBPeripheral

// maximumUpdateValueLength / maximumWriteValueLengthForType return NSUInteger, which is ULong on the
// 64-bit Apple targets but UInt on watchosArm64 (arm64_32). Referencing them from shared appleMain fails
// compileAppleMainKotlinMetadata, so this lives in apple64BitMain with a parallel watchosArm64Main copy.

// The 3-byte ATT header. CoreBluetooth exposes the usable payload length; the ATT MTU is that plus this header.
private const val ATT_HEADER_SIZE = 3

/**
 * The ATT [MTU] negotiated with this [CBCentral], derived from its maximum notification payload length.
 */
val CBCentral.mtu: MTU get() = maximumUpdateValueLength.toInt() + ATT_HEADER_SIZE

/**
 * The ATT [MTU] negotiated with this [CBPeripheral], derived from its maximum write payload length.
 */
val CBPeripheral.mtu: MTU get() = maximumWriteValueLengthForType(CBCharacteristicWriteWithResponse).toInt() + ATT_HEADER_SIZE
