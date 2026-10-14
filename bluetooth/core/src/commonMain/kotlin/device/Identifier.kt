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

import com.splendo.kaluga.base.bytes.sha1
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
 * Generates a UUID v5 [Identifier] (RFC 4122) from [namespace] and [name].
 *
 * Uses SHA-1 over `namespace + name` bytes, then stamps version 5 and the RFC 4122
 * variant bits into the first 16 bytes of the digest, producing a standard UUID v5.
 *
 * [namespace] scopes the result so the same [name] in different applications does not
 * collide. Choose a fixed, application-specific byte sequence and never change it,
 * or existing identifiers will shift.
 *
 * The same namespace + name always produces the same identifier across sessions.
 */
fun nameBasedIdentifier(namespace: ByteArray, name: String): Identifier {
    val identifier = namespace + name.encodeToByteArray()
    val hash = identifier.sha1()
    val bytes = hash.copyOf(16)
    bytes[6] = ((bytes[6].toInt() and 0x0F) or 0x50).toByte() // version 5
    bytes[8] = ((bytes[8].toInt() and 0x3F) or 0x80).toByte() // RFC 4122 variant
    val uuid = buildString {
        for (i in 0..15) {
            if (i in intArrayOf(4, 6, 8, 10)) append('-')
            append((bytes[i].toInt() and 0xFF).toString(16).padStart(2, '0'))
        }
    }
    return requireNotNull(identifierFromString(uuid)) { "Could not create identifier from '$uuid'" }
}

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
