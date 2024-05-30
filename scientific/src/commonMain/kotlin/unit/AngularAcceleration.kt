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
 * Set of all [AngularAcceleration]
 */
val AngularAccelerationUnits: Set<AngularAcceleration> get() = AngularVelocityUnits.flatMap { angularVelocity ->
    TimeUnits.map { angularVelocity per it }
}.toSet()

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.AngularAcceleration]
 * SI unit is `Radian per Second per Second`
 * @property angularVelocity the [AngularVelocity] component
 * @property per the [Time] component
 */
@Serializable
data class AngularAcceleration(
    val angularVelocity: AngularVelocity,
    val per: Time,
) : AbstractScientificUnit<PhysicalQuantity.AngularAcceleration>(), MetricAndImperialScientificUnit<PhysicalQuantity.AngularAcceleration> {
    override val quantity = PhysicalQuantity.AngularAcceleration
    override val system = MeasurementSystem.MetricAndImperial
    override val symbol: String by lazy {
        if (angularVelocity.per == per) {
            "${angularVelocity.angle.symbol}/${per.symbol}²"
        } else {
            "${angularVelocity.angle.symbol}/${angularVelocity.per.symbol}·${per.symbol}"
        }
    }
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(angularVelocity.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = angularVelocity.toSIUnit(per.fromSIUnit(value))
}

/**
 * Gets an [AngularAcceleration] from an [AngularVelocity] and a [Time]
 * @param time the [Time] component
 * @return the [AngularAcceleration] represented by the units
 */
infix fun AngularVelocity.per(time: Time) = AngularAcceleration(this, time)
