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

package com.splendo.kaluga.bluetooth.externalsource

import com.splendo.kaluga.bluetooth.annotations.Advertising
import com.splendo.kaluga.bluetooth.annotations.Bluetooth
import com.splendo.kaluga.bluetooth.annotations.BluetoothCharacteristic
import com.splendo.kaluga.bluetooth.annotations.BluetoothService
import com.splendo.kaluga.bluetooth.annotations.Readable

// The consumer's own device. It composes the external ExternalService (whose symbols come from the provider module) with
// its own OwnService. The consumer module marks the external spec as externalAnnotationSource(), so ExternalService
// resolves for the reference below without its Remote*/Local* symbols being regenerated (which would clash at link time).

@Bluetooth
internal interface ComposedDevice {
    @Advertising
    val externalService: ExternalService

    val ownService: OwnService
}

@BluetoothService("e200")
internal interface OwnService {
    val ownCharacteristic: OwnCharacteristic
}

@BluetoothCharacteristic("e201")
internal interface OwnCharacteristic {
    @Readable
    val value: Int
}
