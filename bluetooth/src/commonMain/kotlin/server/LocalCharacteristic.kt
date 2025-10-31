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

import com.splendo.kaluga.base.collections.concurrentMutableMapOf
import com.splendo.kaluga.bluetooth.Characteristic
import com.splendo.kaluga.bluetooth.CharacteristicProperty
import com.splendo.kaluga.bluetooth.UUID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

interface LocalCharacteristicDSL {
    fun readable(encrypted: Boolean = false, onRead: suspend LocalCharacteristic.(ConnectedDevice, Int) -> GattResponse.ReadResponse)
    fun writable(
        properties: Set<CharacteristicProperty.Writable> = setOf(CharacteristicProperty.Write),
        encrypted: Boolean = false,
        onWrite: suspend LocalCharacteristic.(ConnectedDevice, ByteArray, Int) -> GattResponse.WriteResponse,
    )

    fun notifiable(
        properties: Set<CharacteristicProperty.Notifiable> = setOf(CharacteristicProperty.Notify),
        encrypted: Boolean = false,
        onSubscribe: LocalCharacteristic.Notifiable.(ConnectedDevice) -> Unit,
        onUnsubscribe: LocalCharacteristic.Notifiable.(ConnectedDevice) -> Unit,
    )

    fun descriptor(uuid: UUID, descriptor: LocalDescriptorDSL.() -> Unit)

    fun Flow<ByteArray>.attachIn(
        scope: CoroutineScope,
        started: SharingStarted,
        replay: Int = 0,
        properties: Set<CharacteristicProperty.Notifiable> = setOf(CharacteristicProperty.Notify),
        encrypted: Boolean = false,
    ) {
        val sharedFlow = shareIn(scope, started, replay)
        sharedFlow.attachIn(scope, properties, encrypted)
    }

    fun SharedFlow<ByteArray>.attachIn(
        scope: CoroutineScope,
        properties: Set<CharacteristicProperty.Notifiable> = setOf(CharacteristicProperty.Notify),
        encrypted: Boolean = false,
    ) {
        val observingJobs = concurrentMutableMapOf<ConnectedDevice, Job>()
        notifiable(
            properties,
            encrypted,
            onSubscribe = { device ->
                observingJobs[device] = scope.launch {
                    collect { value ->
                        notify(device, value)
                    }
                }
            },
            onUnsubscribe = { device ->
                observingJobs.remove(device)?.cancel()
            },
        )
    }

    fun StateFlow<ByteArray>.attachIn(
        scope: CoroutineScope,
        properties: Set<CharacteristicProperty.Notifiable> = setOf(CharacteristicProperty.Notify),
        encrypted: Boolean = false,
    ) {
        var hasStarted = false
        notifiable(
            properties,
            encrypted,
            onSubscribe = { device ->
                // We only know the Characteristic on first subscription, so this is the point at which to collect the state flow
                if (!hasStarted) {
                    hasStarted = true
                    scope.launch {
                        collect(this@notifiable)
                    }
                } else {
                    // If scope already launched, then the subscription will have missed the initial value. So report it immediately
                    scope.launch {
                        notify(device, value)
                    }
                }
            },
            onUnsubscribe = {},
        )
    }
}

expect sealed class LocalCharacteristic : Characteristic {

    enum class Permission {
        READABLE,
        WRITABLE,
        READ_ENCRYPTION_REQUIRED,
        WRITE_ENCRYPTION_REQUIRED,
    }

    class Static : LocalCharacteristic {
        override val descriptors: List<LocalDescriptor>
    }

    class Notifiable :
        LocalCharacteristic,
        FlowCollector<ByteArray> {

        val subscribedDevices: StateFlow<List<ConnectedDevice>>
        override val descriptors: List<LocalDescriptor>
        suspend fun notify(device: ConnectedDevice, value: ByteArray): Boolean
        suspend fun notifyAll(value: ByteArray): Boolean
        override suspend fun emit(value: ByteArray)
    }

    override val uuid: UUID
    override val service: LocalService
    override val properties: Set<CharacteristicProperty>
    val permissions: Set<Permission>
    abstract override val descriptors: List<LocalDescriptor>
}
