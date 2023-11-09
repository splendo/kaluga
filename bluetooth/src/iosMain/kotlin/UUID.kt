/*
 Copyright (c) 2020. Splendo Consulting B.V. The Netherlands

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

import platform.CoreBluetooth.CBUUID
import platform.Foundation.NSUUID

/**
 * The Unique Identifier of a Bluetooth property
 */
actual typealias UUID = CBUUID

/**
 * The string representation of a [UUID]
 */
actual val UUID.uuidString: String
    get() = UUIDString

internal actual fun unsafeUUIDFrom(uuidString: String) = CBUUID.UUIDWithString(uuidString)

/**
 * Gets a random [UUID]
 * @return a random [UUID]
 */
actual fun randomUUID(): UUID {
    return CBUUID.UUIDWithNSUUID(NSUUID.UUID())
}
