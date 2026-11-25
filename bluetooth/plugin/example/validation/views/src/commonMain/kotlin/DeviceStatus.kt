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
package com.splendo.kaluga.bluetooth.views

import com.splendo.kaluga.bluetooth.annotations.Advertising
import com.splendo.kaluga.bluetooth.annotations.Bluetooth
import com.splendo.kaluga.bluetooth.annotations.BluetoothCharacteristic
import com.splendo.kaluga.bluetooth.annotations.BluetoothService
import com.splendo.kaluga.bluetooth.annotations.Notifiable
import com.splendo.kaluga.bluetooth.annotations.Readable
import com.splendo.kaluga.bluetooth.annotations.Writable
import com.splendo.kaluga.bytes.annotations.BytesLayout
import com.splendo.kaluga.bytes.annotations.Length
import com.splendo.kaluga.bytes.annotations.Size
import com.splendo.kaluga.bytes.annotations.Trailing
import com.splendo.kaluga.bytes.annotations.Unsigned

/** A status value a peripheral may send without its trailing fields, for example on a protocol version that did not have them yet. */
@BytesLayout
interface DeviceStatus {
    @Size(Length.`8_BIT`)
    @Unsigned
    val battery: Int

    val voltage: UShort

    @Trailing
    val serviceVersion: UShort?

    @Trailing
    val serial: ULong?
}

@Bluetooth
interface StatusDevice {
    @Advertising
    val statusService: StatusService
}

@BluetoothService("fff0")
interface StatusService {
    val status: StatusCharacteristic
}

@BluetoothCharacteristic("fff1")
interface StatusCharacteristic {
    @Readable
    @Writable
    @Notifiable
    val status: DeviceStatus
}
