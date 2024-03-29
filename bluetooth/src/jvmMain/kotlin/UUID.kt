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

@file:Suppress("EXTENSION_SHADOWED_BY_MEMBER")

package com.splendo.kaluga.bluetooth

/**
 * The Unique Identifier of a Bluetooth property
 * @property uuidString the String representation of the unique identifier
 */
actual data class UUID(val uuidString: String)

/**
 * The string representation of a [UUID]
 */
actual val UUID.uuidString: String
    get() = uuidString

internal actual fun unsafeUUIDFrom(uuidString: String): UUID = UUID(uuidString = uuidString)

/**
 * Gets a random [UUID]
 * @return a random [UUID]
 */
actual fun randomUUID(): UUID = UUID(randomUUIDString())
