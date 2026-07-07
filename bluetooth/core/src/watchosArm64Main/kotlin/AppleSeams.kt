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
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBManagerStatePoweredOn
import platform.CoreBluetooth.CBPeripheral

actual val CBCentral.mtu: MTU get() = maximumUpdateValueLength.toInt() + ATT_HEADER_SIZE

actual val CBPeripheral.mtu: MTU get() = maximumWriteValueLengthForType(CBCharacteristicWriteWithResponse).toInt() + ATT_HEADER_SIZE

internal actual fun CBCentralManager.isPoweredOn(): Boolean = state == CBManagerStatePoweredOn
