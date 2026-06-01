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

@file:JvmName("DeviceInfoAndroid")

package com.splendo.kaluga.bluetooth.device

import com.splendo.kaluga.bluetooth.randomUUIDString

/**
 * Unique identifier of a Bluetooth [Device]
 */
actual typealias Identifier = String

/**
 * Gets a random [Identifier]
 * @return a random [Identifier]
 */
actual fun randomIdentifier() = randomUUIDString()

/**
 * Gets an [Identifier] from a string value
 * @param stringValue the string value to get the [Identifier] from
 * @return an [Identifier] matching the string value or `null` if it could not be generated
 */
actual fun identifierFromString(stringValue: String): Identifier? = stringValue

/**
 * Gets a string representation of an [Identifier]
 */
actual val Identifier.stringValue: String
    get() = this
