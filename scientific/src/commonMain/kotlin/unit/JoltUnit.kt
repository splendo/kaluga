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
 * Set of all [MetricJolt]
 */
val MetricJoltUnits: Set<MetricJolt> get() = MetricAccelerationUnits.flatMap { acceleration ->
    TimeUnits.map { acceleration per it }
}.toSet()

/**
 * Set of all [ImperialJolt]
 */
val ImperialJoltUnits: Set<ImperialJolt> get() = ImperialAccelerationUnits.flatMap { acceleration ->
    TimeUnits.map { acceleration per it }
}.toSet()

/**
 * Set of all [Jolt]
 */
val JoltUnits: Set<Jolt> get() = MetricJoltUnits +
    ImperialJoltUnits

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.Jolt]
 * SI unit is `Meter per Second per Second per Second`
 */
@Serializable
sealed class Jolt : AbstractScientificUnit<PhysicalQuantity.Jolt>() {
    abstract val acceleration: Acceleration
    abstract val per: Time
    override val quantity = PhysicalQuantity.Jolt
    override val symbol: String by lazy {
        if (acceleration.symbol != acceleration.defaultSymbol) {
            "${acceleration.symbol} / ${per.symbol}"
        } else {
            val perSymbol = when {
                per == acceleration.per && acceleration.speed.per == per -> "${per.symbol}3"
                per == acceleration.per -> "${per.symbol}2*${acceleration.speed.per}"
                per == acceleration.speed.per -> "${per.symbol}2*${acceleration.per}"
                acceleration.per == acceleration.speed.per -> "${per.symbol}*${acceleration.per}2"
                else -> "${per.symbol}*${acceleration.per.symbol}*${acceleration.speed.per.symbol}"
            }
            "${acceleration.speed.distance.symbol} / $perSymbol"
        }
    }
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(acceleration.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = acceleration.toSIUnit(per.fromSIUnit(value))
}

/**
 * A [Jolt] for [MeasurementSystem.MetricAndImperial]
 * @param acceleration the [MetricAndImperialAcceleration] component
 * @param per the [Time] component
 */
@Serializable
data class MetricAndImperialJolt(
    override val acceleration: MetricAndImperialAcceleration,
    override val per: Time,
) : Jolt(), MetricAndImperialScientificUnit<PhysicalQuantity.Jolt> {
    override val system = MeasurementSystem.MetricAndImperial

    val metric = acceleration.metric per per
    val imperial = acceleration.imperial per per
}

/**
 * A [Jolt] for [MeasurementSystem.Metric]
 * @param acceleration the [MetricAcceleration] component
 * @param per the [Time] component
 */
@Serializable
data class MetricJolt(override val acceleration: MetricAcceleration, override val per: Time) : Jolt(), MetricScientificUnit<PhysicalQuantity.Jolt> {
    override val system = MeasurementSystem.Metric
}

/**
 * A [Jolt] for [MeasurementSystem.Imperial]
 * @param acceleration the [ImperialAcceleration] component
 * @param per the [Time] component
 */
@Serializable
data class ImperialJolt(override val acceleration: ImperialAcceleration, override val per: Time) : Jolt(), ImperialScientificUnit<PhysicalQuantity.Jolt> {
    override val system = MeasurementSystem.Imperial
}

/**
 * Gets a [MetricAndImperialJolt] from a [MetricAndImperialAcceleration] and a [Time]
 * @param time the [Time] component
 * @return the [MetricAndImperialJolt] represented by the units
 */
infix fun MetricAndImperialAcceleration.per(time: Time) = MetricAndImperialJolt(this, time)

/**
 * Gets a [MetricJolt] from a [MetricAcceleration] and a [Time]
 * @param time the [Time] component
 * @return the [MetricJolt] represented by the units
 */
infix fun MetricAcceleration.per(time: Time) = MetricJolt(this, time)

/**
 * Gets an [ImperialJolt] from an [ImperialAcceleration] and a [Time]
 * @param time the [Time] component
 * @return the [ImperialJolt] represented by the units
 */
infix fun ImperialAcceleration.per(time: Time) = ImperialJolt(this, time)
