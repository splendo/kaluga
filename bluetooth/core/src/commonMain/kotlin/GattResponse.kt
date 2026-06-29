/*
 Copyright 2025 Splendo Consulting B.V. The Netherlands

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

import com.splendo.kaluga.bluetooth.serialization.BluetoothFormat
import kotlinx.serialization.SerializationStrategy

/**
 * Response given when performing a Bluetooth Gatt Request
 */
sealed interface GattResponse {

    /**
     * A [GattResponse] given to a Read request
     */
    sealed interface ReadResponse : GattResponse

    /**
     * A successful [GattResponse] indicating the request completed successfully
     */
    sealed interface Success : GattResponse {
        override val statusCode: Int get() = 0x00
    }

    /**
     * A [Success] [ReadResponse]
     * @property value the [ByteArray] read by the Read Request
     */
    data class ReadSuccess(val value: ByteArray) :
        ReadResponse,
        Success {

        companion object {
            /**
             * Generates a [GattResponse.ReadSuccess] response for a value [T]
             * @param T the type of the value read
             * @param value the value read
             * @param offset the offset of the ByteArray to read
             * @param serializationStrategy the [SerializationStrategy] used to encode [value]
             * @param bluetoothFormat the [BluetoothFormat] to use for encoding [value]
             * @throws kotlinx.serialization.SerializationException if [value] cannot be encoded using [serializationStrategy]
             * @return a [GattResponse.ReadSuccess] response where [GattResponse.ReadSuccess.value] can be deserialized by [bluetoothFormat] using a [kotlinx.serialization.DeserializationStrategy] similar to [serializationStrategy]
             */
            operator fun <T> invoke(value: T, offset: Int = 0, serializationStrategy: SerializationStrategy<T>, bluetoothFormat: BluetoothFormat = BluetoothFormat): ReadSuccess =
                ReadSuccess(bluetoothFormat.encodeToByteArray(serializationStrategy, value).drop(offset).toByteArray())

            /**
             * Generates a [GattResponse.ReadSuccess] response for a value [T]
             * @param T the type of the value read
             * @param value the value read
             * @param offset the offset of the ByteArray to read
             * @param bluetoothFormat the [BluetoothFormat] to use for encoding [value]
             * @throws kotlinx.serialization.SerializationException if [value] cannot be encoded
             * @return a [GattResponse.ReadSuccess] response where [GattResponse.ReadSuccess.value] can be deserialized by [bluetoothFormat]
             */
            inline operator fun <reified T : Any> invoke(value: T, offset: Int = 0, bluetoothFormat: BluetoothFormat = BluetoothFormat) =
                invoke(value, offset, bluetoothFormat.serializer<T>(), bluetoothFormat)
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) {
                return false
            }

            other as ReadSuccess

            return value.contentEquals(other.value)
        }

