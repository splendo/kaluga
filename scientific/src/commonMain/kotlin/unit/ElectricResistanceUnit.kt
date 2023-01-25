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
    Abohm
)

@Serializable
sealed class ElectricResistance : AbstractScientificUnit<PhysicalQuantity.ElectricResistance>(), MetricAndImperialScientificUnit<PhysicalQuantity.ElectricResistance>

@Serializable
object Ohm : ElectricResistance(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricResistance> {
    override val symbol = "Ω"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.ElectricResistance
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
sealed class OhmMultiple : ElectricResistance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricResistance, Ohm>

@Serializable
object Nanoohm : OhmMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricResistance, Ohm> by Nano(Ohm)
@Serializable
object Abohm : OhmMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricResistance, Ohm> by Nano(Ohm) {
    override val symbol = "abΩ"
}
@Serializable
object Microohm : OhmMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricResistance, Ohm> by Micro(Ohm)
@Serializable
object Milliohm : OhmMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricResistance, Ohm> by Milli(Ohm)
@Serializable
object Centiohm : OhmMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricResistance, Ohm> by Centi(Ohm)
@Serializable
object Deciohm : OhmMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricResistance, Ohm> by Deci(Ohm)
@Serializable
object Decaohm : OhmMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricResistance, Ohm> by Deca(Ohm)
@Serializable
object HectoOhm : OhmMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricResistance, Ohm> by Hecto(Ohm)
@Serializable
object Kiloohm : OhmMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricResistance, Ohm> by Kilo(Ohm)
@Serializable
object Megaohm : OhmMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricResistance, Ohm> by Mega(Ohm)
@Serializable
object Gigaohm : OhmMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.ElectricResistance, Ohm> by Giga(Ohm)
