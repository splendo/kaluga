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
import com.splendo.kaluga.bluetooth.RSSI
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.math.pow

/**
 * Accessor to the platform level Bluetooth device
 */
expect interface DeviceWrapper {
    /**
     * Name of the Bluetooth device
     */
    val name: String?

    /**
     * [Identifier] of the Bluetooth device
     */
    val identifier: Identifier
}

/**
 * Properties of a Bluetooth Device
 */
interface DeviceInfo {

    /**
     * Name of the Bluetooth device
     */
    val name: String?

    /**
     * [Identifier] of the Bluetooth device
     */
    val identifier: Identifier

    /**
     * [RSSI] value of the Bluetooth device
     */
    val rssi: RSSI

    /**
     * Current [BaseAdvertisementData] of the Bluetooth device
     */
    val advertisementData: BaseAdvertisementData

    /**
     * The [KalugaDate] at which the device last advertised an update
     */
    val updatedAt: KalugaDate

    /**
     * Calculates the distance to the device in meters
     * @param environmentalFactor the constant to account for environmental interference. Should usually range between 2.0 and 4.0
     * @return the distance to the device in meters
     */
    fun distance(environmentalFactor: Double = 2.0): Double {
        if (advertisementData.txPowerLevel == Int.MIN_VALUE || environmentalFactor.isNaN()) return Double.NaN
        val difference = advertisementData.txPowerLevel.toDouble() - rssi.toDouble()
        val factor = 10.0 * environmentalFactor
        return 10.0.pow(difference / factor)
    }
}

/**
 * An implementation of [DeviceInfo]
 */
data class DeviceInfoImpl(override val name: String?, override val identifier: Identifier, override val rssi: RSSI, override val advertisementData: BaseAdvertisementData) :
    DeviceInfo {

    /**
     * Constructor
     * @param wrapper the [DeviceWrapper] to the device
     * @param rssi the current RSSI value
     * @param advertisementData the [BaseAdvertisementData] last received
     */
    constructor(
        wrapper: DeviceWrapper,
        rssi: RSSI,
        advertisementData: BaseAdvertisementData,
    ) : this(
        name = advertisementData.name ?: wrapper.name,
        identifier = wrapper.identifier,
        rssi = rssi,
        advertisementData = advertisementData,
    )
    override val updatedAt = DefaultKalugaDate.now()
}
