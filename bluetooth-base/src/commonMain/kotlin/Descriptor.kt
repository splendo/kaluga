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

/**
 * [Characteristic] descriptors are used to contain related information about the Characteristic Value.
 * The GATT profile defines a standard set of characteristic descriptors that can be used by higher layer profiles.
 * Higher layer profiles may define additional characteristic descriptors that are profile specific.
 */
interface Descriptor : Attribute {

    companion object {

        /**
         * The [UUID] used by the Client Characteristic Configuration Descriptor, which is required for enabling notifications.
         */
        val CLIENT_CHARACTERISTIC_CONFIGURATION_DESCRIPTOR: UUID = uuidFrom("2902")
    }

    /**
     * The [Characteristic] this descriptor belongs to
     */
    val characteristic: Characteristic
}
