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
import com.splendo.kaluga.bluetooth.annotations.Writable

// The "external" definitions. The provider module (:validation:external-source-provider) owns these and generates their
// Remote*/Local* symbols. A consumer references ExternalService via externalAnnotationSource(): its own generation
// resolves the type but skips regenerating these symbols, reusing the provider's instead.

@Bluetooth
internal interface ExternalDevice {
    @Advertising
    val externalService: ExternalService
}

@BluetoothService("e100")
internal interface ExternalService {
    val externalCharacteristic: ExternalCharacteristic
}

@BluetoothCharacteristic("e101")
internal interface ExternalCharacteristic {
    @Readable
    val level: Int

    @Writable
    val target: Int
}
