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

package com.splendo.kaluga.bluetooth

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/**
 * The Attribute Protocol allows a device referred to as the server to expose a set of attributes and their associated values to a peer device referred to as the client.
 * These attributes exposed by the server can be discovered, read, and written by a client, and can be indicated and notified by the server.
 *
 * Each attribute has an attribute type that identifies, by means of a [UUID], what the attribute represents so that a client can understand the attributes exposed by a server.
 */
interface Attribute {
    /**
     * The [UUID] of the attribute
     */
    val uuid: UUID
}

/**
 * Gets the [Attribute] of [T] with the given [UUID] from a [List] of [T]
 * @throws NoSuchElementException if the [Attribute] is not found
 * @return the [Attribute] of [T] with the given [UUID]
 */
operator fun <T : Attribute> List<T>.get(uuid: UUID) = first { it.uuid.uuidString == uuid.uuidString }

/**
 * Gets the [Attribute] of [T] with the given [UUID] from a [List] of [T] or `null` if it cannot be found
 * @return the [Attribute] of [T] with the given [UUID] or `null` if it cannot be found
 */
fun <T : Attribute> List<T>.getOrNull(uuid: UUID) = find { it.uuid.uuidString == uuid.uuidString }

/**
 * Gets the [Flow] of [Attribute] of [T] with the given [UUID] from a [Flow] of [List] of [T]
 * @return the [Flow] of [Attribute] of [T] with the given [UUID]. Flow throws [NoSuchElementException] if the attribute cannot be found.
 */
operator fun <T : Attribute> Flow<List<T>>.get(uuid: UUID): Flow<T> = this.map { attributes ->
    attributes[uuid]
}.distinctUntilChanged()

/**
 * Gets the [Flow] of [Attribute] of [T] with the given [UUID] from a [Flow] of [List] of [T], or `null` if it cannot be found
 * @return the [Flow] of [Attribute] of [T] with the given [UUID] or `null` if it cannot be found.
 */
fun <T : Attribute> Flow<List<T>>.getOrNull(uuid: UUID): Flow<T?> = this.map { attributes ->
    attributes.getOrNull(uuid)
}.distinctUntilChanged()

/**
 * An Exception thrown when [RemoteAttribute.read] could not read into a data object
 * @property reason the [GattResponse.ReadError] that caused the read to fail
 */
class FailedToReadException(val reason: GattResponse.ReadError) : Exception()
