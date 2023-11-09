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
 * Set of all [LuminousIntensity]
 */
val LuminousIntensityUnits: Set<LuminousIntensity> get() = setOf(
    Candela,
    Nanocandela,
    Microcandela,
    Millicandela,
    Centicandela,
    Decicandela,
    Decacandela,
    Hectocandela,
    Kilocandela,
    Megacandela,
    Gigacandela,
)

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.LuminousIntensity]
 * SI unit is [Candela]
 */
@Serializable
sealed class LuminousIntensity : AbstractScientificUnit<PhysicalQuantity.LuminousIntensity>(), MetricAndImperialScientificUnit<PhysicalQuantity.LuminousIntensity>

@Serializable
data object Candela : LuminousIntensity(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.LuminousIntensity> {
    override val symbol = "cd"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.LuminousIntensity
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
sealed class CandelaMultiple : LuminousIntensity(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.LuminousIntensity, Candela>

@Serializable
data object Nanocandela : CandelaMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.LuminousIntensity, Candela> by Nano(Candela)

@Serializable
data object Microcandela : CandelaMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.LuminousIntensity, Candela> by Micro(Candela)

@Serializable
data object Millicandela : CandelaMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.LuminousIntensity, Candela> by Milli(Candela)

@Serializable
data object Centicandela : CandelaMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.LuminousIntensity, Candela> by Centi(Candela)

@Serializable
data object Decicandela : CandelaMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.LuminousIntensity, Candela> by Deci(Candela)

@Serializable
data object Decacandela : CandelaMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.LuminousIntensity, Candela> by Deca(Candela)

@Serializable
data object Hectocandela : CandelaMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.LuminousIntensity, Candela> by Hecto(Candela)

@Serializable
data object Kilocandela : CandelaMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.LuminousIntensity, Candela> by Kilo(Candela)

@Serializable
data object Megacandela : CandelaMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.LuminousIntensity, Candela> by Mega(Candela)

@Serializable
data object Gigacandela : CandelaMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.LuminousIntensity, Candela> by Giga(Candela)
