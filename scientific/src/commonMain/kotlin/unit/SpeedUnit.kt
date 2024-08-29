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
 * Set of all [MetricSpeed]
 */
val MetricSpeedUnits: Set<MetricSpeed> get() = MetricLengthUnits.flatMap { length ->
    TimeUnits.map { MetricSpeed(length, it) }
}.toSet()

/**
 * Set of all [ImperialSpeed]
 */
val ImperialSpeedUnits: Set<ImperialSpeed> get() = ImperialLengthUnits.flatMap { length ->
    TimeUnits.map { ImperialSpeed(length, it) }
}.toSet()

/**
 * Set of all [Speed]
 */
val SpeedUnits: Set<Speed> get() = MetricSpeedUnits + ImperialSpeedUnits

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.Speed]
 * SI unit is `Meter per Second`
 */
@Serializable
sealed class Speed : AbstractScientificUnit<PhysicalQuantity.Speed>() {

    /**
     * The [Length] component
     */
    abstract val distance: Length

    /**
     * The [Time] component
     */
    abstract val per: Time
    override val quantity = PhysicalQuantity.Speed
    override val symbol: String by lazy { "${distance.symbol}/${per.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(distance.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = distance.toSIUnit(per.fromSIUnit(value))
}

/**
 * A [Speed] for [MeasurementSystem.Metric]
 * @param distance the [MetricLength] component
 * @param per the [Time] component
 */
@Serializable
data class MetricSpeed(override val distance: MetricLength, override val per: Time) :
    Speed(),
    MetricScientificUnit<PhysicalQuantity.Speed> {
    override val system = MeasurementSystem.Metric
}

/**
 * A [Speed] for [MeasurementSystem.Imperial]
 * @param distance the [ImperialLength] component
 * @param per the [Time] component
 */
@Serializable
data class ImperialSpeed(override val distance: ImperialLength, override val per: Time) :
    Speed(),
    ImperialScientificUnit<PhysicalQuantity.Speed> {
    override val system = MeasurementSystem.Imperial
}

/**
 * Gets a [MetricSpeed] from a [MetricLength] and a [Time]
 * @param time the [Time] component
 * @return the [MetricSpeed] represented by the units
 */
infix fun MetricLength.per(time: Time) = MetricSpeed(this, time)

/**
 * Gets an [ImperialSpeed] from an [ImperialLength] and a [Time]
 * @param time the [Time] component
 * @return the [ImperialSpeed] represented by the units
 */
infix fun ImperialLength.per(time: Time) = ImperialSpeed(this, time)

/**
 * The [Speed] of light in `Meter per Second`
 */
val MetricSpeedOfLight = 299792458(Meter per Second)

/**
 * The [Speed] of light in `Foot per Second`
 */
val ImperialSpeedOfLight = MetricSpeedOfLight.convert(Foot per Second)
