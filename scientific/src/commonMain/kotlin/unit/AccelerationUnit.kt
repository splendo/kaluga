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
import com.splendo.kaluga.scientific.convert
import com.splendo.kaluga.scientific.invoke
import kotlinx.serialization.Serializable

/**
 * Set of all [MetricAcceleration]
 */
val MetricAccelerationUnits: Set<MetricAcceleration> get() = MetricSpeedUnits.flatMap { speed ->
    TimeUnits.map { speed per it }
}.toSet()

/**
 * Set of all [ImperialAcceleration]
 */
val ImperialAccelerationUnits: Set<ImperialAcceleration> get() = ImperialSpeedUnits.flatMap { speed ->
    TimeUnits.map { speed per it }
}.toSet()

/**
 * Set of all [Acceleration]
 */
val AccelerationUnits: Set<Acceleration> get() = MetricAccelerationUnits +
    ImperialAccelerationUnits

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.Acceleration]
 * SI unit is `Meter per Second per Second`
 */
@Serializable
sealed class Acceleration : AbstractScientificUnit<PhysicalQuantity.Acceleration>() {

    /**
     * The [Speed] component
     */
    abstract val speed: Speed

    /**
     * The [Time] component
     */
    abstract val per: Time
    override val quantity = PhysicalQuantity.Acceleration
    override val symbol: String by lazy {
        if (speed.per == per) {
            "${speed.distance.symbol} / ${per.symbol}2"
        } else {
            "${speed.distance.symbol} / (${speed.per.symbol} * ${per.symbol})"
        }
    }
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(speed.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = speed.toSIUnit(per.fromSIUnit(value))
}

/**
 * An [Acceleration] for [MeasurementSystem.Metric]
 * @param speed the [MetricSpeed] component
 * @param per the [Time] component
 */
@Serializable
data class MetricAcceleration(
    override val speed: MetricSpeed,
    override val per: Time
) : Acceleration(), MetricScientificUnit<PhysicalQuantity.Acceleration> {
    override val system = MeasurementSystem.Metric
}

/**
 * An [Acceleration] for [MeasurementSystem.Imperial]
 * @param speed the [ImperialSpeed] component
 * @param per the [Time] component
 */
@Serializable
data class ImperialAcceleration(
    override val speed: ImperialSpeed,
    override val per: Time
) : Acceleration(), ImperialScientificUnit<PhysicalQuantity.Acceleration> {
    override val system = MeasurementSystem.Imperial
}

/**
 * Gets a [MetricAcceleration] from a [MetricSpeed] and a [Time]
 * @param time the [Time] component
 * @return the [MetricAcceleration] represented by the units
 */
infix fun MetricSpeed.per(time: Time) = MetricAcceleration(this, time)
/**
 * Gets an [ImperialAcceleration] from an [ImperialSpeed] and a [Time]
 * @param time the [Time] component
 * @return the [ImperialAcceleration] represented by the units
 */
infix fun ImperialSpeed.per(time: Time) = ImperialAcceleration(this, time)

/**
 * The standard [Acceleration] due to gravity in `Meter per Second per Second`
 */
val MetricStandardGravityAcceleration = 9.80665(Meter per Second per Second)

/**
 * The standard [Acceleration] due to gravity in `Foot per Second per Second`
 */
val ImperialStandardGravityAcceleration = MetricStandardGravityAcceleration.convert(Foot per Second per Second)
