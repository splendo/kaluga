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

package com.splendo.kaluga.scientific.unit

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.PhysicalQuantity
import kotlinx.serialization.Serializable

/**
 * Set of all [MetricLuminousExposure]
 */
val MetricLuminousExposureUnits: Set<MetricLuminousExposure> get() = MetricIlluminanceUnits.flatMap { illuminance ->
    TimeUnits.map { illuminance x it }
}.toSet()

/**
 * Set of all [ImperialLuminousExposure]
 */
val ImperialLuminousExposureUnits: Set<ImperialLuminousExposure> get() = ImperialIlluminanceUnits.flatMap { illuminance ->
    TimeUnits.map { illuminance x it }
}.toSet()

/**
 * Set of all [LuminousExposure]
 */
val LuminousExposureUnits: Set<LuminousExposure> get() = MetricLuminousExposureUnits + ImperialLuminousExposureUnits

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.LuminousExposure]
 * SI unit is `Lux x Second`
 */
@Serializable
sealed class LuminousExposure : AbstractScientificUnit<PhysicalQuantity.LuminousExposure>() {
    /**
     * The [Illuminance] component
     */
    abstract val illuminance: Illuminance

    /**
     * The [Time] component
     */
    abstract val time: Time
    override val symbol: String get() = "${illuminance.symbol}⋅${time.symbol}"
    override val quantity = PhysicalQuantity.LuminousExposure
    override fun fromSIUnit(value: Decimal): Decimal = illuminance.fromSIUnit(time.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = time.toSIUnit(illuminance.toSIUnit(value))
}

/**
 * A [LuminousExposure] for [MeasurementSystem.Metric]
 * @param illuminance the [MetricIlluminance] component
 * @param time the [Time] component
 */
@Serializable
data class MetricLuminousExposure(override val illuminance: MetricIlluminance, override val time: Time) :
    LuminousExposure(),
    MetricScientificUnit<PhysicalQuantity.LuminousExposure> {
    override val system = MeasurementSystem.Metric
}

/**
 * A [LuminousExposure] for [MeasurementSystem.Imperial]
 * @param illuminance the [ImperialIlluminance] component
 * @param time the [Time] component
 */
@Serializable
data class ImperialLuminousExposure(override val illuminance: ImperialIlluminance, override val time: Time) :
    LuminousExposure(),
    ImperialScientificUnit<PhysicalQuantity.LuminousExposure> {
    override val system = MeasurementSystem.Imperial
}

/**
 * Gets a [MetricLuminousExposure] from a [MetricIlluminance] and a [Time]
 * @param time the [Time] component
 * @return the [MetricLuminousExposure] represented by the units
 */
infix fun MetricIlluminance.x(time: Time) = MetricLuminousExposure(this, time)

/**
 * Gets an [ImperialLuminousExposure] from an [ImperialIlluminance] and a [Time]
 * @param time the [Time] component
 * @return the [ImperialLuminousExposure] represented by the units
 */
infix fun ImperialIlluminance.x(time: Time) = ImperialLuminousExposure(this, time)
