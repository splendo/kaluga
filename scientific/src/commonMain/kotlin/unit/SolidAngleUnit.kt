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
import com.splendo.kaluga.base.utils.div
import com.splendo.kaluga.base.utils.times
import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.scientific.PhysicalQuantity
import kotlinx.serialization.Serializable
import kotlin.math.PI

/**
 * Set of all [SolidAngle]
 */
val SolidAngleUnits: Set<SolidAngle> get() = setOf(
    Steradian,
    Nanosteradian,
    Microsteradian,
    Millisteradian,
    Centisteradian,
    Decisteradian,
    Spat,
    SquareDegree,
)

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.SolidAngle]
 * SI unit is [Steradian]
 */
@Serializable
sealed class SolidAngle : AbstractScientificUnit<PhysicalQuantity.SolidAngle>(), MetricAndImperialScientificUnit<PhysicalQuantity.SolidAngle>

@Serializable
data object Steradian : SolidAngle(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.SolidAngle> {
    override val symbol = "sr"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.SolidAngle
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
sealed class SteradianMultiple : SolidAngle(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.SolidAngle, Steradian>

@Serializable
data object Nanosteradian : SteradianMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.SolidAngle, Steradian> by Nano(Steradian)

@Serializable
data object Microsteradian : SteradianMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.SolidAngle, Steradian> by Micro(Steradian)

@Serializable
data object Millisteradian : SteradianMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.SolidAngle, Steradian> by Milli(Steradian)

@Serializable
data object Centisteradian : SteradianMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.SolidAngle, Steradian> by Centi(Steradian)

@Serializable
data object Decisteradian : SteradianMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.SolidAngle, Steradian> by Deci(Steradian)

@Serializable
data object Spat : SolidAngle(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.SolidAngle> {
    private const val STERADIAN_IN_SPAT = 4.0 * PI
    override val symbol = "sp"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.SolidAngle
    override fun fromSIUnit(value: Decimal): Decimal = value / STERADIAN_IN_SPAT.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value * STERADIAN_IN_SPAT.toDecimal()
}

@Serializable
data object SquareDegree : SolidAngle() {
    override val symbol = "°2"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.SolidAngle
    override fun fromSIUnit(value: Decimal): Decimal = Degree.fromSIUnit(Degree.fromSIUnit(value))
    override fun toSIUnit(value: Decimal): Decimal = Degree.toSIUnit(Degree.toSIUnit(value))
}
