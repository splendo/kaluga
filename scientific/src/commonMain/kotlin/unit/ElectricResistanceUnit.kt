/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands
 
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
import com.splendo.kaluga.scientific.MeasurementType
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
sealed class ElectricResistance : AbstractScientificUnit<MeasurementType.ElectricResistance>(), MetricAndImperialScientificUnit<MeasurementType.ElectricResistance>

@Serializable
object Ohm : ElectricResistance(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricResistance> {
    override val symbol = "Ω"
    override val system = MeasurementSystem.MetricAndImperial
    override val type = MeasurementType.ElectricResistance
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
object Nanoohm : ElectricResistance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricResistance, Ohm> by Nano(Ohm)
@Serializable
object Abohm : ElectricResistance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricResistance, Ohm> by Nano(Ohm) {
    override val symbol = "abΩ"
}
@Serializable
object Microohm : ElectricResistance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricResistance, Ohm> by Micro(Ohm)
@Serializable
object Milliohm : ElectricResistance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricResistance, Ohm> by Milli(Ohm)
@Serializable
object Centiohm : ElectricResistance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricResistance, Ohm> by Centi(Ohm)
@Serializable
object Deciohm : ElectricResistance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricResistance, Ohm> by Deci(Ohm)
@Serializable
object Decaohm : ElectricResistance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricResistance, Ohm> by Deca(Ohm)
@Serializable
object HectoOhm : ElectricResistance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricResistance, Ohm> by Hecto(Ohm)
@Serializable
object Kiloohm : ElectricResistance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricResistance, Ohm> by Kilo(Ohm)
@Serializable
object Megaohm : ElectricResistance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricResistance, Ohm> by Mega(Ohm)
@Serializable
object Gigaohm : ElectricResistance(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricResistance, Ohm> by Giga(Ohm)
