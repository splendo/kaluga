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

sealed interface GattResponse {
    sealed interface ReadResponse : GattResponse

    sealed interface Success : GattResponse {
        override val statusCode: Int get() = 0
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
    sealed interface Error :
        ReadResponse,
        WriteResponse
    data object InvalidHandle : Error {
        override val statusCode: Int get() = 1
    }
    data object InvalidOffset : Error {
        override val statusCode: Int get() = 7
    }
    data object AttributeNotFound : Error {
        override val statusCode: Int get() = 10
    }
    data object AttributeNotLong : Error {
        override val statusCode: Int get() = 11
    }
    data object InvalidAttributeLength : Error {
        override val statusCode: Int get() = 13
    }
    data object UnlikelyError : Error {
        override val statusCode: Int get() = 14
    }
    data object InsufficientResources : Error {
        override val statusCode: Int get() = 17
    }

    val statusCode: Int
}
