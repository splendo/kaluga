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

package com.splendo.kaluga.bluetooth.device

import com.splendo.kaluga.base.utils.DefaultKalugaDate
import com.splendo.kaluga.base.utils.KalugaDate
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.math.pow

expect class Identifier

expect fun randomIdentifier(): Identifier

expect fun identifierFromString(stringValue: String): Identifier?

expect val Identifier.stringValue: String

@Serializable(with = IdentifierSerializer::class)
data class SerializableIdentifier(val identifier: Identifier)

val Identifier.serializable get() = SerializableIdentifier(this)

open class IdentifierSerializer :
    KSerializer<SerializableIdentifier> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("IdentifierString", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: SerializableIdentifier) {
        val string = value.identifier.stringValue
        encoder.encodeString(string)
    }

    override fun deserialize(decoder: Decoder): SerializableIdentifier {
        val string = decoder.decodeString()
        return SerializableIdentifier(identifierFromString(string)!!)
    }
}

expect interface DeviceWrapper {
    val name: String?
    val identifier: Identifier
}

interface DeviceInfo {
    val name: String?
    val identifier: Identifier
    val rssi: Int
    val advertisementData: BaseAdvertisementData
    val updatedAt: KalugaDate
    fun distance(environmentalFactor: Double = 2.0): Double
}

data class DeviceInfoImpl(
    override val name: String?,
    override val identifier: Identifier,
    override val rssi: Int,
    override val advertisementData: BaseAdvertisementData
) : DeviceInfo {

    constructor(
        wrapper: DeviceWrapper,
        rssi: Int,
        advertisementData: BaseAdvertisementData
    ) : this(
        name = wrapper.name,
        identifier = wrapper.identifier,
        rssi = rssi,
        advertisementData = advertisementData,
    )
    override val updatedAt = DefaultKalugaDate.now()

    override fun distance(environmentalFactor: Double): Double {
        if (advertisementData.txPowerLevel == Int.MIN_VALUE || environmentalFactor.isNaN())
            return Double.NaN
        val difference = advertisementData.txPowerLevel.toDouble() - rssi.toDouble()
        val factor = 10.0 * environmentalFactor
        return 10.0.pow(difference / factor)
    }
}
