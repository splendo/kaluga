/*
 Copyright 2024 Splendo Consulting B.V. The Netherlands

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

/**
 * The manner in which a value is written to a [RemoteCharacteristic].
 * A characteristic may support either or both; use this to choose explicitly when both are available.
 */
enum class WriteType {

    /**
     * The write is acknowledged by the peripheral. Requires the [CharacteristicProperty.Write] or [CharacteristicProperty.SignedWrite] property.
     */
    WithResponse,

    /**
     * The write is not acknowledged by the peripheral. Requires the [CharacteristicProperty.WriteWithoutResponse] property.
     */
    WithoutResponse,
}
