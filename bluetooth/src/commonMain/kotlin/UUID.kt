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
@file:Suppress("EXTENSION_SHADOWED_BY_MEMBER")

package com.splendo.kaluga.bluetooth

import com.splendo.kaluga.base.text.format
import kotlin.jvm.JvmName

private object Constants {
    val formatValidationRegex = Regex(
        "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}|[0-9a-f]{4}",
        RegexOption.IGNORE_CASE,
    )
    const val BASE_BLUETOOTH_UUID = "0000%s-0000-1000-8000-00805f9b34fb"
}

/**
 * An [Exception] dealing with issues to the Unique identifier
 * @param message the message of the exception
 */
sealed class UUIDException(message: String?) : Exception(message) {

    /**
     * A [UUIDException] thrown when [uuidFrom] cannot convert a string to a [UUID]
     * @param uuidString the string that could not be converted to a [UUID]
     */
    class InvalidFormat(uuidString: String) :
        UUIDException("String '$uuidString' does not represent a valid UUID")
}

/**
 * The Unique Identifier of a Bluetooth property
 */
expect class UUID

/**
 * The string representation of a [UUID]
 */
expect val UUID.uuidString: String

/**
 * Gets the [UUID] from a given string
 * @param uuidString the string to converter to a [UUID]
 * @return the [UUID] associated with the string
 * @throws [UUIDException] if [uuidString] is not a valid [UUID]
 */
@Throws(UUIDException::class)
fun uuidFrom(uuidString: String): UUID = if (uuidString.isValidUUIDString()) unsafeUUIDFrom(uuidString) else throw UUIDException.InvalidFormat(uuidString)

/**
 * Gets a random [UUID]
 * @return a random [UUID]
 */
expect fun randomUUID(): UUID

/**
 * The UUID format is the same for all services and characteristics and defined in bluetooth specification.
 *
 * @see <a href="https://www.bluetooth.com/specifications/gatt/characteristics/">GATT Characteristics specification</a>
 * @see <a href="https://btprodspecificationrefs.blob.core.windows.net/assigned-numbers/Assigned%20Number%20Types/Service%20Discovery.pdf">Service discovery</a>
 */
internal fun uuidFromShort(uuidString: String): UUID = uuidFrom(Constants.BASE_BLUETOOTH_UUID.format(uuidString))

/**
 * Meant for internal usage. It takes string which already passed validation
 */
internal expect fun unsafeUUIDFrom(uuidString: String): UUID

internal fun String.isShortUUID() = length == 4

internal fun String.isValidUUIDString(): Boolean = Constants.formatValidationRegex.matches(this)

/**
 * This function can be used to generate UUID on platforms which don't support it out of the box
 */
internal fun randomUUIDString(): String {
    val alphabet = ('A'..'F') + ('0'..'9')
    fun randomBlock(size: Int) = List(size) { alphabet.random() }.joinToString("")

    return "${randomBlock(8)}-${randomBlock(4)}-${randomBlock(4)}-${randomBlock(4)}-${randomBlock(12)}"
}
