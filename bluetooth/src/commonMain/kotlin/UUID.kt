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

@file:JvmName("CommonUUID")
package com.splendo.kaluga.bluetooth

import com.splendo.kaluga.base.text.format
import kotlin.jvm.JvmName

private object Constants {
    val validationRegex = "/^[0-9a-f]{8}-[0-9a-f]{4}-[0-5][0-9a-f]{3}-[089ab][0-9a-f]{3}-[0-9a-f]{12}$/i".toRegex()
    const val baseBluetoothUUID = "0000%s-0000-1000-8000-00805f9b34fb"
}

expect class UUID

expect val UUID.uuidString: String

@Throws(UUIDException::class)
fun uuidFrom(uuidString : String): UUID =
    if(uuidString.isValidUUIDString()) unsafeUUIDFrom(uuidString)
    else throw UUIDException.InvalidFormat(uuidString)

/**
 * meant for internal usage. It takes string which already passed validation
 */
internal expect fun unsafeUUIDFrom(uuidString:String): UUID

expect fun randomUUID():UUID

/**
 * The UUID format is the same for all services and characteristics and defined in bluetooth specification.
 *
 * @see <a href="https://www.bluetooth.com/specifications/gatt/characteristics/">GATT Characteristics specification</a>
 * @see <a href="https://btprodspecificationrefs.blob.core.windows.net/assigned-numbers/Assigned%20Number%20Types/Service%20Discovery.pdf">Service discovery</a>
 */
fun uuidFromShort(uuidString: String): UUID = uuidFrom(Constants.baseBluetoothUUID.format(uuidString))

internal fun String.isShortUUID() = length == 4

internal fun String.isValidUUIDString(): Boolean = Constants.validationRegex.matches(this)

sealed class UUIDException: Exception() {
    class InvalidFormat(uuidString:String) : Exception("'String $uuidString does not represent a valid UUID'")
}