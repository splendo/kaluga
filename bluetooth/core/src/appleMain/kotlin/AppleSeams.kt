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
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBPeripheral

// CoreBluetooth state enums and the maximum*ValueLength accessors are backed by NSInteger/NSUInteger, which
// are 64-bit on most Apple targets but 32-bit on watchosArm64 (arm64_32). Referencing them from shared
// appleMain fails compileAppleMainKotlinMetadata, so every touch of them is isolated behind these seams,
// with per-pointer-width implementations in apple64BitMain and watchosArm64Main.

// The 3-byte ATT header. CoreBluetooth exposes the usable payload length; the ATT MTU is that plus this header.
internal const val ATT_HEADER_SIZE = 3

/**
 * The ATT [MTU] negotiated with this [CBCentral], derived from its maximum notification payload length.
 */
expect val CBCentral.mtu: MTU

/**
 * The ATT [MTU] negotiated with this [CBPeripheral], derived from its maximum write payload length.
 */
expect val CBPeripheral.mtu: MTU

/**
 * Whether this [CBCentralManager] is currently powered on.
 */
internal expect fun CBCentralManager.isPoweredOn(): Boolean