        override fun hashCode(): Int = value.contentHashCode()
    }

    /**
     * A [GattResponse] given to a Write request
     */
    sealed interface WriteResponse : GattResponse

    /**
     * A [Success] [WriteResponse].
     */
    sealed interface WriteSuccess :
        WriteResponse,
        Success {

        /**
         * The write was acknowledged or otherwise completed with full confidence: a with-response write
         * confirmed by the peripheral, a local GATT server reply, or a no-op where the desired state already held.
         */
        data object Acknowledged : WriteSuccess

        /**
         * A write-without-response handed to the OS while the peripheral could accept it (it was ready).
         * Not confirmed by the peripheral.
         */
        data object Ready : WriteSuccess

        /**
         * A write-without-response sent best-effort without the peripheral confirming it could accept it
         * (it never became ready within the timeout). It may have been dropped before transmission.
         */
        data object NotReady : WriteSuccess
    }

    /**
     * A [GattResponse] given to a MTU request
     * @property mtu the [MTU] of the connection
     */
    sealed interface MTUResponse : GattResponse {
        val mtu: MTU
    }

    /**
     * A [Success] [MTUResponse]
     * @property mtu the new [MTU] of the connection
     */
    data class MTUSuccess(override val mtu: MTU) :
        MTUResponse,
        Success

    /**
     * An error [MTUResponse]
     */
    sealed interface MTUError : MTUResponse

    /**
     * A [MTUError] indicating an [Error] occurred while performing the MTU request
     * @property mtu the [MTU] of the connection
     * @property error the [Error] that occurred
     */
    data class MTUFailure(override val mtu: MTU, val error: Error) : MTUError {
        override val statusCode: Int = error.statusCode
    }

    /**
     * A [MTUError] indicating the MTU request was not permitted
     * @property mtu the [MTU] of the connection
     */
    data class MTUNotPermitted(override val mtu: MTU) : MTUError {
        override val statusCode: Int = -1
    }

    /**
     * A [ReadResponse] that failed to read
     */
    sealed interface ReadError : ReadResponse

    /**
     * A [WriteResponse] that failed to write
     */
    sealed interface WriteError : WriteResponse

    /**
     * An [GattResponse] that failed to perform either a Read or Write request
     * Options are defined by the Bluetooth Core Specification Version 6.2 Vol3, Part F
     */
    sealed interface Error :
        ReadError,
        WriteError {

        /**
         * Gets an [Error] based on a [statusCode]
         * @param value the [statusCode] to get an [Error] for
         * @throws IllegalArgumentException if [value] is not an error code
         * @return the [Error] so that [Error.statusCode] == [value]
         */
        companion object {
            fun from(value: Int): Error = when (value) {
                0x00 -> throw IllegalArgumentException("Gatt Success is not an error")
                InvalidHandle.statusCode -> InvalidHandle
                ReadNotPermitted.statusCode -> ReadNotPermitted
                WriteNotPermitted.statusCode -> WriteNotPermitted
                InvalidPdu.statusCode -> InvalidPdu
                InsufficientAuthentication.statusCode -> InsufficientAuthentication
                RequestNotSupported.statusCode -> RequestNotSupported
                InvalidOffset.statusCode -> InvalidOffset
                InsufficientAuthorization.statusCode -> InsufficientAuthorization
                PrepareQueueFull.statusCode -> PrepareQueueFull
                AttributeNotFound.statusCode -> AttributeNotFound
                AttributeNotLong.statusCode -> AttributeNotLong
                EncryptionKeySizeTooShort.statusCode -> EncryptionKeySizeTooShort
                InvalidAttributeValueLength.statusCode -> InvalidAttributeValueLength
                UnlikelyError.statusCode -> UnlikelyError
                InsufficientEncryption.statusCode -> InsufficientEncryption
                UnsupportedGroupType.statusCode -> UnsupportedGroupType
                InsufficientResources.statusCode -> InsufficientResources
                DatabaseOutOfSync.statusCode -> DatabaseOutOfSync
                ValueNotAllowed.statusCode -> ValueNotAllowed
                WriteRequestRejected.statusCode -> WriteRequestRejected
                ClientCharacteristicConfigurationDescriptorImproperlyConfigured.statusCode -> ClientCharacteristicConfigurationDescriptorImproperlyConfigured
                ProcedureAlreadyInProgress.statusCode -> ProcedureAlreadyInProgress
                OutOfRange.statusCode -> OutOfRange
                else -> ApplicationError(value)
            }
        }
    }

    /**
     * An [Error] where the attribute handle given was not valid on this server.
     */
    data object InvalidHandle : Error {
        override val statusCode: Int = 0x01
    }

    /**
     * An [Error] where the attribute cannot be read.
     */
    data object ReadNotPermitted : Error {
        override val statusCode: Int = 0x02
    }

    /**
     * An [Error] where the attribute cannot be written.
     */
    data object WriteNotPermitted : Error {
        override val statusCode: Int = 0x03
    }

    /**
     * An [Error] where the attribute PDU was invalid
     */
    data object InvalidPdu : Error {
        override val statusCode: Int = 0x04
    }

    /**
     * An [Error] where the attribute requires authentication before it can be read or written.
     */
    data object InsufficientAuthentication : Error {
        override val statusCode: Int = 0x05
    }

    /**
     * An [Error] where the GATT Server does not support the request received from the client.
     */
    data object RequestNotSupported : Error {
        override val statusCode: Int = 0x06
    }

    /**
     * An [Error] where the offset specified was past the end of the attribute.
     */
    data object InvalidOffset : Error {
        override val statusCode: Int = 0x07
    }

    /**
     * An [Error] where the attribute requires authorization before it can be read or written.
     */
    data object InsufficientAuthorization : Error {
        override val statusCode: Int = 0x08
    }

    /**
     * An [Error] where too many prepare writes have been queued.
     */
    data object PrepareQueueFull : Error {
        override val statusCode: Int = 0x09
    }

    /**
     * An [Error] where no attribute was found within the given attribute handle range.
     */
    data object AttributeNotFound : Error {
        override val statusCode: Int = 0x0A
    }

    /**
     * An [Error] where the attribute cannot be read using the ATT_READ_BLOB_REQ_PDU
     */
    data object AttributeNotLong : Error {
        override val statusCode: Int = 0x0B
    }

    /**
     * An [Error] where the Encryption Key Size used for encrypting this link is too short.
     */
    data object EncryptionKeySizeTooShort : Error {
        override val statusCode: Int = 0x0C
    }

    /**
     * An [Error] where the attribute value length is invalid for the operation.
     */
    data object InvalidAttributeValueLength : Error {
        override val statusCode: Int = 0x0D
    }

    /**
     * An [Error] where the attribute request that was requested has encountered an error that was unlikely, and therefore could not be completed as requested.
     */
    data object UnlikelyError : Error {
        override val statusCode: Int = 0x0E
    }

    /**
     * An [Error] where the attribute requires encryption before it can be read or written.
     */
    data object InsufficientEncryption : Error {
        override val statusCode: Int = 0x0F
    }

    /**
     * An [Error] where the attribute type is not a supported grouping attribute as defined by a higher layer specification.
     */
    data object UnsupportedGroupType : Error {
        override val statusCode: Int = 0x10
    }

    /**
     * An [Error] there are insufficient resources to complete the request.
     */
    data object InsufficientResources : Error {
        override val statusCode: Int = 0x11
    }

    /**
     * An [Error] where the server requests the client to rediscover the database.
     */
    data object DatabaseOutOfSync : Error {
        override val statusCode: Int = 0x12
    }

    /**
     * An [Error] where the attribute parameter value was not allowed.
     */
    data object ValueNotAllowed : Error {
        override val statusCode: Int = 0x13
    }

    /**
     * Application [Error] defined by a higher specification.
     * @property statusCode the status code of the error. Must be in the range `0x80` - `0x9F`
     */
    data class ApplicationError(override val statusCode: Int = 0x80) : Error {
        init {
            require(statusCode in 0x80..0x9F) { "Application Error codes must be in the range 0x80 - 0x9F" }
        }
    }

    /**
     * An [Error] that is common for profiles and services to be sent
     */
    sealed interface CommonProfileAndServiceError : Error

    /**
     * A [CommonProfileAndServiceError] used when a requested write operation cannot be fulfilled for reasons other than permissions.
     */
    data object WriteRequestRejected : CommonProfileAndServiceError {
        override val statusCode: Int = 0xFC
    }

    /**
     * A [CommonProfileAndServiceError] used when a Client Characteristic Configuration descriptor is not configured according to the requirements of the profile or service
     */
    data object ClientCharacteristicConfigurationDescriptorImproperlyConfigured : CommonProfileAndServiceError {
        override val statusCode: Int = 0xFD
    }

    /**
     * A [CommonProfileAndServiceError] used when a profile or service request cannot be serviced because an operation that has been previously triggered is still in progress.
     */
    data object ProcedureAlreadyInProgress : CommonProfileAndServiceError {
        override val statusCode: Int = 0xFE
    }

    /**
     * A [CommonProfileAndServiceError] used when an attribute value is out of range as defined by a profile or service specification.
     */
    data object OutOfRange : CommonProfileAndServiceError {
        override val statusCode: Int = 0xFF
    }

    /**
     * A [ReadError]/[WriteError] given if the remote device could not be reached
     */
    data object DeviceUnavailable : WriteError, ReadError {
        override val statusCode: Int = -1
    }

    /**
     * The status code matching the code of the Bluetooth Specification
     */
    val statusCode: Int
}
