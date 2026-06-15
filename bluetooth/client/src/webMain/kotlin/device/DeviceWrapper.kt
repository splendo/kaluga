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

package com.splendo.kaluga.bluetooth.device

/**
 * Accessor to a Web Bluetooth `BluetoothDevice`. The live JS device is held in the per-target
 * registry under [identifier]; this wrapper only carries its [identifier] and [name].
 */
actual interface DeviceWrapper {
    actual val name: String?
    actual val identifier: Identifier
}

/**
 * Default implementation of [DeviceWrapper] for the JS family.
 */
class WebDeviceWrapper(override val identifier: Identifier, override val name: String?) : DeviceWrapper
