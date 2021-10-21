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

package com.splendo.kaluga.scientific

import com.splendo.kaluga.base.utils.Decimal
import kotlinx.serialization.Serializable

@Serializable
sealed class ElectricResistance : AbstractScientificUnit<MeasurementType.ElectricResistance>(), MetricAndImperialScientificUnit<MeasurementType.ElectricResistance>

@Serializable
object Ohm : ElectricResistance(), BaseMetricUnit<MeasurementType.ElectricResistance, MeasurementSystem.MetricAndImperial> {
    override val symbol = "Ω"
    override val system = MeasurementSystem.MetricAndImperial
    override val type = MeasurementType.ElectricResistance
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
object NanoOhm : ElectricResistance(), SystemScientificUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricResistance> by Nano(Ohm)
@Serializable
object MicroOhm : ElectricResistance(), SystemScientificUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricResistance> by Micro(Ohm)
@Serializable
object MilliOhm : ElectricResistance(), SystemScientificUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricResistance> by Milli(Ohm)
@Serializable
object CentiOhm : ElectricResistance(), SystemScientificUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricResistance> by Centi(Ohm)
@Serializable
object DeciOhm : ElectricResistance(), SystemScientificUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricResistance> by Deci(Ohm)
@Serializable
object DecaOhm : ElectricResistance(), SystemScientificUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricResistance> by Deca(Ohm)
@Serializable
object HectoOhm : ElectricResistance(), SystemScientificUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricResistance> by Hecto(Ohm)
@Serializable
object KiloOhm : ElectricResistance(), SystemScientificUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricResistance> by Kilo(Ohm)
@Serializable
object MegaOhm : ElectricResistance(), SystemScientificUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricResistance> by Mega(Ohm)
@Serializable
object GigaOhm : ElectricResistance(), SystemScientificUnit<MeasurementSystem.MetricAndImperial, MeasurementType.ElectricResistance> by Giga(Ohm)

fun <
    ResistanceUnit : ElectricResistance,
    ConductanceUnit : ElectricConductance
    > ResistanceUnit.resistance(conductance: ScientificValue<MeasurementType.ElectricConductance, ConductanceUnit>): ScientificValue<MeasurementType.ElectricResistance, ResistanceUnit> = byInverting(conductance)

fun <
    CurrentUnit : ElectricCurrent,
    VoltageUnit : Voltage,
    ResistanceUnit : ElectricResistance
    > ResistanceUnit.resistance(
    voltage: ScientificValue<MeasurementType.Voltage, VoltageUnit>,
    current: ScientificValue<MeasurementType.ElectricCurrent, CurrentUnit>
): ScientificValue<MeasurementType.ElectricResistance, ResistanceUnit> = byDividing(voltage, current)

fun <ConductanceUnit : ElectricConductance> ScientificValue<MeasurementType.ElectricConductance, ConductanceUnit>.resistance() = Ohm.resistance(this)
infix operator fun <CurrentUnit : ElectricCurrent, VoltageUnit : Voltage> ScientificValue<MeasurementType.Voltage, VoltageUnit>.div(current: ScientificValue<MeasurementType.ElectricCurrent, CurrentUnit>) = Ohm.resistance(this, current)
