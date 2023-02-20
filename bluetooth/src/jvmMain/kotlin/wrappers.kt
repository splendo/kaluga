/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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
 * Java accessor to a Bluetooth characteristic. Not Actually implemented
 */
actual interface CharacteristicWrapper {

    /**
     * The [UUID] of the characteristic
     */
    actual val uuid: UUID

    /**
     * The list of [DescriptorWrapper] of associated with the characteristic
     */
    actual val descriptors: List<DescriptorWrapper>

    /**
     * The current [Value] of the characteristic
     */
    actual val value: Value?

    /**
     * The integer representing all [CharacteristicProperties] of the characteristic
     */
    actual val properties: Int
}

/**
 * Java accessor to a Bluetooth descriptor. Not Actually implemented
 */
actual interface DescriptorWrapper {

    /**
     * The [UUID] of the descriptor
     */
    actual val uuid: UUID

    /**
     * The current [Value] of the descriptor
     */
    actual val value: Value?
}

/**
 * Java accessor to a Bluetooth service. Not Actually implemented
 */
actual interface ServiceWrapper {

    /**
     * The list of [CharacteristicWrapper] associated with the service
     */
    actual val characteristics: List<CharacteristicWrapper>

    /**
     * The [UUID] of the service
     */
    actual val uuid: UUID
}
