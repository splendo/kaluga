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

package com.splendo.kaluga.bluetooth.server

import com.splendo.kaluga.bluetooth.MTU

sealed interface GattResponse {
    sealed interface ReadResponse : GattResponse

    sealed interface Success : GattResponse {
        override val statusCode: Int get() = 0x00
    }
    data class ReadSuccess(val value: ByteArray) :
        ReadResponse,
        Success {
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

    sealed interface WriteResponse : GattResponse

    data object WriteSuccess : WriteResponse, Success

    sealed interface MTUResponse : GattResponse {
        val mtu: MTU
    }
    data class MTUSuccess(override val mtu: MTU) :
        MTUResponse,
        Success

    sealed interface MTUError : MTUResponse
    data class MTUFailure(override val mtu: MTU, val error: Error) : MTUError {
        override val statusCode: Int = error.statusCode
    }
    data class MTUNotPermitted(override val mtu: MTU) : MTUError {
        override val statusCode: Int = -1
    }

    sealed interface Error :
        ReadResponse,
        WriteResponse {

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
    data object InvalidHandle : Error {
        override val statusCode: Int = 0x01
    }
    data object ReadNotPermitted : Error {
        override val statusCode: Int = 0x02
    }
    data object WriteNotPermitted : Error {
        override val statusCode: Int = 0x03
    }
    data object InvalidPdu : Error {
        override val statusCode: Int = 0x04
    }
    data object InsufficientAuthentication : Error {
        override val statusCode: Int = 0x05
    }
    data object RequestNotSupported : Error {
        override val statusCode: Int = 0x06
    }
    data object InvalidOffset : Error {
        override val statusCode: Int = 0x07
    }
    data object InsufficientAuthorization : Error {
        override val statusCode: Int = 0x08
    }
    data object PrepareQueueFull : Error {
        override val statusCode: Int = 0x09
    }
    data object AttributeNotFound : Error {
        override val statusCode: Int = 0x0A
    }
    data object AttributeNotLong : Error {
        override val statusCode: Int = 0x0B
    }
    data object EncryptionKeySizeTooShort : Error {
        override val statusCode: Int = 0x0C
    }
    data object InvalidAttributeValueLength : Error {
        override val statusCode: Int = 0x0D
    }
    data object UnlikelyError : Error {
        override val statusCode: Int = 0x0E
    }
    data object InsufficientEncryption : Error {
        override val statusCode: Int = 0x0F
    }
    data object UnsupportedGroupType : Error {
        override val statusCode: Int = 0x10
    }
    data object InsufficientResources : Error {
        override val statusCode: Int = 0x11
    }
    data object DatabaseOutOfSync : Error {
        override val statusCode: Int = 0x12
    }
    data object ValueNotAllowed : Error {
        override val statusCode: Int = 0x13
    }

    data class ApplicationError(override val statusCode: Int = 0x80) : Error {
        init {
            require(statusCode in 0x80..0x9F) { "Application Error codes must be in the range 0x80 - 0x9F" }
        }
    }

    sealed interface CommonProfileAndServiceError : Error

    data object WriteRequestRejected : CommonProfileAndServiceError {
        override val statusCode: Int = 0xFC
    }
    data object ClientCharacteristicConfigurationDescriptorImproperlyConfigured : CommonProfileAndServiceError {
        override val statusCode: Int = 0xFD
    }
    data object ProcedureAlreadyInProgress : CommonProfileAndServiceError {
        override val statusCode: Int = 0xFE
    }
    data object OutOfRange : CommonProfileAndServiceError {
        override val statusCode: Int = 0xFF
    }

    data object DeviceUnavailable : WriteResponse, ReadResponse {
        override val statusCode: Int = -1
    }

    val statusCode: Int
}
