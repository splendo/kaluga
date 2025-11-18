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

package com.splendo.kaluga.bluetooth.serialization

import com.splendo.kaluga.base.utils.ByteArrayBuilder
import com.splendo.kaluga.base.utils.buildByteArray
import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule

sealed class BluetoothFormat(
    override val serializersModule: SerializersModule = EmptySerializersModule()
) : BinaryFormat {

    class Builder internal constructor(from: BluetoothFormat) {
        var serializersModule: SerializersModule = from.serializersModule
    }

    companion object Default : BluetoothFormat() {
        operator fun invoke(from: BluetoothFormat = this, builder: Builder.() -> Unit): BluetoothFormat {
            val b = Builder(
                from
            ).apply(builder)
            return BluetoothFormatImpl(
                b
            )
        }
    }

    override fun <T> encodeToByteArray(serializer: SerializationStrategy<T>, value: T): ByteArray {
        val builder = BluetoothBinaryEncoder.BinaryBuilder(serializer.descriptor)
        val encoder = BluetoothBinaryEncoder(builder, 0, serializersModule)
        serializer.serialize(encoder, value)

        return buildByteArray(serializer.descriptor.byteOrder, builder::build)
    }

    override fun <T> decodeFromByteArray(deserializer: DeserializationStrategy<T>, bytes: ByteArray): T {
        TODO("Not yet implemented")
    }
}

private class BluetoothFormatImpl(builder: BluetoothFormat.Builder) : BluetoothFormat(builder.serializersModule)
