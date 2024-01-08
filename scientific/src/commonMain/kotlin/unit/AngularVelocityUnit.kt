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
 * Set of all [AngularVelocity]
 */
val AngularVelocityUnits: Set<AngularVelocity> get() = AngleUnits.flatMap { angle ->
    TimeUnits.map { angle per it }
}.toSet()

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.AngularVelocity]
 * SI unit is `Radian per Second`
 * @property angle the [Angle] component
 * @property per the [Time] component
 */
@Serializable
data class AngularVelocity(
    val angle: Angle,
    val per: Time,
) : AbstractScientificUnit<PhysicalQuantity.AngularVelocity>(), MetricAndImperialScientificUnit<PhysicalQuantity.AngularVelocity> {
    override val quantity = PhysicalQuantity.AngularVelocity
    override val system = MeasurementSystem.MetricAndImperial
    override val symbol: String by lazy { "${angle.symbol}/${per.symbol}" }
    override fun fromSIUnit(value: Decimal): Decimal = per.toSIUnit(angle.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = angle.toSIUnit(per.fromSIUnit(value))
}

/**
 * Gets an [AngularVelocity] from an [Angle] and a [Time]
 * @param time the [Time] component
 * @return the [Angle] represented by the units
 */
infix fun Angle.per(time: Time) = AngularVelocity(this, time)
