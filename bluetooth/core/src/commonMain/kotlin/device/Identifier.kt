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

package com.splendo.kaluga.bluetooth.device

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Unique identifier of a Bluetooth [Device]
 */
expect class Identifier

/**
 * Gets a random [Identifier]
 * @return a random [Identifier]
 */
expect fun randomIdentifier(): Identifier

/**
 * Gets an [Identifier] from a string value
 * @param stringValue the string value to get the [Identifier] from
 * @return an [Identifier] matching the string value or `null` if it could not be generated
 */
expect fun identifierFromString(stringValue: String): Identifier?

/**
 * Gets a string representation of an [Identifier]
 */
expect val Identifier.stringValue: String

/**
 * A [Identifier] that can be serialized
 */
@Serializable(with = IdentifierSerializer::class)
data class SerializableIdentifier(val identifier: Identifier)

/**
 * Converts an [Identifier] into a [SerializableIdentifier]
 */
val Identifier.serializable get() = SerializableIdentifier(this)

/**
 * [KSerializer] for a [SerializableIdentifier]
 */
open class IdentifierSerializer : KSerializer<SerializableIdentifier> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("IdentifierString", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: SerializableIdentifier) {
        val string = value.identifier.stringValue
        encoder.encodeString(string)
    }

    override fun deserialize(decoder: Decoder): SerializableIdentifier {
        val string = decoder.decodeString()
        val identifier = identifierFromString(string)
            ?: throw SerializationException("'$string' is not a valid Bluetooth Identifier")
        return SerializableIdentifier(identifier)
    }
}
