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

package com.splendo.kaluga.bytes.serialization

import com.splendo.kaluga.bytes.layout.dump
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule

/**
 * The [dump][com.splendo.kaluga.bytes.layout.dump] of the layout [BytesFormat] uses for [descriptor].
 *
 *
 * @throws kotlinx.serialization.SerializationException when the descriptor describes an invalid layout.
 */
fun layoutDumpOf(descriptor: SerialDescriptor, serializersModule: SerializersModule = EmptySerializersModule()): String =
    BinaryDescriptorRegistry.layout(descriptor, serializersModule).dump()
