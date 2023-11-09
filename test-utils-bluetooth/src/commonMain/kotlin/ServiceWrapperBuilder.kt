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

package com.splendo.kaluga.test.bluetooth

import com.splendo.kaluga.bluetooth.ServiceWrapper
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.randomUUID

@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPEALIAS)
@DslMarker
annotation class MockBuilderDsl

@MockBuilderDsl
typealias CharacteristicList = ArrayList<ServiceWrapperBuilder.Characteristic>

fun CharacteristicList.characteristic(builder: ServiceWrapperBuilder.Characteristic.Builder.() -> Unit) = add(
    ServiceWrapperBuilder.Characteristic.Builder().apply(builder).build(),
)

@MockBuilderDsl
typealias DescriptorList = ArrayList<ServiceWrapperBuilder.Descriptor>

fun DescriptorList.descriptor(uuid: UUID) = add(
    ServiceWrapperBuilder.Descriptor(uuid),
)

@MockBuilderDsl
class ServiceWrapperBuilder {

    data class Descriptor(
        val uuid: UUID = randomUUID(),
    )

    data class Characteristic(
        val uuid: UUID = randomUUID(),
        val descriptors: List<Descriptor> = listOf(Descriptor()),
        val properties: Int = 0,
    ) {

        @MockBuilderDsl
        class Builder {
            var uuid: UUID = randomUUID()
            var properties: Int = 0
            private var _descriptors = arrayListOf<Descriptor>()
            val descriptors: List<Descriptor> get() = _descriptors.toList()

            fun descriptors(builder: DescriptorList.() -> Unit) = builder(_descriptors)
            fun build() = Characteristic(uuid, descriptors, properties)
        }

        val descriptorUUIDs get() = descriptors.map(Descriptor::uuid)
    }

    var uuid = randomUUID()
    private val _characteristics = ArrayList<Characteristic>()
    val characteristics: List<Characteristic> get() = _characteristics.toList()

    fun characteristics(builder: CharacteristicList.() -> Unit) = builder(_characteristics)
}

expect fun ServiceWrapperBuilder.build(): ServiceWrapper
