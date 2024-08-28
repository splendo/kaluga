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
 * Set of all [ElectricResistance]
 */
val ElectricResistanceUnits: Set<ElectricResistance> get() = setOf(
    Ohm,
    Nanoohm,
    Microohm,
    Milliohm,
    Centiohm,
    Deciohm,
    Decaohm,
    HectoOhm,
    Kiloohm,
    Megaohm,
    Gigaohm,
    Abohm,
)

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.ElectricResistance]
 * SI unit is [Ohm]
 */
@Serializable
sealed class ElectricResistance :
    AbstractScientificUnit<PhysicalQuantity.ElectricResistance>(),
    MetricAndImperialScientificUnit<PhysicalQuantity.ElectricResistance>

@Serializable
data object Ohm : ElectricResistance(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricResistance> {
    override val symbol = "Ω"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.ElectricResistance
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
sealed class OhmMultiple :
    ElectricResistance(),
    MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricResistance, Ohm>

@Serializable
data object Nanoohm : OhmMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricResistance, Ohm> by Nano(Ohm)

@Serializable
data object Abohm : OhmMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricResistance, Ohm> by Nano(Ohm) {
    override val symbol = "abΩ"
}

@Serializable
data object Microohm : OhmMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricResistance, Ohm> by Micro(Ohm)

@Serializable
data object Milliohm : OhmMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricResistance, Ohm> by Milli(Ohm)

@Serializable
data object Centiohm : OhmMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricResistance, Ohm> by Centi(Ohm)

@Serializable
data object Deciohm : OhmMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricResistance, Ohm> by Deci(Ohm)

@Serializable
data object Decaohm : OhmMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricResistance, Ohm> by Deca(Ohm)

@Serializable
data object HectoOhm : OhmMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricResistance, Ohm> by Hecto(Ohm)

@Serializable
data object Kiloohm : OhmMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricResistance, Ohm> by Kilo(Ohm)

@Serializable
data object Megaohm : OhmMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricResistance, Ohm> by Mega(Ohm)

@Serializable
data object Gigaohm : OhmMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricResistance, Ohm> by Giga(Ohm)
